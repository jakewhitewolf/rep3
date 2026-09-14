package com.example.rep3.factory;

import com.example.rep3.dao.DatabaseTaskDAO;
import com.example.rep3.dao.TaskDAO;

public class DatabaseDAOFactory implements DAOFactory {

    private final String databaseUrl;

    public DatabaseDAOFactory(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public TaskDAO createTaskDAO() {
        return new DatabaseTaskDAO(databaseUrl);
    }
}