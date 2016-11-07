package ui.rent.control;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.validation.ValidatorFactory;

import org.jdesktop.swingx.JXDatePicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;

import persistence.api.card.LibraryCardEntity;
import persistence.api.card.LibraryCardEntryEntity;
import persistence.api.card.LibraryCardEntryEntity.Status;
import persistence.api.student.StudentEntity;
import persistence.api.volume.VolumeEntity;
import persistence.dao.card.LibraryCardDAO;
import persistence.dao.student.StudentDAO;
import persistence.dao.volume.VolumeDAO;
import ui.core.action.FunctionAction;
import ui.core.binding.connector.JXDatePickerConnector;
import ui.core.crud.config.CrudControlConfiguration;
import ui.core.crud.control.CrudControl;
import ui.core.list.selection.ToggleListSelectionWrapper;
import ui.core.message.MessageResolver;
import ui.core.table.editor.DatePickerTableCellEditor;
import ui.core.table.editor.SpinnerTableCellEditor;
import ui.core.table.renderer.FormattedTableCellRenderer;
import ui.core.table.renderer.builder.ComboBoxTableCellEditorBuilder;
import ui.core.table.renderer.builder.TableCellRendererBuilder;
import ui.core.util.renderer.ButtonColumn;
import ui.rent.binding.adapter.RentTableAdapter;
import ui.rent.binding.adapter.VolumeTableAdapter;
import ui.rent.config.RentConfiguration;
import ui.rent.event.CardChangeEvent;
import ui.rent.event.CardChangeEventListener;
import ui.rent.model.RentModel;
import ui.rent.model.RentSearchQuery;
import ui.rent.model.RentTableCardModel;
import ui.rent.model.RentTableVolumeModel;
import ui.rent.student.model.StudentCellRendererModel;
import ui.rent.view.RentView;
import ui.rent.volume.model.RentTreeTableRootVolumeCellRendererModel;
import ui.rent.volume.model.VolumeCellRenderModel;
import ui.rent.volume.model.VolumeTableListItemModel;
import ui.volume.search.result.control.VolumeSearchResultControl;

public class RentControl extends CrudControl<RentView, RentModel> implements CardChangeEventDispatcher {
    private static final long MIN_DATE_IN_MILLISEC = 0L;
    private static final long MAX_DATE_IN_MILLISEC = 4102444800000L;
    private static final String studentNameFormat = "{0} {1}";
    private static final int maxItemsPerPage = 3;
    private static final Set<String> searchProperties = new HashSet<>(Arrays.asList(
        "searchPattern",
        "searchFrom",
        "searchTo",
        "searchPendingOnly"));
    private static final Logger logger = LoggerFactory.getLogger(VolumeSearchResultControl.class);

    private final RentView view;
    private final RentModel model;
    private final VolumeDAO volumeDAO;
    private final StudentDAO studentDAO;
    private final LibraryCardDAO libraryCardDAO;
    private final MessageResolver messageResolver;
    private final RentConfiguration rentConfiguration;

    private final EventListenerList listenerList = new EventListenerList();

    private final PresentationModel<RentModel> adapter;

    private CompletableFuture<Page<RentTableCardModel>> completableFuture = null;

