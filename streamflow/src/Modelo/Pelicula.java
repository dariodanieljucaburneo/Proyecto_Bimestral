package Modelo;

public class Pelicula extends Contenido {

    private static final double PRECIO_BASE = 5.0;

    public Pelicula(String titulo, String genero, Calidad calidad) {
        super(titulo, genero, calidad);
    }

    @Override
    public String tipoContenido() {
        
        return "Pelicula";
        
    }

    @Override
    public double calcularCosto() {
        
        return PRECIO_BASE * calidad.getMultiplicador();
        
    }
    
    
    
}
