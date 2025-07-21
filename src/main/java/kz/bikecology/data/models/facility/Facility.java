package kz.bikecology.data.models.facility;

import kz.bikecology.data.models.BaseModel;
import kz.bikecology.data.models.region.Region;

import jakarta.persistence.*;

@Entity
@Table(name = "facilities")
public class Facility extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String CATO;

    @JoinColumn(name = "regionId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    private String address;

    private String BIN;

    private String CCEA;

    private String productionProcessSpecification;

    private String objectCategory;

    public Facility(
            String name,
            String CATO,
            int regionId,
            String address,
            String BIN,
            String CCEA,
            String productionProcessSpecification,
            String objectCategory
    ) {
        Region region = new Region();
        region.setId(regionId);

        this.name = name;
        this.CATO = CATO;
        this.region = region;
        this.address = address;
        this.BIN = BIN;
        this.CCEA = CCEA;
        this.productionProcessSpecification = productionProcessSpecification;
        this.objectCategory = objectCategory;
    }

    public Facility() {};

    public String toString() {
        return String.format("%d. %s", this.getId(), this.getAddress());
    }

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

    public String getCATO() {
        return CATO;
    }

    public void setCATO(String CATO) {
        this.CATO = CATO;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBIN() {
        return BIN;
    }

    public void setBIN(String BIN) {
        this.BIN = BIN;
    }

    public String getCCEA() {
        return CCEA;
    }

    public void setCCEA(String CCEA) {
        this.CCEA = CCEA;
    }

    public String getProductionProcessSpecification() {
        return productionProcessSpecification;
    }

    public void setProductionProcessSpecification(String productionProcessSpecification) {
        this.productionProcessSpecification = productionProcessSpecification;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        this.objectCategory = objectCategory;
    }
}
