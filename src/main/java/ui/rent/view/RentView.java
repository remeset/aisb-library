package ui.rent.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

import ui.core.crud.view.CrudControlView;
import ui.core.message.MessageResolver;

@SuppressWarnings("serial")
public class RentView extends JPanel implements CrudControlView {
    private final JButton saveButton;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton cancelButton;
    private final JButton removeButton;
    private final RentEditorView rentEditorView;
    private final RentSearchView rentSearchView;

    /**
     * @wbp.parser.constructor
     */
    @SuppressWarnings("unused")
    private RentView(MessageResolver messageResolver) {
        this(new DefaultValidationResultModel(), messageResolver);
    }

    public RentView(ValidationResultModel validationResultModel, MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("right:default"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("right:default"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JToolBar controlBar = new JToolBar();
        controlBar.setFloatable(false);
        add(controlBar, "1, 1, 13, 1");

        addButton = new JButton(messageResolver.getMessage("i18n.rent.control.new"));
        controlBar.add(addButton);

        editButton = new JButton(messageResolver.getMessage("i18n.rent.control.edit"));
        controlBar.add(editButton);

        saveButton = new JButton(messageResolver.getMessage("i18n.rent.control.save"));
        controlBar.add(saveButton);

        cancelButton = new JButton(messageResolver.getMessage("i18n.rent.control.cancel"));
        controlBar.add(cancelButton);

        removeButton = new JButton(messageResolver.getMessage("i18n.rent.control.delete"));
        controlBar.add(removeButton);

        rentEditorView = new RentEditorView(validationResultModel, messageResolver);
        add(rentEditorView, "2, 3, 11, 1, fill, fill");

        rentSearchView = new RentSearchView(messageResolver);
        rentSearchView.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(rentSearchView, "2, 5, 11, 1, fill, fill");
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

    public JButton getSaveButton() {
        return saveButton;
    }

    public RentEditorView getRentEditorView() {
        return rentEditorView;
    }

    public RentSearchView getRentSearchView() {
        return rentSearchView;
    }

}
