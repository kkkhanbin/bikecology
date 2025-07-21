package kz.bikecology.data;

import kz.bikecology.data.models.facilityFuels.FacilityFuels;
import kz.bikecology.data.models.feeRates.FeeRate;
import kz.bikecology.data.models.fuel.Fuel;
import kz.bikecology.data.models.facility.Facility;
import kz.bikecology.data.models.record.Record;
import kz.bikecology.data.models.region.Region;
import kz.bikecology.data.models.source.Source;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConnection {
    private static final SessionFactory sessionFactory;

    static {
        Configuration cfg = new Configuration();

        cfg.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect")
                .setProperty("hibernate.connection.url", "jdbc:sqlite:src/main/resources/database/database.db")
                .setProperty("hibernate.show_sql", "false")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update");

        cfg.addAnnotatedClass(Source.class)
                .addAnnotatedClass(Record.class)
                .addAnnotatedClass(FeeRate.class)
                .addAnnotatedClass(Fuel.class)
                .addAnnotatedClass(Region.class)
                .addAnnotatedClass(Facility.class)
                .addAnnotatedClass(FacilityFuels.class);

        sessionFactory = cfg.buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}