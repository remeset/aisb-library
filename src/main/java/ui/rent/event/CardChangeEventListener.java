package ui.rent.event;

import java.util.EventListener;

public interface CardChangeEventListener extends EventListener {
    public void cardChanged(CardChangeEvent event);
}
