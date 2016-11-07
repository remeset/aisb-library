package ui.volume.search.result.config;

public class VolumeSearchResultConfiguration {
    private final boolean allowEmptySearchCriterias;

    private VolumeSearchResultConfiguration(Builder builder) {
        this.allowEmptySearchCriterias = builder.allowEmptySearchCriterias;
    }

    public boolean isAllowEmptySearchCriterias() {
        return allowEmptySearchCriterias;
    }

    public static class Builder {
        private boolean allowEmptySearchCriterias = true;

        public Builder withAllowEmptySearchCriterias(boolean allowEmptySearchCriterias) {
            this.allowEmptySearchCriterias = allowEmptySearchCriterias;
            return this;
        }

        public VolumeSearchResultConfiguration build() {
            return new VolumeSearchResultConfiguration(this);
        }
    }
}
