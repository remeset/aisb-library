package ui.volume.search.item.visitor;

import ui.volume.admin.control.VolumeAdminControl;
import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;

/**
 * Application of visitor pattern: as there are at least two different data source (GoogleBooks API/Database) can be identified on {@link VolumeAdminControl} a common data set has to be provided.
 * For more information please see: https://dzone.com/articles/design-patterns-visitor
 */
public interface VolumeListItemVolumeAdminModelVisitor {

    public VolumeAdminModel visit(LocalVolumeListItemModel volumeListItemModel);

    public VolumeAdminModel visit(RemoteVolumeListItemModel volumeListItemModel);
}
