package ui.volume.search.item.renderer.view;

import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import ui.volume.search.item.domain.RemoteVolumeListItemModel;

import java.awt.Font;

@SuppressWarnings("serial")
public class RemoteVolumePanel extends JPanel {
    public RemoteVolumePanel(RemoteVolumeListItemModel volume) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("16dlu"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JLabel thumbnailImage = new JLabel(new ImageIcon(volume.getThumbnail()));
        add(thumbnailImage, "2, 2, 1, 3");

        JLabel titleLable = new JLabel(volume.getTitle());
        titleLable.setFont(new Font("Tahoma", Font.BOLD, 11));
        add(titleLable, "4, 2");

        JLabel authorLabel = new JLabel(Optional
            .ofNullable(volume.getAuthors())
            .map(authors -> String.join(", ", authors))
            .orElse("-"));
        add(authorLabel, "4, 4");
    }
}
