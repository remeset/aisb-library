package ui.volume.admin.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.core.style.ToStringCreator;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

import ui.core.crud.model.CrudModel;
import ui.validation.annotation.PendingVolume;
import ui.validation.annotation.UniqueGoogelId;
import ui.validation.annotation.UniqueISBN;
import ui.validation.group.MergeGroup;
import ui.validation.group.PersistGroup;
import ui.validation.group.RemoveGroup;

public class VolumeAdminModel implements CrudModel<Long> {
    @PendingVolume(message = "{i18n.volume.validation.pendingVolumes}", groups = RemoveGroup.class)
    private Long id;
    @UniqueGoogelId(message = "{i18n.volume.validation.bookAlreadyRegisteredWithRemoteId}", groups = PersistGroup.class)
    private String remoteId;
    @NotBlank(message = "{i18n.volume.validation.blankBookTitle}", groups = {PersistGroup.class, MergeGroup.class})
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    @UniqueISBN(message = "{i18n.volume.validation.bookAlreadyRegisteredWithISBN}", groups = PersistGroup.class)
    private String isbn;
    private String description;
    private Locale language;
    @Min(value = 1, message = "{i18n.volume.validation.minimumNumberNotExceeded}", groups = {PersistGroup.class, MergeGroup.class})
    private Integer numberOfVolumes;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    private final ValidationResultModel validationResultModel = new DefaultValidationResultModel();

    private VolumeAdminModel(Builder builder) {
        this.id = builder.id;
        this.remoteId = builder.remoteId;
        this.title = builder.title;
        this.authors = builder.authors;
        this.publisher = builder.publisher;
        this.publishedDate = builder.publishedDate;
        this.isbn = builder.isbn;
        this.description = builder.description;
        this.language = builder.language;
        this.numberOfVolumes = builder.numberOfVolumes;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        changeSupport.firePropertyChange("id", this.id, this.id = id);
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        changeSupport.firePropertyChange("remoteId", this.remoteId, this.remoteId = remoteId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        changeSupport.firePropertyChange("title", this.title, this.title = title);
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        changeSupport.firePropertyChange("authors", this.authors, this.authors = authors);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        changeSupport.firePropertyChange("publisher", this.publisher, this.publisher = publisher);
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        changeSupport.firePropertyChange("publishedDate", this.publishedDate, this.publishedDate = publishedDate);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        changeSupport.firePropertyChange("isbn", this.isbn, this.isbn = isbn);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        changeSupport.firePropertyChange("description", this.description, this.description = description);
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        changeSupport.firePropertyChange("language", this.language, this.language = language);
    }

    public Integer getNumberOfVolumes() {
        return numberOfVolumes;
    }

    public void setNumberOfVolumes(Integer numberOfVolumes) {
        changeSupport.firePropertyChange("numberOfVolumes", this.numberOfVolumes, this.numberOfVolumes = numberOfVolumes);
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
            .append("remoteId", remoteId)
            .append("title", title)
            .append("authors", authors)
            .append("publisher", publisher)
            .append("publishedDate", publishedDate)
            .append("isbn", isbn)
            .append("description", description)
            .append("language", language)
            .append("numberOfVolumes", numberOfVolumes)
            .toString();
    }

    public static class Builder {
        private Long id;
        private String remoteId;
        private String title;
        private List<String> authors;
        private String publisher;
        private String publishedDate;
        private String isbn;
        private String description;
        private Locale language;
        private Integer numberOfVolumes;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withRemoteId(String remoteId) {
            this.remoteId = remoteId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder withPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder withPublishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
            return this;
        }

        public Builder withIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withLanguage(Locale language) {
            this.language = language;
            return this;
        }

        public Builder withNumberOfVolumes(Integer numberOfVolumes) {
            this.numberOfVolumes = numberOfVolumes;
            return this;
        }

        public VolumeAdminModel build() {
            return new VolumeAdminModel(this);
        }
    }

}
