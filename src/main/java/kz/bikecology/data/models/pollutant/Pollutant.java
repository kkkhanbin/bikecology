package kz.bikecology.data.models.pollutant;

import jakarta.persistence.*;
import kz.bikecology.data.models.BaseModel;

@Entity
@Table(name = "pollutants")
public class Pollutant extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String name;

    public Pollutant(String name) {
        this.name = name;
    }

    public Pollutant() {};

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
}

