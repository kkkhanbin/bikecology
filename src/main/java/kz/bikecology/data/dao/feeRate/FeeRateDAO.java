package kz.bikecology.data.dao.feeRate;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.feeRates.FeeRate;

import java.util.List;

public class FeeRateDAO extends BaseDAO {
    public FeeRate getByName(String name) {
        return session.createQuery("FROM FeeRate WHERE name = :name", FeeRate.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public List<FeeRate> getAll() {
        return session.createQuery("from FeeRate", FeeRate.class).list();
    }
}
