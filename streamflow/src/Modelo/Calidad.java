package Modelo;

// Calidad del contenido. Cada una trae su propio multiplicador
// para el cálculo del costo de suscripción.
public enum Calidad {
    
    SD(0.6),
    HD(1.0),
    CUATRO_K(1.5);
    
    private final double multiplicador;
    
    Calidad(double multiplicador) {
        this.multiplicador = multiplicador;
    }
    
    public double getMultiplicador() {
        return multiplicador;
    }
    
}
