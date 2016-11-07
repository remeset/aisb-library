package persistence.api.student;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import persistence.api.card.LibraryCardEntity;

@Entity
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false, unique = true)
    private String email;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "student")
    private List<LibraryCardEntity> libraryCards;

    public StudentEntity() {
        super();
    }

    private StudentEntity(Builder builder) {
        this.id = builder.id;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.libraryCards = builder.libraryCards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<LibraryCardEntity> getLibraryCards() {
        return libraryCards;
    }

    public void setLibraryCards(List<LibraryCardEntity> libraryCards) {
        this.libraryCards = libraryCards;
    }

    public static class Builder {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private List<LibraryCardEntity> libraryCards;

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

        public Builder withLibraryCards(List<LibraryCardEntity> libraryCards) {
            this.libraryCards = libraryCards;
            return this;
        }

        public StudentEntity build() {
            return new StudentEntity(this);
        }
    }

}
