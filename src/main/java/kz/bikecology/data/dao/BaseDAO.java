package kz.bikecology.data.dao;

import kz.bikecology.data.DBConnection;

import org.hibernate.Session;

public class BaseDAO {
    protected Session session = DBConnection.getSession();
}

