package com.example.alexander.androidproject;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alexander on 26.12.2014.
 */
public class LinkDAO extends BaseDaoImpl<Link, Integer> {

    public LinkDAO(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Link.class);
    }

    public List<Link> getAllLinks() throws SQLException {
        return queryBuilder().query();
    }

    public void clear() throws SQLException {
        TableUtils.clearTable(this.connectionSource, Link.class);
    }
}