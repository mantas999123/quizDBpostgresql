package databaseFiles;

import loginMain.User;

import java.sql.*;

public class ConnectDB {

    public Connection connect() throws SQLException {

        ApplicationProperties properties = ApplicationProperties.getInstance();
        Connection connection = DriverManager.getConnection(properties.getValue("jdbc.postgresql.connection.url"),
                properties.getValue("jdbc.postgresql.connection.name"),
                properties.getValue("jdbc.postgresql.connection.password"));
        if (connection == null) {
            System.out.println("Nepavyko prisijunkti prie PostgresDB");
        }
        return connection;
    }
}