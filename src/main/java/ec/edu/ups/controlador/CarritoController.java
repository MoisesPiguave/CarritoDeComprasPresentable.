package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.*;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.CarritoView.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList; // Importación necesaria para ArrayList

/**
 * Controlador para la gestión de carritos de compras.
 * Se encarga de la interacción entre las vistas del carrito (añadir, listar, modificar, eliminar)
 * y los modelos de datos (Carrito, Producto, Usuario) a través de sus respectivos DAOs.
 * Implementa la lógica de negocio para la manipulación de carritos y sus ítems.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de 7 de 2025
 */
public class CarritoController {
    private final CarritoDAO carritoDAO;
    private final CarritoAnadirView carritoAnadirView;
    private final Carrito carritoActual;
    private final Usuario usuario;
    private final ProductoDAO productoDAO;
    private CarritoDetalleView carritoDetalleView;
    private final CarritoListarView carritoListarView;
    private final CarritoModificarView carritoModificarView;
    private final CarritoEliminarView carritoEliminarView;
    private final MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor del controlador de carritos.
     * Inicializa los DAOs, las vistas y el carrito actual, y configura los oyentes de eventos.
     *
     * @param carritoDAO La interfaz de acceso a datos para carritos.
     * @param carritoAnadirView La vista para añadir productos al carrito.
     * @param productoDAO La interfaz de acceso a datos para productos.
     * @param carritoListarView La vista para listar carritos.
     * @param usuario El usuario autenticado actualmente en el sistema.
     * @param carritoModificarView La vista para modificar carritos.
     * @param carritoEliminarView La vista para eliminar carritos.
     * @param idioma El manejador para los mensajes de internacionalización.
     */
    public CarritoController(CarritoDAO carritoDAO, CarritoAnadirView carritoAnadirView,
                             ProductoDAO productoDAO, CarritoListarView carritoListarView,
                             Usuario usuario, CarritoModificarView carritoModificarView,
                             CarritoEliminarView carritoEliminarView,
                             MensajeInternacionalizacionHandler idioma) {
        this.carritoDAO = carritoDAO;
        this.carritoAnadirView = carritoAnadirView;
        this.carritoActual = new Carrito();
        this.productoDAO = productoDAO;
        this.usuario = usuario;
        this.carritoListarView = carritoListarView;
        this.carritoModificarView = carritoModificarView;
        this.carritoEliminarView = carritoEliminarView;
        this.idioma = idioma;
        configurarEventos();
    }

    /**
     * Configura los ActionListeners para los botones de todas las vistas del carrito.
     * Asocia las acciones de los botones con los métodos correspondientes en el controlador.
     */
    public void configurarEventos() {
        carritoAnadirView.getBtnAnadir().addActionListener(e -> agregarProductoAlCarrito());
        carritoAnadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        carritoAnadirView.getBtnLimpiar().addActionListener(e -> vaciarCarrito());

        carritoListarView.getBtnMostrar().addActionListener(e -> buscarCarritos());
        carritoListarView.getBtnListar().addActionListener(e -> mostrarTodosLosCarritos());
        carritoListarView.getBtnMostrarDetalle().addActionListener(e -> mostrarDetalle());

        carritoModificarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaModificar());
        carritoModificarView.getBtnActualizar().addActionListener(e -> modificarCarrito());
        carritoModificarView.getBtnLimpiar().addActionListener(e -> carritoModificarView.limpiarCampos());

        carritoEliminarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaEliminar());
        carritoEliminarView.getBtnEliminar().addActionListener(e -> eliminarCarrito());
    }

    /**
     * Elimina un carrito de la base de datos (DAO) basado en el código ingresado en la vista de eliminación.
     * Valida que el código sea numérico antes de proceder y muestra mensajes de éxito o error.
     */
    private void eliminarCarrito() {
        String textoCodigo = carritoEliminarView.getTxtCodigo().getText().trim();

        if (textoCodigo.isEmpty() || !textoCodigo.chars().allMatch(Character::isDigit)) {
            carritoEliminarView.mostrarMensaje(idioma.get("carrito.eliminar.error.numero"));
            return;
        }

        try {
            int codigo = Integer.parseInt(textoCodigo);
            carritoDAO.eliminar(codigo);
            carritoEliminarView.mostrarMensaje(idioma.get("carrito.eliminar.exito"));
            carritoEliminarView.limpiarCampos();
        } catch (NumberFormatException e) {
            carritoEliminarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
        } catch (Exception e) {
            carritoEliminarView.mostrarMensaje(idioma.get("error.eliminar.carrito") + ": " + e.getMessage());
        }
    }

    /**
     * Busca un carrito por su código para su posible eliminación.
     * Si lo encuentra, carga sus datos en la tabla de la vista de eliminación para confirmación.
     */
    private void buscarCarritoParaEliminar() {
        String codigo = carritoEliminarView.getTxtCodigo().getText().trim();
        if (codigo.isEmpty() || !codigo.chars().allMatch(Character::isDigit)) {
            carritoEliminarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
            return;
        }
        try {
            int codigoCarrito = Integer.parseInt(codigo);
            Carrito carrito = carritoDAO.buscarPorCodigo(codigoCarrito);
            if (carrito != null) {
                carritoEliminarView.cargarDatos(carrito);
                carritoEliminarView.setVisible(true);
                carritoEliminarView.moveToFront();
                carritoEliminarView.requestFocusInWindow();
            } else {
                carritoEliminarView.mostrarMensaje(idioma.get("carrito.no.encontrado") + " " + codigoCarrito);
                carritoEliminarView.limpiarCampos();
            }
        } catch (NumberFormatException e) {
            carritoEliminarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
        } catch (Exception e) {
            carritoEliminarView.mostrarMensaje(idioma.get("error.buscar.carrito") + ": " + e.getMessage());
        }
    }

    /**
     * Modifica la cantidad de un producto específico dentro de un carrito existente.
     * La modificación se basa en la fila seleccionada en la tabla y la cantidad elegida de la vista.
     * Los totales del carrito se actualizan y se persisten en el DAO.
     */
    private void modificarCarrito() {
        int filaSeleccionada = carritoModificarView.getTblProductos().getSelectedRow();
        if (filaSeleccionada == -1) {
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.seleccionar.producto"));
            return;
        }

        String textoCantidad = carritoModificarView.getCbxCantidad().getSelectedItem().toString();
        if (!textoCantidad.matches("\\d+")) {
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.numero.invalido"));
            return;
        }

        int cantidad = Integer.parseInt(textoCantidad);
        if (cantidad < 1 || cantidad > 20) {
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.cantidad.rango"));
            return;
        }

        String textoCodigoCarrito = carritoModificarView.getTxtCarrito().getText().trim();
        if (textoCodigoCarrito.isEmpty() || !textoCodigoCarrito.chars().allMatch(Character::isDigit)) {
            carritoModificarView.mostrarMensaje(idioma.get("carrito.eliminar.error.numero"));
            return;
        }
        int codigoCarrito = Integer.parseInt(textoCodigoCarrito);
        Carrito carrito = carritoDAO.buscarPorCodigo(codigoCarrito);

        if (carrito == null) {
            carritoModificarView.mostrarMensaje(idioma.get("carrito.no.encontrado") + " " + codigoCarrito);
            return;
        }

        int codigoProducto = (int) carritoModificarView.getModelo().getValueAt(filaSeleccionada, 0);

        boolean productoEncontrado = false;
        for (ItemCarrito item : carrito.obtenerItems()) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                item.setCantidad(cantidad);
                productoEncontrado = true;
                break;
            }
        }

        if (!productoEncontrado) {
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.producto.no.encontrado.carrito"));
            return;
        }

        try {
            carritoDAO.actualizar(carrito);

            Locale locale = idioma.getLocale();
            carritoModificarView.cargarDatos(carrito);
            carritoModificarView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(), locale));
            carritoModificarView.getTxtIVA().setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(), locale));
            carritoModificarView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotalConIVA(), locale));
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.cantidad.actualizada"));
        } catch (Exception e) {
            carritoModificarView.mostrarMensaje(idioma.get("error.modificar.carrito") + ": " + e.getMessage());
        }
    }

    /**
     * Busca un carrito por su código para modificarlo.
     * Si lo encuentra, carga sus datos y totales calculados en la vista de modificación.
     */
    private void buscarCarritoParaModificar() {
        String codigo = carritoModificarView.getTxtCarrito().getText().trim();
        if (codigo.isEmpty() || !codigo.chars().allMatch(Character::isDigit)) {
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
            return;
        }
        try {
            int codigoCarrito = Integer.parseInt(codigo);
            Carrito carrito = carritoDAO.buscarPorCodigo(codigoCarrito);
            if (carrito != null) {
                Locale locale = idioma.getLocale();
                carritoModificarView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(), locale));
                carritoModificarView.getTxtIVA().setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(), locale));
                carritoModificarView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotalConIVA(), locale));

                carritoModificarView.cargarDatos(carrito);
                carritoModificarView.setVisible(true);
                carritoModificarView.moveToFront();
                carritoModificarView.requestFocusInWindow();
            } else {
                carritoModificarView.mostrarMensaje(idioma.get("carrito.no.encontrado") + " " + codigoCarrito);
                carritoModificarView.limpiarCampos();
            }
        } catch (NumberFormatException e) {
            carritoModificarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
        } catch (Exception e) {
            carritoModificarView.mostrarMensaje(idioma.get("error.buscar.carrito") + ": " + e.getMessage());
        }
    }

    /**
     * Muestra los detalles de un carrito seleccionado en la vista de listado.
     * Abre una nueva ventana {@link CarritoDetalleView} con la información completa del carrito.
     */
    private void mostrarDetalle() {
        int filaSeleccionada = carritoListarView.getTblProductos().getSelectedRow();

        if (filaSeleccionada != -1) {
            String codigoStr = carritoListarView.getModelo().getValueAt(filaSeleccionada, 0).toString();
            try {
                int codigoCarrito = Integer.parseInt(codigoStr);
                Carrito carrito = carritoDAO.buscarPorCodigo(codigoCarrito);

                if (carrito != null) {
                    if (carritoDetalleView == null || carritoDetalleView.isClosed()) {
                        carritoDetalleView = new CarritoDetalleView(idioma);
                        if (carritoListarView.getDesktopPane() != null) {
                            carritoListarView.getDesktopPane().add(carritoDetalleView);
                        } else {
                            System.err.println("Advertencia: JDesktopPane no disponible en CarritoListarView. No se puede añadir CarritoDetalleView.");
                            return;
                        }
                    }

                    carritoDetalleView.cargarDatos(carrito);
                    Locale locale = idioma.getLocale();
                    carritoDetalleView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(), locale));
                    carritoDetalleView.getTxtIVA().setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(), locale));
                    carritoDetalleView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotalConIVA(), locale));

                    carritoDetalleView.setVisible(true);
                    carritoDetalleView.moveToFront();
                    carritoDetalleView.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(carritoListarView, idioma.get("carrito.no.encontrado") + " " + codigoCarrito);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(carritoListarView, idioma.get("mensaje.codigo.invalido") + ": " + codigoStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(carritoListarView, idioma.get("error.mostrar.detalle.carrito") + ": " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(carritoListarView, idioma.get("mensaje.seleccionar.carrito"));
        }
    }

    /**
     * Guarda el carrito actual en la base de datos (DAO).
     * El carrito se asocia al usuario autenticado y se le asigna la fecha de creación actual.
     * Después de guardar, el carrito se vacía y se actualiza la vista para futuras operaciones.
     */
    private void guardarCarrito() {
        if (carritoActual.estaVacio()) {
            carritoAnadirView.mostrarMensaje(idioma.get("carrito.vacio"));
            return;
        }
        carritoActual.setUsuario(usuario);
        carritoActual.setFechaCreacion(new GregorianCalendar());

        try {
            carritoDAO.crear(carritoActual);
            carritoAnadirView.mostrarMensaje(idioma.get("carrito.guardado") + " " + carritoActual.getCodigo());

            carritoActual.LimpiarCarrito();
            cargarProductos();
            mostrarTotal();
            carritoAnadirView.limpiarCampos();
        } catch (Exception e) {
            carritoAnadirView.mostrarMensaje(idioma.get("error.guardar.carrito") + ": " + e.getMessage());
        }
    }

    /**
     * Agrega un producto al {@link Carrito} actual en memoria.
     * La información del producto y la cantidad se obtienen de la vista de añadir carrito.
     * Valida las entradas y busca el producto en el DAO antes de agregarlo.
     */
    private void agregarProductoAlCarrito() {
        String codigoProductoStr = carritoAnadirView.getTxtCodigo().getText().trim();
        String cantidadStr = carritoAnadirView.getCbxCantidad().getSelectedItem().toString().trim();

        if (codigoProductoStr.isEmpty() || !codigoProductoStr.chars().allMatch(Character::isDigit)) {
            carritoAnadirView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
            return;
        }
        if (cantidadStr.isEmpty() || !cantidadStr.chars().allMatch(Character::isDigit)) {
            carritoAnadirView.mostrarMensaje(idioma.get("mensaje.numero.invalido"));
            return;
        }

        try {
            int codigoProducto = Integer.parseInt(codigoProductoStr);
            Producto producto = productoDAO.buscarPorCodigo(codigoProducto);

            if (producto == null) {
                carritoAnadirView.mostrarMensaje(idioma.get("producto.no.encontrado") + " " + codigoProducto);
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad < 1 || cantidad > 20) {
                carritoAnadirView.mostrarMensaje(idioma.get("mensaje.cantidad.rango"));
                return;
            }

            carritoActual.agregarProducto(producto, cantidad);

            cargarProductos();
            mostrarTotal();
            carritoAnadirView.getTxtCodigo().setText("");
            carritoAnadirView.getCbxCantidad().setSelectedIndex(0);
        } catch (NumberFormatException e) {
            carritoAnadirView.mostrarMensaje(idioma.get("mensaje.numero.invalido"));
        } catch (Exception e) {
            carritoAnadirView.mostrarMensaje(idioma.get("error.agregar.producto.carrito") + ": " + e.getMessage());
        }
    }

    /**
     * Carga los ítems del {@link Carrito} actual en la tabla de la vista de añadir carrito.
     * Formatea el precio y el subtotal de cada ítem a formato de moneda usando el Locale actual.
     */
    private void cargarProductos() {
        List<ItemCarrito> items = carritoActual.obtenerItems();
        DefaultTableModel modelo = (DefaultTableModel) carritoAnadirView.getTblProductos().getModel();
        modelo.setNumRows(0);

        Locale currentLocale = idioma.getLocale();
        for (ItemCarrito item : items) {
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    FormateadorUtils.formatearMoneda(item.getProducto().getPrecio(), currentLocale),
                    item.getCantidad(),
                    FormateadorUtils.formatearMoneda(item.getSubtotal(), currentLocale)
            });
        }
    }

    /**
     * Muestra los totales (subtotal, IVA, total con IVA) del {@link Carrito} actual en la vista de añadir carrito.
     * Los valores se formatean a moneda usando el Locale actual.
     */
    private void mostrarTotal() {
        Locale currentLocale = idioma.getLocale();
        carritoAnadirView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carritoActual.calcularTotal(), currentLocale));
        carritoAnadirView.getTxtIva().setText(FormateadorUtils.formatearMoneda(carritoActual.calcularIVA(), currentLocale));
        carritoAnadirView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carritoActual.calcularTotalConIVA(), currentLocale));
    }

    /**
     * Vacía el carrito actual en memoria y actualiza la vista para reflejar los cambios.
     */
    public void vaciarCarrito() {
        carritoActual.LimpiarCarrito();
        cargarProductos();
        mostrarTotal();
        carritoAnadirView.mostrarMensaje(idioma.get("carrito.vaciado"));
    }

    /**
     * Busca y carga carritos en la vista de listado.
     * Si se proporciona un código en el campo de búsqueda, busca un carrito específico.
     * Si el campo está vacío, lista todos los carritos disponibles.
     * Maneja errores de formato de código y carritos no encontrados.
     */
    public void buscarCarritos() {
        String codigo = carritoListarView.getTxtCarrito().getText().trim();
        List<Carrito> carritosEncontrados;

        if (!codigo.isEmpty()) {
            if (!codigo.chars().allMatch(Character::isDigit)) {
                carritoListarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
                return;
            }
            try {
                int codigoCarrito = Integer.parseInt(codigo);
                Carrito carrito = carritoDAO.buscarPorCodigo(codigoCarrito);
                carritosEncontrados = (carrito != null) ? Collections.singletonList(carrito) : new ArrayList<>();
            } catch (NumberFormatException e) {
                carritoListarView.mostrarMensaje(idioma.get("mensaje.codigo.invalido"));
                return;
            } catch (Exception e) {
                carritoListarView.mostrarMensaje(idioma.get("error.buscar.carrito") + ": " + e.getMessage());
                return;
            }
        } else {
            try {
                carritosEncontrados = carritoDAO.listarTodos();
            } catch (Exception e) {
                carritoListarView.mostrarMensaje(idioma.get("error.listar.carritos") + ": " + e.getMessage());
                return;
            }
        }

        if (carritosEncontrados.isEmpty()) {
            carritoListarView.mostrarMensaje(idioma.get("carrito.no.encontrado"));
            carritoListarView.limpiarCampos();
        } else {
            carritoListarView.cargarDatos(carritosEncontrados);
            carritoListarView.mostrarMensaje(idioma.get("carrito.busqueda.exitosa") + " (" + carritosEncontrados.size() + ")");
        }
    }

    /**
     * Muestra todos los carritos disponibles en la base de datos (DAO) en la vista de listado.
     * Si la lista de carritos está vacía, muestra un mensaje indicándolo y limpia la tabla.
     */
    public void mostrarTodosLosCarritos() {
        try {
            List<Carrito> carritos = carritoDAO.listarTodos();
            if (carritos.isEmpty()) {
                carritoListarView.mostrarMensaje(idioma.get("carrito.lista.vacia"));
                carritoListarView.limpiarCampos();
            } else {
                carritoListarView.cargarDatos(carritos);
                carritoListarView.mostrarMensaje(idioma.get("carrito.listado.exitoso") + " (" + carritos.size() + ")");
            }
        } catch (Exception e) {
            carritoListarView.mostrarMensaje(idioma.get("error.listar.carritos") + ": " + e.getMessage());
        }
    }

    /**
     * Lista todos los carritos disponibles y los muestra en la vista de listado.
     * Este método es público para ser invocado desde la clase principal (Main)
     * cuando se necesite refrescar la lista de carritos en la UI.
     * Si no hay carritos, la tabla se vacía y se muestra un mensaje informativo.
     */
    public void listarCarritos() {
        try {
            List<Carrito> carritos = carritoDAO.listarTodos();
            if (carritos.isEmpty()) {
                carritoListarView.mostrarMensaje(idioma.get("carrito.lista.vacia"));
                carritoListarView.limpiarCampos(); // Limpia la tabla si no hay carritos
            } else {
                carritoListarView.cargarDatos(carritos);
                carritoListarView.mostrarMensaje(idioma.get("carrito.listado.exitoso") + " (" + carritos.size() + ")");
            }
        } catch (Exception e) {
            carritoListarView.mostrarMensaje(idioma.get("error.listar.carritos") + ": " + e.getMessage());
        }
    }
}