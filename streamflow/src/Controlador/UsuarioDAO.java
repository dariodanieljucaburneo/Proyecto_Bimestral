package Controlador;

import Modelo.Usuario;
import Util.ConexionSQLite;
import java.sql.*;

public class UsuarioDAO implements IUsuarioDAO {

    // Todo vive en una sola tabla ("streamflow"), diferenciado por la
    // columna tipo. Para usuarios, tipo = 'USUARIO'.
    // La tabla no tiene PRIMARY KEY, asi que el id se calcula a mano.

    @Override
    public void registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO streamflow (id, tipo, nombre, correo, contrasena) VALUES (?, 'USUARIO', ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, siguienteId(conn));
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getCorreo());
            pstmt.setString(4, usuario.getContrasena());
            pstmt.executeUpdate();
            System.out.println("Usuario registrado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario validarCredenciales(String correo, String contrasena) {
        String sql = "SELECT * FROM streamflow WHERE tipo = 'USUARIO' AND correo = ? AND contrasena = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(rs.getString("nombre"), rs.getString("correo"), rs.getString("contrasena"));
                    usuario.setId(rs.getInt("id"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar credenciales: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean existeCorreo(String correo) {
        String sql = "SELECT id FROM streamflow WHERE tipo = 'USUARIO' AND correo = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al validar correo: " + e.getMessage());
            return false;
        }
    }

    // Como la tabla no tiene PRIMARY KEY/AUTOINCREMENT, el siguiente id
    // se calcula tomando el maximo id actual de toda la tabla y sumando 1.
    static int siguienteId(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS siguiente FROM streamflow";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getInt("siguiente");
        }
    }

}
