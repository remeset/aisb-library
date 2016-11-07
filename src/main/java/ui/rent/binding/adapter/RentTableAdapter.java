package ui.rent.binding.adapter;

import java.util.List;

import ui.core.treetable.adapter.AbstractListBasedTreeTableModel;
import ui.rent.model.RentTableModel;

public class RentTableAdapter extends AbstractListBasedTreeTableModel<RentTableModel<?>> {
    public static final int ID_COLUMN_INDEX = 0;
    public static final int STUDENT_COLUMN_INDEX = 1;
    public static final int VOLUME_COLUMN_INDEX = 2;
    public static final int AMOUNT_COLUMN_INDEX = 3;
    public static final int FROM_COLUMN_INDEX = 4;
    public static final int TO_COLUMN_INDEX = 5;

    public RentTableAdapter(List<? extends RentTableModel<?>> model, String[] columnNames) {
        super(model, columnNames);
    }

    protected Object getValueAt(RentTableModel<?> model, int column) {
        Object result = null;
        switch (column) {
            case ID_COLUMN_INDEX:
                result = String.format("%02d", model.getId());
                break;
            case STUDENT_COLUMN_INDEX:
                result = model.getStudent();
                break;
            case VOLUME_COLUMN_INDEX:
                result = model.getVolume();
                break;
            case AMOUNT_COLUMN_INDEX:
                result = model.getAmount();
                break;
            case FROM_COLUMN_INDEX:
                result = model.getFrom();
                break;
            case TO_COLUMN_INDEX:
                result = model.getTo();
                break;
        }
        return result;
    }
}
