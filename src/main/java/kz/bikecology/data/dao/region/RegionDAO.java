package kz.bikecology.data.dao.region;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.region.Region;

import java.util.List;

public class RegionDAO extends BaseDAO {
    public List<Region> getAll() {
        return session.createQuery("from Region", Region.class).list();
    }
}
