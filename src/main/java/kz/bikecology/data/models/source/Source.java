package kz.bikecology.data.models.source;

import jakarta.persistence.*;
import kz.bikecology.data.models.BaseModel;

@Entity
@Table(name = "sources")
public class Source extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Source(String name) {
        this.name = name;
    }

    public Source() {};

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
}
