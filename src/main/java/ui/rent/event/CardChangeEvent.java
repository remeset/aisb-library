package ui.rent.event;

import persistence.api.card.LibraryCardEntity;

public class CardChangeEvent {
    public static enum Type {
        INSERT, UPDATE, DELETE
    }

    private CardChangeEvent(Builder builder) {
        this.type = builder.type;
        this.card = builder.card;
    }

    private final Type type;
    private final LibraryCardEntity card;

    public Type getType() {
        return type;
    }

    public LibraryCardEntity getCard() {
        return card;
    }

    public static class Builder {
        private Type type;
        private LibraryCardEntity card;

        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        public Builder withCard(LibraryCardEntity card) {
            this.card = card;
            return this;
        }

        public CardChangeEvent build() {
            return new CardChangeEvent(this);
        }
    }

}
