package modelo;

import java.sql.PreparedStatement;
import controlador.conexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class productoDado {

    DefaultTableModel ModeloTabla;
    conexionBD conexion = new conexionBD(); // Instancia de Conexion Base de Datos
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List listar() {
        String sql = "select * from repuestos";
        List<producto> lista = new ArrayList<>();
        try {
            con = conexion.conectarBaseDatos();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                producto Producto = new producto();
                Producto.setCodigo(rs.getInt(1));
                Producto.setNombre(rs.getString(2));
                Producto.setMarca(rs.getString(3));
                Producto.setPrecio(rs.getDouble(4));
                Producto.setCantidad(rs.getInt(5));
                lista.add(Producto);
            }

        } catch (Exception e) {
            System.out.println("Error al listar: " + e);
        }
        return lista;
    }  // FIN DEL METODO LISTAR

    //METODO AGREGAR
    public void agregar(producto Producto) {
        String sql = "insert into repuestos(Nombre, Marca, Precio, Cantidad) values(?, ?, ?, ?)";
        try {
            con = conexion.conectarBaseDatos();
            ps = con.prepareStatement(sql);
            ps.setString(1, Producto.getNombre());
            ps.setString(2, Producto.getMarca());
            ps.setDouble(3, Producto.getPrecio());
            ps.setInt(4, Producto.getCantidad());
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "El producto ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error en agregar: " + e);
        }
    } //FIN DEL METODO AGREGAR

    //METODO ACTUALIZAR
    public void actualizar(producto Producto) {
        String sql = "update repuestos set Nombre=?, Marca=?, Precio=?, Cantidad=? where Codigo=?";
        try {
            con = conexion.conectarBaseDatos();
            ps = con.prepareStatement(sql);
            ps.setString(1, Producto.getNombre());
            ps.setString(2, Producto.getMarca());
            ps.setDouble(3, Producto.getPrecio());
            ps.setInt(4, Producto.getCantidad());
            ps.setInt(5, Producto.getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en ActualizarDado" + e);

        }
    }   //FIN DEL METODO ACTUAlIZAR

//METODO BORRAR
    public void borrar(int id) {
        String sql = "delete from repuestos where Codigo=" + id;
        try {
            con = conexion.conectarBaseDatos();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en borrarDado: " + e);
        }
    } // FIN DEL METODO BORRAR

    //METODO BUSCAR
    public void Buscar(String valor, String filtro, JTable tabla) {

        String[] columnas = {"Codigo", "Nombre", "Marca", "Precio", "Cantidad"};
        Object[] registros = new Object[5];
        ModeloTabla = new DefaultTableModel(null, columnas);
        String sql;
        Connection con = null;

        if (filtro.equals("Codigo")) {
            sql = "SELECT * FROM repuestos WHERE Codigo LIKE  '%" + valor + "%'";

        } else if (filtro.equals("Nombre")) {

            sql = "SELECT * FROM repuestos WHERE Nombre LIKE  '%" + valor + "%'";
        } else {
            sql = "SELECT * FROM repuestos WHERE Marca LIKE  '%" + valor + "%'";
        }

        try {
            con = conexion.conectarBaseDatos();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                registros[0] = rs.getInt("Codigo");
                registros[1] = rs.getString("Nombre");
                registros[2] = rs.getString("Marca");
                registros[3] = rs.getString("Precio");
                registros[4] = rs.getString("Cantidad");

                ModeloTabla.addRow(registros);
            }
            tabla.setModel(ModeloTabla);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Error durante el procedimiento Buscar");

        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error de conexión");
                }
            }
        }

    } //FIN DEL METODO BUSCAR

} //FIN DE LA CLASE ProductoDado
