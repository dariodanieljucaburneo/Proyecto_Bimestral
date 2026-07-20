package Controlador;

import Modelo.Usuario;

public interface IUsuarioDAO {

    void registrarUsuario(Usuario usuario);

    Usuario validarCredenciales(String correo, String contrasena);

    boolean existeCorreo(String correo);

}
