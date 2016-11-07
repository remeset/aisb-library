package ui.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ui.validation.validator.AvailableAmountOfVolumesValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableAmountOfVolumesValidator.class)
@Documented
public @interface AvailableAmountOfVolumes {
    String message() default "Do not have enough volumes for the selected book.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
