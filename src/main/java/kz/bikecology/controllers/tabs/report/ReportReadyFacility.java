package kz.bikecology.controllers.tabs.report;

import kz.bikecology.data.models.facility.Facility;

public class ReportReadyFacility {
    private Boolean selected;
    private Facility facility;

    public ReportReadyFacility(Boolean selected, Facility facility) {
        this.selected = selected;
        this.facility = facility;
    }

    public Boolean isSelected() {
        return this.selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
