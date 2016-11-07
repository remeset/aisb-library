package ui.rent.view;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXDatePicker;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import ui.core.message.MessageResolver;
import ui.rent.student.model.StudentCellRendererModel;

@SuppressWarnings("serial")
public class RentEditorView extends JPanel {
    private final JComboBox<StudentCellRendererModel> studentComboBox;
    private final JXDatePicker fromDatePicker;
    private final JXDatePicker toDatePicker;
    private final JButton addVolumeButton;
    private final JXBusyLabel studentBusyLabel;
    private final JXBusyLabel volumesBusyLabel;
    private final JButton studentRefreshButton;
    private final JButton volumesRefreshButton;
    private final JTable volumeTable;

    /**
     * @wbp.parser.constructor
     */
    @SuppressWarnings("unused")
    private RentEditorView(MessageResolver messageResolver) {
        this(new DefaultValidationResultModel(), messageResolver);
    }

    public RentEditorView(ValidationResultModel validationResultModel, MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
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
                FormSpecs.DEFAULT_COLSPEC,},
            new RowSpec[] {
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,}));

        JComponent validationResultsComponent = ValidationResultViewFactory.createReportList(validationResultModel);
        add(validationResultsComponent, "5, 1, 5, 1");

        studentBusyLabel = new JXBusyLabel();
        add(studentBusyLabel, "1, 3, right, default");

        JLabel studentLabel = new JLabel("Student");
        add(studentLabel, "3, 3, right, default");

        studentComboBox = new JComboBox<StudentCellRendererModel>();
        studentComboBox.setEditable(true);
        add(studentComboBox, "5, 3, 5, 1, fill, default");

        studentRefreshButton = new JButton("↻");
        studentRefreshButton.setMargin(new Insets(2, 2, 2, 2));
        add(studentRefreshButton, "11, 3");

        JLabel fromLabel = new JLabel("From");
        add(fromLabel, "1, 5, 3, 1, right, default");

        fromDatePicker = new JXDatePicker();
        add(fromDatePicker, "5, 5");

        JLabel toLabel = new JLabel("To");
        add(toLabel, "7, 5");

        toDatePicker = new JXDatePicker();
        add(toDatePicker, "9, 5");

        volumesBusyLabel = new JXBusyLabel();
        add(volumesBusyLabel, "1, 7");

        JLabel volumesLabel = new JLabel("Volumes");
        add(volumesLabel, "3, 7");

        JScrollPane volumeScrollPane = new JScrollPane();
        add(volumeScrollPane, "5, 7, 5, 1, fill, fill");

        volumeTable = new JTable();
        volumeTable.setPreferredScrollableViewportSize(new Dimension(450, 250));
        volumeScrollPane.setViewportView(volumeTable);

        volumesRefreshButton = new JButton("↻");
        volumesRefreshButton.setMargin(new Insets(2, 2, 2, 2));
        add(volumesRefreshButton, "11, 7, default, fill");

        addVolumeButton = new JButton("Add Volume");
        add(addVolumeButton, "5, 9, 5, 1");
    }

    public JComboBox<StudentCellRendererModel> getStudentComboBox() {
        return studentComboBox;
    }

    public JXDatePicker getFromDatePicker() {
        return fromDatePicker;
    }

    public JXDatePicker getToDatePicker() {
        return toDatePicker;
    }

    public JTable getVolumeTable() {
        return volumeTable;
    }

    public JButton getAddVolumeButton() {
        return addVolumeButton;
    }

    public JXBusyLabel getStudentBusyLabel() {
        return studentBusyLabel;
    }

    public JXBusyLabel getVolumesBusyLabel() {
        return volumesBusyLabel;
    }

    public JButton getStudentRefreshButton() {
        return studentRefreshButton;
    }

    public JButton getVolumesRefreshButton() {
        return volumesRefreshButton;
    }
}
