package kz.bikecology.data.dao.source;

import kz.bikecology.data.dao.BaseDAO;
import kz.bikecology.data.models.source.Source;
import org.hibernate.Transaction;

import java.util.List;

public class SourceDAO extends BaseDAO {
    public void create(String name) {
        Transaction tr = session.beginTransaction();

        Source source = new Source(name);
        session.persist(source);

        tr.commit();
    }

    public List<Source> getAll() {
        return session.createQuery("from Source", Source.class).list();
    }

    public Source getById(int id) {
        return session.find(Source.class, id);
    }
}
