package ui.student.control;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.validation.ValidatorFactory;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.common.format.EmptyFormat;

import persistence.api.student.StudentEntity;
import persistence.dao.student.StudentDAO;
import ui.core.crud.control.CrudControl;
import ui.core.list.renderer.SelectableListCellRendererWrapper;
import ui.core.list.selection.ToggleListSelectionWrapper;
import ui.core.message.MessageResolver;
import ui.student.model.StudentAdminModel;
import ui.student.renderer.StudentListCellRenderer;
import ui.student.renderer.model.StudentListItemModel;
import ui.student.view.StudentAdminView;

public class StudentAdminControl extends CrudControl<StudentAdminView, StudentAdminModel> {
    private final String NAME_MESSAGE_FORMAT = "{0} {1}";

    private final StudentAdminView view;
    private final StudentAdminModel model;
    private final StudentDAO studentDAO;
    private final MessageResolver messageResolver;

    public StudentAdminControl(
            StudentAdminView view,
            StudentAdminModel model,
            StudentDAO studentDAO,
            MessageResolver messageResolver,
            ValidatorFactory validatorFactory) {
        super(view, model, view, validatorFactory, Arrays.asList(
                view.getEmailField(),
                view.getFirstnameField(),
                view.getLastnameField()));
        this.view = view;
        this.model = model;
        this.studentDAO = studentDAO;
        this.messageResolver = messageResolver;
        setupModelViewBinding();
        setupPatternChangeListener();
        setupStudentResultListCellRenderer();
        // Setup result list selection change listener
        model.addPropertyChangeListener(event -> {
            // When a student is selected from the result list the model needs to be updated
            if("selectedStudentListItemModel".equals(event.getPropertyName())) {
                // Re-init model with the given selection
                initModel(Optional.ofNullable((StudentListItemModel) event.getNewValue())
                    .map(item -> item.getEntity())
                    .map(entity -> new StudentAdminModel.Builder()
                        .withId(entity.getId())
                        .withEmail(entity.getEmail())
                        .withFirstname(entity.getFirstname())
                        .withLastname(entity.getLastname())
                        .build())
                    .orElse(new StudentAdminModel.Builder().build()));
            }
        });
        // Init crud control with empty model
        initModel(null);
        // Update pattern with empty string to perform a search
        updateSearchResultList(null);
        // Disable id field
        SwingUtilities.invokeLater(() -> {
            view.getIdField().setEditable(false);
            view.getIdField().setEnabled(false);
        });
    }

    @Override
    public void setEditMode(boolean value) {
        super.setEditMode(value);
        // When the panel is in edit mode, the search result list and it's control needs to be disabled
        Arrays
            .asList(view.getPatternField(), view.getSearchResultList())
            .stream()
            .forEach(component -> component.setEnabled(!value));
    }

    public void reloadStudentList() {
        // Update the list based on the current pattern
        updateSearchResultList(model.getPattern());
    }

    private void setupStudentResultListCellRenderer() {
        view.getSearchResultList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getSearchResultList().setCellRenderer(new SelectableListCellRendererWrapper<StudentListItemModel>(new StudentListCellRenderer()));
    }

    @SuppressWarnings("unchecked")
    private void setupModelViewBinding() {
        PresentationModel<StudentAdminModel> adapter = new PresentationModel<>(model);
        Bindings.bind(view.getIdField(), ConverterFactory.createStringConverter(adapter.getModel("id"), new EmptyFormat(DecimalFormat.getInstance())));
        Bindings.bind(view.getEmailField(), adapter.getModel("email"));
        Bindings.bind(view.getFirstnameField(), adapter.getModel("firstname"));
        Bindings.bind(view.getLastnameField(), adapter.getModel("lastname"));
        Bindings.bind(view.getPatternField(), adapter.getModel("pattern"));
        SelectionInList<StudentListItemModel> selectionInList = new SelectionInList<>(adapter.getModel("studentListItemModels"), adapter.getModel("selectedStudentListItemModel"));
        // 2. It needs to be set as model to the list
        view.getSearchResultList().setModel(selectionInList);
        // 3. Instead of the selectionInList the wrapper model has to be used (with toggle support)
        view.getSearchResultList().setSelectionModel(new ToggleListSelectionWrapper(new SingleListSelectionAdapter(selectionInList.getSelectionIndexHolder())));
        // 4. The component property handler needs to be setup the same way as in case of regular bindings
        Bindings.addComponentPropertyHandler(view.getSearchResultList(), selectionInList.getSelectionHolder());
        // Bindings.bind(view.getSearchResultList(), new SelectionInList<>(adapter.getModel("studentListItemModels"), adapter.getModel("selectedStudentListItemModel")));
    }

