package ui.core.table.renderer.builder;

import java.util.Optional;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class CheckboxTableCellEditorBuilder {
    private Integer alignment;

    public CheckboxTableCellEditorBuilder withHorizontalAlignment(int alignment) {
        this.alignment = alignment;
        return this;
    }

    public DefaultCellEditor build() {
        JCheckBox checkbox = new JCheckBox();
        checkbox.setHorizontalAlignment(Optional.ofNullable(alignment).orElse(SwingConstants.LEFT));
        DefaultCellEditor editor = new DefaultCellEditor(checkbox);
        return editor;
    }
}
