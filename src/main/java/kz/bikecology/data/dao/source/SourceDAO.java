package kz.bikecology.data.dao.source;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.limit.Limit;
import kz.bikecology.data.models.source.Source;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SourceDAO extends BaseDAO {
    public List<Source> getAll() {
        return session.createQuery("from Source", Source.class).list();
    }

    public Source getById(int id) {
        return session.find(Source.class, id);
    }

    public List<Source> getByFacilityId(int facilityId) {
        String hql = "FROM Source WHERE facility.id = :facilityId";
        Query<Source> query = session.createQuery(hql, Source.class);
        query.setParameter("facilityId", facilityId);

        return query.getResultList();
    }
}
