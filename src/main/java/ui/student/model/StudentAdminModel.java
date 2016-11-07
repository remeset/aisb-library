package ui.student.model;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.core.style.ToStringCreator;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

import ui.core.crud.model.CrudModel;
import ui.student.renderer.model.StudentListItemModel;
import ui.validation.annotation.StudentWithPendingVolume;
import ui.validation.annotation.UniqueStudentEmail;
import ui.validation.group.MergeGroup;
import ui.validation.group.PersistGroup;
import ui.validation.group.RemoveGroup;

public class StudentAdminModel implements CrudModel<Long> {
    @StudentWithPendingVolume(message = "{i18n.student.validation.pendingVolumes}", groups = RemoveGroup.class)
    private Long id;
    @NotBlank(message = "{i18n.student.validation.blankFirstname}", groups = {PersistGroup.class, MergeGroup.class})
    private String firstname;
    @NotBlank(message = "{i18n.student.validation.blankLastname}", groups = {PersistGroup.class, MergeGroup.class})
    private String lastname;
    @NotBlank(message = "{i18n.student.validation.blankEmailAddress}", groups = {PersistGroup.class, MergeGroup.class})
    @Email(message = "{i18n.student.validation.wrongEmailAddressFormat}", groups = {PersistGroup.class, MergeGroup.class})
    @UniqueStudentEmail(message = "{i18n.student.validation.emailAddressIsNotUnique}", groups = PersistGroup.class)
    private String email;
    private String pattern;
    private List<StudentListItemModel> studentListItemModels;
    private StudentListItemModel selectedStudentListItemModel;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    private final ValidationResultModel validationResultModel = new DefaultValidationResultModel();

    private StudentAdminModel(Builder builder) {
        this.id = builder.id;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.pattern = builder.pattern;
        this.studentListItemModels = builder.studentListItemModels;
        this.selectedStudentListItemModel = builder.selectedStudentListItemModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        changeSupport.firePropertyChange("id", this.id, this.id = id);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        changeSupport.firePropertyChange("firstname", this.firstname, this.firstname = firstname);
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        changeSupport.firePropertyChange("lastname", this.lastname, this.lastname = lastname);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        changeSupport.firePropertyChange("email", this.email, this.email = email);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        changeSupport.firePropertyChange("pattern", this.pattern, this.pattern = pattern);
    }

    public List<StudentListItemModel> getStudentListItemModels() {
        return studentListItemModels;
    }

    public void setStudentListItemModels(List<StudentListItemModel> studentListItemModels) {
        changeSupport.firePropertyChange("studentListItemModels", this.studentListItemModels, this.studentListItemModels = studentListItemModels);
    }

    public StudentListItemModel getSelectedStudentListItemModel() {
        return selectedStudentListItemModel;
    }

    public void setSelectedStudentListItemModel(StudentListItemModel selectedStudentListItemModel) {
        changeSupport.firePropertyChange("selectedStudentListItemModel", this.selectedStudentListItemModel, this.selectedStudentListItemModel = selectedStudentListItemModel);
    }

    @Override
    public ValidationResultModel getValidationResultModel() {
        return validationResultModel;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("id", id)
            .append("firstname", firstname)
            .append("lastname", lastname)
            .append("email", email)
            .toString();
    }

    public static class Builder {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private String pattern;
        private List<StudentListItemModel> studentListItemModels;
        private StudentListItemModel selectedStudentListItemModel;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder withLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder withStudentListItemModels(List<StudentListItemModel> studentListItemModels) {
            this.studentListItemModels = studentListItemModels;
            return this;
        }

        public Builder withSelectedStudentListItemModel(StudentListItemModel selectedStudentListItemModel) {
            this.selectedStudentListItemModel = selectedStudentListItemModel;
            return this;
        }

        public StudentAdminModel build() {
            return new StudentAdminModel(this);
        }
    }

}
