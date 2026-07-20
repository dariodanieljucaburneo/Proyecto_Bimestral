package Controlador;

import Modelo.Contenido;
import java.util.ArrayList;

public interface ISuscripcionDAO {

    void contratarContenido(int usuarioId, int contenidoId);

    ArrayList<Contenido> obtenerContratados(int usuarioId);

    boolean yaContratado(int usuarioId, int contenidoId);

}
