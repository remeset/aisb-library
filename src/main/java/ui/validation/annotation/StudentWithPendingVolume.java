package ui.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ui.validation.validator.StudentWithPendingVolumeValidator;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StudentWithPendingVolumeValidator.class)
@Documented
public @interface StudentWithPendingVolume {
    String message() default "Student has a pending volume.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
