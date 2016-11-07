package ui.volume.admin.control;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.validation.ValidatorFactory;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.common.format.EmptyFormat;

import persistence.api.volume.VolumeEntity;
import persistence.dao.volume.VolumeDAO;
import remote.googlebooks.domain.response.Volume;
import ui.core.binding.converter.StringListToStringConverter;
import ui.core.crud.config.CrudControlConfiguration;
import ui.core.crud.control.CrudControl;
import ui.core.message.MessageResolver;
import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.admin.view.VolumeAdminView;
import ui.volume.search.facade.VolumeListItemModelFacade;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;
import ui.volume.search.item.domain.VolumeListItemModel;
import ui.volume.search.item.visitor.VolumeListItemVolumeAdminModelVisitor;
import ui.volume.search.query.control.VolumeSearchQueryModuleControl;
import ui.volume.search.query.model.VolumeSearchQueryModuleModel;

public class VolumeAdminControl extends CrudControl<VolumeAdminView, VolumeAdminModel> {
    static final String VOLUME_MODEL_CLIENT_PROPERTY = "VOLUME_MODEL";

    private final VolumeAdminView view;
    private final VolumeAdminModel model;
    private final VolumeSearchQueryModuleModel volumeSearchQueryModuleModel;
    private final VolumeSearchQueryModuleControl volumeSearchQueryModuleControl;
    private final VolumeListItemVolumeAdminModelVisitor volumeAdminModelConverter;
    private final VolumeDAO volumeDAO;
    private final MessageResolver messageResolver;

    public VolumeAdminControl(
            VolumeAdminView volumeAdminView,
            VolumeAdminModel volumeAdminModel,
            VolumeListItemModelFacade<LocalVolumeListItemModel, VolumeEntity> localVolumeModelFacade,
            VolumeListItemModelFacade<RemoteVolumeListItemModel, Volume> remoteVolumeModelFacade,
            VolumeListItemVolumeAdminModelVisitor volumeAdminModelConverter,
            VolumeDAO volumeDAO,
            MessageResolver messageResolver,
            ValidatorFactory validatorFactory) {
        super(volumeAdminView, volumeAdminModel, volumeAdminView, validatorFactory, Arrays.asList(
            volumeAdminView.getTitleField(),
            volumeAdminView.getAuthorField(),
            volumeAdminView.getPublisherField(),
            volumeAdminView.getPublishedDateField(),
            volumeAdminView.getLanguageComboBox(),
            volumeAdminView.getIsbnField(),
            volumeAdminView.getDescriptionTextArea(),
            volumeAdminView.getNumberOfVolumesSpinner()), new CrudControlConfiguration.Builder().withClearModelBeforeAdd(false).build());
        this.view = volumeAdminView;
        this.model = volumeAdminModel;
        this.volumeSearchQueryModuleModel = new VolumeSearchQueryModuleModel();
        this.volumeSearchQueryModuleControl = new VolumeSearchQueryModuleControl(volumeAdminView.getSearchView(), volumeSearchQueryModuleModel, localVolumeModelFacade, remoteVolumeModelFacade);
        this.volumeAdminModelConverter = volumeAdminModelConverter;
        this.volumeDAO = volumeDAO;
        this.messageResolver = messageResolver;
        // Bind view model to view elements
        setupModelViewBinding();
        // Setup search result change listener
        setupSearchSelectionChangeListener(volumeSearchQueryModuleModel);
        // The id and remote id fields must not be editable
        makeIdFieldPermanentlyUneditable();
        // Init crud control with empty model
        initModel(null);
    }

    @Override
    public void setEditMode(boolean value) {
        super.setEditMode(value);
        // In case of edit mode the search are needs to become unavailable
        volumeSearchQueryModuleControl.setEditable(!value);
    }

    @Override
    protected void updateModel(Optional<VolumeAdminModel> update) {
        VolumeAdminModel model = update.orElse(new VolumeAdminModel.Builder().build());
        this.model.setId(model.getId());
        this.model.setRemoteId(model.getRemoteId());
        this.model.setTitle(model.getTitle());
        this.model.setAuthors(model.getAuthors());
        this.model.setPublisher(model.getPublisher());
        this.model.setPublishedDate(model.getPublishedDate());
        this.model.setDescription(model.getDescription());
        this.model.setIsbn(model.getIsbn());
        this.model.setLanguage(model.getLanguage());
        this.model.setNumberOfVolumes(model.getNumberOfVolumes());
    }

