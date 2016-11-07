package ui.volume.search.result.control;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.swing.JSpinner;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;

import remote.googlebooks.domain.request.VolumesSearchQuery;
import ui.core.list.renderer.SelectableListCellRendererWrapper;
import ui.core.list.selection.ToggleListSelectionWrapper;
import ui.volume.search.facade.VolumeListItemModelFacade;
import ui.volume.search.item.domain.VolumeListItemModel;
import ui.volume.search.item.domain.VolumesListModelImpl;
import ui.volume.search.result.compare.VolumeSearchCriteriaComparator;
import ui.volume.search.result.config.VolumeSearchResultConfiguration;
import ui.volume.search.result.event.SearchResultChangeEvent;
import ui.volume.search.result.event.SearchResultChangeEventListener;
import ui.volume.search.result.model.VolumeSearchResultModel;
import ui.volume.search.result.view.VolumeSearchResultView;

public class VolumeSearchResultControl<V extends VolumeListItemModel<D>, D> {
    private static final VolumeSearchCriteriaComparator searchCriteriaComparator = new VolumeSearchCriteriaComparator();
    private static final VolumesSearchQuery emptySearchCriteria = new VolumesSearchQuery.Builder().build();
    private static final int maxItemsPerPage = 20;

    private static final Logger logger = LoggerFactory.getLogger(VolumeSearchResultControl.class);

    private final VolumeSearchResultView<V, D> view;
    private final VolumeSearchResultModel<V, D> model;
    private final PresentationModel<VolumeSearchResultModel<V, D>> adapter;
    private final VolumeListItemModelFacade<V, D> volumeListItemModelFacade;
    private final VolumeSearchResultConfiguration volumeSearchResultConfiguration;

    private final EventListenerList listenerList = new EventListenerList();

    private CompletableFuture<VolumesListModelImpl<V, D>> completableFuture = null;

    public VolumeSearchResultControl(
            VolumeSearchResultView<V, D> resultView,
            VolumeSearchResultModel<V, D> resultModel,
            VolumeListItemModelFacade<V, D> volumeModelRetrivalFacade,
            ListCellRenderer<V> resultListCellRenderer) {
        this(resultView, resultModel, volumeModelRetrivalFacade, resultListCellRenderer, new VolumeSearchResultConfiguration.Builder().build());
    }

    public VolumeSearchResultControl(
            VolumeSearchResultView<V, D> resultView,
            VolumeSearchResultModel<V, D> resultModel,
            VolumeListItemModelFacade<V, D> volumeModelRetrivalFacade,
            ListCellRenderer<V> resultListCellRenderer,
            VolumeSearchResultConfiguration volumeSearchResultConfiguration) {
        this.view = resultView;
        this.model = resultModel;
        this.adapter = new PresentationModel<>(model);
        this.volumeListItemModelFacade = volumeModelRetrivalFacade;
        this.volumeSearchResultConfiguration = volumeSearchResultConfiguration;
        setupModelViewBinding();
        setupResultListCellRenderer(resultListCellRenderer);
        setupPageNumberSpinnerChangeListener();
        clearPageNumberSpinnerBinding();
    }

    public void addSearchResultChangeEventListenerr(SearchResultChangeEventListener listener) {
        listenerList.add(SearchResultChangeEventListener.class, listener);
    }

    public void removeSearchResultChangeEventListener(SearchResultChangeEventListener listener) {
        listenerList.remove(SearchResultChangeEventListener.class, listener);
    }

    public void setEditable(boolean value) {
        SwingUtilities.invokeLater(() -> {
            Arrays.asList(view.getResultList(), view.getPageNumberSpinner()).stream().forEach(component -> component.setEnabled(value));
        });
    }

    public void reload() {
        search(Optional
            .ofNullable((VolumesSearchQuery) view.getResultList().getClientProperty("STORED_SEARCH_QUERY"))
            .orElse(new VolumesSearchQuery.Builder().build()));
    }

    public void search(Optional<String> title, Optional<String> author, Optional<String> isbn) {
        VolumesSearchQuery volumesSearchQuery = new VolumesSearchQuery.Builder()
            .withTitle(title)
            .withAuthor(author)
            .withIsbn(isbn)
            .withStartIndex(0)
            .withMaxResults(maxItemsPerPage)
            .build();
        // Execute search operation
        search(volumesSearchQuery);
    }

