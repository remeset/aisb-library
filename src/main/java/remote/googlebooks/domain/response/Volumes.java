package remote.googlebooks.domain.response;

import java.util.List;

public class Volumes {
    private String kind;
    private Long totalItems;
    private List<Volume> items;

    public Volumes() {
        super();
    }

    private Volumes(Builder builder) {
        this.kind = builder.kind;
        this.totalItems = builder.totalItems;
        this.items = builder.items;
      }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public List<Volume> getItems() {
        return items;
    }

    public void setItems(List<Volume> items) {
        this.items = items;
    }

    public static class Builder {
        private String kind;
        private Long totalItems;
        private List<Volume> items;

        public Builder withKind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder withTotalItems(Long totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public Builder withItems(List<Volume> items) {
            this.items = items;
            return this;
        }

        public Volumes build() {
            return new Volumes(this);
        }
    }
}
