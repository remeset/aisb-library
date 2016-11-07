package ui.volume.search.item.renderer;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ui.volume.search.item.domain.RemoteVolumeListItemModel;
import ui.volume.search.item.renderer.view.RemoteVolumePanel;

public class RemoteVolumeListCellRenderer implements ListCellRenderer<RemoteVolumeListItemModel> {
    @Override
    public Component getListCellRendererComponent(JList<? extends RemoteVolumeListItemModel> list, RemoteVolumeListItemModel value, int index, boolean isSelected, boolean cellHasFocus) {
        return new RemoteVolumePanel(value);
    }
}