    public RentControl(
            RentView view,
            RentModel model,
            VolumeDAO volumeDAO,
            StudentDAO studentDAO,
            LibraryCardDAO libraryCardDAO,
            MessageResolver messageResolver,
            ValidatorFactory validatorFactory,
            RentConfiguration rentConfiguration) {
        super(view, model, view, validatorFactory, Arrays.asList(
                view.getRentEditorView().getStudentComboBox(),
                view.getRentEditorView().getFromDatePicker(),
                view.getRentEditorView().getToDatePicker(),
                view.getRentEditorView().getVolumeTable(),
                view.getRentEditorView().getAddVolumeButton(),
                view.getRentEditorView().getStudentRefreshButton(),
                view.getRentEditorView().getVolumesRefreshButton()), new CrudControlConfiguration.Builder().build());
        this.view = view;
        this.model = model;
        this.volumeDAO = volumeDAO;
        this.studentDAO = studentDAO;
        this.libraryCardDAO = libraryCardDAO;
        this.messageResolver = messageResolver;
        this.rentConfiguration = rentConfiguration;
        this.adapter = new PresentationModel<>(model);
        // Create a custom volume table cell editor
        // Please note: this instance will be used for all cell editing operation therefore the model binding can be done directly with this instance
        DefaultCellEditor volumeCellEditor = new ComboBoxTableCellEditorBuilder().withEditable(true).build();
        // Setup model and view binding
        setupModelViewBinding(view, model, volumeCellEditor);
        // Setup table cell editors/renderers e.g.: volume selector combo box (editor), checkbox to indicate whether the book is returned or not, remove row button
        setupVolumeTableCellRenderersAndEditors(view, model, volumeCellEditor);
        // Setup preferred column width settings
        setupVolumeTableStyleDefinitions(view);
        // Setup add volume button click listener
        setupAddVolumeButtonClickListener(view);
        setupSearchPropertiesChangeListeners(model);
        setupRentTreeTableSelectionModel(view);
        setupRentTreeTableSelectionListener(view);
        setupVolumeTableSelectionMode();
        setupAvailableVolumeRefreshClickListner();
        setupStudentRefreshClickListner();
        setupSearchPageNumberChangeListener(model);
        // Force update to initialize model of student and volume selectors
        updateStudentList();
        updateAvailableVolumeList();
        // Init crud control with empty model
        initModel(null);
        // Execute a search manually
        search(createSearchQueryByModel());
    }

    private void setupSearchPageNumberChangeListener(RentModel model) {
        model.addPropertyChangeListener(event -> {
            if(event.getPropertyName().equals("pageNumber")) {
                search(createSearchQueryByModel());
            }
        });
    }

    private void setupRentTreeTableSelectionModel(RentView view) {
        view.getRentSearchView().getRentTreeTable().setSelectionModel(new ToggleListSelectionWrapper(view.getRentSearchView().getRentTreeTable().getSelectionModel()));
    }

    @Override
    protected void cancelButtonClickListener() {
        // To last edit operation must be finalized otherwise the control (editor) would remain on the cell
        Optional.ofNullable(view.getRentEditorView().getVolumeTable().getCellEditor()).ifPresent(editor -> editor.stopCellEditing());
        super.cancelButtonClickListener();
    }

    @Override
    protected void saveButtonClickListener() {
        // Remove editor because of the same reason as in case of 'cancel'
        Optional.ofNullable(view.getRentEditorView().getVolumeTable().getCellEditor()).ifPresent(editor -> editor.stopCellEditing());
        super.saveButtonClickListener();
    }

    private void setupRentTreeTableSelectionListener(RentView view) {
        // As the binding framework is not applicable on rent tree table the selection listener must be added manually
        view.getRentSearchView().getRentTreeTable().addTreeSelectionListener(event -> {
            RentModel model = null;
            if(view.getRentSearchView().getRentTreeTable().getSelectedRow() != -1) {
                LibraryCardEntity card = ((RentTableCardModel)Optional
                    .ofNullable(event.getPath().getLastPathComponent())
                    .filter(item -> item instanceof RentTableCardModel)
                    .orElse(event.getPath().getParentPath().getLastPathComponent())).getEntity();
                model = new RentModel.Builder()
                        .withId(card.getId())
                        .withSelectedStudent(createStudentCellRendererModel(card.getStudent()))
                        .withFrom(card.getCreationDate())
                        .withTo(card.getExpiryDate())
                        .withVolumes(card.getEntries().stream().map(entry -> new VolumeTableListItemModel.Builder()
                            .withId(entry.getId())
                            .withAmount(entry.getAmount())
                            .withVolume(createVolumeCellRenderer(entry.getVolume()))
                            .withReturnDate(entry.getReturnDate())
                            .build()).collect(Collectors.toList()))
                        .build();
            }
            initModel(model);
        });
    }

