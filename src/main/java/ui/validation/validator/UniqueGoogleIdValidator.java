package ui.validation.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import persistence.dao.volume.VolumeDAO;
import ui.validation.annotation.UniqueGoogelId;

public class UniqueGoogleIdValidator implements ConstraintValidator<UniqueGoogelId, String> {
    @Autowired
    private VolumeDAO volumeDAO;

    @Override
    public void initialize(UniqueGoogelId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
            .map(id -> !Optional.ofNullable(volumeDAO.findOneByRemoteId(id)).isPresent())
            .orElse(true);
    }
}
