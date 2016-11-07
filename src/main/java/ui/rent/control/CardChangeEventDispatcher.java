package ui.rent.control;

import ui.rent.event.CardChangeEventListener;

public interface CardChangeEventDispatcher {

    void addCardChangeEventListener(CardChangeEventListener listener);

    void removeCardChangeEventListener(CardChangeEventListener listener);

}