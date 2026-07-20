package Controlador;

import Modelo.*;
import Util.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;

public class ContenidoDAO implements IContenidoDAO {

    // Todo vive en una sola tabla ("streamflow"), diferenciado por la
    // columna tipo. Para contenido del catalogo, tipo = 'CONTENIDO'.

    @Override
    public void registrarContenido(Contenido contenido) {
        String sql = "INSERT INTO streamflow (id, tipo, titulo, genero, tipoContenido, calidad, costo) "
                + "VALUES (?, 'CONTENIDO', ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, UsuarioDAO.siguienteId(conn));
            pstmt.setString(2, contenido.getTitulo());
            pstmt.setString(3, contenido.getGenero());
            pstmt.setString(4, contenido.tipoContenido());
            pstmt.setString(5, contenido.getCalidad().name());
            pstmt.setDouble(6, contenido.calcularCosto());
            pstmt.executeUpdate();
            System.out.println("Contenido registrado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar contenido: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Contenido> obtenerContenidos() {
        ArrayList<Contenido> lista = new ArrayList<>();
        String sql = "SELECT * FROM streamflow WHERE tipo = 'CONTENIDO'";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Contenido contenido = mapearContenido(rs);
                if (contenido != null) {
                    lista.add(contenido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener contenidos: " + e.getMessage());
        }

        return lista;
    }

    // Reconstruye el objeto correcto (Pelicula, Serie o Documental) a partir
    // de una fila de la tabla unica. No es logica de negocio, asi que no
    // rompe el OCP del Servicio ni de la Vista.
    static Contenido mapearContenido(ResultSet rs) throws SQLException {
        String titulo = rs.getString("titulo");
        String genero = rs.getString("genero");
        String tipo = rs.getString("tipoContenido");
        Calidad calidad = Calidad.valueOf(rs.getString("calidad"));

        Contenido contenido = switch (tipo) {
            case "Pelicula" -> new Pelicula(titulo, genero, calidad);
            case "Serie" -> new Serie(titulo, genero, calidad);
            case "Documental" -> new Documental(titulo, genero, calidad);
            default -> null;
        };

        if (contenido != null) {
            contenido.setId(rs.getInt("id"));
        }

        return contenido;
    }

}
