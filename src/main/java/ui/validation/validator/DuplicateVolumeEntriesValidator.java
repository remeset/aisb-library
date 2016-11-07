package ui.validation.validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import ui.rent.volume.model.VolumeTableListItemModel;
import ui.validation.annotation.DuplicateVolumeEntries;

public class DuplicateVolumeEntriesValidator implements ConstraintValidator<DuplicateVolumeEntries, List<VolumeTableListItemModel>> {
    @Override
    public void initialize(DuplicateVolumeEntries constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<VolumeTableListItemModel> value, ConstraintValidatorContext context) {
        Set<VolumeTableListItemModel> duplicates = Optional
            .ofNullable(value)
            // Remove invalid items from the list
            .map(list -> list
                .stream()
                .filter(item -> Optional
                    .ofNullable(item)
                    .flatMap(model -> Optional.ofNullable(model.getVolume()))
                    .flatMap(volume -> Optional.ofNullable(volume.getId())).isPresent())
                .collect(Collectors.toList()))
            // Lookup duplicate items on the list
            .map(list -> {
                List<Long> ids = list
                    .stream()
                    .map(item -> item.getVolume().getId())
                    .collect(Collectors.toList());
                return list
                    .stream()
                    .filter(item -> ids
                        .stream()
                        .filter(id -> item.getVolume().getId().equals(id))
                        .count() > 1)
                    .collect(Collectors.toSet());
            }).orElse(Collections.emptySet());
        if(duplicates.size() > 0) {
            String template = context.getDefaultConstraintMessageTemplate();
            context.disableDefaultConstraintViolation();
            context.unwrap(HibernateConstraintValidatorContext.class)
                .addExpressionVariable("duplicates", duplicates
                    .stream()
                    .map(model -> model.getVolume().getTitle())
                    .collect(Collectors.joining(", ")))
                .buildConstraintViolationWithTemplate(template)
                .addConstraintViolation();
        }
        return duplicates.size() == 0;
    }
}
