package ui.rent.volume.control;

import java.text.DecimalFormat;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;

import ui.rent.volume.model.VolumeTableListItemModel;
import ui.rent.volume.view.VolumeSelectorListItemView;

public class VolumeSelectorListItemControl {
    public VolumeSelectorListItemControl(VolumeSelectorListItemView view, VolumeTableListItemModel model) {
        // Setup binding
        PresentationModel<VolumeTableListItemModel> adapter = new PresentationModel<>(model);
        Bindings.bind(view.getIndexLabel(), ConverterFactory.createStringConverter(adapter.getModel("index"), DecimalFormat.getInstance()));
        Bindings.bind(view.getVolumeComboBox(), new SelectionInList<>(adapter.getModel("volumes"), adapter.getModel("selectedVolume")));
    }
}
