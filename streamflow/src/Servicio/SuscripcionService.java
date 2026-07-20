package Servicio;

import Controlador.IContenidoDAO;
import Controlador.ISuscripcionDAO;
import Modelo.Contenido;
import java.util.ArrayList;

public class SuscripcionService {

    // DIP: el servicio recibe las interfaces por constructor, no crea
    // ni conoce las implementaciones concretas de los DAO.
    private IContenidoDAO contenidoDAO;
    private ISuscripcionDAO suscripcionDAO;

    public SuscripcionService(IContenidoDAO contenidoDAO, ISuscripcionDAO suscripcionDAO) {
        this.contenidoDAO = contenidoDAO;
        this.suscripcionDAO = suscripcionDAO;
    }

    // Navegar el catalogo es gratis, no genera ningun costo.
    public ArrayList<Contenido> verCatalogo() {
        return contenidoDAO.obtenerContenidos();
    }

    public ArrayList<Contenido> recomendarPorGenero(String genero) {
        ArrayList<Contenido> recomendados = new ArrayList<>();
        ArrayList<Contenido> lista = contenidoDAO.obtenerContenidos();

        for (Contenido c : lista) {
            if (c.getGenero().equalsIgnoreCase(genero)) {
                recomendados.add(c);
            }
        }

        return recomendados;
    }

    // Aqui es donde realmente se genera un costo: el usuario elige contratar
    // un contenido puntual del catalogo.
    public boolean contratarContenido(int usuarioId, int contenidoId) {
        if (suscripcionDAO.yaContratado(usuarioId, contenidoId)) {
            System.out.println("Ya tienes contratado ese contenido.");
            return false;
        }

        suscripcionDAO.contratarContenido(usuarioId, contenidoId);
        return true;
    }

    public ArrayList<Contenido> obtenerMiSuscripcion(int usuarioId) {
        return suscripcionDAO.obtenerContratados(usuarioId);
    }

    // El total mensual solo suma lo que el usuario contrato, no todo el catalogo.
    public double calcularSuscripcionMensual(int usuarioId) {
        ArrayList<Contenido> contratados = suscripcionDAO.obtenerContratados(usuarioId);
        double total = 0.0;

        for (Contenido c : contratados) {
            total += c.calcularCosto();
        }

        return total;
    }

}
