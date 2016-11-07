package ui.validation.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import persistence.dao.student.StudentDAO;
import ui.validation.annotation.UniqueStudentEmail;

public class UniqueStudentEmailValidator implements ConstraintValidator<UniqueStudentEmail, String> {
    @Autowired
    private StudentDAO studentDAO;

    @Override
    public void initialize(UniqueStudentEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
            .map(id -> !Optional.ofNullable(studentDAO.findOneByEmail(value)).isPresent())
            .orElse(true);
    }
}
