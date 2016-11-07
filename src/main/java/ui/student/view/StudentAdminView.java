package ui.student.view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
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
import ui.student.renderer.model.StudentListItemModel;

@SuppressWarnings("serial")
public class StudentAdminView extends JPanel implements CrudControlView {
    private final JButton saveButton;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton cancelButton;
    private final JButton removeButton;
    private final JTextField idField;
    private final JTextField emailField;
    private final JTextField firstnameField;
    private final JTextField lastnameField;
    private final JTextField patternField;
    private final JList<StudentListItemModel> searchResultList;
    private final JProgressBar searchProgressBar;

    /**
     * @wbp.parser.constructor
     */
    @SuppressWarnings("unused")
    private StudentAdminView(MessageResolver messageResolver) {
        this(new DefaultValidationResultModel(), messageResolver);
    }

    public StudentAdminView(ValidationResultModel validationResultModel, MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("right:default:grow"),
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
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JToolBar controlBar = new JToolBar();
        controlBar.setFloatable(false);
        add(controlBar, "1, 1, 9, 1");

        addButton = new JButton(messageResolver.getMessage("i18n.student.control.add"));
        controlBar.add(addButton);

        editButton = new JButton(messageResolver.getMessage("i18n.student.control.edit"));
        controlBar.add(editButton);

        saveButton = new JButton(messageResolver.getMessage("i18n.student.control.save"));
        controlBar.add(saveButton);

        cancelButton = new JButton(messageResolver.getMessage("i18n.student.control.cancel"));
        controlBar.add(cancelButton);

        removeButton = new JButton(messageResolver.getMessage("i18n.student.control.remove"));
        controlBar.add(removeButton);

        JComponent validationResultsComponent = ValidationResultViewFactory.createReportList(validationResultModel);
        add(validationResultsComponent, "4, 3, 5, 1");

        JLabel idLabel = new JLabel("Id");
        add(idLabel, "2, 5, right, default");

        idField = new JTextField();
        add(idField, "4, 5, fill, default");
        idField.setColumns(10);

        JLabel firstNameLabel = new JLabel("Firstname");
        add(firstNameLabel, "2, 9, right, default");

        firstnameField = new JTextField();
        add(firstnameField, "4, 9, fill, default");
        firstnameField.setColumns(10);

        JLabel surnameLabel = new JLabel("Surname");
        add(surnameLabel, "6, 9, right, default");

        lastnameField = new JTextField();
        add(lastnameField, "8, 9, fill, default");
        lastnameField.setColumns(10);

        JLabel emailLabel = new JLabel("Email");
        add(emailLabel, "2, 7, right, default");

        emailField = new JTextField();
        add(emailField, "4, 7, 5, 1, fill, default");

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(searchPanel, "2, 11, 7, 1, fill, fill");
        searchPanel.setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JLabel patternLabel = new JLabel("Pattern");
        searchPanel.add(patternLabel, "2, 2, right, default");

        patternField = new JTextField();
        searchPanel.add(patternField, "4, 2, fill, default");
        patternField.setColumns(10);

        JScrollPane searchResultScrollPane = new JScrollPane();
        searchPanel.add(searchResultScrollPane, "2, 4, 3, 1, fill, fill");

        searchResultList = new JList<>();
        searchResultScrollPane.setViewportView(searchResultList);

        searchProgressBar = new JProgressBar();
        searchProgressBar.setVisible(false);
        searchProgressBar.setIndeterminate(true);
        searchPanel.add(searchProgressBar, "2, 6, 3, 1");
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JTextField getIdField() {
        return idField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getFirstnameField() {
        return firstnameField;
    }

    public JTextField getLastnameField() {
        return lastnameField;
    }

    public JTextField getPatternField() {
        return patternField;
    }

    public JList<StudentListItemModel> getSearchResultList() {
        return searchResultList;
    }

    public JProgressBar getSearchProgressBar() {
        return searchProgressBar;
    }

}
