package kz.bikecology.controllers.tabs.report;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import kz.bikecology.data.models.facility.Facility;

public class ReportReadyFacility {
    private final BooleanProperty selected = new SimpleBooleanProperty(true);
    private Facility facility;

    public ReportReadyFacility(Facility facility) {
        this.facility = facility;
    }

    public BooleanProperty selectedProperty() { return this.selected; }

    public Boolean isSelected() {
        return this.selected.get();
    }

    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
