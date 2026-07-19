package Modelo;

public abstract class Contenido implements IReproducible, IDetallable {
    
    protected int id;
    protected String titulo;
    protected String genero;
    protected Calidad calidad;

    public Contenido(String titulo, String genero, Calidad calidad) {
        this.titulo = titulo;
        this.genero = genero;
        this.calidad = calidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Calidad getCalidad() {
        return calidad;
    }

    public void setCalidad(Calidad calidad) {
        this.calidad = calidad;
    }
    
    // OCP: nuevos tipos de contenido (Podcast, AudioLibro, etc.) solo
    // tienen que extender esta clase e implementar estos dos métodos,
    // sin tocar Servicio ni Controlador.
    public abstract String tipoContenido();
    public abstract double calcularCosto();

    @Override
    public String obtenerDetalles() {
        return tipoContenido() + " - \"" + titulo + "\" (" + genero + ", " + calidad + ")";
    }

    @Override
    public void reproducir() {
        System.out.println("Reproduciendo " + tipoContenido().toLowerCase() + ": " + titulo);
    }
    
}
