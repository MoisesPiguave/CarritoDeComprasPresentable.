package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * Representa un artículo individual dentro de un carrito de compras, encapsulando
 * un {@link Producto} y la cantidad deseada de dicho producto.
 * Implementa {@link Serializable} para permitir que los objetos {@code ItemCarrito}
 * puedan ser convertidos a un flujo de bytes y reconstruidos, lo cual es esencial
 * para la persistencia en archivos o la transmisión de objetos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ItemCarrito implements Serializable {
    private static final long serialVersionUID = 1L;

    private Producto producto;
    private int cantidad;

    /**
     * Constructor de la clase ItemCarrito.
     * Inicializa un nuevo ítem de carrito con un producto y una cantidad específicos.
     *
     * @param producto El {@link Producto} que se va a añadir al carrito. Debe ser Serializable.
     * @param cantidad La cantidad de unidades de dicho producto.
     */
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto asociado a este ítem del carrito.
     *
     * @return El objeto {@link Producto} de este ítem.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el producto asociado a este ítem del carrito.
     *
     * @param producto El objeto {@link Producto} a establecer para este ítem.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad de unidades del producto en este ítem del carrito.
     *
     * @return La cantidad entera del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades del producto en este ítem del carrito.
     *
     * @param cantidad La cantidad entera a establecer.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Calcula y retorna el subtotal de este ítem del carrito.
     * El subtotal se calcula multiplicando el precio del producto por la cantidad.
     * Incluye una verificación de nulidad para el producto para evitar {@link NullPointerException}.
     *
     * @return El valor subtotal (precio * cantidad) de este ítem; 0.0 si el producto es nulo.
     */
    public double getSubtotal() {
        if (this.producto != null) {
            return this.producto.getPrecio() * this.cantidad;
        }
        return 0.0;
    }

    /**
     * Retorna una representación en cadena de texto del objeto ItemCarrito.
     * Incluye el nombre del producto, la cantidad y el subtotal calculado para este ítem.
     *
     * @return Una cadena formateada que describe el ItemCarrito.
     */
    @Override
    public String toString() {
        String productName = (producto != null) ? producto.getNombre() : "Producto Desconocido";
        return "ItemCarrito{" +
                "producto=" + productName +
                ", cantidad=" + cantidad +
                ", subtotal=" + getSubtotal() +
                '}';
    }

    /**
     * Crea y retorna una copia profunda de este objeto ItemCarrito.
     * Esto significa que no solo se copian los valores primitivos, sino que también
     * se crea una nueva instancia del {@link Producto} asociado, si existe,
     * asegurando que la copia sea independiente del original.
     *
     * @return Un nuevo objeto {@link ItemCarrito} que es una copia independiente de este ítem.
     */
    public ItemCarrito copiar() {
        return new ItemCarrito(this.producto != null ? this.producto.copiar() : null, this.cantidad);
    }
}