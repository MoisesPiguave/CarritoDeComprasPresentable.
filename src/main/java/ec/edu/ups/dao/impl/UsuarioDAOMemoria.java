package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementación de {@link UsuarioDAO} que gestiona la persistencia de objetos {@link Usuario}
 * en memoria. Los usuarios son almacenados en una lista {@link ArrayList} y no persisten al
 * reiniciar la aplicación. Al inicializar, se crea un usuario administrador por defecto si no existe.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioDAOMemoria implements UsuarioDAO {

    private List<Usuario> usuarios;
    private CuestionarioDAO cuestionarioDAO;

    /**
     * Constructor de la clase UsuarioDAOMemoria.
     * Inicializa la lista de usuarios y el DAO de cuestionario.
     * Si no existe, crea un usuario administrador por defecto con credenciales predefinidas.
     *
     * @param cuestionarioDAO El DAO para la gestión de cuestionarios, que no es utilizado
     * directamente en este DAO de Usuario pero es necesario para la inyección de dependencias.
     */
    public UsuarioDAOMemoria(CuestionarioDAO cuestionarioDAO) {
        this.usuarios = new ArrayList<>();
        this.cuestionarioDAO = cuestionarioDAO;

        String adminCedula = "2450097775";
        String adminPassword = "Admin@";
        String adminNombre = "Administrador Principal";
        String adminCelular = "0998765432";
        String adminCorreo = "admin@example.com";
        String adminFechaNacimiento = "01/Enero/1990";

        if (buscarPorUsuario(adminCedula) == null) {
            Usuario nuevoAdmin = new Usuario(adminCedula, adminPassword, Rol.ADMINISTRADOR,
                    adminNombre, adminFechaNacimiento, adminCelular, adminCorreo);
            usuarios.add(nuevoAdmin);
            System.out.println("DEBUG -> Administrador por defecto creado en memoria: " + adminCedula);
        } else {
            System.out.println("DEBUG -> El administrador por defecto (" + adminCedula + ") ya existe en memoria.");
        }
    }

    /**
     * Autentica a un usuario verificando su nombre de usuario (cédula) y contraseña.
     *
     * @param username La cédula del usuario.
     * @param contrasenia La contraseña del usuario.
     * @return El objeto {@link Usuario} si las credenciales son correctas, de lo contrario, {@code null}.
     */
    @Override
    public Usuario autenticar(String username, String contrasenia) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(username) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Crea un nuevo usuario y lo añade a la lista en memoria.
     *
     * @param usuario El objeto {@link Usuario} a añadir.
     */
    @Override
    public void crear(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("DEBUG -> Usuario creado: " + usuario.getCedula());
    }

    /**
     * Busca un usuario en la lista en memoria por su nombre de usuario (cédula).
     *
     * @param username La cédula del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, de lo contrario, {@code null}.
     */
    @Override
    public Usuario buscarPorUsuario(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Elimina un usuario de la lista en memoria por su nombre de usuario (cédula).
     *
     * @param username La cédula del usuario a eliminar.
     */
    @Override
    public void eliminar(String username) {
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getCedula().equals(username)) {
                iterator.remove();
                System.out.println("DEBUG -> Usuario eliminado: " + username);
                break;
            }
        }
    }

    /**
     * Actualiza un usuario existente en la lista en memoria.
     * Si encuentra un usuario con la misma cédula, reemplaza el objeto existente con el proporcionado.
     *
     * @param usuario El objeto {@link Usuario} con los datos actualizados y la cédula de un usuario existente.
     */
    @Override
    public void actualizar(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCedula().equals(usuario.getCedula())) {
                usuarios.set(i, usuario);
                System.out.println("DEBUG -> Usuario actualizado: " + usuario.getCedula());
                return;
            }
        }
    }

    /**
     * Retorna una copia de la lista de todos los usuarios actualmente almacenados en memoria.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Usuario} en memoria.
     */
    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    /**
     * Lista usuarios cuyos campos de cédula o nombre completo comienzan con (o contienen)
     * el texto proporcionado, de forma insensible a mayúsculas y minúsculas.
     * Si el nombre de usuario es nulo o vacío, devuelve una copia de todos los usuarios.
     *
     * @param username El texto a buscar en la cédula o nombre completo de los usuarios.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<Usuario> listarPorUsuario(String username) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        if (username == null || username.isEmpty()) {
            return new ArrayList<>(usuarios);
        }
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().toLowerCase().startsWith(username.toLowerCase()) ||
                    (usuario.getNombreCompleto() != null && usuario.getNombreCompleto().toLowerCase().contains(username.toLowerCase()))) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }
}