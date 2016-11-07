package ui.rent.model;

import ui.core.treetable.model.TreeTableRootModel;

public interface RentTableRootModel<E, F> extends RentTableModel<E>, TreeTableRootModel<RentTableModel<F>> {

}