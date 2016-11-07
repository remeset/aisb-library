package ui.rent.view;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTreeTable;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import ui.core.message.MessageResolver;

@SuppressWarnings("serial")
public class RentSearchView extends JPanel {
    private final JTextField searchPatternField;
    private final JXDatePicker searchFromDatePicker;
    private final JXDatePicker searchToDatePicker;
    private final JCheckBox searchPendingOnlyCheckBox;
    private final JXTreeTable rentTreeTable;
    private final JLabel totalPageNumberLabel;
    private final JSpinner pageNumberSpinner;
    private final JLabel pageNumberLabel;
    private final JProgressBar searchProgressBar;

    public RentSearchView(MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
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
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JLabel patternLabel = new JLabel("Pattern");
        add(patternLabel, "2, 2, right, default");

        searchPatternField = new JTextField();
        add(searchPatternField, "4, 2, 13, 1, fill, default");
        searchPatternField.setColumns(10);

        JLabel fromLabel = new JLabel("From");
        add(fromLabel, "2, 4");

        searchFromDatePicker = new JXDatePicker();
        add(searchFromDatePicker, "4, 4");

        JLabel toLabel = new JLabel("To");
        add(toLabel, "6, 4");

        searchToDatePicker = new JXDatePicker();
        add(searchToDatePicker, "8, 4");

        searchPendingOnlyCheckBox = new JCheckBox("Pending only");
        add(searchPendingOnlyCheckBox, "10, 4, 7, 1");

        JScrollPane statusScrollPane = new JScrollPane();
        add(statusScrollPane, "2, 6, 15, 1, fill, fill");

        rentTreeTable = new JXTreeTable();
        statusScrollPane.setViewportView(rentTreeTable);

        searchProgressBar = new JProgressBar();
        add(searchProgressBar, "2, 8, 7, 1");

        pageNumberLabel = new JLabel("Page Number:");
        add(pageNumberLabel, "10, 8, right, default");

        pageNumberSpinner = new JSpinner();
        add(pageNumberSpinner, "12, 8");
        pageNumberSpinner.setPreferredSize(new Dimension(40, 20));

        JLabel perLabel = new JLabel("/");
        add(perLabel, "14, 8");

        totalPageNumberLabel = new JLabel("1");
        add(totalPageNumberLabel, "16, 8");
    }

    public JTextField getSearchPatternField() {
        return searchPatternField;
    }

    public JXDatePicker getSearchFromDatePicker() {
        return searchFromDatePicker;
    }

    public JXDatePicker getSearchToDatePicker() {
        return searchToDatePicker;
    }

    public JCheckBox getSearchPendingOnlyCheckBox() {
        return searchPendingOnlyCheckBox;
    }

    public JXTreeTable getRentTreeTable() {
        return rentTreeTable;
    }

    public JLabel getTotalPageNumberLabel() {
        return totalPageNumberLabel;
    }

    public JSpinner getPageNumberSpinner() {
        return pageNumberSpinner;
    }

    public JLabel getPageNumberLabel() {
        return pageNumberLabel;
    }

    public JProgressBar getSearchProgressBar() {
        return searchProgressBar;
    }
}
