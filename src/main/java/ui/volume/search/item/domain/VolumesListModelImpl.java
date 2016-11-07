package ui.volume.search.item.domain;

import java.util.List;

import org.springframework.core.style.ToStringCreator;

public class VolumesListModelImpl<V extends VolumeListItemModel<D>, D> implements VolumeListModel<V, D> {
    private final List<V> volumes;
    private final long totalItems;

    private VolumesListModelImpl(Builder<V, D> builder) {
        this.volumes = builder.volumes;
        this.totalItems = builder.totalItems;
    }

    @Override
    public List<V> getVolumes() {
        return volumes;
    }

    @Override
    public long getTotalItems() {
        return totalItems;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("totalItems", totalItems)
            .append("volumes", volumes)
            .toString();
    }

    public static class Builder<V extends VolumeListItemModel<D>, D> {
        private List<V> volumes;
        private long totalItems;

        public Builder<V, D> withVolumes(List<V> volumes) {
            this.volumes = volumes;
            return this;
        }

        public Builder<V, D> withTotalItems(long totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public VolumesListModelImpl<V, D> build() {
            return new VolumesListModelImpl<V, D>(this);
        }
    }

}
