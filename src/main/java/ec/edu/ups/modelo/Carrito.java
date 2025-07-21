package ec.edu.ups.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Representa un carrito de compras que contiene una colección de productos
 * (ítems de carrito) que un usuario desea adquirir.
 * Implementa {@link Serializable} para permitir la persistencia del objeto
 * en archivos o a través de la red.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class Carrito implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codigo;
    private GregorianCalendar fechaCreacion;
    private List<ItemCarrito> items;
    private Usuario usuario;

    /**
     * Constructor por defecto de la clase Carrito.
     * Inicializa la lista de ítems como un nuevo {@link ArrayList} y establece
     * la fecha de creación al momento actual.
     */
    public Carrito() {
        this.items = new ArrayList<>();
        this.fechaCreacion = new GregorianCalendar();
    }

    /**
     * Constructor utilizado para reconstruir un objeto Carrito, típicamente desde
     * un medio de persistencia como un archivo de texto, donde los ítems podrían
     * no estar disponibles de inmediato.
     * Llama al constructor por defecto para asegurar la inicialización de la lista de ítems y la fecha.
     *
     * @param codigo El código único del carrito.
     * @param usuario El {@link Usuario} asociado a este carrito.
     */
    public Carrito(int codigo, Usuario usuario) {
        this();
        this.codigo = codigo;
        this.usuario = usuario;
    }

    /**
     * Obtiene el {@link Usuario} al que pertenece este carrito.
     *
     * @return El objeto {@link Usuario} asociado al carrito.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el {@link Usuario} al que pertenece este carrito.
     *
     * @param usuario El objeto {@link Usuario} a asociar con el carrito.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el código único del carrito.
     *
     * @return El código entero del carrito.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único del carrito.
     *
     * @param codigo El código entero a asignar al carrito.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene la fecha y hora de creación del carrito.
     *
     * @return Un objeto {@link GregorianCalendar} representando la fecha de creación.
     */
    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación del carrito.
     *
     * @param fechaCreacion Un objeto {@link GregorianCalendar} con la fecha de creación deseada.
     */
    public void setFechaCreacion(GregorianCalendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Agrega un producto al carrito con una cantidad específica.
     * Si el producto ya existe en el carrito, su cantidad se actualiza.
     * Si no existe, se añade como un nuevo {@link ItemCarrito}.
     *
     * @param producto El {@link Producto} a agregar.
     * @param cantidad La cantidad del producto a agregar.
     */
    public void agregarProducto(Producto producto, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == producto.getCodigo()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(producto, cantidad));
    }

    /**
     * Elimina un producto del carrito utilizando el código del producto.
     * Si se encuentran múltiples ítems con el mismo código de producto (aunque no debería ocurrir si el modelo está bien diseñado),
     * solo la primera ocurrencia será eliminada.
     *
     * @param codigoProducto El código del producto a eliminar del carrito.
     */
    public void eliminarProducto(int codigoProducto) {
        Iterator<ItemCarrito> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().getProducto().getCodigo() == codigoProducto) {
                it.remove();
                break;
            }

        }
    }

    /**
     * Vacía completamente el carrito, eliminando todos sus ítems.
     */
    public void LimpiarCarrito() {
        items.clear();
    }

    /**
     * Calcula el subtotal del carrito (suma de los precios de los productos multiplicados por su cantidad,
     * antes de aplicar impuestos).
     * Incluye verificaciones de nulidad para los ítems y productos para evitar errores.
     *
     * @return El valor total (subtotal) de todos los ítems en el carrito.
     */
    public double calcularTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            if (item != null && item.getProducto() != null) {
                total += item.getProducto().getPrecio() * item.getCantidad();
            }
        }
        return total;
    }

    /**
     * Obtiene el total actual del carrito, que es el resultado de {@link #calcularTotal()}.
     * Este método es provisto para compatibilidad y para obtener el valor del total calculado.
     *
     * @return El total calculado del carrito.
     */
    public double getTotal() {
        return calcularTotal();
    }

    /**
     * Obtiene la lista de todos los {@link ItemCarrito} contenidos en este carrito.
     *
     * @return Una {@link List} de {@link ItemCarrito} que forman parte del carrito.
     */
    public List<ItemCarrito> obtenerItems() {
        return items;
    }

    /**
     * Verifica si el carrito está vacío (no contiene ningún ítem).
     *
     * @return {@code true} si el carrito no tiene ítems, {@code false} en caso contrario.
     */
    public boolean estaVacio() {
        return items.isEmpty();
    }

    /**
     * Calcula el valor del Impuesto al Valor Agregado (IVA) para el total del carrito.
     * Se asume una tasa de IVA del 12% (0.12).
     *
     * @return El monto del IVA calculado.
     */
    public double calcularIVA() {
        double total = calcularTotal();
        return total * 0.12;
    }

    /**
     * Calcula el total final del carrito, incluyendo el subtotal y el IVA.
     *
     * @return El valor total a pagar (subtotal + IVA).
     */
    public double calcularTotalConIVA() {
        return calcularTotal() + calcularIVA();
    }

    /**
     * Crea una copia profunda de este objeto Carrito.
     * La copia incluye la fecha de creación, el código, el usuario y una copia de cada {@link ItemCarrito}
     * y {@link Producto} dentro de la lista de ítems para asegurar que no haya referencias compartidas.
     *
     * @return Un nuevo objeto {@link Carrito} que es una copia independiente de este carrito.
     */
    public Carrito copiar(){
        Carrito copia = new Carrito();
        copia.setFechaCreacion(this.fechaCreacion);
        copia.setCodigo(this.codigo);
        copia.setUsuario(this.usuario);
        for (ItemCarrito item : this.items) {
            if (item != null && item.getProducto() != null) {
                copia.agregarProducto(item.getProducto().copiar(), item.getCantidad());
            }
        }
        return copia;
    }
}