package ui.rent.model;

import java.util.Date;
import java.util.List;

import persistence.api.card.LibraryCardEntity;
import persistence.api.card.LibraryCardEntryEntity;
import ui.rent.student.model.StudentCellRendererModel;
import ui.rent.volume.model.Volume;

public class RentTableCardModel implements RentTableRootModel<LibraryCardEntity, LibraryCardEntryEntity> {
    private final Long id;
    private final StudentCellRendererModel student;
    private final Volume volume;
    private final Integer amount;
    private final Date from;
    private final Date to;
    private final List<RentTableVolumeModel> children;
    private final LibraryCardEntity entity;

    private RentTableCardModel(Builder builder) {
        this.id = builder.id;
        this.student = builder.student;
        this.volume = builder.volume;
        this.amount = builder.amount;
        this.from = builder.from;
        this.to = builder.to;
        this.children = builder.children;
        this.entity = builder.entity;
    }

    public Long getId() {
        return id;
    }

    public StudentCellRendererModel getStudent() {
        return student;
    }

    public Volume getVolume() {
        return volume;
    }

    public Integer getAmount() {
        return amount;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public List<RentTableVolumeModel> getChildren() {
        return children;
    }

    public LibraryCardEntity getEntity() {
        return entity;
    }

    public static class Builder {
        private Long id;
        private StudentCellRendererModel student;
        private Volume volume;
        private Integer amount;
        private Date from;
        private Date to;
        private List<RentTableVolumeModel> children;
        private LibraryCardEntity entity;

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

        public Builder withChildren(List<RentTableVolumeModel> children) {
            this.children = children;
            return this;
        }

        public Builder withEntity(LibraryCardEntity entity) {
            this.entity = entity;
            return this;
        }

        public RentTableCardModel build() {
            return new RentTableCardModel(this);
        }
    }

}
