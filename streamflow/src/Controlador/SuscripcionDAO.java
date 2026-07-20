package Controlador;

import Modelo.*;
import Util.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;

public class SuscripcionDAO implements ISuscripcionDAO {

    // Todo vive en una sola tabla ("streamflow"), diferenciado por la
    // columna tipo. Una suscripcion es una fila con tipo = 'SUSCRIPCION'
    // que enlaza un usuarioId con un contenidoId, ambos ids de esa misma tabla.

    @Override
    public void contratarContenido(int usuarioId, int contenidoId) {
        String sql = "INSERT INTO streamflow (id, tipo, usuarioId, contenidoId) VALUES (?, 'SUSCRIPCION', ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, UsuarioDAO.siguienteId(conn));
            pstmt.setInt(2, usuarioId);
            pstmt.setInt(3, contenidoId);
            pstmt.executeUpdate();
            System.out.println("Contenido contratado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al contratar contenido: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Contenido> obtenerContratados(int usuarioId) {
        ArrayList<Contenido> lista = new ArrayList<>();
        String sql = "SELECT c.* FROM streamflow c "
                + "INNER JOIN streamflow s ON c.id = s.contenidoId "
                + "WHERE s.tipo = 'SUSCRIPCION' AND c.tipo = 'CONTENIDO' AND s.usuarioId = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contenido contenido = ContenidoDAO.mapearContenido(rs);
                    if (contenido != null) {
                        lista.add(contenido);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener contratados: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean yaContratado(int usuarioId, int contenidoId) {
        String sql = "SELECT id FROM streamflow WHERE tipo = 'SUSCRIPCION' AND usuarioId = ? AND contenidoId = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, contenidoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al validar suscripcion: " + e.getMessage());
            return false;
        }
    }

}
