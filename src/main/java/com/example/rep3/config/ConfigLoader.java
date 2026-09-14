package com.example.rep3.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigLoader {

    private final Properties properties = new Properties();

    public ConfigLoader() {
        load();
    }

    private void load() {
        Path path = Path.of("config.properties");

        if (!Files.exists(path)) {
            throw new RuntimeException(
                    "Файл config.properties не найден"
            );
        }

        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Ошибка загрузки config.properties",
                    e
            );
        }
    }

    public String getDataSource() {
        return properties.getProperty("datasource");
    }

    public String getFilePath() {
        return properties.getProperty("file.path");
    }

    public String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }
}