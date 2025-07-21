package kz.bikecology.data.models.record;

import kz.bikecology.data.models.BaseModel;
import kz.bikecology.data.models.fuel.Fuel;

import jakarta.persistence.*;
import kz.bikecology.data.models.facility.Facility;

@Entity
@Table(name = "records")
public class Record extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "facilityId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    @JoinColumn(name = "fuelId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Fuel fuel;

    private int quarter;
    private int year;
    private Float amount;

    public Record(int facilityId, int fuelId, int quarter, int year, Float amount) {
        Facility facility = new Facility();
        facility.setId(facilityId);

        Fuel fuel = new Fuel();
        fuel.setId(fuelId);

        this.facility = facility;
        this.fuel = fuel;
        this.quarter = quarter;
        this.year = year;
        this.amount = amount;
    }

    public Record() {};

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}

