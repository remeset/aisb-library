package ui.volume.search.facade;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import persistence.api.volume.VolumeEntity;
import persistence.dao.volume.VolumeDAO;
import remote.googlebooks.domain.request.VolumesSearchQuery;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.VolumesListModelImpl;

@Component
public class LocalVolumeListItemModelFacade implements VolumeListItemModelFacade<LocalVolumeListItemModel, VolumeEntity> {
    @Autowired
    private VolumeDAO volumeDAO;

    @Async
    @Override
    public CompletableFuture<VolumesListModelImpl<LocalVolumeListItemModel, VolumeEntity>> lookup(VolumesSearchQuery query) {
        Page<VolumeEntity> volumes = volumeDAO.findByTitleAndAuthorsAndISBN(
            query.getTitle().orElse(""),
            query.getAuthor().orElse(""),
            query.getIsbn().orElse(""),
            new PageRequest(
                query.getStartIndex() / query.getMaxResults(),
                query.getMaxResults()));
        return CompletableFuture.completedFuture(new VolumesListModelImpl.Builder<LocalVolumeListItemModel, VolumeEntity>()
            .withTotalItems(volumes.getTotalElements())
            .withVolumes(Optional
                .ofNullable(volumes.getContent())
                .orElse(Collections.emptyList())
                .stream()
                .map(volume -> new LocalVolumeListItemModel.Builder()
                    .withId(volume.getId())
                    .withTitle(volume.getTitle())
                    .withAuthors(volume.getAuthors())
                    .withData(volume)
                    .build())
                .collect(Collectors.toList()))
            .build());
    }
}
