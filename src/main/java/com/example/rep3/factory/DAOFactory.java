package com.example.rep3.factory;

import com.example.rep3.dao.TaskDAO;

public interface DAOFactory {

    TaskDAO createTaskDAO();
}