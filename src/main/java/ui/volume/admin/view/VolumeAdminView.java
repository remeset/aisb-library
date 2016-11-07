package ui.volume.admin.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import ui.core.crud.view.CrudControlView;
import ui.core.message.MessageResolver;
import ui.volume.search.query.view.VolumeSearchQueryModuleView;

@SuppressWarnings("serial")
public class VolumeAdminView extends JPanel implements CrudControlView {
    private final JTextField idField;
    private final JTextField remoteIdField;
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField isbnField;
    private final JTextField publisherField;
    private final JTextField publishedDateField;
    private final JTextArea descriptionTextArea;
    private final JComboBox<Locale> languageComboBox;
    private final JSpinner numberOfVolumesSpinner;
    private final JButton saveButton;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton cancelButton;
    private final JButton removeButton;
    private final VolumeSearchQueryModuleView volumeSearchQueryModuleView;

    /**
     * @wbp.parser.constructor
     */
    @SuppressWarnings("unused")
    private VolumeAdminView(MessageResolver messageResolver) {
        this(new DefaultValidationResultModel(), messageResolver);
    }

    public VolumeAdminView(ValidationResultModel validationResultModel, MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.UNRELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JToolBar controlBar = new JToolBar();
        controlBar.setFloatable(false);
        add(controlBar, "1, 1, 9, 1");

        addButton = new JButton(messageResolver.getMessage("i18n.volume.control.add"));
        controlBar.add(addButton);

        editButton = new JButton(messageResolver.getMessage("i18n.volume.control.edit"));
        controlBar.add(editButton);

        saveButton = new JButton(messageResolver.getMessage("i18n.volume.control.save"));
        controlBar.add(saveButton);
        
        cancelButton = new JButton(messageResolver.getMessage("i18n.volume.control.cancel"));
        controlBar.add(cancelButton);
        
        removeButton = new JButton(messageResolver.getMessage("i18n.volume.control.remove"));
        controlBar.add(removeButton);

        JComponent validationResultsComponent = ValidationResultViewFactory.createReportList(validationResultModel);
        add(validationResultsComponent, "4, 3, 5, 1");

        JLabel idLabel = new JLabel("Id");
        add(idLabel, "2, 5, right, default");

        idField = new JTextField();
        add(idField, "4, 5, fill, default");
        idField.setColumns(10);

        JLabel remoteIdLabel = new JLabel("Remote Id");
        add(remoteIdLabel, "6, 5, right, default");

        remoteIdField = new JTextField();
        add(remoteIdField, "8, 5, fill, default");
        remoteIdField.setColumns(10);

        JLabel titleLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.title"));
        add(titleLabel, "2, 7, right, default");

        titleField = new JTextField();
        add(titleField, "4, 7, 5, 1");
        titleField.setColumns(10);

        JLabel authorLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.author"));
        add(authorLabel, "2, 9, right, default");

        authorField = new JTextField();
        add(authorField, "4, 9, 5, 1");
        authorField.setColumns(10);

        JLabel isbnLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.ISBN"));
        add(isbnLabel, "2, 11, right, default");

        isbnField = new JTextField();
        add(isbnField, "4, 11, 5, 1, fill, default");
        isbnField.setColumns(10);

        JLabel descriptionLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.description"));
        add(descriptionLabel, "2, 13, right, default");

        JScrollPane descriptionScrollPane = new JScrollPane();
        descriptionScrollPane.setMinimumSize(new Dimension(23, 88));
        add(descriptionScrollPane, "4, 13, 5, 1, fill, fill");

        descriptionTextArea = new JTextArea();
        descriptionTextArea.setRows(4);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        JLabel publisherLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.publisher"));
        add(publisherLabel, "2, 15, right, default");

        publisherField = new JTextField();
        add(publisherField, "4, 15, 5, 1, fill, default");
        publisherField.setColumns(10);

        JLabel publishedDateLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.publishedDate"));
        add(publishedDateLabel, "2, 17, right, default");

        publishedDateField = new JTextField();
        add(publishedDateField, "4, 17, 5, 1, fill, default");
        publishedDateField.setColumns(10);

        JLabel languageLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.language"));
        add(languageLabel, "2, 19, right, default");

        languageComboBox = new JComboBox<Locale>();
        add(languageComboBox, "4, 19, 5, 1, fill, default");

        JLabel numberOfVolumesLabel = new JLabel(messageResolver.getMessage("i18n.volume.form.numberOfVolumes"));
        add(numberOfVolumesLabel, "2, 21, right, default");

        numberOfVolumesSpinner = new JSpinner();
        add(numberOfVolumesSpinner, "4, 21, 5, 1");

        volumeSearchQueryModuleView = new VolumeSearchQueryModuleView(messageResolver);
        volumeSearchQueryModuleView.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Search", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        add(volumeSearchQueryModuleView, "2, 23, 7, 1, fill, fill");
    }

    @Override
    public JButton getAddButton() {
        return addButton;
    }

    @Override
    public JButton getEditButton() {
        return editButton;
    }

    @Override
    public JButton getSaveButton() {
        return saveButton;
    }

    @Override
    public JButton getCancelButton() {
        return cancelButton;
    }

    @Override
    public JButton getRemoveButton() {
        return removeButton;
    }

    public JTextField getIdField() {
        return idField;
    }

    public JTextField getRemoteIdField() {
        return remoteIdField;
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public JTextField getAuthorField() {
        return authorField;
    }

    public JTextField getIsbnField() {
        return isbnField;
    }

    public JTextField getPublisherField() {
        return publisherField;
    }

    public JTextField getPublishedDateField() {
        return publishedDateField;
    }

    public JComboBox<Locale> getLanguageComboBox() {
        return languageComboBox;
    }

    public JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public JSpinner getNumberOfVolumesSpinner() {
        return numberOfVolumesSpinner;
    }

    public VolumeSearchQueryModuleView getSearchView() {
        return volumeSearchQueryModuleView;
    }
}
