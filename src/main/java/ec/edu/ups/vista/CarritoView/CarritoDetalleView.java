package ec.edu.ups.vista.CarritoView;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Locale;

/**
 * Vista interna (JInternalFrame) para mostrar los detalles de un carrito de compras específico.
 * Presenta los productos incluidos en el carrito en una tabla, junto con el subtotal,
 * el IVA y el total final. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CarritoDetalleView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable tblProductos;
    private JTextField txtSubtotal;
    private JTextField txtIVA;
    private JTextField txtTotal;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;
    private JLabel lblDetalle;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista CarritoDetalleView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa el modelo de la tabla y carga los textos en el idioma actual.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public CarritoDetalleView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle(idioma.get("carrito.detalle.titulo"));
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
        tblProductos.setModel(modelo);

        cambiarIdioma();
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
     * @param panelPrincipal El {@link JPanel} a establecer como panel principal.
     */
    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    /**
     * Obtiene la tabla donde se muestran los productos detallados del carrito.
     *
     * @return El {@link JTable} de productos del carrito.
     */
    public JTable getTblProductos() {
        return tblProductos;
    }

    /**
     * Establece la tabla de productos.
     *
     * @param tblProductos El {@link JTable} a establecer.
     */
    public void setTblProductos(JTable tblProductos) {
        this.tblProductos = tblProductos;
    }

    /**
     * Obtiene el campo de texto que muestra el subtotal del carrito.
     *
     * @return El {@link JTextField} del subtotal.
     */
    public JTextField getTxtSubtotal() {
        return txtSubtotal;
    }

    /**
     * Establece el campo de texto para el subtotal.
     *
     * @param txtSubtotal El {@link JTextField} a establecer.
     */
    public void setTxtSubtotal(JTextField txtSubtotal) {
        this.txtSubtotal = txtSubtotal;
    }

    /**
     * Obtiene el campo de texto que muestra el IVA calculado del carrito.
     *
     * @return El {@link JTextField} del IVA.
     */
    public JTextField getTxtIVA() {
        return txtIVA;
    }

    /**
     * Establece el campo de texto para el IVA.
     *
     * @param txtIVA El {@link JTextField} a establecer.
     */
    public void setTxtIVA(JTextField txtIVA) {
        this.txtIVA = txtIVA;
    }

    /**
     * Obtiene el campo de texto que muestra el total final del carrito (con IVA).
     *
     * @return El {@link JTextField} del total.
     */
    public JTextField getTxtTotal() {
        return txtTotal;
    }

    /**
     * Establece el campo de texto para el total.
     *
     * @param txtTotal El {@link JTextField} a establecer.
     */
    public void setTxtTotal(JTextField txtTotal) {
        this.txtTotal = txtTotal;
    }

    /**
     * Obtiene el modelo de tabla asociado a {@link #tblProductos}.
     *
     * @return El {@link DefaultTableModel} de la tabla de productos.
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo de tabla para {@link #tblProductos}.
     *
     * @param modelo El {@link DefaultTableModel} a establecer.
     */
    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene la etiqueta para el campo de subtotal.
     *
     * @return El {@link JLabel} de subtotal.
     */
    public JLabel getLblSubtotal() {
        return lblSubtotal;
    }

    /**
     * Establece la etiqueta para el campo de subtotal.
     *
     * @param lblSubtotal El {@link JLabel} a establecer.
     */
    public void setLblSubtotal(JLabel lblSubtotal) {
        this.lblSubtotal = lblSubtotal;
    }

    /**
     * Obtiene la etiqueta para el campo de IVA.
     *
     * @return El {@link JLabel} de IVA.
     */
    public JLabel getLblIVA() {
        return lblIVA;
    }

    /**
     * Establece la etiqueta para el campo de IVA.
     *
     * @param lblIVA El {@link JLabel} a establecer.
     */
    public void setLblIVA(JLabel lblIVA) {
        this.lblIVA = lblIVA;
    }

    /**
     * Obtiene la etiqueta para el campo de total.
     *
     * @return El {@link JLabel} de total.
     */
    public JLabel getLblTotal() {
        return lblTotal;
    }

    /**
     * Establece la etiqueta para el campo de total.
     *
     * @param lblTotal El {@link JLabel} a establecer.
     */
    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
    }

    /**
     * Obtiene la etiqueta del título principal de la vista de detalle de carrito.
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblDetalle() {
        return lblDetalle;
    }

    /**
     * Establece la etiqueta del título principal de la vista de detalle de carrito.
     *
     * @param lblDetalle El {@link JLabel} a establecer como título.
     */
    public void setLblDetalle(JLabel lblDetalle) {
        this.lblDetalle = lblDetalle;
    }

    /**
     * Limpia la tabla de productos y los campos de texto de subtotal, IVA y total.
     */
    public void limpiarTabla() {
        modelo.setRowCount(0);
        txtSubtotal.setText("");
        txtIVA.setText("");
        txtTotal.setText("");
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
     * Carga los datos de un {@link Carrito} específico en la tabla de productos y actualiza
     * los campos de totales (subtotal, IVA, total).
     *
     * @param carrito El objeto {@link Carrito} del cual se cargarán los detalles.
     */
    public void cargarDatos(Carrito carrito) {
        modelo.setRowCount(0);

        for (ItemCarrito itemCarrito : carrito.obtenerItems()) {
            Locale locale = idioma.getLocale();
            Producto producto = itemCarrito.getProducto();
            Object[] fila = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    FormateadorUtils.formatearMoneda(producto.getPrecio(),locale),
                    itemCarrito.getCantidad()
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas y encabezados de la tabla.
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("carrito.detalle.titulo"));
        lblDetalle.setText(idioma.get("carrito.detalle.etiqueta"));
        lblSubtotal.setText(idioma.get("carrito.detalle.subtotal"));
        lblIVA.setText(idioma.get("carrito.detalle.iva"));
        lblTotal.setText(idioma.get("carrito.detalle.total"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("carrito.detalle.columna.codigo"),
                idioma.get("carrito.detalle.columna.nombre"),
                idioma.get("carrito.detalle.columna.precio"),
                idioma.get("carrito.detalle.columna.cantidad")
        });
    }
}