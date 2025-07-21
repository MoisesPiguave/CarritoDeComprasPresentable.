package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.CarritoView.CarritoAnadirView;
import ec.edu.ups.vista.ProductoView.ProductoActualizarView;
import ec.edu.ups.vista.ProductoView.ProductoAnadirView;
import ec.edu.ups.vista.ProductoView.ProductoEliminarView;
import ec.edu.ups.vista.ProductoView.ProductoListaView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

/**
 * Controlador para la gestión de productos.
 * Se encarga de la interacción entre las vistas de producto (añadir, listar, modificar, eliminar)
 * y el modelo de datos (Producto) a través de su DAO.
 * Implementa la lógica de negocio para la manipulación de productos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de 7 de 2025
 */
public class ProductoController {

    private final ProductoAnadirView productoAnadirView;
    private final ProductoListaView productoListaView;
    private final CarritoAnadirView carritoAnadirView;
    private final ProductoEliminarView productoEliminarView;
    private final ProductoActualizarView productoActualizarView;
    private final ProductoDAO productoDAO;
    private final MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor del controlador de productos.
     * Inicializa los DAOs, las vistas y configura los oyentes de eventos.
     *
     * @param productoDAO La interfaz de acceso a datos para productos.
     * @param productoAnadirView La vista para añadir productos.
     * @param productoListaView La vista para listar productos.
     * @param carritoAnadirView La vista para añadir productos al carrito (para buscar productos).
     * @param productoEliminarView La vista para eliminar productos.
     * @param productoActualizarView La vista para actualizar productos.
     * @param idioma El manejador para los mensajes de internacionalización.
     */
    public ProductoController(ProductoDAO productoDAO,
                              ProductoAnadirView productoAnadirView,
                              ProductoListaView productoListaView,
                              CarritoAnadirView carritoAnadirView,
                              ProductoEliminarView productoEliminarView,
                              ProductoActualizarView productoActualizarView,
                              MensajeInternacionalizacionHandler idioma) {

        this.productoDAO = productoDAO;
        this.productoAnadirView = productoAnadirView;
        this.productoListaView = productoListaView;
        this.carritoAnadirView = carritoAnadirView;
        this.productoEliminarView = productoEliminarView;
        this.productoActualizarView = productoActualizarView;
        this.idioma = idioma;
        this.configurarEventosEnVistas();
    }

    /**
     * Configura los ActionListeners para los botones de todas las vistas de producto.
     * Asocia las acciones de los botones con los métodos correspondientes en el controlador.
     */
    private void configurarEventosEnVistas() {
        productoAnadirView.getBtnAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });

        productoListaView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });

        productoListaView.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProductos();
            }
        });

        carritoAnadirView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorCodigo();
            }
        });

        productoEliminarView.getBuscarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorCodigoEliminar();
            }
        });

        productoEliminarView.getEliminarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });

        productoActualizarView.getBuscarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorCodigoActualizar();
            }
        });

        productoActualizarView.getActualizarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarProducto();
            }
        });
    }

    /**
     * Guarda un nuevo producto en la base de datos obteniendo los datos de la vista de añadir producto.
     */
    private void guardarProducto() {
        int codigo = Integer.parseInt(productoAnadirView.getTxtCodigo().getText());
        String nombre = productoAnadirView.getTxtNombre().getText();
        double precio = Double.parseDouble(productoAnadirView.getTxtPrecio().getText());

        productoDAO.crear(new Producto(codigo, nombre, precio));
        productoAnadirView.mostrarMensaje(idioma.get("producto.guardado"));
        productoAnadirView.limpiarCampos();
        productoAnadirView.mostrarProductos(productoDAO.listarTodos());
    }

    /**
     * Busca productos por nombre y carga los resultados en la vista de lista.
     */
    private void buscarProducto() {
        String nombre = productoListaView.getTxtBuscar().getText();

        List<Producto> productosEncontrados = productoDAO.buscarPorNombre(nombre);
        productoListaView.cargarDatos(productosEncontrados);
    }

    /**
     * Lista todos los productos disponibles y los carga en la vista de lista.
     */
    public void listarProductos() {
        List<Producto> productos = productoDAO.listarTodos();
        productoListaView.cargarDatos(productos);
    }

    /**
     * Busca un producto por su código y muestra su información en la vista de añadir al carrito.
     * Formatea el precio a moneda usando la configuración de internacionalización.
     */
    private void buscarProductoPorCodigo() {
        Locale locale = idioma.getLocale();
        int codigo = Integer.parseInt(carritoAnadirView.getTxtCodigo().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto == null) {
            carritoAnadirView.mostrarMensaje(idioma.get("producto.no_encontrado"));
            carritoAnadirView.getTxtNombre().setText("");
            carritoAnadirView.getTxtPrecio().setText("");
        } else {
            carritoAnadirView.getTxtNombre().setText(producto.getNombre());
            carritoAnadirView.getTxtPrecio().setText(FormateadorUtils.formatearMoneda(producto.getPrecio(), locale));
        }
    }

    /**
     * Elimina un producto de la base de datos basado en el código ingresado en la vista de eliminación.
     * Valida la entrada del código antes de proceder.
     */
    private void eliminarProducto() {
        String cod = productoEliminarView.getTextField1().getText().trim();

        if (cod.isEmpty()) {
            productoEliminarView.mostrarMensaje(idioma.get("producto.error.codigo_vacio"));
            return;
        }

        int codigo;
        try {
            codigo = Integer.parseInt(cod);
        } catch (NumberFormatException e) {
            productoEliminarView.mostrarMensaje(idioma.get("producto.error.codigo_invalido"));
            return;
        }

        productoDAO.eliminar(codigo);
        productoEliminarView.mostrarMensaje(idioma.get("producto.eliminado"));
    }

    /**
     * Busca un producto por su código para mostrarlo en la vista de eliminación.
     * Si el producto es encontrado, lo carga en la tabla de la vista.
     */
    private void buscarProductoPorCodigoEliminar() {
        String code = productoEliminarView.getTextField1().getText();
        if (!code.isEmpty()) {
            int codigo = Integer.parseInt(code);
            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto != null) {
                productoEliminarView.cargarDatos(List.of(producto));
            } else {
                productoEliminarView.mostrarMensaje(idioma.get("producto.no_encontrado"));
                productoEliminarView.cargarDatos(List.of());
                productoEliminarView.limpiarCampos();
            }
        }
    }

    /**
     * Busca un producto por su código para mostrarlo en la vista de actualización.
     * Si el producto es encontrado, lo carga en la tabla y campos de la vista.
     */
    private void buscarProductoPorCodigoActualizar() {
        String code = productoActualizarView.getTextField1().getText();
        if (!code.isEmpty()) {
            int codigo = Integer.parseInt(code);
            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto != null) {
                productoActualizarView.cargarDatos(List.of(producto));
            } else {
                productoActualizarView.mostrarMensaje(idioma.get("producto.no_encontrado"));
                productoActualizarView.cargarDatos(List.of());
                productoActualizarView.limpiarCampos();
            }
        }
    }

    /**
     * Actualiza un producto existente en la base de datos con los nuevos datos de la vista de actualización.
     */
    private void actualizarProducto() {
        String cod = productoActualizarView.getTextField1().getText();
        int codigo = Integer.parseInt(cod);
        String nombre = productoActualizarView.getTextField2().getText();
        double precio = Double.parseDouble(productoActualizarView.getTextField3().getText());

        Producto producto = new Producto(codigo, nombre, precio);
        productoDAO.actualizar(producto);
        productoActualizarView.mostrarMensaje(idioma.get("producto.modificado"));
    }
}