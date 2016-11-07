package ui.core.treetable.model;

import java.util.List;

public interface TreeTableRootModel<C extends TreeTableModel> extends TreeTableModel {

    List<? extends C> getChildren();

}