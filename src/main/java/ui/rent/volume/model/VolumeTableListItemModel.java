package ui.rent.volume.model;

import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import ui.validation.annotation.AvailableAmountOfVolumes;
import ui.validation.model.QuantitativeVolumeModelHolder;

@AvailableAmountOfVolumes(message = "{i18n.rent.validation.tooManyVolumes}")
public class VolumeTableListItemModel implements QuantitativeVolumeModelHolder {
    private Long id;
    @NotNull(message = "{i18n.rent.validation.emptyVolume}")
    private VolumeCellRenderModel volume;
    private Integer amount;
    private Date returnDate;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    private VolumeTableListItemModel(Builder builder) {
        this.id = builder.id;
        this.volume = builder.volume;
        this.amount = builder.amount;
        this.returnDate = builder.returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        changeSupport.firePropertyChange("id", this.id, this.id = id);
    }

    public VolumeCellRenderModel getVolume() {
        return volume;
    }

    public void setVolume(VolumeCellRenderModel volume) {
        changeSupport.firePropertyChange("volume", this.volume, this.volume = volume);
    }

    @Override
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        changeSupport.firePropertyChange("amount", this.amount, this.amount = amount);
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        changeSupport.firePropertyChange("returnDate", this.returnDate, this.returnDate = returnDate);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public static class Builder {
        private Long id;
        private VolumeCellRenderModel volume;
        private Integer amount;
        private Date returnDate;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withVolume(VolumeCellRenderModel volume) {
            this.volume = volume;
            return this;
        }

        public Builder withAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder withReturnDate(Date returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public VolumeTableListItemModel build() {
            return new VolumeTableListItemModel(this);
        }
    }
}
