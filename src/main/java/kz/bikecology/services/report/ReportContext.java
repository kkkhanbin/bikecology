package kz.bikecology.services.report;

import kz.bikecology.data.models.facility.Facility;

import java.util.List;

public class ReportContext {
    private List<Facility> facilities;
    private int quarter;
    private int year;

    public ReportContext(List<Facility> facilities, int quarter, int year) {
        this.facilities = facilities;
        this.quarter = quarter;
        this.year = year;
    }

    public ReportContext() {}

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
