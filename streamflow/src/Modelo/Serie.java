package Modelo;

public class Serie extends Contenido {

    private static final double PRECIO_BASE = 8.0;

    public Serie(String titulo, String genero, Calidad calidad) {
        super(titulo, genero, calidad);
    }

    @Override
    public String tipoContenido() {
        
        return "Serie";
        
    }

    @Override
    public double calcularCosto() {
        return PRECIO_BASE * calidad.getMultiplicador();
    }
    
    
    
}
