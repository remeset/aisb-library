package ui.volume.search.result.event;

import java.util.List;

import ui.volume.search.item.domain.VolumeListItemModel;

public class SearchResultChangeEvent<V extends VolumeListItemModel<D>, D> {
    private final List<V> result;

    public SearchResultChangeEvent(List<V> result) {
        this.result = result;
    }

    public List<V> getResult() {
        return result;
    }
}
