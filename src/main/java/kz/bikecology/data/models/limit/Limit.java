package kz.bikecology.data.models.limit;

import jakarta.persistence.*;

import kz.bikecology.data.models.BaseModel;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.pollutant.Pollutant;
import kz.bikecology.data.models.source.Source;

@Entity
@Table(name = "limits")
public class Limit extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "pollutantId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Pollutant pollutant;

    @JoinColumn(name = "sourceId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Source source;

    @JoinColumn(name = "facilityId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    private Float value;
    private int year;

    public Limit(int pollutantId, int facilityId, int sourceId, Float value, int year) {
        Pollutant pollutant = new Pollutant();
        pollutant.setId(pollutantId);

        Facility facility = new Facility();
        facility.setId(facilityId);

        Source source = new Source();
        source.setId(sourceId);

        this.facility = facility;
        this.pollutant = pollutant;
        this.source = source;
        this.value = value;
        this.year = year;
    }

    public Limit() {};

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Pollutant getPollutant() {
        return pollutant;
    }

    public void setPollutant(Pollutant pollutant) {
        this.pollutant = pollutant;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}

