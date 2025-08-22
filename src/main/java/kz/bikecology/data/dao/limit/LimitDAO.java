package kz.bikecology.data.dao.limit;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.limit.Limit;
import org.hibernate.query.Query;

import java.util.List;

public class LimitDAO extends BaseDAO {
    public Limit getById(int id) {
        return session.find(Limit.class, id);
    }

    public Limit getByPollutantIdAndFacilityId(int pollutantId, int facilityId) {
        String hql = "FROM Limit WHERE pollutant.id = :pollutantId AND facility.id = :facilityId";
        Query<Limit> query = session.createQuery(hql, Limit.class);
        query.setParameter("pollutantId", pollutantId);
        query.setParameter("facilityId", facilityId);

        return query.getSingleResult();
    }

    public List<Limit> getAll() {
        return session.createQuery("from Limit", Limit.class).list();
    }
}
