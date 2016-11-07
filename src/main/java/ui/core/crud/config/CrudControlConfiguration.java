package ui.core.crud.config;

public class CrudControlConfiguration {
    private final boolean clearModelBeforeAdd;


    private CrudControlConfiguration(Builder builder) {
        this.clearModelBeforeAdd = builder.clearModelBeforeAdd;
    }

    public boolean isClearModelBeforeAdd() {
        return clearModelBeforeAdd;
    }

    public static class Builder {
        private boolean clearModelBeforeAdd;

        public Builder withClearModelBeforeAdd(boolean clearModelBeforeAdd) {
            this.clearModelBeforeAdd = clearModelBeforeAdd;
            return this;
        }

        public CrudControlConfiguration build() {
            return new CrudControlConfiguration(this);
        }
    }

}
