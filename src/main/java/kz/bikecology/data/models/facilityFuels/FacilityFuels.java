package kz.bikecology.data.models.facilityFuels;

import jakarta.persistence.*;

import kz.bikecology.data.models.BaseModel;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.fuel.Fuel;

@Entity
@Table(name = "facilityFuels")
public class FacilityFuels extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "facilityId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    @JoinColumn(name = "fuelId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Fuel fuel;

    public FacilityFuels(int facilityId, int fuelId) {
        Facility facility = new Facility();
        facility.setId(facilityId);

        Fuel fuel = new Fuel();
        fuel.setId(fuelId);

        this.facility = facility;
        this.fuel = fuel;
    }

    public FacilityFuels() {};

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