    private void setupVolumeTableStyleDefinitions(RentView view) {
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.ROW_NUMBER_COLUMN_INDEX).setMaxWidth(20);
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.AMOUNT_COLUMN_INDEX).setMaxWidth(60);
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.RETURNED_COLUMN_INDEX).setMinWidth(100);
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.RETURNED_COLUMN_INDEX).setMaxWidth(100);
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.CONTROL_COLUMN_INDEX).setMaxWidth(60);
        view.getRentEditorView().getVolumeTable().setRowHeight(22);
    }

    private void setupSearchPropertiesChangeListeners(RentModel model) {
        model.addPropertyChangeListener(event -> {
            if(searchProperties.contains(event.getPropertyName())) {
                search(createSearchQueryByModel());
            }
        });
    }

    private RentSearchQuery createSearchQueryByModel() {
        return new RentSearchQuery.Builder()
            .withPattern(model.getSearchPattern())
            .withFrom(model.getSearchFrom())
            .withTo(model.getSearchTo())
            .withPendingOnly(model.isSearchPendingOnly())
            .withStartIndex((Optional.ofNullable(model.getPageNumber()).orElse(1) - 1) * maxItemsPerPage)
            .withMaxResults(maxItemsPerPage)
            .build();
    }

    private void search(RentSearchQuery rentSearchQuery) {
        // Cancel future if exists
        Optional.ofNullable(completableFuture).ifPresent(future -> future.cancel(true));
        // Show progress bar
        view.getRentSearchView().getSearchProgressBar().setVisible(true);
        // Reset page spinner
        clearPageNumberSpinnerBinding();
        // Execute search query asynchronously by pattern
        completableFuture = CompletableFuture.supplyAsync(() -> findCardsByPattern(rentSearchQuery));
        completableFuture.thenAccept(page -> {
            SwingUtilities.invokeLater(() -> {
                int numberOfPages = Math.max(1, Double.valueOf(Math.ceil(Double.valueOf(page.getTotalElements()) / rentSearchQuery.getMaxResults())).intValue());
                // Re-bind page number spinner
                initPageNumberSpinnerBinding(1, numberOfPages);
                // Update total page number label via binding
                model.setTotalPageNumbers(numberOfPages);
                // As no binding is applied for tree table the model needs to be set manually
                setupRentTreeTableModel(page);
                setupRentTreeTableColumnStyleDeclarations();
                // Hide progress bar
                view.getRentSearchView().getSearchProgressBar().setVisible(false);
            });
        }).exceptionally(ex -> {
            if(ex instanceof CancellationException || ex instanceof CompletionException) {
                logger.info("The completable future execution has been canceled.");
            } else {
                logger.error("Tho following error occures search: ", ex);
            }
            return null;
        });
    }

    private void setupRentTreeTableColumnStyleDeclarations() {
        // Hide root element in the tree table
        view.getRentSearchView().getRentTreeTable().setRootVisible(false);
        // Update table layout (this can be done only after the model has been set)
        view.getRentSearchView().getRentTreeTable().getColumn(RentTableAdapter.AMOUNT_COLUMN_INDEX).setCellRenderer(new TableCellRendererBuilder().withHorizontalAlignment(SwingConstants.RIGHT).build());
        view.getRentSearchView().getRentTreeTable().getColumn(RentTableAdapter.FROM_COLUMN_INDEX).setCellRenderer(new TableCellRendererBuilder().withFormat(SimpleDateFormat.getDateInstance()).withHorizontalAlignment(SwingConstants.CENTER).build());
        view.getRentSearchView().getRentTreeTable().getColumn(RentTableAdapter.TO_COLUMN_INDEX).setCellRenderer(new TableCellRendererBuilder().withFormat(SimpleDateFormat.getDateInstance()).withHorizontalAlignment(SwingConstants.CENTER).build());
        // Init column size
        view.getRentSearchView().getRentTreeTable().getColumnModel().getColumn(RentTableAdapter.ID_COLUMN_INDEX).setMaxWidth(100);
        view.getRentSearchView().getRentTreeTable().getColumnModel().getColumn(RentTableAdapter.AMOUNT_COLUMN_INDEX).setMaxWidth(50);
        view.getRentSearchView().getRentTreeTable().getColumnModel().getColumn(RentTableAdapter.FROM_COLUMN_INDEX).setMaxWidth(75);
        view.getRentSearchView().getRentTreeTable().getColumnModel().getColumn(RentTableAdapter.TO_COLUMN_INDEX).setMaxWidth(75);
    }

    private void setupRentTreeTableModel(Page<RentTableCardModel> page) {
        view.getRentSearchView().getRentTreeTable().setTreeTableModel(new RentTableAdapter(page.getContent(), new String[]{
            messageResolver.getMessage("i18n.rent.table.header.id"),
            messageResolver.getMessage("i18n.rent.table.header.student"),
            messageResolver.getMessage("i18n.rent.table.header.volume"),
            messageResolver.getMessage("i18n.rent.table.header.amount"),
            messageResolver.getMessage("i18n.rent.table.header.from"),
            messageResolver.getMessage("i18n.rent.table.header.to"),
        }));
    }

    private Page<RentTableCardModel> findCardsByPattern(RentSearchQuery rentSearchQuery) {
        return ((PageImpl<LibraryCardEntity>)libraryCardDAO.findByPattern(
                Optional.ofNullable(rentSearchQuery.getPattern()).orElse(""),
                Optional.ofNullable(rentSearchQuery.getFrom()).orElse(new Date(MIN_DATE_IN_MILLISEC)),
                Optional.ofNullable(rentSearchQuery.getTo()).orElse(new Date(MAX_DATE_IN_MILLISEC)),
                rentSearchQuery.isPendingOnly() ? Arrays.asList(Status.PENDING) : Arrays.asList(Status.PENDING, Status.RETURNED),
                new PageRequest(rentSearchQuery.getStartIndex() / rentSearchQuery.getMaxResults(), rentSearchQuery.getMaxResults())))
            .map(card -> convertLibraryCardToRentTableCardModel(card));
    }

    @Override
    public void addCardChangeEventListener(CardChangeEventListener listener) {
        listenerList.add(CardChangeEventListener.class, listener);
    }

    @Override
    public void removeCardChangeEventListener(CardChangeEventListener listener) {
        listenerList.remove(CardChangeEventListener.class, listener);
    }

    private void fireCardChangeEvent(CardChangeEvent event) {
        Arrays.asList(listenerList.getListeners(CardChangeEventListener.class)).forEach(listener -> listener.cardChanged(event));
    }

    private void clearPageNumberSpinnerBinding() {
        view.getRentSearchView().getPageNumberSpinner().setModel(new SpinnerNumberModel(1, 1, 1, 1));
    }

    private void initPageNumberSpinnerBinding(int min, int max) {
        view.getRentSearchView().getPageNumberSpinner().setModel(
            SpinnerAdapterFactory.createNumberAdapter(adapter.getModel("pageNumber"), 1, min, max, 1));
    }

    private void setupAddVolumeButtonClickListener(RentView view) {
        view.getRentEditorView().getAddVolumeButton().addActionListener(event -> {
            addEmptyVolumeRow();
        });
    }

    private void addEmptyVolumeRow() {
        model.setVolumes(ImmutableList.<VolumeTableListItemModel>builder()
            .addAll(Optional.ofNullable(model.getVolumes()).orElse(Collections.emptyList()))
            .add(new VolumeTableListItemModel.Builder().withAmount(1).build())
            .build());
    }

    private void setupVolumeTableSelectionMode() {
        view.getRentEditorView().getVolumeTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupVolumeTableCellRenderersAndEditors(RentView view, RentModel model, DefaultCellEditor volumeCellEditor) {
        // Setup renderers
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.ROW_NUMBER_COLUMN_INDEX).setCellRenderer(new TableCellRendererBuilder().withFormat(new TableCellRendererBuilder.StringFormat("%02d")).withHorizontalAlignment(SwingConstants.RIGHT).build());
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.AMOUNT_COLUMN_INDEX).setCellRenderer(new TableCellRendererBuilder().withHorizontalAlignment(SwingConstants.RIGHT).build());
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.RETURNED_COLUMN_INDEX).setCellRenderer(new FormattedTableCellRenderer(SimpleDateFormat.getDateInstance()));
        // Create a cell editor with the given combo box and assign it to the table column
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.VOLUME_COLUMN_INDEX).setCellEditor(volumeCellEditor);
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.AMOUNT_COLUMN_INDEX).setCellEditor(new SpinnerTableCellEditor(new SpinnerNumberModel(1, 1, 10, 1)));
        view.getRentEditorView().getVolumeTable().getColumnModel().getColumn(VolumeTableAdapter.RETURNED_COLUMN_INDEX).setCellEditor(new DatePickerTableCellEditor());
        resetVolumeTableControlColumn(view, model);
    }

    private void resetVolumeTableControlColumn(RentView view, RentModel model) {
        // Please note: This is just an evil trick as the cell rendering mechanism for JTable does not provide to option to have fully functional buttons in
        // non-editable fields, therefore the same trick needs to be applied what described in the following article:
        // https://tips4java.wordpress.com/2009/07/12/table-button-column/
        new ButtonColumn(view.getRentEditorView().getVolumeTable(), new FunctionAction(event -> {
            // Lookup row number
            int row = Integer.valueOf(event.getActionCommand());
            // Instead of removing the item directly from the list the model needs to be reset
            model.setVolumes(IntStream.range(0, model.getVolumes().size())
                .filter(index -> index != row)
                .mapToObj(index -> model.getVolumes().get(index))
                .collect(Collectors.toList()));
        }), VolumeTableAdapter.CONTROL_COLUMN_INDEX);
    }

    private void setupAvailableVolumeRefreshClickListner() {
        view.getRentEditorView().getVolumesRefreshButton().addActionListener(event -> {
            updateAvailableVolumeList();
        });
    }

    private void updateAvailableVolumeList() {
        view.getRentEditorView().getVolumesBusyLabel().setVisible(true);
        view.getRentEditorView().getVolumesBusyLabel().setBusy(true);
        // Execute search asynchronously
        CompletableFuture
            .supplyAsync(() -> Lists.newArrayList(volumeDAO.findAll()).stream().map(volume -> createVolumeCellRenderer(volume)).collect(Collectors.toList()))
            .thenAccept(volumes -> SwingUtilities.invokeLater(() -> {
                // Update model
                model.setAvailableVolumes(volumes);
                // Hide loading mask
                view.getRentEditorView().getVolumesBusyLabel().setVisible(false);
                view.getRentEditorView().getVolumesBusyLabel().setBusy(false);
            })).exceptionally(ex -> {
                logger.error("Tho following error occures during volume loading: ", ex);
                return null;
            });
    }

    private VolumeCellRenderModel createVolumeCellRenderer(VolumeEntity volume) {
        return new VolumeCellRenderModel.Builder()
            .withId(volume.getId())
            .withTitle(volume.getTitle())
            .withAuthors(volume.getAuthors())
            .withEntry(volume)
            .build();
    }

    @SuppressWarnings("unchecked")
    private void setupModelViewBinding(RentView view, RentModel model, DefaultCellEditor volumeCellEditor) {
        // As JXDatePicker is not a standard swing component a custom PropertyConnector needs to be used
        new JXDatePickerConnector(adapter.getModel("from"), (JXDatePicker)view.getRentEditorView().getFromDatePicker()).updateComponent();
        new JXDatePickerConnector(adapter.getModel("to"), (JXDatePicker)view.getRentEditorView().getToDatePicker()).updateComponent();
        // Bind student combo box
        Bindings.bind(view.getRentEditorView().getStudentComboBox(), new SelectionInList<>(adapter.getModel("students"), adapter.getModel("selectedStudent")));
        // Bind table volume selector as cell editor
        // The cell editor (to be a bit more precise: it's combo box) has it's own binding to the model and as the same instance will be used during the table editing ther's no need for additional binding
        // 1. Bind the given combo box to the model (there's no need to bind the selection as well, as it will be done by the table model)
        Bindings.bind((JComboBox<VolumeCellRenderModel>) volumeCellEditor.getComponent(), new SelectionInList<>(adapter.getModel("availableVolumes")));
        // 2. Bind the volumes to the list
        SelectionInList<VolumeTableListItemModel> selectionInList = new SelectionInList<>(adapter.getModel("volumes"));
        view.getRentEditorView().getVolumeTable().setModel(new VolumeTableAdapter(selectionInList, new String[] {
            messageResolver.getMessage("i18n.rent.volume.table.header.index"),
            messageResolver.getMessage("i18n.rent.volume.table.header.volume"),
            messageResolver.getMessage("i18n.rent.volume.table.header.amount"),
            messageResolver.getMessage("i18n.rent.volume.table.header.returned"),
            messageResolver.getMessage("i18n.rent.volume.table.header.control")
        }));
        // Bind search controls
        Bindings.bind(view.getRentSearchView().getSearchPatternField(), adapter.getModel("searchPattern"));
        new JXDatePickerConnector(adapter.getModel("searchFrom"), (JXDatePicker)view.getRentSearchView().getSearchFromDatePicker()).updateComponent();
        new JXDatePickerConnector(adapter.getModel("searchTo"), (JXDatePicker)view.getRentSearchView().getSearchToDatePicker()).updateComponent();
        Bindings.bind(view.getRentSearchView().getSearchPendingOnlyCheckBox(), adapter.getModel("searchPendingOnly"));
    }

    private void setupStudentRefreshClickListner() {
        view.getRentEditorView().getStudentRefreshButton().addActionListener(event -> {
            updateStudentList();
        });
    }

    private void updateStudentList() {
        // Show loading animation
        view.getRentEditorView().getStudentBusyLabel().setVisible(true);
        view.getRentEditorView().getStudentBusyLabel().setBusy(true);
        // Execute search asynchronously
        CompletableFuture
            .supplyAsync(() -> Lists.newArrayList(studentDAO.findAll()).stream().map(student -> createStudentCellRendererModel(student)).collect(Collectors.toList()))
            .thenAccept(students -> SwingUtilities.invokeLater(() -> {
                // Hide loading animation
                view.getRentEditorView().getStudentBusyLabel().setVisible(false);
                view.getRentEditorView().getStudentBusyLabel().setBusy(false);
                // Update model (and combo box as well via binding)
                model.setStudents(students);
            })).exceptionally(ex -> {
                logger.error("Tho following error occures during student loading: ", ex);
                return null;
            });
    }

    private StudentCellRendererModel createStudentCellRendererModel(StudentEntity student) {
        return new StudentCellRendererModel.Builder()
            .withName(MessageFormat.format(studentNameFormat, student.getFirstname(), student.getLastname()))
            .withEntity(student)
            .build();
    }

    @Override
    protected void addButtonClickListener() {
        super.addButtonClickListener();
        // In case of add the from and to dates needs to be initialized
        model.setFrom(convertLocalDateToDate(LocalDate.now()));
        model.setTo(convertLocalDateToDate(LocalDate.now().plus(rentConfiguration.getDefaultToDateOffsetInDays(), ChronoUnit.DAYS)));
    }

    @Override
    protected void updateModel(Optional<RentModel> update) {
        RentModel model = update.orElse(new RentModel.Builder().build());
        this.model.setId(model.getId());
        this.model.setSelectedStudent(model.getSelectedStudent());
        this.model.setFrom(model.getFrom());
        this.model.setTo(model.getTo());
        this.model.setVolumes(model.getVolumes());
    }

    @Override
    protected void performSave() {
        // As this call is outside the DB transaction boundaries the 'unit of work' approach cannot apply here so the 'save' has to be called even if the entity is already managed.
        LibraryCardEntity entity = Optional.ofNullable(model.getId()).map(id -> libraryCardDAO.findOne(id)).orElse(new LibraryCardEntity.Builder().build());
        // Update entity
        entity.setId(model.getId());
        entity.setCreationDate(model.getFrom());
        entity.setExpiryDate(model.getTo());
        entity.setStudent(model.getSelectedStudent().getEntity());
        entity.setEntries(model.getVolumes()
            .stream()
            .map(item -> new LibraryCardEntryEntity.Builder()
                .withId(item.getId())
                .withVolume(item.getVolume().getEntry())
                .withAmount(item.getAmount())
                .withReturnDate(item.getReturnDate())
                .withStatus(Optional.ofNullable(item.getReturnDate()).isPresent() ? Status.RETURNED : Status.PENDING)
                .build())
            .collect(Collectors.toList()));
        // Save entity
        entity = libraryCardDAO.save(entity);


        // Update counterpart of the relation
        // entity.getStudent().


        // Fire card change listener
        fireCardChangeEvent(new CardChangeEvent.Builder()
            .withType(Optional.ofNullable(model.getId())
                .map(id -> CardChangeEvent.Type.UPDATE)
                .orElse(CardChangeEvent.Type.INSERT))
            .withCard(entity)
            .build());
        // Update model with the generated id
        model.setId(entity.getId());
        // Show notification
        JOptionPane.showMessageDialog(SwingUtilities.getRoot(view), messageResolver.getMessage("i18n.rent.notification.saveSucceeded",
            String.format("%04d", model.getId()),
            model.getSelectedStudent().getName(),
            model.getVolumes()
                .stream()
                .map(volume -> volume.getVolume().getTitle())
                .collect(Collectors.joining(", "))));
        // Execute a search to update rent table
        search(createSearchQueryByModel());
    }

    @Override
    protected void performDelete() {
        libraryCardDAO.delete(model.getId());
        // Show notification
        JOptionPane.showMessageDialog(SwingUtilities.getRoot(view), messageResolver.getMessage("i18n.rent.notification.deleteSucceeded", model.getId()));
        // Execute a search to update rent table
        search(createSearchQueryByModel());
    }

    private Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private RentTableCardModel convertLibraryCardToRentTableCardModel(LibraryCardEntity card) {
        return new RentTableCardModel.Builder()
            .withId(card.getId())
            .withStudent(createStudentCellRendererModel(card.getStudent()))
            .withVolume(new RentTreeTableRootVolumeCellRendererModel(card.getEntries()
                .stream()
                .map(entry -> entry.getVolume().getTitle())
                .collect(Collectors.joining(", "))))
            .withAmount(card.getEntries()
                .stream()
                .mapToInt(entry -> entry.getAmount())
                .sum())
            .withFrom(card.getCreationDate())
            .withTo(card.getExpiryDate())
            .withEntity(card)
            .withChildren(IntStream.range(0, card.getEntries().size()).mapToObj(index -> {
                LibraryCardEntryEntity entry = card.getEntries().get(index);
                return new RentTableVolumeModel.Builder()
                    .withId(Integer.valueOf(index).longValue() + 1)
                    .withStudent(createStudentCellRendererModel(card.getStudent()))
                    .withAmount(entry.getAmount())
                    .withFrom(card.getCreationDate())
                    .withTo(entry.getReturnDate())
                    .withVolume(createVolumeCellRenderer(entry.getVolume()))
                    .build();
            }).collect(Collectors.toList()))
            .build();
    }

}
