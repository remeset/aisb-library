package ui.volume.search.item.domain;

import java.util.List;

import org.springframework.core.style.ToStringCreator;

import persistence.api.volume.VolumeEntity;
import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.search.item.visitor.VolumeListItemVolumeAdminModelVisitor;

public class LocalVolumeListItemModel implements VolumeListItemModel<VolumeEntity> {
    private final Long id;
    private final String title;
    private final List<String> authors;
    private final VolumeEntity data;

    private LocalVolumeListItemModel(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.authors = builder.authors;
        this.data = builder.data;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public VolumeEntity getData() {
        return data;
    }

    @Override
    public VolumeAdminModel accept(VolumeListItemVolumeAdminModelVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("id", id)
            .append("title", title)
            .append("authors", authors)
            .append("data", data)
            .toString();
    }

    public static class Builder{
        private Long id;
        private String title;
        private List<String> authors;
        private VolumeEntity data;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder withData(VolumeEntity data) {
            this.data = data;
            return this;
        }

        public LocalVolumeListItemModel build() {
            return new LocalVolumeListItemModel(this);
        }
    }

}
