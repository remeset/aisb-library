package ui.rent.model;

import java.util.Date;

public class RentSearchQuery {
    private final String pattern;
    private final Date from;
    private final Date to;
    private final boolean pendingOnly;
    private final int startIndex;
    private final int maxResults;

    private RentSearchQuery(Builder builder) {
        this.pattern = builder.pattern;
        this.from = builder.from;
        this.to = builder.to;
        this.pendingOnly = builder.pendingOnly;
        this.startIndex = builder.startIndex;
        this.maxResults = builder.maxResults;
    }

    public String getPattern() {
        return pattern;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public boolean isPendingOnly() {
        return pendingOnly;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public static class Builder {
        private String pattern;
        private Date from;
        private Date to;
        private boolean pendingOnly;
        private int startIndex;
        private int maxResults;

        public Builder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder withFrom(Date from) {
            this.from = from;
            return this;
        }

        public Builder withTo(Date to) {
            this.to = to;
            return this;
        }

        public Builder withPendingOnly(boolean pendingOnly) {
            this.pendingOnly = pendingOnly;
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

        public RentSearchQuery build() {
            return new RentSearchQuery(this);
        }
    }
}
