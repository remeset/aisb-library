package ui.core.list.selection;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class ToggleListSelectionWrapper implements ListSelectionModel {
    private final ListSelectionModel listSelectionModel;

    public ToggleListSelectionWrapper(ListSelectionModel listSelectionModel) {
        this.listSelectionModel = listSelectionModel;
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
        if (index0 == index1 && listSelectionModel.isSelectedIndex(index0)) {
            listSelectionModel.removeSelectionInterval(index0, index0);
        } else {
            listSelectionModel.setSelectionInterval(index0, index1);
        }
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        if (index0 == index1 && listSelectionModel.isSelectedIndex(index0)) {
            listSelectionModel.removeSelectionInterval(index0, index0);
        } else {
            listSelectionModel.addSelectionInterval(index0, index1);
        }
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
        listSelectionModel.removeSelectionInterval(index0, index1);
    }

    @Override
    public int getMinSelectionIndex() {
        return listSelectionModel.getMinSelectionIndex();
    }

    @Override
    public int getMaxSelectionIndex() {
        return listSelectionModel.getMaxSelectionIndex();
    }

    @Override
    public boolean isSelectedIndex(int index) {
        return listSelectionModel.isSelectedIndex(index);
    }

    @Override
    public int getAnchorSelectionIndex() {
        return listSelectionModel.getAnchorSelectionIndex();
    }

    @Override
    public void setAnchorSelectionIndex(int index) {
        listSelectionModel.setAnchorSelectionIndex(index);
    }

    @Override
    public int getLeadSelectionIndex() {
        return listSelectionModel.getLeadSelectionIndex();
    }

    @Override
    public void setLeadSelectionIndex(int index) {
        listSelectionModel.setLeadSelectionIndex(index);
    }

    @Override
    public void clearSelection() {
        listSelectionModel.clearSelection();
    }

    @Override
    public boolean isSelectionEmpty() {
        return listSelectionModel.isSelectionEmpty();
    }

    @Override
    public void insertIndexInterval(int index, int length, boolean before) {
        listSelectionModel.insertIndexInterval(index, length, before);
    }

    @Override
    public void removeIndexInterval(int index0, int index1) {
        listSelectionModel.removeIndexInterval(index0, index1);
    }

    @Override
    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        listSelectionModel.setValueIsAdjusting(valueIsAdjusting);
    }

    @Override
    public boolean getValueIsAdjusting() {
        return listSelectionModel.getValueIsAdjusting();
    }

    @Override
    public void setSelectionMode(int selectionMode) {
        listSelectionModel.setSelectionMode(selectionMode);
    }

    @Override
    public int getSelectionMode() {
        return listSelectionModel.getSelectionMode();
    }

    @Override
    public void addListSelectionListener(ListSelectionListener x) {
        listSelectionModel.addListSelectionListener(x);
    }

    @Override
    public void removeListSelectionListener(ListSelectionListener x) {
        listSelectionModel.removeListSelectionListener(x);
    }
}
