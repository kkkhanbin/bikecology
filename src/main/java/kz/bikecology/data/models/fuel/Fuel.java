package kz.bikecology.data.models.fuel;

import jakarta.persistence.*;
import kz.bikecology.data.models.BaseModel;

@Entity
@Table(name = "fuels")
public class Fuel extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String displayName;

    public Fuel(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public Fuel() {};

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

