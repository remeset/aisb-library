package ui.volume.search.item.domain;

import java.util.List;

import org.springframework.core.style.ToStringCreator;

import remote.googlebooks.domain.response.Volume;
import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.search.item.visitor.VolumeListItemVolumeAdminModelVisitor;

public class RemoteVolumeListItemModel implements VolumeListItemModel<Volume> {
    private final String title;
    private final List<String> authors;
    private final byte[] thumbnail;
    private final Volume data;

    private RemoteVolumeListItemModel(Builder builder) {
        this.title = builder.title;
        this.authors = builder.authors;
        this.thumbnail = builder.thumbnail;
        this.data = builder.data;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public Volume getData() {
        return data;
    }

    @Override
    public VolumeAdminModel accept(VolumeListItemVolumeAdminModelVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("title", title)
            .append("authors", authors)
            .append("thumbnail", new byte[0])
            .append("data", data)
            .toString();
    }

    public static class Builder {
        private String title;
        private List<String> authors;
        private byte[] thumbnail;
        private Volume data;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder withThumbnail(byte[] thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder withData(Volume data) {
            this.data = data;
            return this;
        }

        public RemoteVolumeListItemModel build() {
            return new RemoteVolumeListItemModel(this);
        }
    }
}
