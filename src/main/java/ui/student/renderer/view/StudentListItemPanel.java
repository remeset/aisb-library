package ui.student.renderer.view;

import java.awt.Font;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import ui.student.renderer.model.StudentListItemModel;

@SuppressWarnings("serial")
public class StudentListItemPanel extends JPanel {
    public StudentListItemPanel(StudentListItemModel model) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("right:default"),
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JLabel nameLabel = new JLabel(model.getName());
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        add(nameLabel, "2, 2");
        
        JLabel numberOfRentedBooksLabel = new JLabel(String.join("", Collections.nCopies(model.getNumberOfRentedBooks(), "●")));
        numberOfRentedBooksLabel.setFont(new Font("Tahoma", Font.PLAIN, 8));
        add(numberOfRentedBooksLabel, "4, 2");

        JLabel emailLabel = new JLabel(model.getEmail());
        add(emailLabel, "2, 4, 3, 1");
    }

}
