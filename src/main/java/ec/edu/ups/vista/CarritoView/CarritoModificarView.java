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
 * Vista interna (JInternalFrame) para modificar carritos de compras existentes.
 * Permite buscar un carrito por su código, visualizar los productos que contiene,
 * ajustar las cantidades de los ítems y actualizar el carrito.
 * Soporta internacionalización de textos y muestra los totales (subtotal, IVA, total).
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CarritoModificarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtCarrito;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JButton btnActualizar;
    private JComboBox cbxCantidad;
    private JTextField txtSubtotal;
    private JTextField txtIVA;
    private JTextField txtTotal;
    private JButton btnLimpiar;
    private JLabel lblCodigo;
    private JLabel lblCantidad;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;
    private JPanel lblModificar; // Este campo parece ser un JPanel con nombre de JLabel
    private JLabel lbl; // Etiqueta principal de la vista
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista CarritoModificarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre, el tamaño y las propiedades de ventana interna.
     * Carga las opciones del JComboBox de cantidad, inicializa el modelo de la tabla,
     * cambia el idioma de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public CarritoModificarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle(idioma.get("carrito.modificar.titulo"));
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        cargarDatosCantidad();

        modelo = new DefaultTableModel();
        tblProductos.setModel(modelo);

        cambiarIdioma();
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y encabezados de la tabla.
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("carrito.modificar.titulo"));
        lbl.setText(idioma.get("carrito.modificar.etiqueta"));
        lblCodigo.setText(idioma.get("carrito.modificar.codigo"));
        lblCantidad.setText(idioma.get("carrito.modificar.cantidad"));
        lblSubtotal.setText(idioma.get("carrito.modificar.subtotal"));
        lblIVA.setText(idioma.get("carrito.modificar.iva"));
        lblTotal.setText(idioma.get("carrito.modificar.total"));
        btnBuscar.setText(idioma.get("boton.buscar"));
        btnActualizar.setText(idioma.get("boton.actualizar"));
        btnLimpiar.setText(idioma.get("boton.limpiar"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("carrito.columna.codigo"),
                idioma.get("carrito.columna.nombre"),
                idioma.get("carrito.columna.precio"),
                idioma.get("carrito.columna.cantidad")
        });
    }

    /**
     * Obtiene el panel principal de la vista.
     * @return El {@link JPanel} principal.
     */
    public JPanel getPanelPrincipal() { return panelPrincipal; }

    /**
     * Establece el panel principal de la vista.
     * @param panelPrincipal El {@link JPanel} a establecer.
     */
    public void setPanelPrincipal(JPanel panelPrincipal) { this.panelPrincipal = panelPrincipal; }

    /**
     * Obtiene el campo de texto para el código del carrito.
     * @return El {@link JTextField} del código del carrito.
     */
    public JTextField getTxtCarrito() { return txtCarrito; }

    /**
     * Establece el campo de texto para el código del carrito.
     * @param txtCarrito El {@link JTextField} a establecer.
     */
    public void setTxtCarrito(JTextField txtCarrito) { this.txtCarrito = txtCarrito; }

    /**
     * Obtiene el botón de "Buscar".
     * @return El {@link JButton} de buscar.
     */
    public JButton getBtnBuscar() { return btnBuscar; }

    /**
     * Establece el botón de "Buscar".
     * @param btnBuscar El {@link JButton} a establecer.
     */
    public void setBtnBuscar(JButton btnBuscar) { this.btnBuscar = btnBuscar; }

    /**
     * Obtiene la tabla de productos del carrito.
     * @return El {@link JTable} de productos.
     */
    public JTable getTblProductos() { return tblProductos; }

    /**
     * Establece la tabla de productos del carrito.
     * @param tblProductos El {@link JTable} a establecer.
     */
    public void setTblProductos(JTable tblProductos) { this.tblProductos = tblProductos; }

    /**
     * Obtiene el botón de "Actualizar".
     * @return El {@link JButton} de actualizar.
     */
    public JButton getBtnActualizar() { return btnActualizar; }

    /**
     * Establece el botón de "Actualizar".
     * @param btnActualizar El {@link JButton} a establecer.
     */
    public void setBtnActualizar(JButton btnActualizar) { this.btnActualizar = btnActualizar; }

    /**
     * Obtiene el JComboBox para seleccionar la cantidad de un producto.
     * @return El {@link JComboBox} de cantidad.
     */
    public JComboBox getCbxCantidad() { return cbxCantidad; }

    /**
     * Establece el JComboBox para la cantidad.
     * @param cbxCantidad El {@link JComboBox} a establecer.
     */
    public void setCbxCantidad(JComboBox cbxCantidad) { this.cbxCantidad = cbxCantidad; }

    /**
     * Obtiene el modelo de la tabla de productos.
     * @return El {@link DefaultTableModel} de la tabla.
     */
    public DefaultTableModel getModelo() { return modelo; }

    /**
     * Establece el modelo de la tabla de productos.
     * @param modelo El {@link DefaultTableModel} a establecer.
     */
    public void setModelo(DefaultTableModel modelo) { this.modelo = modelo; }

    /**
     * Obtiene el campo de texto para el subtotal del carrito.
     * @return El {@link JTextField} del subtotal.
     */
    public JTextField getTxtSubtotal() { return txtSubtotal; }

    /**
     * Establece el campo de texto para el subtotal.
     * @param txtSubtotal El {@link JTextField} a establecer.
     */
    public void setTxtSubtotal(JTextField txtSubtotal) { this.txtSubtotal = txtSubtotal; }

    /**
     * Obtiene el campo de texto para el IVA del carrito.
     * @return El {@link JTextField} del IVA.
     */
    public JTextField getTxtIVA() { return txtIVA; }

    /**
     * Establece el campo de texto para el IVA.
     * @param txtIVA El {@link JTextField} a establecer.
     */
    public void setTxtIVA(JTextField txtIVA) { this.txtIVA = txtIVA; }

    /**
     * Obtiene el campo de texto para el total del carrito.
     * @return El {@link JTextField} del total.
     */
    public JTextField getTxtTotal() { return txtTotal; }

    /**
     * Establece el campo de texto para el total.
     * @param txtTotal El {@link JTextField} a establecer.
     */
    public void setTxtTotal(JTextField txtTotal) { this.txtTotal = txtTotal; }

    /**
     * Obtiene el botón de "Limpiar".
     * @return El {@link JButton} de limpiar.
     */
    public JButton getBtnLimpiar() { return btnLimpiar; }

    /**
     * Establece el botón de "Limpiar".
     * @param btnLimpiar El {@link JButton} a establecer.
     */
    public void setBtnLimpiar(JButton btnLimpiar) { this.btnLimpiar = btnLimpiar; }

    /**
     * Obtiene la etiqueta del código del carrito.
     * @return El {@link JLabel} del código.
     */
    public JLabel getLblCodigo() { return lblCodigo; }

    /**
     * Establece la etiqueta del código del carrito.
     * @param lblCodigo El {@link JLabel} a establecer.
     */
    public void setLblCodigo(JLabel lblCodigo) { this.lblCodigo = lblCodigo; }

    /**
     * Obtiene la etiqueta de la cantidad.
     * @return El {@link JLabel} de cantidad.
     */
    public JLabel getLblCantidad() { return lblCantidad; }

    /**
     * Establece la etiqueta de la cantidad.
     * @param lblCantidad El {@link JLabel} a establecer.
     */
    public void setLblCantidad(JLabel lblCantidad) { this.lblCantidad = lblCantidad; }

    /**
     * Obtiene la etiqueta del subtotal.
     * @return El {@link JLabel} del subtotal.
     */
    public JLabel getLblSubtotal() { return lblSubtotal; }

    /**
     * Establece la etiqueta del subtotal.
     * @param lblSubtotal El {@link JLabel} a establecer.
     */
    public void setLblSubtotal(JLabel lblSubtotal) { this.lblSubtotal = lblSubtotal; }

    /**
     * Obtiene la etiqueta del IVA.
     * @return El {@link JLabel} del IVA.
     */
    public JLabel getLblIVA() { return lblIVA; }

    /**
     * Establece la etiqueta del IVA.
     * @param lblIVA El {@link JLabel} a establecer.
     */
    public void setLblIVA(JLabel lblIVA) { this.lblIVA = lblIVA; }

    /**
     * Obtiene la etiqueta del total.
     * @return El {@link JLabel} del total.
     */
    public JLabel getLblTotal() { return lblTotal; }

    /**
     * Establece la etiqueta del total.
     * @param lblTotal El {@link JLabel} a establecer.
     */
    public void setLblTotal(JLabel lblTotal) { this.lblTotal = lblTotal; }

    /**
     * Obtiene el panel que actúa como etiqueta para la sección de modificación.
     * @return El {@link JPanel} para la sección de modificación.
     */
    public JPanel getLblModificar() { return lblModificar; }

    /**
     * Establece el panel que actúa como etiqueta para la sección de modificación.
     * @param lblModificar El {@link JPanel} a establecer.
     */
    public void setLblModificar(JPanel lblModificar) { this.lblModificar = lblModificar; }

    /**
     * Obtiene la etiqueta principal de la vista.
     * @return El {@link JLabel} principal.
     */
    public JLabel getLbl() { return lbl; }

    /**
     * Establece la etiqueta principal de la vista.
     * @param lbl El {@link JLabel} a establecer.
     */
    public void setLbl(JLabel lbl) { this.lbl = lbl; }

    /**
     * Muestra un mensaje en un cuadro de diálogo emergente.
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Limpia los campos de entrada de la vista y restablece la tabla de productos
     * a un estado inicial vacío.
     */
    public void limpiarCampos() {
        modelo.setRowCount(0);
        txtCarrito.setText("");
        txtSubtotal.setText("");
        txtIVA.setText("");
        txtTotal.setText("");
    }

    /**
     * Carga las opciones del JComboBox de cantidad, del 1 al 20.
     */
    private void cargarDatosCantidad() {
        cbxCantidad.removeAllItems();
        for (int i = 0; i < 20; i++) {
            cbxCantidad.addItem(String.valueOf(i + 1));
        }
    }

    /**
     * Carga los datos de un {@link Carrito} específico en la tabla de productos de la vista.
     * Muestra el código, nombre, precio y cantidad de cada ítem.
     * Los precios se formatean según el {@link Locale} actual.
     *
     * @param carrito El objeto {@link Carrito} del cual se cargarán los detalles en la tabla.
     */
    public void cargarDatos(Carrito carrito) {
        Locale locale = idioma.getLocale();
        modelo.setRowCount(0);
        for (ItemCarrito itemCarrito : carrito.obtenerItems()) {
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
     * Carga y establece los iconos para los botones de "Buscar", "Actualizar" y "Limpiar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos(){
        URL botonBuscar = LoginView.class.getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonBuscar != null) {
            ImageIcon icono = new ImageIcon(botonBuscar);
            btnBuscar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonActualizar = LoginView.class.getClassLoader().getResource("imagenes/Actualizar.svg.png");
        if (botonActualizar != null) {
            ImageIcon icono = new ImageIcon(botonActualizar);
            btnActualizar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonLimpiar = LoginView.class.getClassLoader().getResource("imagenes/LimpiarTodo.svg.png");
        if (botonLimpiar != null) {
            ImageIcon icono = new ImageIcon(botonLimpiar);
            btnLimpiar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}