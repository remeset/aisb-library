package ui.volume.search.query.control;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;

import persistence.api.volume.VolumeEntity;
import remote.googlebooks.domain.response.Volume;
import ui.volume.search.facade.VolumeListItemModelFacade;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;
import ui.volume.search.item.domain.VolumeListItemModel;
import ui.volume.search.item.renderer.LocalVolumeListCellRenderer;
import ui.volume.search.item.renderer.RemoteVolumeListCellRenderer;
import ui.volume.search.query.domain.VolumeSearchQueryModuleMVC;
import ui.volume.search.query.domain.VolumeSearchQueryModuleMVC.SearchResultViewType;
import ui.volume.search.query.model.VolumeSearchQueryModuleModel;
import ui.volume.search.query.view.VolumeSearchQueryModuleView;
import ui.volume.search.result.config.VolumeSearchResultConfiguration;
import ui.volume.search.result.control.VolumeSearchResultControl;
import ui.volume.search.result.model.VolumeSearchResultModel;
import ui.volume.search.result.view.VolumeSearchResultView;

public class VolumeSearchQueryModuleControl {
    private final VolumeSearchQueryModuleView view;
    private final VolumeSearchQueryModuleModel model;
    private final List<VolumeSearchQueryModuleMVC<? extends VolumeListItemModel<?>, ?>> volumeSearchQueryModuleMVCs;

    public VolumeSearchQueryModuleControl(
            VolumeSearchQueryModuleView volumeSearchQueryModuleView,
            VolumeSearchQueryModuleModel volumeSearchQueryModuleModel,
            VolumeListItemModelFacade<LocalVolumeListItemModel, VolumeEntity> localVolumeModelFacade,
            VolumeListItemModelFacade<RemoteVolumeListItemModel, Volume> remoteVolumeModelFacade) {
        this.view = volumeSearchQueryModuleView;
        this.model = volumeSearchQueryModuleModel;
        this.volumeSearchQueryModuleMVCs = Arrays.asList(
            buildResultListMVC(SearchResultViewType.LOCAL, view.getLocalResultView(), localVolumeModelFacade, new LocalVolumeListCellRenderer(), new VolumeSearchResultConfiguration.Builder().build()),
            buildResultListMVC(SearchResultViewType.REMOTE, view.getRemoteResultView(), remoteVolumeModelFacade, new RemoteVolumeListCellRenderer(), new VolumeSearchResultConfiguration.Builder().withAllowEmptySearchCriterias(false).build())
        );
        setupModelViewBinding();
        setupSearchButtonClickListener();
        setupSearchResultListSelectionChangeListener();
//        setupSearchButtonAccessibilityControl();
    }

    public void reload() {
        // There is no need to reload the remote source, so only local should be refreshed
        volumeSearchQueryModuleMVCs
            .stream()
            .filter(mvc -> SearchResultViewType.LOCAL.equals(mvc.getType()))
            .findFirst()
            .ifPresent(mvc -> mvc.getControl().reload());
    }

    public void setEditable(boolean value) {
        SwingUtilities.invokeLater(() -> {
            Arrays.asList(view.getTitleField(), view.getAuthorField(), view.getIsbnField()).stream().forEach(field -> {
                field.setEnabled(value);
                field.setEditable(value);
            });
            view.getSearchButton().setEnabled(value);
        });
        // Delegate request to search results
        volumeSearchQueryModuleMVCs.stream().forEach(mvc -> mvc.getControl().setEditable(value));
    }

    private <V extends VolumeListItemModel<D>, D> VolumeSearchQueryModuleMVC<V, D> buildResultListMVC(
            SearchResultViewType type,
            VolumeSearchResultView<V, D> view,
            VolumeListItemModelFacade<V, D> volumeModelFacade,
            ListCellRenderer<V> resultListCellRenderer,
            VolumeSearchResultConfiguration volumeSearchResultConfiguration) {
        VolumeSearchResultModel<V, D> model = new VolumeSearchResultModel<>();
        VolumeSearchResultControl<V, D> control = new VolumeSearchResultControl<>(view, model, volumeModelFacade, resultListCellRenderer, volumeSearchResultConfiguration);
        return new VolumeSearchQueryModuleMVC.Builder<V, D>().withView(view).withModel(model).withControl(control).build();
    }

    private void setupModelViewBinding() {
        PresentationModel<VolumeSearchQueryModuleModel> adapter = new PresentationModel<>(model);
        Bindings.bind(view.getTitleField(), adapter.getModel("byTitle"));
        Bindings.bind(view.getAuthorField(), adapter.getModel("byAuthor"));
        Bindings.bind(view.getIsbnField(), adapter.getModel("byISBN"));
    }

//    private void setupSearchButtonAccessibilityControl() {
//        // The 'search' button must not be enabled if there is no search param
//        List<JTextComponent> searchFields = Arrays.asList(
//            view.getTitleField(),
//            view.getAuthorField(),
//            view.getIsbnField());
//        // Setup document change listeners on the search fields (title/author/isbn)
//        searchFields
//            .stream()
//            .forEach(field -> field.getDocument().addDocumentListener(new DocumentListenerBuilder().withListener(event -> {
//                updateSearchButtonAccessibility(searchFields);
//            }).build()));
//        // Trigger the update on the first time
//        updateSearchButtonAccessibility(searchFields);
//    }

    private void setupSearchButtonClickListener() {
        view.getSearchButton().addActionListener(event -> {
            // Execute search both on local and remote sources
            this.volumeSearchQueryModuleMVCs.stream().forEach(mvc -> mvc.getControl().search(
                Optional.ofNullable(model.getByTitle()).filter(value -> !value.isEmpty()),
                Optional.ofNullable(model.getByAuthor()).filter(value -> !value.isEmpty()),
                Optional.ofNullable(model.getByISBN()).filter(value -> !value.isEmpty())));
        });
    }

    private void setupSearchResultListSelectionChangeListener() {
        volumeSearchQueryModuleMVCs.stream().forEach(mvc -> mvc.getModel().addPropertyChangeListener(event -> {
            if("selectedVolume".equals(event.getPropertyName())) {
                this.model.setSelectedVolume((VolumeListItemModel<?>)event.getNewValue());
            }
        }));
    }

//    private void updateSearchButtonAccessibility(List<JTextComponent> searchFields) {
//        view.getSearchButton().setEnabled(evaluateSearchButtonAccessibility(searchFields));
//    }

//    private boolean evaluateSearchButtonAccessibility(List<JTextComponent> searchFields) {
//        return searchFields.stream().anyMatch(component -> !component.getText().trim().isEmpty());
//    }

}
