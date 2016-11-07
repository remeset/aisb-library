package ui.core.table.editor;

import java.awt.Component;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.jdesktop.swingx.JXDatePicker;

@SuppressWarnings("serial")
public class DatePickerTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JXDatePicker datePicker;

    public DatePickerTableCellEditor() {
        this.datePicker = new JXDatePicker();
    }

    public DatePickerTableCellEditor(Locale locale) {
        this.datePicker = new JXDatePicker(locale);
    }

    @Override
    public Object getCellEditorValue() {
        return datePicker.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Update value
        datePicker.setDate((Date)value);
        return datePicker;
    }
}
