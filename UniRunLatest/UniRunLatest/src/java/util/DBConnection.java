package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the JDBC connection to the JavaDB (Apache Derby) database.
 * Make sure the JavaDB Network Server is running (NetBeans starts it
 * automatically when you connect in the Services window) and that a
 * database called UniRunDB has been created (see sql/create_database.sql).
 */
public class DBConnection {

    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static final String URL = "jdbc:derby://localhost:1527/UniRunDB1";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Derby Client Driver not found in classpath.", e);
        }
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }
}
