package kz.bikecology.data.dao.facility;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.source.Source;

import java.util.List;

public class FacilityDAO extends BaseDAO {
    public Facility getById(int id) {
        return session.find(Facility.class, id);
    }

    public List<Facility> getAll() {
        return session.createQuery("from Facility", Facility.class).list();
    }
}
