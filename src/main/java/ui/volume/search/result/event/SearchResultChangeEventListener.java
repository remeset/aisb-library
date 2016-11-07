package ui.volume.search.result.event;

import java.util.EventListener;

public interface SearchResultChangeEventListener extends EventListener {
    public void searchResultChanged(SearchResultChangeEvent event);
}
