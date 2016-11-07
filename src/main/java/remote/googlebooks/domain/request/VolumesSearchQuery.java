package remote.googlebooks.domain.request;

import java.util.Optional;

import org.springframework.core.style.ToStringCreator;

public class VolumesSearchQuery {
    private final Optional<String> title;
    private final Optional<String> author;
    private final Optional<String> isbn;
    private final int startIndex;
    private final int maxResults;

    private VolumesSearchQuery(Builder builder) {
        this.title = builder.title;
        this.author = builder.author;
        this.isbn = builder.isbn;
        this.startIndex = builder.startIndex;
        this.maxResults = builder.maxResults;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getAuthor() {
        return author;
    }

    public Optional<String> getIsbn() {
        return isbn;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("title", title)
            .append("author", author)
            .append("isbn", isbn)
            .append("startIndex", startIndex)
            .append("maxResults", maxResults)
            .toString();
    }

    public static class Builder {
        private Optional<String> title = Optional.empty();
        private Optional<String> author = Optional.empty();
        private Optional<String> isbn = Optional.empty();
        private int startIndex;
        private int maxResults;

        public Builder withTitle(Optional<String> title) {
            this.title = title;
            return this;
        }

        public Builder withAuthor(Optional<String> author) {
            this.author = author;
            return this;
        }

        public Builder withIsbn(Optional<String> isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder withStartIndex(int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder withMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public VolumesSearchQuery build() {
            return new VolumesSearchQuery(this);
        }
    }
}
