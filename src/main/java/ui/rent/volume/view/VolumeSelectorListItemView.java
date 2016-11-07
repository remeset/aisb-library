package ui.rent.volume.view;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import ui.rent.volume.model.VolumeCellRenderModel;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class VolumeSelectorListItemView extends JPanel {
    private final JLabel indexLabel;
    private final JComboBox<VolumeCellRenderModel> volumeComboBox;
    private final JButton removeButton;

    public VolumeSelectorListItemView() {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        indexLabel = new JLabel("");
        add(indexLabel, "2, 2, right, default");

        volumeComboBox = new JComboBox<>();
        volumeComboBox.setEditable(true);
        add(volumeComboBox, "4, 2, fill, default");

        removeButton = new JButton("✖");
        removeButton.setMargin(new Insets(2, 2, 2, 2));
        add(removeButton, "6, 2, center, default");
    }

    public JLabel getIndexLabel() {
        return indexLabel;
    }

    public JComboBox<VolumeCellRenderModel> getVolumeComboBox() {
        return volumeComboBox;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

}
