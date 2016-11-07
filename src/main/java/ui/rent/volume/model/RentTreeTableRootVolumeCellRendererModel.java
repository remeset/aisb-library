package ui.rent.volume.model;

import java.util.List;

public class RentTreeTableRootVolumeCellRendererModel implements Volume {
    private final String title;

    public RentTreeTableRootVolumeCellRendererModel(String title) {
        this.title = title;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getAuthors() {
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
