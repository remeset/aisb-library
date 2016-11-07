package persistence.api.card;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import persistence.api.volume.VolumeEntity;

@Entity
public class LibraryCardEntryEntity {
    public static enum Status {
        PENDING, RETURNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volumeId", nullable = false)
    private VolumeEntity volume;
    @Column(nullable = false)
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date returnDate;

    public LibraryCardEntryEntity() {
        super();
    }

    private LibraryCardEntryEntity(Builder builder) {
        this.id = builder.id;
        this.volume = builder.volume;
        this.amount = builder.amount;
        this.status = builder.status;
        this.returnDate = builder.returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VolumeEntity getVolume() {
        return volume;
    }

    public void setVolume(VolumeEntity volume) {
        this.volume = volume;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public static class Builder {
        private Long id;
        private VolumeEntity volume;
        private Integer amount;
        private Status status;
        private Date returnDate;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withVolume(VolumeEntity volume) {
            this.volume = volume;
            return this;
        }

        public Builder withAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withReturnDate(Date returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public LibraryCardEntryEntity build() {
            return new LibraryCardEntryEntity(this);
        }
    }

}
