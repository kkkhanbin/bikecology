package kz.bikecology.data.dao.pollutant;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.pollutant.Pollutant;

import org.hibernate.query.Query;

import java.util.List;

public class PollutantDAO extends BaseDAO {
    public Pollutant getById(int id) {
        return session.find(Pollutant.class, id);
    }

    public Pollutant getByCode(String code) {
        String hql = "FROM Pollutant WHERE code = :code";
        Query<Pollutant> query = session.createQuery(hql, Pollutant.class);
        query.setParameter("code", code);

        return query.getSingleResult();
    }

    public List<Pollutant> getAll() {
        return session.createQuery("from Pollutant", Pollutant.class).list();
    }
}
