package ec.edu.ups.dao;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.Usuario;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para la entidad {@link Carrito}.
 * Define el contrato para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y otras operaciones de consulta específicas relacionadas con los carritos de compra
 * en cualquier medio de persistencia (por ejemplo, base de datos, archivos, memoria).
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public interface CarritoDAO {

    /**
     * Crea un nuevo registro de carrito en el medio de persistencia.
     *
     * @param carrito El objeto {@link Carrito} que se desea persistir.
     */
    void crear(Carrito carrito);

    /**
     * Busca y retorna un carrito específico utilizando su código único.
     *
     * @param codigo El código entero que identifica al carrito.
     * @return El objeto {@link Carrito} correspondiente al código, o {@code null} si no se encuentra.
     */
    Carrito buscarPorCodigo(int codigo);

    /**
     * Busca y retorna una lista de carritos asociados a un usuario específico.
     *
     * @param usuario El objeto {@link Usuario} cuyos carritos se desean buscar.
     * @return Una {@link List} de objetos {@link Carrito} que pertenecen al usuario dado.
     * Retorna una lista vacía si el usuario no tiene carritos o si el usuario es nulo.
     */
    List<Carrito> buscarPorUsuario(Usuario usuario);

    /**
     * Actualiza un carrito existente en el medio de persistencia.
     * Los datos del carrito se sobrescriben con la información proporcionada.
     *
     * @param carrito El objeto {@link Carrito} con los datos actualizados.
     */
    void actualizar(Carrito carrito);

    /**
     * Elimina un carrito del medio de persistencia utilizando su código.
     *
     * @param codigo El código entero del carrito que se desea eliminar.
     */
    void eliminar(int codigo);

    /**
     * Lista y retorna todos los carritos presentes en el medio de persistencia.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Carrito} almacenados.
     * Retorna una lista vacía si no hay carritos.
     */
    List<Carrito> listarTodos();
}