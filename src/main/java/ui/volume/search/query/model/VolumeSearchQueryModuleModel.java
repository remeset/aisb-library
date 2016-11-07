package ui.volume.search.query.model;

import java.beans.PropertyChangeListener;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import ui.volume.search.item.domain.VolumeListItemModel;

public class VolumeSearchQueryModuleModel {
    private String byTitle;
    private String byAuthor;
    private String byISBN;
    private VolumeListItemModel<?> selectedVolume;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public String getByTitle() {
        return byTitle;
    }

    public void setByTitle(String byTitle) {
        changeSupport.firePropertyChange("byTitle", this.byTitle, this.byTitle = byTitle);
    }

    public String getByAuthor() {
        return byAuthor;
    }

    public void setByAuthor(String byAuthor) {
        changeSupport.firePropertyChange("byAuthor", this.byAuthor, this.byAuthor = byAuthor);
    }

    public String getByISBN() {
        return byISBN;
    }

    public void setByISBN(String byISBN) {
        changeSupport.firePropertyChange("byISBN", this.byISBN, this.byISBN = byISBN);
    }

    public VolumeListItemModel<?> getSelectedVolume() {
        return selectedVolume;
    }

    public void setSelectedVolume(VolumeListItemModel<?> selectedVolume) {
        changeSupport.firePropertyChange("selectedVolume", this.selectedVolume, this.selectedVolume = selectedVolume);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
