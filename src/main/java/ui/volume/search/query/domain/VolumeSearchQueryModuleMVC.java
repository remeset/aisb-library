package ui.volume.search.query.domain;

import ui.volume.search.item.domain.VolumeListItemModel;
import ui.volume.search.result.control.VolumeSearchResultControl;
import ui.volume.search.result.model.VolumeSearchResultModel;
import ui.volume.search.result.view.VolumeSearchResultView;

public class VolumeSearchQueryModuleMVC<V extends VolumeListItemModel<D>, D> {
    public static enum SearchResultViewType {
        LOCAL, REMOTE
    }

    private final SearchResultViewType type;
    private final VolumeSearchResultView<V, D> view;
    private final VolumeSearchResultModel<V, D> model;
    private final VolumeSearchResultControl<V, D> control;

    private VolumeSearchQueryModuleMVC(Builder<V, D> builder) {
        this.type = builder.type;
        this.view = builder.view;
        this.model = builder.model;
        this.control = builder.control;
    }

    public SearchResultViewType getType() {
        return type;
    }

    public VolumeSearchResultView<V, D> getView() {
        return view;
    }

    public VolumeSearchResultModel<V, D> getModel() {
        return model;
    }

    public VolumeSearchResultControl<V, D> getControl() {
        return control;
    }

    public static class Builder<V extends VolumeListItemModel<D>, D> {
        private SearchResultViewType type;
        private VolumeSearchResultView<V, D> view;
        private VolumeSearchResultModel<V, D> model;
        private VolumeSearchResultControl<V, D> control;

        public Builder<V, D> withType(SearchResultViewType type) {
            this.type = type;
            return this;
        }

        public Builder<V, D> withView(VolumeSearchResultView<V, D> view) {
            this.view = view;
            return this;
        }

        public Builder<V, D> withModel(VolumeSearchResultModel<V, D> model) {
            this.model = model;
            return this;
        }

        public Builder<V, D> withControl(VolumeSearchResultControl<V, D> control) {
            this.control = control;
            return this;
        }

        public VolumeSearchQueryModuleMVC<V, D> build() {
            return new VolumeSearchQueryModuleMVC<V, D>(this);
        }
    }
}
