package Controlador;

import Modelo.Contenido;
import java.util.ArrayList;

public interface IContenidoDAO {
    
    void registrarContenido(Contenido contenido);
    
    ArrayList<Contenido> obtenerContenidos();
    
}
