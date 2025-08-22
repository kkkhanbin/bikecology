package kz.bikecology.data.dao.record;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.record.Record;

import org.apache.poi.hssf.record.RecalcIdRecord;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RecordDAO extends BaseDAO {
    public void create(int facilityId, int fuelId, int quarter, int year, Float amount) {
        Transaction tr = session.beginTransaction();

        Record record = new Record(facilityId, fuelId, quarter, year, amount);
        session.persist(record);

        tr.commit();
    }

    public void merge(int facilityId, int fuelId, int quarter, int year, Float amount) {
        Transaction tr = session.beginTransaction();

        try {
            Record record = get(facilityId, fuelId, quarter, year);
            record.setAmount(amount);
        } catch (NoResultException ignored) {
            session.persist(new Record(facilityId, fuelId, quarter, year, amount));
        }

        tr.commit();
    }

    public List<Record> getByYear(int year) {
        String hql = "FROM Record WHERE year = :year";
        Query<Record> query = session.createQuery(hql, Record.class);
        query.setParameter("year", year);

        return query.getResultList();
    }

    public List<Record> getByQuarterAndYear(int quarter, int year) {
        String hql = "FROM Record WHERE quarter = :quarter AND year = :year";
        Query<Record> query = session.createQuery(hql, Record.class);
        query.setParameter("quarter", quarter);
        query.setParameter("year", year);

        return query.getResultList();
    }

    public List<Record> getByFacilityQuarterAndYear(int facilityId, int quarter, int year) {
        String hql = "FROM Record WHERE facility.id = :facilityId AND quarter = :quarter AND year = :year";
        Query<Record> query = session.createQuery(hql, Record.class);
        query.setParameter("facilityId", facilityId);
        query.setParameter("quarter", quarter);
        query.setParameter("year", year);

        return query.getResultList();
    }

    public List<Record> getByFacilityQuarterAndYear(
            List<Integer> facilityIds, List<Integer> quarters, List<Integer> years
    ) {
        String hql = "FROM Record WHERE facility.id IN (:facilityIds) AND quarter IN (:quarters) AND year IN (:years)";
        Query<Record> query = session.createQuery(hql, Record.class);
        query.setParameter("facilityIds", facilityIds);
        query.setParameter("quarters", quarters);
        query.setParameter("years", years);

        return query.getResultList();
    }

    public Record get(int facilityId, int fuelId, int quarter, int year) {
        String hql = """
FROM Record WHERE facility.id = :facilityId AND fuel.id = :fuelId AND quarter = :quarter AND year = :year""";
        Query<Record> query = session.createQuery(hql, Record.class);
        query.setParameter("facilityId", facilityId);
        query.setParameter("fuelId", fuelId);
        query.setParameter("quarter", quarter);
        query.setParameter("year", year);

        return query.getSingleResult();
    }

    public List<Record> getAll() {
        return session.createQuery("from Record", Record.class).list();
    }
}