    private void setupPatternChangeListener() {
        model.addPropertyChangeListener(event -> {
            // When the content of the pattern field has been changed a new search needs to be initiated
            if("pattern".equals(event.getPropertyName())) {
                updateSearchResultList((String) event.getNewValue());
            }
        });
    }

    private void updateSearchResultList(String pattern) {
        // Show progress bar
        view.getSearchProgressBar().setVisible(true);
        // The matching students needs to be find in the database (in an asynchronous way to avoid blocking of the event dispatching thread) ...
        CompletableFuture.<List<StudentEntity>>supplyAsync(() -> studentDAO.findByPattern(Optional.ofNullable(pattern).orElse(""))).thenAccept(students -> {
            // ... and when the result is arrived the model has to be refreshed with the given data (the list will be refreshed by the binding right after this)
            SwingUtilities.invokeLater(() -> {
                // Update model
                model.setStudentListItemModels(createStudentListItemModel(students));
                // Hide progress bar
                view.getSearchProgressBar().setVisible(false);
            });
        });
    }

    private List<StudentListItemModel> createStudentListItemModel(List<StudentEntity> students) {
        return students
            .stream()
            .map(student -> new StudentListItemModel.Builder()
                .withEmail(student.getEmail())
                .withName(MessageFormat.format(NAME_MESSAGE_FORMAT, student.getFirstname(), student.getLastname()))
                .withNumberOfRentedBooks(Optional
                    .ofNullable(student.getLibraryCards()) // Use existing library cards if possible ...
                    .orElse(Collections.emptyList()) // ... otherwise an empty list ...
                    .stream()
                    .flatMap(card -> card.getEntries().stream()) // Collect all entries from the cards into a single list ...
                    .filter(entry -> !Optional.ofNullable(entry).isPresent()) // ... filter out the returned volumes ...
                    .mapToInt(entry -> entry.getAmount()) // ... collect the amounts of volumes ...
                    .sum()) // ... and calculate the sum
                .withEntity(student)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    protected void updateModel(Optional<StudentAdminModel> update) {
        StudentAdminModel model = update.orElse(new StudentAdminModel.Builder().build());
        this.model.setId(model.getId());
        this.model.setEmail(model.getEmail());
        this.model.setFirstname(model.getFirstname());
        this.model.setLastname(model.getLastname());
    }

    @Override
    protected void performSave() {
        // As this call is outside the DB transaction boundaries the 'unit of work' approach cannot apply here so the 'save' has to be called even if the entity is already managed.
        StudentEntity entity = Optional.ofNullable(model.getId()).map(id -> studentDAO.findOne(id)).orElse(new StudentEntity.Builder().build());
        // Update entity
        entity.setId(model.getId());
        entity.setFirstname(model.getFirstname());
        entity.setLastname(model.getLastname());
        entity.setEmail(model.getEmail());
        // Save entity
        entity = studentDAO.save(entity);
        // Update model with the generated id
        model.setId(entity.getId());
        // Update search result list
        updateSearchResultList(model.getPattern());
        // Show notification
        JOptionPane.showMessageDialog(SwingUtilities.getRoot(view), messageResolver.getMessage("i18n.student.notification.saveSucceeded", model.getFirstname(), model.getLastname()));
    }

    @Override
    protected void performDelete() {
        // Remove entity from database
        studentDAO.delete(model.getId());
        // Show notification
        JOptionPane.showMessageDialog(SwingUtilities.getRoot(view), messageResolver.getMessage("i18n.student.notification.deleteSucceeded", model.getFirstname(), model.getLastname()));
        // Update search result list
        reloadStudentList();
    }
}
