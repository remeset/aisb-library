package ui.rent.config;

public class RentConfiguration {
    private final long defaultToDateOffsetInDays;

    private RentConfiguration(Builder builder) {
        this.defaultToDateOffsetInDays = builder.defaultToDateOffsetInDays;
    }

    public long getDefaultToDateOffsetInDays() {
        return defaultToDateOffsetInDays;
    }

    public static class Builder {
        private long defaultToDateOffsetInDays;

        public Builder withDefaultToDateOffsetInDays(long defaultToDateOffsetInDays) {
            this.defaultToDateOffsetInDays = defaultToDateOffsetInDays;
            return this;
        }

        public RentConfiguration build() {
            return new RentConfiguration(this);
        }
    }
}
