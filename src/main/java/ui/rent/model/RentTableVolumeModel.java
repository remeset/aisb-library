package ui.rent.model;

import java.util.Date;

import persistence.api.card.LibraryCardEntryEntity;
import ui.rent.student.model.StudentCellRendererModel;
import ui.rent.volume.model.Volume;

public class RentTableVolumeModel implements RentTableLeafModel<LibraryCardEntryEntity> {
    private final Long id;
    private final StudentCellRendererModel student;
    private final Volume volume;
    private final Integer amount;
    private final Date from;
    private final Date to;
    private final LibraryCardEntryEntity entity;

    private RentTableVolumeModel(Builder builder) {
        this.id = builder.id;
        this.student = builder.student;
        this.volume = builder.volume;
        this.amount = builder.amount;
        this.from = builder.from;
        this.to = builder.to;
        this.entity = builder.entity;
    }

    @Override
    public StudentCellRendererModel getStudent() {
        return student;
    }

    @Override
    public Volume getVolume() {
        return volume;
    }

    @Override
    public Integer getAmount() {
        return amount;
    }

    @Override
    public Date getFrom() {
        return from;
    }

    @Override
    public Date getTo() {
        return to;
    }

    @Override
    public LibraryCardEntryEntity getEntity() {
        return entity;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static class Builder {
        private Long id;
        private StudentCellRendererModel student;
        private Volume volume;
        private Integer amount;
        private Date from;
        private Date to;
        private LibraryCardEntryEntity entity;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withStudent(StudentCellRendererModel student) {
            this.student = student;
            return this;
        }

        public Builder withVolume(Volume volume) {
            this.volume = volume;
            return this;
        }

        public Builder withAmount(Integer amount) {
            this.amount = amount;
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

        public Builder withEntity(LibraryCardEntryEntity entity) {
            this.entity = entity;
            return this;
        }

        public RentTableVolumeModel build() {
            return new RentTableVolumeModel(this);
        }
    }
}
