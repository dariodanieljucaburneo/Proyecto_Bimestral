package Modelo;

public class Documental extends Contenido {

    private static final double PRECIO_BASE = 4.0;

    public Documental(String titulo, String genero, Calidad calidad) {
        super(titulo, genero, calidad);
    }

    @Override
    public String tipoContenido() {
        
        return "Documental";
    }

    @Override
    public double calcularCosto() {
        return PRECIO_BASE * calidad.getMultiplicador();
    }
    
    
    
}
