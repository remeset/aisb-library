package ui.rent.volume.model;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import persistence.api.volume.VolumeEntity;

public class VolumeCellRenderModel implements Volume {
    private static final String SUFFIX = ")";
    private static final String PREFIX = " (";
    private static final String DELIMITER = ", ";

    private final Long id;
    private final String title;
    private final List<String> authors;
    private final VolumeEntity entry;

    private VolumeCellRenderModel(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.authors = builder.authors;
        this.entry = builder.entry;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getAuthors() {
        return authors;
    }

    public VolumeEntity getEntry() {
        return entry;
    }

    // Please note: as no additional cell renderer has been defined for student combo box, the label will be generated based on this 'toString' method
    @Override
    public String toString() {
        return MessageFormat.format("{0}{1}", title, Optional
                .ofNullable(authors)
                .orElse(Collections.emptyList())
            .stream()
            .collect(Collectors.joining(DELIMITER, PREFIX, SUFFIX)));
    }

    public static class Builder {
        private Long id;
        private String title;
        private List<String> authors;
        private VolumeEntity entry;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder withEntry(VolumeEntity entry) {
            this.entry = entry;
            return this;
        }

        public VolumeCellRenderModel build() {
            return new VolumeCellRenderModel(this);
        }
    }
}
