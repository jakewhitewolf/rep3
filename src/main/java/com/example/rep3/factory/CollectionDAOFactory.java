package com.example.rep3.factory;

import com.example.rep3.dao.CollectionTaskDAO;
import com.example.rep3.dao.TaskDAO;

public class CollectionDAOFactory implements DAOFactory {

    @Override
    public TaskDAO createTaskDAO() {
        return new CollectionTaskDAO();
    }
}