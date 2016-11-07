package persistence.api.card;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import persistence.api.student.StudentEntity;

@Entity
public class LibraryCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LibraryCardEntryEntity> entries;
    @Column(nullable = false)
    private Date creationDate;
    @Column(nullable = false)
    private Date expiryDate;
    @JoinColumn(name = "studentId")
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(cascade = CascadeType.REFRESH)
    private StudentEntity student;

    public LibraryCardEntity() {
        super();
    }

    private LibraryCardEntity(Builder builder) {
        this.id = builder.id;
        this.entries = builder.entries;
        this.creationDate = builder.creationDate;
        this.expiryDate = builder.expiryDate;
        this.student = builder.student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LibraryCardEntryEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<LibraryCardEntryEntity> entries) {
        this.entries = entries;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public static class Builder {
        private Long id;
        private List<LibraryCardEntryEntity> entries;
        private Date creationDate;
        private Date expiryDate;
        private StudentEntity student;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withEntries(List<LibraryCardEntryEntity> entries) {
            this.entries = entries;
            return this;
        }

        public Builder withCreationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder withExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder withStudent(StudentEntity student) {
            this.student = student;
            return this;
        }

        public LibraryCardEntity build() {
            return new LibraryCardEntity(this);
        }
    }

}
