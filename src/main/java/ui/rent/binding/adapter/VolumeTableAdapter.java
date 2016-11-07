package ui.rent.binding.adapter;

import java.util.Date;

import javax.swing.ListModel;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

import ui.rent.volume.model.VolumeCellRenderModel;
import ui.rent.volume.model.VolumeTableListItemModel;

/**
 * This binding adapter class is required to be able to bind {@link VolumeTableListItemModel} to specific table columns.
 */
@SuppressWarnings("serial")
public class VolumeTableAdapter extends AbstractTableAdapter<VolumeTableListItemModel> {
    public static final int ROW_NUMBER_COLUMN_INDEX = 0;
    public static final int VOLUME_COLUMN_INDEX = 1;
    public static final int AMOUNT_COLUMN_INDEX = 2;
    public static final int RETURNED_COLUMN_INDEX = 3;
    public static final int CONTROL_COLUMN_INDEX = 4;

    // For more information why do we need this is described in ui.rent.control.RentControl.RentControl(RentView, RentModel, VolumeDAO, StudentDAO, MessageResolver)
    private static final String REMOVE_BUTTON_LABEL = "✖";

    public VolumeTableAdapter(ListModel<VolumeTableListItemModel> listModel, String[] columnNames) {
        super(listModel, columnNames);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != ROW_NUMBER_COLUMN_INDEX;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(this.getRowCount() >= rowIndex) {
            VolumeTableListItemModel model = getRow(rowIndex);
            switch (columnIndex) {
                case VOLUME_COLUMN_INDEX:
                    model.setVolume((VolumeCellRenderModel) aValue);
                    break;
                case AMOUNT_COLUMN_INDEX:
                    model.setAmount((Integer) aValue);
                    break;
                case RETURNED_COLUMN_INDEX:
                    model.setReturnDate((Date) aValue);
                    break;
                }
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        VolumeTableListItemModel model = getRow(rowIndex);
        switch (columnIndex) {
            case ROW_NUMBER_COLUMN_INDEX:
                return rowIndex + 1;
            case VOLUME_COLUMN_INDEX:
                return model.getVolume();
            case AMOUNT_COLUMN_INDEX:
                return model.getAmount();
            case RETURNED_COLUMN_INDEX:
                return model.getReturnDate();
            default:
                return REMOVE_BUTTON_LABEL;
        }
    }
}
