package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * Representa un producto disponible en el sistema.
 * Contiene información básica como un código único, nombre y precio.
 * Implementa {@link Serializable} para permitir que los objetos {@code Producto}
 * puedan ser convertidos a un flujo de bytes y reconstruidos, lo cual es esencial
 * para la persistencia en archivos o la transmisión de objetos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codigo;
    private String nombre;
    private double precio;

    /**
     * Constructor por defecto de la clase Producto.
     * Inicializa un objeto Producto con valores predeterminados.
     */
    public Producto() {
    }

    /**
     * Constructor de la clase Producto.
     * Inicializa un nuevo producto con el código, nombre y precio especificados.
     *
     * @param codigo El código único del producto.
     * @param nombre El nombre del producto.
     * @param precio El precio del producto.
     */
    public Producto(int codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    /**
     * Establece el código único del producto.
     *
     * @param codigo El código entero a asignar al producto.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nombre del producto a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio El precio del producto a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el código único del producto.
     *
     * @return El código entero del producto.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Retorna una representación en cadena de texto del objeto Producto.
     * El formato es "nombre - $precio".
     *
     * @return Una cadena formateada que describe el producto.
     */
    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }

    /**
     * Crea y retorna una copia profunda de este objeto Producto.
     * Esto significa que se crea una nueva instancia de {@code Producto} con los mismos
     * valores de código, nombre y precio, asegurando que la copia sea independiente del original.
     *
     * @return Un nuevo objeto {@link Producto} que es una copia independiente de este producto.
     */
    public Producto copiar() {
        return new Producto(this.codigo, this.nombre, this.precio);
    }
}