package kz.bikecology.data.dao.facilityFuels;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.facilityFuels.FacilityFuels;

import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class FacilityFuelsDAO extends BaseDAO {
    public void create(int facilityId, int fuelId) {
        Transaction tr = session.beginTransaction();

        FacilityFuels facilityFuels = new FacilityFuels(facilityId, fuelId);
        session.persist(facilityFuels);

        tr.commit();
    }

    public List<FacilityFuels> getByFacilityId(int facilityId) {
        String hql = "FROM FacilityFuels WHERE facility.id = :facilityId";
        Query<FacilityFuels> query = session.createQuery(hql, FacilityFuels.class);
        query.setParameter("facilityId", facilityId);

        return query.getResultList();
    }

    public List<FacilityFuels> getAll() {
        return session.createQuery("from FacilityFuels", FacilityFuels.class).list();
    }
}
