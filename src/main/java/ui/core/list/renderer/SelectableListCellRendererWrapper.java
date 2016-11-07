package ui.core.list.renderer;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class SelectableListCellRendererWrapper<E> implements ListCellRenderer<E> {
    private final ListCellRenderer<E> renderer;

    public SelectableListCellRendererWrapper(ListCellRenderer<E> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        Component result = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            result.setBackground(list.getSelectionBackground());
            result.setForeground(list.getSelectionForeground());
        } else if(!list.isEnabled() && list.getParent() != null) {
            result.setBackground(list.getParent().getBackground());
            result.setForeground(list.getParent().getBackground());
        } else {
            result.setBackground(list.getBackground());
            result.setForeground(list.getForeground());
        }
        return result;
    }

}
