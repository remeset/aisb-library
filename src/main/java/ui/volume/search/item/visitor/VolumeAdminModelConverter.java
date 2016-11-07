package ui.volume.search.item.visitor;

import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Component;

import persistence.api.volume.VolumeEntity;
import remote.googlebooks.domain.response.Volume;
import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;

@Component
public class VolumeAdminModelConverter implements VolumeListItemVolumeAdminModelVisitor {
    @Override
    public VolumeAdminModel visit(LocalVolumeListItemModel volumeListItemModel) {
        return convert(volumeListItemModel.getData());
    }

    @Override
    public VolumeAdminModel visit(RemoteVolumeListItemModel volumeListItemModel) {
        return convert(volumeListItemModel.getData());
    }

    private VolumeAdminModel convert(Volume volume) {
        return new VolumeAdminModel.Builder()
            .withRemoteId(volume.getId())
            .withTitle(volume.getVolumeInfo().getTitle())
            .withAuthors(volume.getVolumeInfo().getAuthors())
            .withDescription(volume.getVolumeInfo().getDescription())
            .withIsbn(Optional
                .ofNullable(volume.getVolumeInfo().getIndustryIdentifiers())
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(id -> id.getIdentifier())
                .orElse(null))
            .withPublisher(volume.getVolumeInfo().getPublisher())
            .withPublishedDate(volume.getVolumeInfo().getPublishedDate())
            .withLanguage(volume.getVolumeInfo().getLanguage())
            .build();
    }

    private VolumeAdminModel convert(VolumeEntity volumeEntity) {
        return new VolumeAdminModel.Builder()
            .withId(volumeEntity.getId())
            .withRemoteId(volumeEntity.getRemoteId())
            .withTitle(volumeEntity.getTitle())
            .withAuthors(volumeEntity.getAuthors())
            .withDescription(volumeEntity.getDescription())
            .withIsbn(volumeEntity.getIsbn())
            .withPublisher(volumeEntity.getPublisher())
            .withPublishedDate(volumeEntity.getPublishedDate())
            .withLanguage(volumeEntity.getLanguage())
            .withNumberOfVolumes(volumeEntity.getNumberOfVolumes())
            .build();
    }

}
