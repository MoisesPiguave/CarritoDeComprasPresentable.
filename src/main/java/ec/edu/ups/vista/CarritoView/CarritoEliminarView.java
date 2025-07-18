package ec.edu.ups.vista.CarritoView;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import java.util.Locale;

/**
 * Vista interna (JInternalFrame) para la eliminación de carritos de compras.
 * Permite al usuario buscar un carrito por su código y visualizar sus detalles
 * antes de proceder con la eliminación. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CarritoEliminarView extends JInternalFrame {
    private JTable table1;
    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JLabel lblCodigo;
    private JLabel lblEliminar;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista CarritoEliminarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre y el tamaño. Inicializa el modelo de la tabla
     * con las columnas apropiadas, cambia el idioma de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public CarritoEliminarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle(idioma.get("carrito.eliminar.titulo"));
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {
                idioma.get("carrito.eliminar.columna.codigo"),
                idioma.get("carrito.eliminar.columna.nombre"),
                idioma.get("carrito.eliminar.columna.precio"),
                idioma.get("carrito.eliminar.columna.cantidad"),
                idioma.get("carrito.eliminar.columna.subtotal"),
                idioma.get("carrito.eliminar.columna.total")
        };
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);

        cambiarIdioma();
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y encabezados de la tabla.
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("carrito.eliminar.titulo"));
        lblEliminar.setText(idioma.get("carrito.eliminar.etiqueta"));
        lblCodigo.setText(idioma.get("carrito.eliminar.etiqueta.codigo"));
        btnBuscar.setText(idioma.get("carrito.eliminar.boton.buscar"));
        btnEliminar.setText(idioma.get("carrito.eliminar.boton.eliminar"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("carrito.eliminar.columna.codigo"),
                idioma.get("carrito.eliminar.columna.nombre"),
                idioma.get("carrito.eliminar.columna.precio"),
                idioma.get("carrito.eliminar.columna.cantidad"),
                idioma.get("carrito.eliminar.columna.subtotal"),
                idioma.get("carrito.eliminar.columna.total")
        });
    }

    /**
     * Obtiene la tabla principal de la vista que muestra los ítems del carrito.
     *
     * @return El {@link JTable} para mostrar los productos del carrito.
     */
    public JTable getTable1() {
        return table1;
    }

    /**
     * Establece la tabla principal de la vista.
     *
     * @param table1 El {@link JTable} a establecer.
     */
    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    /**
     * Obtiene el panel principal de la vista.
     *
     * @return El {@link JPanel} principal que contiene todos los componentes.
     */
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    /**
     * Establece el panel principal de la vista.
     *
     * @param panelPrincipal El {@link JPanel} a establecer.
     */
    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    /**
     * Obtiene el campo de texto para ingresar el código del carrito a buscar o eliminar.
     *
     * @return El {@link JTextField} del código del carrito.
     */
    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    /**
     * Establece el campo de texto para el código del carrito.
     *
     * @param txtCodigo El {@link JTextField} a establecer.
     */
    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }

    /**
     * Obtiene el botón para buscar un carrito.
     *
     * @return El {@link JButton} de búsqueda.
     */
    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    /**
     * Establece el botón de búsqueda.
     *
     * @param btnBuscar El {@link JButton} a establecer.
     */
    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    /**
     * Obtiene el botón para eliminar el carrito.
     *
     * @return El {@link JButton} de eliminar.
     */
    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    /**
     * Establece el botón de eliminar.
     *
     * @param btnEliminar El {@link JButton} a establecer.
     */
    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    /**
     * Obtiene el modelo de tabla asociado a {@link #table1}.
     *
     * @return El {@link DefaultTableModel} de la tabla.
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo de tabla para {@link #table1}.
     *
     * @param modelo El {@link DefaultTableModel} a establecer.
     */
    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene la etiqueta para el campo de código de carrito.
     *
     * @return El {@link JLabel} de código.
     */
    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    /**
     * Establece la etiqueta para el campo de código de carrito.
     *
     * @param lblCodigo El {@link JLabel} a establecer.
     */
    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    /**
     * Obtiene la etiqueta del título principal de la vista de eliminación de carrito.
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblEliminar() {
        return lblEliminar;
    }

    /**
     * Establece la etiqueta del título principal de la vista de eliminación de carrito.
     *
     * @param lblEliminar El {@link JLabel} a establecer como título.
     */
    public void setLblEliminar(JLabel lblEliminar) {
        this.lblEliminar = lblEliminar;
    }

    /**
     * Carga los datos de un {@link Carrito} específico en la tabla de la vista.
     * Muestra el código, nombre, precio, cantidad y subtotal de cada ítem,
     * además del total general del carrito en la última columna.
     *
     * @param carrito El objeto {@link Carrito} del cual se cargarán los detalles en la tabla.
     */
    public void cargarDatos(Carrito carrito) {
        modelo.setRowCount(0);

        Locale currentLocale = idioma.getLocale();

        for (ItemCarrito itemCarrito : carrito.obtenerItems()) {
            Producto producto = itemCarrito.getProducto();

            Object[] fila = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    FormateadorUtils.formatearMoneda(producto.getPrecio(), currentLocale),
                    FormateadorUtils.formatearMoneda((double)itemCarrito.getCantidad(), currentLocale),
                    FormateadorUtils.formatearMoneda(itemCarrito.getSubtotal(), currentLocale),
                    FormateadorUtils.formatearMoneda(carrito.calcularTotal(), currentLocale)
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Limpia los campos de entrada de la vista y restablece la tabla de productos
     * a un estado inicial vacío.
     */
    public void limpiarCampos() {
        modelo.setRowCount(0);
        txtCodigo.setText("");
    }

    /**
     * Muestra un mensaje en un cuadro de diálogo emergente.
     *
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar" y "Eliminar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos() {
        URL botonBuscar = getClass().getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonBuscar != null) {
            ImageIcon icono = new ImageIcon(botonBuscar);
            btnBuscar.setIcon(icono);
        } else {
            System.err.println("Icono BuscarTodo.svg.png no encontrado.");
        }

        URL botonEliminarCarrito = getClass().getClassLoader().getResource("imagenes/EliminarTodo.svg.png");
        if (botonEliminarCarrito != null) {
            ImageIcon icono = new ImageIcon(botonEliminarCarrito);
            btnEliminar.setIcon(icono);
        } else {
            System.err.println("Icono EliminarTodo.svg.png no encontrado.");
        }
    }
}