    private void search(VolumesSearchQuery volumesSearchQuery) {
        // Store search query
        view.getResultList().putClientProperty("STORED_SEARCH_QUERY", volumesSearchQuery);
        // If there is a running CompletableFuture (from a previous search) it needs to be cancelled
        Optional.ofNullable(completableFuture).ifPresent(future -> future.cancel(true));
        // Call search facade or display notification about missing search parameters
        if(volumeSearchResultConfiguration.isAllowEmptySearchCriterias() || searchCriteriaComparator.compare(emptySearchCriteria, volumesSearchQuery) != 0) {
            logger.info("The following request has been sent: '{}'", volumesSearchQuery);
            // Hide empty search criteria notification
            view.getInsufficientSearchCriteriaNotificationPanel().setVisible(false);
            // Show progress bar
            view.getSearchProgressBar().setVisible(true);
            // As this operation requires more time this needs to be done in an asynchronous way
            completableFuture = volumeListItemModelFacade.lookup(volumesSearchQuery);
            completableFuture.thenAccept(volumes -> {
                logger.info("The following result recieved: '{}'", volumes);
                // Calculate total number of pages
                int numberOfPages = Math.max(1, Double.valueOf(Math.ceil(Double.valueOf(volumes.getTotalItems()) / maxItemsPerPage)).intValue());
                // Update page number spinner
                initPageNumberSpinnerBinding(1, numberOfPages); // It's not enough to update the model as the limits (min and max values of the spinner) needs to be increased as well
                // Update total page number
                this.model.setTotalPageNumbers(numberOfPages);
                // Update result list
                this.model.setVolumes(volumes.getVolumes());
                // Fire search result change event with the actual result
                fireSearchResultChangeEvent(new SearchResultChangeEvent<V, D>(volumes.getVolumes()));
                // The refresh operation must be on the event dispatch thread
                SwingUtilities.invokeLater(() -> {
                    // Hide progress bar
                    view.getSearchProgressBar().setVisible(false);
                });
            }).exceptionally(ex -> {
                if(ex instanceof CancellationException || ex instanceof CompletionException) {
                    logger.info("The completable future execution has been canceled.");
                } else {
                    logger.error("Tho following error occures during image processing: ", ex);
                }
                return null;
            });
        } else {
            // Hide progress bar
            view.getSearchProgressBar().setVisible(false);
            // Show notification
            view.getInsufficientSearchCriteriaNotificationPanel().setVisible(true);
            // Clear binding for page spinner (to avoid additional page change requests due to binding)
            clearPageNumberSpinnerBinding();
            // Reset model
            model.setPageNumber(1);
            model.setTotalPageNumbers(1);
            model.setVolumes(Collections.emptyList());
            // Fire search result change event with empty result
            fireSearchResultChangeEvent(new SearchResultChangeEvent<V, D>(Collections.emptyList()));
        }
    }

    private void fireSearchResultChangeEvent(SearchResultChangeEvent<V, D> event) {
        Arrays.asList(listenerList.getListeners(SearchResultChangeEventListener.class)).forEach(listener -> listener.searchResultChanged(event));
    }

    private void setupPageNumberSpinnerChangeListener() {
        // Setup page spinner change listener
        view.getPageNumberSpinner().addChangeListener(event -> {
            // Use stored search query as a baseline if presence otherwise create query based on view elements
            Optional<VolumesSearchQuery> volumesSearchQuery = Optional
                .ofNullable((VolumesSearchQuery)view.getResultList().getClientProperty("STORED_SEARCH_QUERY"))
                .map(stored -> new VolumesSearchQuery.Builder()
                    .withTitle(stored.getTitle())
                    .withAuthor(stored.getAuthor())
                    .withIsbn(stored.getIsbn())
                    .withMaxResults(stored.getMaxResults())
                    // Only the page number specific query parameter needs to be overridden
                    .withStartIndex((((Number)((JSpinner)event.getSource()).getValue()).intValue() - 1) * maxItemsPerPage)
                    .build());
            // Execute search operation
            volumesSearchQuery.ifPresent(query -> search(query));
        });
    }

    @SuppressWarnings("unchecked")
    private void setupModelViewBinding() {
        // In this case the binding becomes a bit more complex as the toggle mode is not supported by jgoodies original SingleListSelectionAdapter, so the binding cannot be done like this:
        // Bindings.bind(view.getResultList(), new SelectionInList<>(adapter.getModel("volumes"), adapter.getModel("selectedVolume")));
        // 1. The selectionInList needs to be created the same way as in case of regular binding
        SelectionInList<V> selectionInList = new SelectionInList<>(adapter.getModel("volumes"), adapter.getModel("selectedVolume"));
        // 2. It needs to be set as model to the list
        view.getResultList().setModel(selectionInList);
        // 3. Instead of the selectionInList the wrapper model has to be used (with toggle support)
        view.getResultList().setSelectionModel(new ToggleListSelectionWrapper(new SingleListSelectionAdapter(selectionInList.getSelectionIndexHolder())));
        // 4. The component property handler needs to be setup the same way as in case of regular bindings
        Bindings.addComponentPropertyHandler(view.getResultList(), selectionInList.getSelectionHolder());
        Bindings.bind(view.getTotalPageNumberLabel(), ConverterFactory.createStringConverter(adapter.getModel("totalPageNumbers"), DecimalFormat.getInstance()));
    }

    private void clearPageNumberSpinnerBinding() {
        view.getPageNumberSpinner().setModel(new SpinnerNumberModel(1, 1, 1, 1));
    }

    private void initPageNumberSpinnerBinding(int min, int max) {
        view.getPageNumberSpinner().setModel(
            SpinnerAdapterFactory.createNumberAdapter(adapter.getModel("pageNumber"), 1, min, max, 1));
    }

    private void setupResultListCellRenderer(ListCellRenderer<V> resultListCellRenderer) {
        view.getResultList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getResultList().setCellRenderer(new SelectableListCellRendererWrapper<V>(resultListCellRenderer));
    }

}
