package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionSQLite {
    
    private static final String URL = "jdbc:sqlite:db/streamflow.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    
}
