package ui.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ui.validation.annotation.DateRange;
import ui.validation.model.DateRangeModel;

public class DateRangeValidator implements ConstraintValidator<DateRange, DateRangeModel> {
    @Override
    public void initialize(DateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(DateRangeModel value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value.getFrom() != null && value.getTo() != null) {
            valid = value.getFrom().before(value.getTo());
        }
        return valid;
    }
}