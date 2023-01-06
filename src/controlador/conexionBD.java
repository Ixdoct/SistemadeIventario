package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionBD {

    Connection con;
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://127.0.0.1/inventario";
    String usuario = "root";
    String contraseña = "";

    public Connection conectarBaseDatos() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexion Exitosa");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error en la conexion: " + e);
        }
        return con;
    }

}

class prueba {

    public static void main(String[] args) {
        Connection con;
        conexionBD conexion = new conexionBD();
        con = conexion.conectarBaseDatos();
    }
}
