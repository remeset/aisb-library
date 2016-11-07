package ui.core.table.renderer;

import java.awt.Component;
import java.util.Optional;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class CheckboxTableCellRenerer extends JCheckBox implements TableCellRenderer {
    public CheckboxTableCellRenerer() {
        setHorizontalAlignment(JLabel.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelected(Optional
            .ofNullable(value)
            .map(v -> ((Boolean) v).booleanValue())
            .orElse(false));
        return this;
    }
}
