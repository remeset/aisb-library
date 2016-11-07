package ui.student.renderer.model;

import persistence.api.student.StudentEntity;

public class StudentListItemModel {
    private final String name;
    private final String email;
    private final Integer numberOfRentedBooks;
    private final StudentEntity entity;

    private StudentListItemModel(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.numberOfRentedBooks = builder.numberOfRentedBooks;
        this.entity = builder.entity;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getNumberOfRentedBooks() {
        return numberOfRentedBooks;
    }

    public StudentEntity getEntity() {
        return entity;
    }

    public static class Builder {
        private String name;
        private String email;
        private Integer numberOfRentedBooks;
        private StudentEntity entity;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withNumberOfRentedBooks(Integer numberOfRentedBooks) {
            this.numberOfRentedBooks = numberOfRentedBooks;
            return this;
        }

        public Builder withEntity(StudentEntity entity) {
            this.entity = entity;
            return this;
        }

        public StudentListItemModel build() {
            return new StudentListItemModel(this);
        }
    }
}
