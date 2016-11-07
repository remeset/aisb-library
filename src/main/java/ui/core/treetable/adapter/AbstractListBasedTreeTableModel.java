package ui.core.treetable.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import ui.core.treetable.model.TreeTableLeafModel;
import ui.core.treetable.model.TreeTableModel;
import ui.core.treetable.model.TreeTableRootModel;
import ui.rent.model.RentTableModel;

public abstract class AbstractListBasedTreeTableModel<V extends TreeTableModel> extends AbstractTreeTableModel {
    final String[] columnNames;

    public AbstractListBasedTreeTableModel(List<? extends V> model, String[] columnNames) {
        super(model);
        this.columnNames = columnNames;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof TreeTableLeafModel;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getValueAt(Object node, int column) {
        Object result = null;
        if(node instanceof RentTableModel) {
            result = getValueAt((V)node, column);
        }
        return result;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return getChildren(parent).get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return getChildren(parent).size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return getChildren(parent).indexOf(child);
    }

    abstract protected Object getValueAt(V model, int column);

    @SuppressWarnings("unchecked")
    protected List<? extends V> getChildren(Object parent) {
        if(parent instanceof TreeTableRootModel) {
            return Optional.ofNullable(((TreeTableRootModel<? extends V>)parent)
                    .getChildren())
                    .orElse(Collections.emptyList());
        } else {
            return (List<V>) parent;
        }
    }

}