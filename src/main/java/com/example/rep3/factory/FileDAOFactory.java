package com.example.rep3.factory;

import com.example.rep3.dao.FileTaskDAO;
import com.example.rep3.dao.TaskDAO;

public class FileDAOFactory implements DAOFactory {

    private final String filePath;

    public FileDAOFactory(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public TaskDAO createTaskDAO() {
        return new FileTaskDAO(filePath);
    }
}