package kz.bikecology.data.models.feeRates;

import kz.bikecology.data.models.BaseModel;

import jakarta.persistence.*;

@Entity
@Table(name = "feeRates")
public class FeeRate extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Float feeRate;

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

    public Float getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(Float feeRate) {
        this.feeRate = feeRate;
    }
}

