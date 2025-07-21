package kz.bikecology.data.dao.facility;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.facility.Facility;

import java.util.List;

public class FacilityDAO extends BaseDAO {
    public List<Facility> getAll() {
        return session.createQuery("from Facility", Facility.class).list();
    }
}
