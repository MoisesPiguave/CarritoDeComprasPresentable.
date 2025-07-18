package ec.edu.ups.dao;

import ec.edu.ups.modelo.Producto;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para la entidad {@link Producto}.
 * Define el contrato para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y otras operaciones de consulta específicas relacionadas con los productos
 * en cualquier medio de persistencia (por ejemplo, base de datos, archivos, memoria).
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public interface ProductoDAO {

    /**
     * Crea un nuevo registro de producto en el medio de persistencia.
     *
     * @param producto El objeto {@link Producto} que se desea persistir.
     */
    void crear(Producto producto);

    /**
     * Busca y retorna un producto específico utilizando su código único.
     *
     * @param codigo El código entero que identifica al producto.
     * @return El objeto {@link Producto} correspondiente al código, o {@code null} si no se encuentra.
     */
    Producto buscarPorCodigo(int codigo);

    /**
     * Busca y retorna una lista de productos cuyos nombres coincidan parcial o totalmente
     * con el nombre proporcionado (usualmente una búsqueda de "contiene" o "empieza con").
     * La implementación específica determinará si la búsqueda es sensible a mayúsculas y minúsculas.
     *
     * @param nombre El nombre o parte del nombre del producto a buscar.
     * @return Una {@link List} de objetos {@link Producto} que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran productos con el nombre especificado.
     */
    List<Producto> buscarPorNombre(String nombre);

    /**
     * Actualiza un producto existente en el medio de persistencia.
     * Los datos del producto se sobrescriben con la información proporcionada.
     *
     * @param producto El objeto {@link Producto} con los datos actualizados.
     */
    void actualizar(Producto producto);

    /**
     * Elimina un producto del medio de persistencia utilizando su código.
     *
     * @param codigo El código entero del producto que se desea eliminar.
     */
    void eliminar(int codigo);

    /**
     * Lista y retorna todos los productos presentes en el medio de persistencia.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Producto} almacenados.
     * Retorna una lista vacía si no hay productos.
     */
    List<Producto> listarTodos();
}