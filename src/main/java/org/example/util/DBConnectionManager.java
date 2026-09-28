package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Central place for obtaining a JDBC Connection to the MySQL database.
 *
 * Credentials are NEVER hard-coded here. They are read, in order of
 * preference, from:
 *   1. Environment variables: DB_URL, DB_USER, DB_PASSWORD
 *   2. A db.properties file on the classpath (src/main/resources/db.properties)
 *
 * db.properties is listed in .gitignore, so it never gets committed.
 * Copy db.properties.example to db.properties and fill in your own values.
 */
public final class DBConnectionManager {

    private static final Properties PROPS = new Properties();
    private static boolean loaded = false;

    private DBConnectionManager() {
        // utility class, no instances
    }

    private static synchronized void loadProperties() {
        if (loaded) {
            return;
        }
        try (InputStream in = DBConnectionManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read db.properties (" + e.getMessage() + "). "
                    + "Falling back to environment variables.");
        }
        loaded = true;
    }

    private static String resolve(String envKey, String propKey, String defaultValue) {
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        loadProperties();
        return PROPS.getProperty(propKey, defaultValue);
    }

    public static Connection getConnection() throws SQLException {
        String url = resolve("DB_URL", "db.url", "jdbc:mysql://localhost:3306/student_management_system");
        String user = resolve("DB_USER", "db.user", "root");
        String password = resolve("DB_PASSWORD", "db.password", "");

        return DriverManager.getConnection(url, user, password);
    }
}
