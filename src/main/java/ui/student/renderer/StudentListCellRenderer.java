package ui.student.renderer;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ui.student.renderer.model.StudentListItemModel;
import ui.student.renderer.view.StudentListItemPanel;

public class StudentListCellRenderer implements ListCellRenderer<StudentListItemModel> {
    @Override
    public Component getListCellRendererComponent(JList<? extends StudentListItemModel> list, StudentListItemModel value, int index, boolean isSelected, boolean cellHasFocus) {
        return new StudentListItemPanel(value);
    }
}
