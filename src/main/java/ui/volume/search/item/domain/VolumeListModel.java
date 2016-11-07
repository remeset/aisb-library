package ui.volume.search.item.domain;

import java.util.List;

public interface VolumeListModel<V extends VolumeListItemModel<D>, D> {

    List<V> getVolumes();

    long getTotalItems();

}