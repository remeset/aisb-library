package ui.core.crud.control;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;

import ui.core.crud.config.CrudControlConfiguration;
import ui.core.crud.model.CrudModel;
import ui.core.crud.view.CrudControlView;
import ui.validation.group.MergeGroup;
import ui.validation.group.PersistGroup;
import ui.validation.group.RemoveGroup;

public abstract class CrudControl<V extends JComponent, M extends CrudModel<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrudControl.class);

    private static final String PREVIOUS_VALUE_CLIENT_PROPERTY = "previous_value";

    private final V view;
    private final M model;
    private final CrudControlView crudControlView;
    private final List<JComponent> editableFields;
    private final ValidatorFactory validatorFactory;
    private final CrudControlConfiguration configuration;

    public CrudControl(V view, M model, CrudControlView crudControlView, ValidatorFactory validatorFactory, List<JComponent> editableFields) {
        this(view, model, crudControlView, validatorFactory, editableFields, new CrudControlConfiguration.Builder().build());
    }

    public CrudControl(V view, M model, CrudControlView crudControlView, ValidatorFactory validatorFactory, List<JComponent> editableFields, CrudControlConfiguration configuration) {
        this.view = view;
        this.model = model;
        this.crudControlView = crudControlView;
        this.editableFields = editableFields;
        this.validatorFactory = validatorFactory;
        this.configuration = configuration;
        // Setup button click listeners
        setupAddButtonActionListener();
        setupEditButtonActionListener();
        setupSaveButtonActionListener();
        setupRemoveButtonActionListener();
        setupCancelButtonActionListener();
    }

    public void setEditMode(boolean value) {
        editableFields
            .stream()
            .forEach(component -> component.setEnabled(value));
    }

    public boolean getEditMode() {
        return editableFields
            .stream()
            .findAny()
            .map(component -> component.isEnabled())
            .orElse(false);
    }

    public void initModel(M model) {
        LOGGER.info("'{}' model has been reset.", model);
        // Initialize edit mode
        setEditMode(false);
        // Store volume model as client property (for 'cancel')
        view.putClientProperty(PREVIOUS_VALUE_CLIENT_PROPERTY, model);
        // Update model (and view via binding) with the selected data
        updateModel(Optional.ofNullable(model));
        // Update control bar accessibility
        updateCrudControlView();
        // Clear validation result
        clearValidationResultModel();
        // Repaint panel
        view.repaint();
        view.revalidate();
    }

    protected abstract void updateModel(Optional<M> update);

    protected abstract void performSave();

    protected abstract void performDelete();

    private ValidationResult performModelValidation(Class<?> ... groups) {
        // Verify model before save
        ValidationResult validationResult = new ValidationResult();
        // Validate the name field
        validationResult.addAll(validatorFactory
                .getValidator()
                .validate(model, groups)
                .stream()
                .map(error -> new SimpleValidationMessage(error.getMessage(), Severity.ERROR))
                .collect(Collectors.toList()));
        return validationResult;
    }

    private Class<?>[] calculateSaveValidationGroup() {
        // If a volume id is presented then it means that the volume is already registered in the database therefore some of the validation rules must not be applied on it and this must be highlighted via validation groups
        return Optional.ofNullable(model.getId())
                .map(id -> new Class[]{PersistGroup.class})
                .orElse(new Class[]{MergeGroup.class});
    }

    private void updateCrudControlView() {
        crudControlView.getAddButton().setEnabled(!Optional.ofNullable(model.getId()).isPresent() && !getEditMode());
        crudControlView.getEditButton().setEnabled(Optional.ofNullable(model.getId()).isPresent() && !getEditMode());
        crudControlView.getSaveButton().setEnabled(getEditMode());
        crudControlView.getCancelButton().setEnabled(getEditMode());
        crudControlView.getRemoveButton().setEnabled(Optional.ofNullable(model.getId()).isPresent());
    }

    protected void editButtonClickListener() {
        LOGGER.info("Initializing edit mode for '{}'...", model);
        // Set fields editable
        setEditMode(true);
        // Update control bar accessibility
        updateCrudControlView();
        // Repaint panel
        view.repaint();
        view.revalidate();
    }

    protected void addButtonClickListener() {
        LOGGER.info("Initializing add mode for '{}'...", model);
        // Clear model if it's required
        if(configuration.isClearModelBeforeAdd()) {
            updateModel(Optional.empty());
        }
        // Set fields editable
        setEditMode(true);
        // Update control bar accessibility
        updateCrudControlView();
        // Repaint panel
        view.repaint();
        view.revalidate();
    }

    @SuppressWarnings("unchecked")
    protected void cancelButtonClickListener() {
        LOGGER.info("Canceling operation for '{}'...", model);
        // Reset model by stored (or empty) volume model
        updateModel(Optional.ofNullable((M) view.getClientProperty(PREVIOUS_VALUE_CLIENT_PROPERTY)));
        // Set form uneditable
        setEditMode(false);
        // Clear validation result
        clearValidationResultModel();
        // Update control bar
        updateCrudControlView();
        // Repaint panel
        view.repaint();
        view.revalidate();
    }

    protected void saveButtonClickListener() {
        LOGGER.info("Saving '{}'...", model);
        // Perform validation
        ValidationResult validationResult = performModelValidation(calculateSaveValidationGroup());
        // Update validation result model
        model.getValidationResultModel().setResult(validationResult);
        // Save book
        if(!validationResult.hasErrors()) {
            performSave();
            // Set form uneditable
            setEditMode(false);
            // Clear validation result
            clearValidationResultModel();
            // Update control bar
            updateCrudControlView();
        }
        // Repaint panel
        view.repaint();
        view.revalidate();
    }

    protected void removeButtonClickListener() {
        LOGGER.info("Removing '{}'...", model);
        // Perform validation
        ValidationResult validationResult = performModelValidation(RemoveGroup.class);
        // Update validation result model
        model.getValidationResultModel().setResult(validationResult);
        // Execute operation
        if(!validationResult.hasErrors()) {
            performDelete();
            // Clear model
            updateModel(Optional.empty());
            // Set form uneditable
            setEditMode(false);
            // Clear validation result
            clearValidationResultModel();
            // Update control bar
            updateCrudControlView();
        }
        // Repaint panel
        view.repaint();
        view.revalidate();
    }

    private void setupEditButtonActionListener() {
        Optional.ofNullable(crudControlView.getEditButton()).ifPresent(view -> {
            view.addActionListener(event -> {
                editButtonClickListener();
            });
        });
    }

    private void setupAddButtonActionListener() {
        Optional.ofNullable(crudControlView.getAddButton()).ifPresent(view -> {
            view.addActionListener(event -> {
                addButtonClickListener();
            });
        });
    }

    private void setupCancelButtonActionListener() {
        Optional.ofNullable(crudControlView.getCancelButton()).ifPresent(view -> {
            view.addActionListener(event -> {
                cancelButtonClickListener();
            });
        });
    }

    private void setupSaveButtonActionListener() {
        Optional.ofNullable(crudControlView.getSaveButton()).ifPresent(view -> {
            view.addActionListener(event -> {
                saveButtonClickListener();
            });
        });
    }

    private void setupRemoveButtonActionListener() {
        Optional.ofNullable(crudControlView.getRemoveButton()).ifPresent(view -> {
            view.addActionListener(event -> {
                removeButtonClickListener();
            });
        });
    }

    private void clearValidationResultModel() {
        model.getValidationResultModel().setResult(new ValidationResult());
    }

}