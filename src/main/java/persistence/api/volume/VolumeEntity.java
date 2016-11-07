package persistence.api.volume;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.springframework.core.style.ToStringCreator;

@Entity
public class VolumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String remoteId;
    @Column(nullable = false)
    private String title;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="author", joinColumns = @JoinColumn(name="author_name"))
    @Column(name="authors")
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    @Column(unique = true)
    private String isbn;
    @Column(length = 4096)
    private String description;
    private Locale language;
    @Column(nullable = false)
    private Integer numberOfVolumes;
    @Column(nullable = false)
    private Date registrationDate;

    public VolumeEntity() {
        super();
    }

    private VolumeEntity(Builder builder) {
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
        this.registrationDate = builder.registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public Integer getNumberOfVolumes() {
        return numberOfVolumes;
    }

    public void setNumberOfVolumes(Integer numberOfVolumes) {
        this.numberOfVolumes = numberOfVolumes;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
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
            .append("registrationDate", registrationDate)
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
        private Date registrationDate;

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

        public Builder withRegistrationDate(Date registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public VolumeEntity build() {
            return new VolumeEntity(this);
        }
    }

}
