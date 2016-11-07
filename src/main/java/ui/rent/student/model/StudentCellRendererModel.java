package ui.rent.student.model;

import persistence.api.student.StudentEntity;

public class StudentCellRendererModel {
    private final String name;
    private final StudentEntity entity;

    private StudentCellRendererModel(Builder builder) {
        this.name = builder.name;
        this.entity = builder.entity;
    }

    public String getName() {
        return name;
    }

    public StudentEntity getEntity() {
        return entity;
    }

    // Please note: as no additional cell renderer has been defined for student combo box, the label will be generated based on this 'toString' method
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof StudentCellRendererModel) {
            StudentCellRendererModel other = (StudentCellRendererModel) obj;
            if(entity != null && entity.getId() != null && other.entity != null) {
                return entity.getId().equals(other.getEntity().getId());
            }
        }
        return false;
    }

    public static class Builder {
        private String name;
        private StudentEntity entity;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withEntity(StudentEntity entity) {
            this.entity = entity;
            return this;
        }

        public StudentCellRendererModel build() {
            return new StudentCellRendererModel(this);
        }
    }
}
