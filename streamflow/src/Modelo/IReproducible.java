package Modelo;

// Interfaz segregada (ISP): solo agrupa el comportamiento de reproducción,
// no mezclamos aquí métodos de persistencia ni de facturación.
public interface IReproducible {
    
    void reproducir();
    
}
