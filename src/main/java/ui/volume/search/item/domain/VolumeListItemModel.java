package ui.volume.search.item.domain;

import java.util.List;

import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.search.item.visitor.VolumeListItemVolumeAdminModelVisitor;

public interface VolumeListItemModel<D> {

    String getTitle();

    List<String> getAuthors();

    D getData();

    /**
     * For more information please see: {@link VolumeListItemVolumeAdminModelVisitor}
     */
    VolumeAdminModel accept(VolumeListItemVolumeAdminModelVisitor visitor);

}