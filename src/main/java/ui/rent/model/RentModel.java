package ui.rent.model;

import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.style.ToStringCreator;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

import ui.core.crud.model.CrudModel;
import ui.rent.student.model.StudentCellRendererModel;
import ui.rent.volume.model.VolumeCellRenderModel;
import ui.rent.volume.model.VolumeTableListItemModel;
import ui.validation.annotation.DateRange;
import ui.validation.annotation.DuplicateVolumeEntries;
import ui.validation.group.MergeGroup;
import ui.validation.group.PersistGroup;
import ui.validation.model.DateRangeModel;

@DateRange(message = "{i18n.rent.validation.dateOrder}", groups = {PersistGroup.class, MergeGroup.class})
public class RentModel implements CrudModel<Long>, DateRangeModel {
    private Long id;
    @NotNull(message = "{i18n.rent.validation.emptyStudent}", groups = {PersistGroup.class, MergeGroup.class})
    private StudentCellRendererModel selectedStudent;
    private List<StudentCellRendererModel> students;
    @NotNull(message = "{i18n.rent.validation.emptyFromDate}", groups = {PersistGroup.class, MergeGroup.class})
    private Date from;
    @NotNull(message = "{i18n.rent.validation.emptyToDate}", groups = {PersistGroup.class, MergeGroup.class})
    private Date to;
    @Valid
    @DuplicateVolumeEntries(message = "{i18n.rent.validation.duplicateVolume}", groups = {PersistGroup.class, MergeGroup.class})
    @NotEmpty(message = "{i18n.rent.validation.emptyVolume}", groups = {PersistGroup.class, MergeGroup.class})
    private List<VolumeTableListItemModel> volumes;
    private List<VolumeCellRenderModel> availableVolumes;
    private String searchPattern;
    private Date searchFrom;
    private Date searchTo;
    private boolean searchPendingOnly;
    private Integer pageNumber = 1;
    private Integer totalPageNumbers = 1;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    private final ValidationResultModel validationResultModel = new DefaultValidationResultModel();

    private RentModel(Builder builder) {
        this.id = builder.id;
        this.selectedStudent = builder.selectedStudent;
        this.students = builder.students;
        this.from = builder.from;
        this.to = builder.to;
        this.volumes = builder.volumes;
        this.availableVolumes = builder.availableVolumes;
        this.searchPattern = builder.pattern;
        this.pageNumber = builder.pageNumber;
        this.totalPageNumbers = builder.totalPageNumbers;
        this.searchFrom = builder.searchFrom;
        this.searchTo = builder.searchTo;
        this.searchPendingOnly = builder.pendingOnly;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        changeSupport.firePropertyChange("id", this.id, this.id = id);
    }

    public StudentCellRendererModel getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(StudentCellRendererModel selectedStudent) {
        changeSupport.firePropertyChange("selectedStudent", this.selectedStudent, this.selectedStudent = selectedStudent);
    }

    public List<StudentCellRendererModel> getStudents() {
        return students;
    }

    public void setStudents(List<StudentCellRendererModel> students) {
        changeSupport.firePropertyChange("students", this.students, this.students = students);
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        changeSupport.firePropertyChange("from", this.from, this.from = from);
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        changeSupport.firePropertyChange("to", this.to, this.to = to);
    }

    public List<VolumeTableListItemModel> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeTableListItemModel> volumes) {
        changeSupport.firePropertyChange("volumes", this.volumes, this.volumes = volumes);
    }

    public List<VolumeCellRenderModel> getAvailableVolumes() {
        return availableVolumes;
    }

    public void setAvailableVolumes(List<VolumeCellRenderModel> availableVolumes) {
        changeSupport.firePropertyChange("availableVolumes", this.availableVolumes, this.availableVolumes = availableVolumes);
    }

    public ValidationResultModel getValidationResultModel() {
        return validationResultModel;
    }

    public String getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(String searchPattern) {
        changeSupport.firePropertyChange("searchPattern", this.searchPattern, this.searchPattern = searchPattern);
    }

    public Date getSearchFrom() {
        return searchFrom;
    }

    public void setSearchFrom(Date searchFrom) {
        changeSupport.firePropertyChange("searchFrom", this.searchFrom, this.searchFrom = searchFrom);
    }

    public Date getSearchTo() {
        return searchTo;
    }

    public void setSearchTo(Date searchTo) {
        changeSupport.firePropertyChange("searchTo", this.searchTo, this.searchTo = searchTo);
    }

    public boolean isSearchPendingOnly() {
        return searchPendingOnly;
    }

    public void setSearchPendingOnly(boolean searchPendingOnly) {
        changeSupport.firePropertyChange("searchPendingOnly", this.searchPendingOnly, this.searchPendingOnly = searchPendingOnly);
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        changeSupport.firePropertyChange("pageNumber", this.pageNumber, this.pageNumber = pageNumber);
    }

    public Integer getTotalPageNumbers() {
        return totalPageNumbers;
    }

    public void setTotalPageNumbers(Integer totalPageNumbers) {
        changeSupport.firePropertyChange("totalPageNumbers", this.totalPageNumbers, this.totalPageNumbers = totalPageNumbers);
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
            .append("selectedStudent", selectedStudent)
            .append("students", students)
            .append("from", from)
            .append("to", to)
            .append("volumes", volumes)
            .append("availableVolumes", availableVolumes)
            .append("searchPattern", searchPattern)
            .append("searchFrom", searchFrom)
            .append("searchTo", searchTo)
            .append("searchPendingOnly", searchPendingOnly)
            .append("pageNumber", pageNumber)
            .append("totalPageNumbers", totalPageNumbers)
            .toString();
    }

    public static class Builder {
        private Long id;
        private StudentCellRendererModel selectedStudent;
        private List<StudentCellRendererModel> students;
        private Date from;
        private Date to;
        private List<VolumeTableListItemModel> volumes;
        private List<VolumeCellRenderModel> availableVolumes;
        private String pattern;
        private Date searchFrom;
        private Date searchTo;
        private boolean pendingOnly;
        private Integer pageNumber;
        private Integer totalPageNumbers;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withSelectedStudent(StudentCellRendererModel selectedStudent) {
            this.selectedStudent = selectedStudent;
            return this;
        }

        public Builder withStudents(List<StudentCellRendererModel> students) {
            this.students = students;
            return this;
        }

        public Builder withFrom(Date from) {
            this.from = from;
            return this;
        }

        public Builder withTo(Date to) {
            this.to = to;
            return this;
        }

        public Builder withVolumes(List<VolumeTableListItemModel> volumes) {
            this.volumes = volumes;
            return this;
        }

        public Builder withAvailableVolumes(List<VolumeCellRenderModel> availableVolumes) {
            this.availableVolumes = availableVolumes;
            return this;
        }

        public Builder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder withSearchFrom(Date searchFrom) {
            this.searchFrom = searchFrom;
            return this;
        }

        public Builder withSearchTo(Date searchTo) {
            this.searchTo = searchTo;
            return this;
        }

        public Builder withPendingOnly(boolean pendingOnly) {
            this.pendingOnly = pendingOnly;
            return this;
        }

        public Builder withPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder withTotalPageNumbers(Integer totalPageNumbers) {
            this.totalPageNumbers = totalPageNumbers;
            return this;
        }

        public RentModel build() {
            return new RentModel(this);
        }
    }
}
