package ui.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ui.validation.validator.PendingVolumeValidator;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PendingVolumeValidator.class)
@Documented
public @interface PendingVolume {
    String message() default "All volumes mst be returned before removal.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
