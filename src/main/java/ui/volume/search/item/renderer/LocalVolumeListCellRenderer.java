package ui.volume.search.item.renderer;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.renderer.view.LocalVolumePanel;

public class LocalVolumeListCellRenderer implements ListCellRenderer<LocalVolumeListItemModel> {
    @Override
    public Component getListCellRendererComponent(JList<? extends LocalVolumeListItemModel> list, LocalVolumeListItemModel value, int index, boolean isSelected, boolean cellHasFocus) {
        return new LocalVolumePanel(value);
    }
}
