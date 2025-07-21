package kz.bikecology.data.dao.fuel;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.fuel.Fuel;

import java.util.List;

public class FuelDAO extends BaseDAO {
    public List<Fuel> getAll() {
        return session.createQuery("from Fuel", Fuel.class).list();
    }
}
