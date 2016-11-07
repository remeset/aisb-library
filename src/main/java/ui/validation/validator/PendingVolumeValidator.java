package ui.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import persistence.dao.card.LibraryCardEntryDAO;
import ui.validation.annotation.PendingVolume;

public class PendingVolumeValidator implements ConstraintValidator<PendingVolume, Long> {
    @Autowired
    private LibraryCardEntryDAO libraryCardEntryDAO;

    @Override
    public void initialize(PendingVolume constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return libraryCardEntryDAO.findAmountSumByVolumeId(value) == 0;
    }
}