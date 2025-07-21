package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementación de {@link ProductoDAO} que gestiona la persistencia de objetos {@link Producto}
 * en memoria. Los productos se almacenan en una lista {@link ArrayList} y no persisten al reiniciar la aplicación.
 * Al inicializar, se crea un producto predeterminado para poblar la lista.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoDAOMemoria implements ProductoDAO {

    private List<Producto> productos;

    /**
     * Constructor de la clase ProductoDAOMemoria.
     * Inicializa la lista de productos y añade un producto de ejemplo ("Balon Molten 7")
     * con un precio de 30 para comenzar.
     */
    public ProductoDAOMemoria() {
        productos = new ArrayList<Producto>();
        crear(new Producto(1, "Balon Molten 7", 30));
    }

    /**
     * Crea un nuevo producto y lo añade a la lista en memoria.
     *
     * @param producto El objeto {@link Producto} a ser creado y añadido.
     */
    @Override
    public void crear(Producto producto) {
        productos.add(producto);
    }

    /**
     * Busca un producto en la lista en memoria por su código.
     *
     * @param codigo El código entero del producto a buscar.
     * @return El objeto {@link Producto} si se encuentra, o {@code null} si no existe un producto con ese código.
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Busca productos en la lista en memoria cuyos nombres comiencen con el prefijo especificado.
     * La búsqueda es sensible a mayúsculas y minúsculas.
     *
     * @param nombre El prefijo del nombre del producto a buscar.
     * @return Una {@link List} de objetos {@link Producto} que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().startsWith(nombre)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    /**
     * Actualiza un producto existente en la lista en memoria.
     * Itera sobre la lista y, si encuentra un producto con el mismo código, lo reemplaza por el producto proporcionado.
     *
     * @param producto El objeto {@link Producto} con los datos actualizados y el código de un producto existente.
     */
    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
            }
        }
    }

    /**
     * Elimina un producto de la lista en memoria por su código.
     * Utiliza un {@link Iterator} para recorrer y eliminar el producto de manera segura.
     *
     * @param codigo El código entero del producto a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        Iterator<Producto> iterator = productos.iterator();
        while (iterator.hasNext()) {
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
                iterator.remove();
            }
        }
    }

    /**
     * Lista todos los productos actualmente almacenados en memoria.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Producto} en memoria.
     */
    @Override
    public List<Producto> listarTodos() {
        return productos;
    }
}