package ui.volume.search.item.renderer.view;

import java.awt.Font;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import ui.volume.search.item.domain.LocalVolumeListItemModel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class LocalVolumePanel extends JPanel {
    public LocalVolumePanel(LocalVolumeListItemModel volume) {
        setLayout(new FormLayout(new ColumnSpec[] {
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
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        // Right pad with zeros: 1 -> 00001
        JLabel idLabel = new JLabel(String.format("%05d", volume.getId()));
        add(idLabel, "2, 2, 1, 3");
        
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        add(separator, "4, 2, 1, 3");

        JLabel titleLable = new JLabel(volume.getTitle());
        titleLable.setFont(new Font("Tahoma", Font.BOLD, 11));
        add(titleLable, "6, 2");

        JLabel authorLabel = new JLabel(Optional
            .ofNullable(volume.getAuthors())
            .map(authors -> String.join(", ", authors))
            .orElse("-"));
        add(authorLabel, "6, 4");
    }
}
