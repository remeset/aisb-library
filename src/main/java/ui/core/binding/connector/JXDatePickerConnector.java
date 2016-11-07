package ui.core.binding.connector;

import static com.jgoodies.common.base.Preconditions.checkNotNull;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXDatePicker;

import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.base.Objects;

public class JXDatePickerConnector {
    private final ValueModel subject;
    private final JXDatePicker datePicker;
    private final SubjectValueChangeHandler subjectValueChangeHandler;
    private final ActionListener actionHandler;

    public JXDatePickerConnector(ValueModel subject, JXDatePicker datePicker) {
        this.subject = checkNotNull(subject, MUST_NOT_BE_NULL, "subject");
        this.datePicker = checkNotNull(datePicker, MUST_NOT_BE_NULL, "text component");
        this.subjectValueChangeHandler = new SubjectValueChangeHandler();
        this.actionHandler = new DatePickerActionHandler();
        datePicker.addActionListener(actionHandler);
        subject.addValueChangeListener(subjectValueChangeHandler);
    }

    public void updateSubject() {
        setSubjectDate(getDatePickerDate());
    }

    public void updateComponent() {
        setDatePickerDateSilently(getSubjectDate());
    }

    private Date getSubjectDate() {
        return (Date) subject.getValue();
    }

    private void setSubjectDate(Date newDate) {
        subjectValueChangeHandler.setUpdateLater(true);
        subject.setValue(newDate);
        subjectValueChangeHandler.setUpdateLater(false);
    }
    
    private Date getDatePickerDate() {
        return datePicker.getDate();
    }

    private void setDatePickerDateSilently(Date newDate) {
        datePicker.removeActionListener(actionHandler);
        datePicker.setDate(newDate);
        datePicker.addActionListener(actionHandler);
    }

    private final class DatePickerActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            updateSubject();
        }
    }
    
    private final class SubjectValueChangeHandler implements PropertyChangeListener {
        private boolean updateLater;

        void setUpdateLater(boolean updateLater) {
            this.updateLater = updateLater;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            final Date oldText = getDatePickerDate();
            final Date newText = (Date)evt.getNewValue();
            if (Objects.equals(oldText, newText)) {
                return;
            }
            if (updateLater) {
                SwingUtilities.invokeLater(() -> setDatePickerDateSilently(newText));
            } else {
                setDatePickerDateSilently(newText);
            }
        }
    }
}