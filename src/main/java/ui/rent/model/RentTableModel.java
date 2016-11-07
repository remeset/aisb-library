package ui.rent.model;

import java.util.Date;

import ui.core.treetable.model.TreeTableModel;
import ui.rent.student.model.StudentCellRendererModel;
import ui.rent.volume.model.Volume;

public interface RentTableModel<E> extends TreeTableModel {

    Long getId();

    StudentCellRendererModel getStudent();

    Volume getVolume();

    Integer getAmount();

    Date getFrom();

    Date getTo();

    E getEntity();

}