package ui.validation.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import persistence.dao.volume.VolumeDAO;
import ui.validation.annotation.UniqueISBN;

public class UniqueISBNValidator implements ConstraintValidator<UniqueISBN, String> {
    @Autowired
    private VolumeDAO volumeDAO;

    @Override
    public void initialize(UniqueISBN constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
            .map(id -> !Optional.ofNullable(volumeDAO.findOneByIsbn(id)).isPresent())
            .orElse(true);
    }
}
