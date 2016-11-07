package ui.volume.search.facade;

import java.util.concurrent.CompletableFuture;

import remote.googlebooks.domain.request.VolumesSearchQuery;
import ui.volume.search.item.domain.VolumeListItemModel;
import ui.volume.search.item.domain.VolumesListModelImpl;

public interface VolumeListItemModelFacade<V extends VolumeListItemModel<D>, D> {

    CompletableFuture<VolumesListModelImpl<V, D>> lookup(VolumesSearchQuery query);

}