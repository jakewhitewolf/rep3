package com.example.rep3.factory;

public class DAOFactoryProvider {

    public static DAOFactory create(
            String dataSource,
            String filePath,
            String databaseUrl
    ) {
        if (dataSource == null) {
            throw new IllegalArgumentException(
                    "Источник данных не указан"
            );
        }

        switch (dataSource.toLowerCase()) {
            case "collection":
                return new CollectionDAOFactory();

            case "file":
                return new FileDAOFactory(filePath);

            case "database":
                return new DatabaseDAOFactory(databaseUrl);

            default:
                throw new IllegalArgumentException(
                        "Неизвестный источник данных: " + dataSource
                );
        }
    }
}