    @Override
    protected void performSave() {
        // As this call is outside the DB transaction boundaries the 'unit of work' approach cannot apply here so the 'save' has to be called even if the entity is already managed.
        VolumeEntity entity = Optional.ofNullable(model.getId()).map(id -> volumeDAO.findOne(id)).orElse(new VolumeEntity.Builder().withRegistrationDate(new Date()).build());
        // Update entity
        entity.setRemoteId(model.getRemoteId());
        entity.setTitle(model.getTitle());
        entity.setAuthors(model.getAuthors());
        entity.setPublisher(model.getPublisher());
        entity.setPublishedDate(model.getPublishedDate());
        entity.setIsbn(model.getIsbn());
        entity.setDescription(model.getDescription());
        entity.setLanguage(model.getLanguage());
        entity.setNumberOfVolumes(model.getNumberOfVolumes());
        // Save entity
        entity = volumeDAO.save(entity);
        // Update model with the generated id if necessary
        model.setId(entity.getId());
        // Refresh search result list
        volumeSearchQueryModuleControl.reload();
        // Show notification
        JOptionPane.showMessageDialog(SwingUtilities.getRoot(view), messageResolver.getMessage("i18n.volume.notification.saveSucceeded", model.getTitle()));
    }

    @Override
    protected void performDelete() {
        // Remove entity from database
        volumeDAO.delete(model.getId());
        // Show notification
        JOptionPane.showMessageDialog(SwingUtilities.getRoot(view), messageResolver.getMessage("i18n.volume.notification.deleteSucceeded", model.getTitle()));
    }

    @SuppressWarnings("unchecked")
    private void setupModelViewBinding() {
        PresentationModel<VolumeAdminModel> adapter = new PresentationModel<>(model);
        Bindings.bind(view.getIdField(), ConverterFactory.createStringConverter(adapter.getModel("id"), new EmptyFormat(DecimalFormat.getInstance())));
        Bindings.bind(view.getRemoteIdField(), adapter.getModel("remoteId"));
        Bindings.bind(view.getTitleField(), adapter.getModel("title"));
        Bindings.bind(view.getAuthorField(), new ConverterValueModel(adapter.getModel("authors"), new StringListToStringConverter()));
        Bindings.bind(view.getIsbnField(), adapter.getModel("isbn"));
        Bindings.bind(view.getPublisherField(), adapter.getModel("publisher"));
        Bindings.bind(view.getPublishedDateField(), adapter.getModel("publishedDate"));
        Bindings.bind(view.getDescriptionTextArea(), adapter.getModel("description"));
        view.getLanguageComboBox().setModel(new ComboBoxAdapter<Locale>(lookupAvailableLanguages(), adapter.getModel("language")));
        view.getNumberOfVolumesSpinner().setModel(SpinnerAdapterFactory.createNumberAdapter(adapter.getModel("numberOfVolumes"), 0, 0, 50, 1));
    }

    private void setupSearchSelectionChangeListener(VolumeSearchQueryModuleModel volumeSearchQueryModuleModel) {
        volumeSearchQueryModuleModel.addPropertyChangeListener(event -> {
            if("selectedVolume".equals(event.getPropertyName())) {
                // Retrieve selected model via visitor, for more information please {@link VolumeListItemVolumeAdminModelVisitor}
                initModel(Optional.ofNullable((VolumeListItemModel<?>) event.getNewValue()).map(volumeModel -> volumeModel.accept(volumeAdminModelConverter)).orElse(null));
            }
        });
    }

    private Locale[] lookupAvailableLanguages() {
        return Arrays.asList(DateFormat.getAvailableLocales())
            .stream()
            .map(locale -> locale.getLanguage())
            .distinct()
            .map(language -> new Locale(language))
            .collect(Collectors.toList())
            .toArray(new Locale[]{});
    }

    private void makeIdFieldPermanentlyUneditable() {
        SwingUtilities.invokeLater(() -> {
            Arrays.asList(view.getIdField(), view.getRemoteIdField()).stream().forEach(field -> {
                field.setEditable(false);
                field.setEnabled(false);
            });
        });
    }

}
