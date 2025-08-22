package kz.bikecology.data.models.source;

import jakarta.persistence.*;
import kz.bikecology.data.models.BaseModel;
import kz.bikecology.data.models.facility.Facility;

import java.util.List;

@Entity
@Table(name = "sources")
public class Source extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private String name;
    private Boolean isOrganized;

    @JoinColumn(name = "facilityId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    public Source(String name, String code, Boolean isOrganized, int facilityId) {
        Facility facility = new Facility();
        facility.setId(facilityId);

        this.name = name;
        this.code = code;
        this.isOrganized = isOrganized;
        this.facility = facility;
    }

    public Source() {};

    // Utilities
    public static int getNumberOfOrganized(List<Source> sources) {
        return sources.stream().filter(Source::isOrganized).toList().size();
    }

    public static int getNumberOfNonOrganized(List<Source> sources) {
        return sources.stream().filter(source -> !source.isOrganized()).toList().size();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isOrganized() {
        return isOrganized;
    }

    public void setOrganized(Boolean organized) {
        isOrganized = organized;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
