package kz.bikecology.data.models.region;

import jakarta.persistence.*;
import kz.bikecology.data.models.BaseModel;

@Entity
@Table(name = "regions")
public class Region extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Region(String name) {
        this.name = name;
    }

    public Region() {}

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
}
