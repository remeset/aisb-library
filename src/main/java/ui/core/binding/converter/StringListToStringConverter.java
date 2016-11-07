package ui.core.binding.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jgoodies.binding.value.BindingConverter;

public class StringListToStringConverter implements BindingConverter<List<String>, String> {
    @Override
    public String targetValue(List<String> sourceValue) {
        return Optional
            .ofNullable(sourceValue)
            .orElse(Collections.emptyList())
            .stream()
            .collect(Collectors.joining(", "));
    }

    @Override
    public List<String> sourceValue(String targetValue) {
        return Arrays.asList(targetValue.split(", "));
    }
}
