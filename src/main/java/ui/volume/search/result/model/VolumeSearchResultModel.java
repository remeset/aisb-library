package ui.volume.search.result.model;

import java.beans.PropertyChangeListener;
import java.util.List;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import ui.volume.search.item.domain.VolumeListItemModel;

public class VolumeSearchResultModel<V extends VolumeListItemModel<D>, D> {
    private List<V> volumes;
    private V selectedVolume;
    private Integer pageNumber = 1;
    private Integer totalPageNumbers = 1;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public List<V> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<V> volumes) {
        changeSupport.firePropertyChange("volumes", this.volumes, this.volumes = volumes);
    }

    public V getSelectedVolume() {
        return selectedVolume;
    }

    public void setSelectedVolume(V selectedVolume) {
        changeSupport.firePropertyChange("selectedVolume", this.selectedVolume, this.selectedVolume = selectedVolume);
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        changeSupport.firePropertyChange("pageNumber", this.pageNumber, this.pageNumber = pageNumber);
    }

    public Integer getTotalPageNumbers() {
        return totalPageNumbers;
    }

    public void setTotalPageNumbers(Integer totalPageNumbers) {
        changeSupport.firePropertyChange("totalPageNumbers", this.totalPageNumbers, this.totalPageNumbers = totalPageNumbers);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
