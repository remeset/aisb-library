package ui.core.table.renderer.builder;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import com.google.common.base.Optional;

public class ComboBoxTableCellEditorBuilder {
    private Boolean editable;

    public ComboBoxTableCellEditorBuilder withEditable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public DefaultCellEditor build() {
        JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.setEditable(Optional.fromNullable(editable).or(false));
        DefaultCellEditor editor = new DefaultCellEditor(comboBox);
        return editor;
    }
}
