package ec.edu.ups.dao;

import ec.edu.ups.modelo.Usuario;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para la entidad {@link Usuario}.
 * Define el contrato para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y otras operaciones de consulta específicas relacionadas con los usuarios,
 * como la autenticación, en cualquier medio de persistencia (por ejemplo, base de datos, archivos, memoria).
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public interface UsuarioDAO {

    /**
     * Autentica un usuario verificando el nombre de usuario (usualmente una cédula) y la contraseña.
     *
     * @param username El nombre de usuario (cédula) a autenticar.
     * @param contrasenia La contraseña a verificar.
     * @return El objeto {@link Usuario} si la autenticación es exitosa y las credenciales coinciden;
     * de lo contrario, devuelve {@code null}.
     */
    Usuario autenticar(String username, String contrasenia);

    /**
     * Crea un nuevo registro de usuario en el medio de persistencia.
     *
     * @param usuario El objeto {@link Usuario} que se desea persistir.
     */
    void crear(Usuario usuario);

    /**
     * Busca y retorna un usuario específico utilizando su nombre de usuario (cédula).
     *
     * @param username El nombre de usuario (cédula) del usuario a buscar.
     * @return El objeto {@link Usuario} correspondiente al nombre de usuario, o {@code null} si no se encuentra.
     */
    Usuario buscarPorUsuario(String username);

    /**
     * Elimina un usuario del medio de persistencia utilizando su nombre de usuario (cédula).
     *
     * @param username El nombre de usuario (cédula) del usuario que se desea eliminar.
     */
    void eliminar(String username);

    /**
     * Actualiza un usuario existente en el medio de persistencia.
     * Los datos del usuario se sobrescriben con la información proporcionada,
     * identificando al usuario por su cédula.
     *
     * @param usuario El objeto {@link Usuario} con los datos actualizados.
     */
    void actualizar(Usuario usuario);

    /**
     * Lista y retorna todos los usuarios presentes en el medio de persistencia.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Usuario} almacenados.
     * Retorna una lista vacía si no hay usuarios.
     */
    List<Usuario> listarTodos();

    /**
     * Lista y retorna usuarios que coinciden con el nombre de usuario (cédula) proporcionado.
     * La implementación puede variar para realizar búsquedas exactas, parciales o por otros atributos.
     * Si el parámetro `username` es nulo o vacío, este método debería retornar todos los usuarios.
     *
     * @param username El nombre de usuario (cédula) o un criterio de búsqueda para filtrar la lista.
     * @return Una {@link List} de objetos {@link Usuario} que cumplen con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran usuarios que coincidan.
     */
    List<Usuario> listarPorUsuario(String username);
}