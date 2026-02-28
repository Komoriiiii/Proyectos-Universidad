package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/base";
    private static final String USER = "root";
    private static final String PASS = "root"; //

    //retorna una conexion nueva cada vez que se llama
    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de MySQL: " + e.getMessage());
        }
    }
}