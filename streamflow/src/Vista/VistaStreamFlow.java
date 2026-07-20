package Vista;

import Modelo.*;
import Controlador.*;
import Servicio.SuscripcionService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class VistaStreamFlow {
    
    // DIP: la vista solo conoce las interfaces de los DAO, no las clases concretas.
    private IContenidoDAO contenidoDAO;
    private IUsuarioDAO usuarioDAO;
    private ISuscripcionDAO suscripcionDAO;
    private SuscripcionService servicio;
    private Scanner scanner;
    
    private Usuario usuarioActual;

    public VistaStreamFlow() {
        contenidoDAO = new ContenidoDAO();
        usuarioDAO = new UsuarioDAO();
        suscripcionDAO = new SuscripcionDAO();
        servicio = new SuscripcionService(contenidoDAO, suscripcionDAO);
        scanner = new Scanner(System.in);
    }
    
    // Menu de bienvenida: primero hay que iniciar sesion o registrarse.
    
    public void menu(){
        int opcion;
        
        do{
            System.out.println("\nBienvenido a StreamFlow");
            System.out.println("1. Iniciar sesion.");
            System.out.println("2. Registrarme.");
            System.out.println("0. Salir ");
            opcion = scanner.nextInt();
                switch (opcion) {
                    case 1: 
                        iniciarSesion();
                        break;
                    case 2:
                        registrarUsuario();
                        break;
                    case 0:
                        System.out.println("Muchas gracias por usar StreamFlow.");
                        break;
                    default:
                        System.out.println("Ingrese una opcion valida entre 0 y 2");
                        break;
                }
                
                // Si el login/registro dejo una sesion activa, entramos al menu principal.
                if (usuarioActual != null) {
                    menuPrincipal();
                    usuarioActual = null;
                }
        }while(opcion != 0 );
       
    }
    
        public void registrarUsuario() {
            scanner.nextLine();
            System.out.println("Ingresa tu nombre: ");
            String nombre = scanner.nextLine();
            System.out.println("Ingresa tu correo: ");
            String correo = scanner.nextLine();
            
            if (usuarioDAO.existeCorreo(correo)) {
                System.out.println("Ya existe una cuenta con ese correo.");
                return;
            }
            
            System.out.println("Ingresa una contrasena: ");
            String contrasena = scanner.nextLine();
            
            Usuario nuevo = new Usuario(nombre, correo, contrasena);
            usuarioDAO.registrarUsuario(nuevo);
            
            System.out.println("Cuenta creada. Ahora inicia sesion.");
        }
        
        public void iniciarSesion() {
            scanner.nextLine();
            System.out.println("Correo: ");
            String correo = scanner.nextLine();
            System.out.println("Contrasena: ");
            String contrasena = scanner.nextLine();
            
            Usuario usuario = usuarioDAO.validarCredenciales(correo, contrasena);
            
            if (usuario == null) {
                System.out.println("Correo o contrasena incorrectos.");
                return;
            }
            
            usuarioActual = usuario;
            System.out.println("Hola, " + usuario.getNombre() + "!");
        }
        
        // Menu principal, ya con sesion iniciada.
        
        public void menuPrincipal() {
            int opcion;
            
            do{
                System.out.println("\n--- Menu de " + usuarioActual.getNombre() + " ---");
                System.out.println("1. Ver catalogo (gratis).");
                System.out.println("2. Recomendaciones por genero (gratis).");
                System.out.println("3. Contratar contenido.");
                System.out.println("4. Ver mi suscripcion.");
                System.out.println("0. Cerrar sesion ");
                opcion = scanner.nextInt();
                    switch (opcion) {
                        case 1: 
                            mostrarCatalogo();
                            break;
                        case 2:
                            recomendarPorGenero();
                            break;
                        case 3:
                            contratarContenido();
                            break;
                        case 4:
                            verMiSuscripcion();
                            break;
                        case 0:
                            System.out.println("Sesion cerrada.");
                            break;
                        default:
                            System.out.println("Ingrese una opcion valida entre 0 y 4");
                            break;
                    }
            }while(opcion != 0 );
        }
    
        public void mostrarCatalogo() {
             ArrayList<Contenido> lista = servicio.verCatalogo();
        
             System.out.println("\n--- Catalogo StreamFlow (por genero) ---");
        
             if (lista.isEmpty()) {
                System.out.println("No hay contenidos registrados.");
                return;
            }
            
            // Agrupamos por genero en un mapa simple: genero -> lista de contenidos
            LinkedHashMap<String, ArrayList<Contenido>> porGenero = new LinkedHashMap<>();
            
            for (Contenido item : lista) {
                String genero = item.getGenero();
                
                if (!porGenero.containsKey(genero)) {
                    porGenero.put(genero, new ArrayList<>());
                }
                
                porGenero.get(genero).add(item);
            }
            
            for (String genero : porGenero.keySet()) {
                System.out.println("\n>> " + genero + ":");
                
                for (Contenido item : porGenero.get(genero)) {
                    System.out.println("   [" + item.getId() + "] " + item.obtenerDetalles() + " - $" + item.calcularCosto() + "/mes si lo contratas");
                }
            }
        }
        
        public void recomendarPorGenero() {
            scanner.nextLine();
            System.out.println("Ingresa el genero a buscar: ");
            String genero = scanner.nextLine();
            
            ArrayList<Contenido> recomendados = servicio.recomendarPorGenero(genero);
            
            System.out.println("\n--- Recomendaciones de genero " + genero + " ---");
            
            if (recomendados.isEmpty()) {
                System.out.println("No hay contenidos de ese genero.");
            } else {
                for (Contenido item : recomendados){
                    System.out.println("   [" + item.getId() + "] " + item.obtenerDetalles());
                }
            }
        }
        
        public void contratarContenido() {
            mostrarCatalogo();
            
            System.out.println("\nIngresa el id del contenido que quieres contratar: ");
            int contenidoId = scanner.nextInt();
            
            boolean exito = servicio.contratarContenido(usuarioActual.getId(), contenidoId);
            
            if (exito) {
                System.out.println("Listo, ya puedes verlo en tu suscripcion.");
            }
        }
        
        public void verMiSuscripcion() {
            ArrayList<Contenido> contratados = servicio.obtenerMiSuscripcion(usuarioActual.getId());
            
            System.out.println("\n--- Mi suscripcion ---");
            
            if (contratados.isEmpty()) {
                System.out.println("Todavia no has contratado ningun contenido.");
                return;
            }
            
            for (Contenido item : contratados) {
                System.out.println("   " + item.obtenerDetalles() + " - $" + item.calcularCosto() + "/mes");
            }
            
            double total = servicio.calcularSuscripcionMensual(usuarioActual.getId());
            System.out.println("\nTotal de tu suscripcion mensual: $" + total);
        }
    
}
