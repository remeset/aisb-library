package ui.validation.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import persistence.dao.card.LibraryCardEntryDAO;
import persistence.dao.volume.VolumeDAO;
import ui.validation.annotation.AvailableAmountOfVolumes;
import ui.validation.model.QuantitativeVolumeModelHolder;

public class AvailableAmountOfVolumesValidator implements ConstraintValidator<AvailableAmountOfVolumes, QuantitativeVolumeModelHolder> {
    @Autowired
    private LibraryCardEntryDAO libraryCardEntryDAO;

    @Autowired
    private VolumeDAO volumeDAO;

    @Override
    public void initialize(AvailableAmountOfVolumes constraintAnnotation) {
    }

    @Override
    public boolean isValid(QuantitativeVolumeModelHolder value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getVolume() != null && value.getVolume().getId() != null && value.getAmount() != null) {
            int totalNumberOfVolumes = volumeDAO.findOne(value.getVolume().getId()).getNumberOfVolumes();
            long numberOfRentedVolumes = libraryCardEntryDAO.findAmountSumByVolumeId(value.getVolume().getId());
            valid = (totalNumberOfVolumes - numberOfRentedVolumes) > value.getAmount();
        }
        if(!valid) {
            String template = context.getDefaultConstraintMessageTemplate();
            context.disableDefaultConstraintViolation();
            context.unwrap(HibernateConstraintValidatorContext.class)
                .addExpressionVariable("amount", Optional.ofNullable(value.getAmount()))
                .addExpressionVariable("title", Optional.ofNullable(value.getVolume().getTitle()).orElse(""))
                .addExpressionVariable("author", Optional.ofNullable(value.getVolume().getAuthors()).map(authors -> String.join(", ", authors)).orElse(""))
                .buildConstraintViolationWithTemplate(template)
                .addConstraintViolation();
        }
        return valid;
    }
}
