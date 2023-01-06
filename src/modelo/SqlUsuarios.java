package modelo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import controlador.conexionBD;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUsuarios extends conexionBD {

    public boolean registrar(usuarios usr) {

        PreparedStatement ps = null;
        Connection con = (Connection) conectarBaseDatos();

        String sql = "insert into usuarios (usuario, pass, nombre, correo, id_tipo) values(?,?,?,?,?)";

        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, usr.getUsuario());
            ps.setString(2, usr.getPass());
            ps.setString(3, usr.getNombre());
            ps.setString(4, usr.getCorreo());
            ps.setInt(5, usr.getId_tipo());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SqlUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean login(usuarios usr) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = (Connection) conectarBaseDatos();

        String sql = "select u.id, u.usuario, u.pass, u.nombre, u.id_tipo, t.nombre FROM usuarios AS u INNER JOIN tipo_usuario AS t ON u.id_tipo=t.id WHERE usuario = ?";

        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, usr.getUsuario());
            rs = (ResultSet) ps.executeQuery();

            if (rs.next()) {
                if (usr.getPass().equals(rs.getString(3))) {

                    String sqlUpdate = "UPDATE usuarios SET last_session = ? WHERE id=?";

                    ps = (PreparedStatement) con.prepareStatement(sqlUpdate);
                    ps.setString(1, usr.getLast_session());
                    ps.setInt(2, rs.getInt(1));
                    ps.execute();

                    usr.setId(rs.getInt(1));
                    usr.setNombre(rs.getString(4));
                    usr.setId_tipo(rs.getInt(5));
                    usr.setNombre_tipo(rs.getString(6));

                    return true;

                } else {
                    return false;
                }
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(SqlUsuarios.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        }
    }

    public int existeUsuario(String usuario) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = (Connection) conectarBaseDatos();

        String sql = "select count(id) FROM usuarios WHERE usuario = ?";

        try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, usuario);
            rs = (ResultSet) ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(SqlUsuarios.class.getName()).log(Level.SEVERE, null, ex);

            return 1;
        }
    }

    public boolean esEmail(String correo) {

        //Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(correo);
        return mather.find();

    }
}
