package ui.validation.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import persistence.api.card.LibraryCardEntryEntity.Status;
import persistence.dao.student.StudentDAO;
import ui.validation.annotation.StudentWithPendingVolume;

public class StudentWithPendingVolumeValidator implements ConstraintValidator<StudentWithPendingVolume, Long> {
    @Autowired
    private StudentDAO studentDAO;

    @Override
    public void initialize(StudentWithPendingVolume constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Optional
            .ofNullable(studentDAO.findOne(value).getLibraryCards())
            .map(cards -> !cards
                .stream()
                .flatMap(card -> card.getEntries().stream())
                .filter(entry -> Status.PENDING.equals(entry.getStatus()))
                .findAny()
                .isPresent())
            .orElse(true);
    }
}