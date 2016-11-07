package ui.core.table.editor;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class SpinnerTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JSpinner spinner;

    public SpinnerTableCellEditor(SpinnerModel model) {
        this.spinner = new JSpinner(model);
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Update value
        spinner.setValue(value);
        return spinner;
    }
}
