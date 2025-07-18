package ec.edu.ups.vista.CarritoView;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView; // Necesario para cargar los iconos

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;

/**
 * Vista interna (JInternalFrame) para añadir productos a un carrito de compras.
 * Permite buscar productos, añadirlos al carrito, ver el resumen de totales (subtotal, IVA, total)
 * y guardar o limpiar el carrito. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CarritoAnadirView extends JInternalFrame {
    private JButton btnBuscar;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAnadir;
    private JTable tblProductos;
    private JTextField txtSubtotal;
    private JTextField txtIva;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JComboBox cbxCantidad;
    private JPanel panelPrincipal;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblCantidad;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;
    private JLabel lblCarritoAñadir;
    private DefaultTableModel modelo;
    private Carrito carrito; // Puede no ser estrictamente necesario aquí si el controlador maneja el carrito actual
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista CarritoAnadirView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre y el tamaño. Carga las opciones del JComboBox de cantidad,
     * inicializa el modelo de la tabla, cambia el idioma de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public CarritoAnadirView(MensajeInternacionalizacionHandler idioma) {
        super("Carrito de Compras", true, true, false, true);
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        cargarDatos();

        modelo = new DefaultTableModel();
        tblProductos.setModel(modelo);

        cambiarIdioma();
        iconos();
    }

    /**
     * Carga los datos iniciales para los componentes de la vista, como las opciones
     * del JComboBox de cantidad (del 1 al 20).
     */
    private void cargarDatos() {
        cbxCantidad.removeAllItems();
        for (int i = 0; i < 20; i++) {
            cbxCantidad.addItem(String.valueOf(i + 1));
        }
    }

    /**
     * Obtiene el botón de "Buscar" producto.
     *
     * @return El {@link JButton} de búsqueda.
     */
    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    /**
     * Obtiene el campo de texto para el código del producto.
     *
     * @return El {@link JTextField} del código.
     */
    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    /**
     * Obtiene el campo de texto para el nombre del producto.
     *
     * @return El {@link JTextField} del nombre.
     */
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    /**
     * Obtiene el campo de texto para el precio del producto.
     *
     * @return El {@link JTextField} del precio.
     */
    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    /**
     * Obtiene el botón de "Añadir" producto al carrito.
     *
     * @return El {@link JButton} de añadir.
     */
    public JButton getBtnAnadir() {
        return btnAnadir;
    }

    /**
     * Obtiene la tabla donde se muestran los productos añadidos al carrito.
     *
     * @return El {@link JTable} de productos del carrito.
     */
    public JTable getTblProductos() {
        return tblProductos;
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
     * Obtiene el campo de texto que muestra el IVA calculado del carrito.
     *
     * @return El {@link JTextField} del IVA.
     */
    public JTextField getTxtIva() {
        return txtIva;
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
     * Obtiene el botón de "Guardar" el carrito actual.
     *
     * @return El {@link JButton} de guardar.
     */
    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    /**
     * Obtiene el botón de "Limpiar" el carrito.
     *
     * @return El {@link JButton} de limpiar.
     */
    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    /**
     * Obtiene el JComboBox para seleccionar la cantidad de un producto.
     *
     * @return El {@link JComboBox} de cantidad.
     */
    public JComboBox getCbxCantidad() {
        return cbxCantidad;
    }

    /**
     * Obtiene el panel principal de la vista.
     *
     * @return El {@link JPanel} principal.
     */
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    /**
     * Obtiene el objeto {@link Carrito} asociado a esta vista.
     *
     * @return El objeto {@link Carrito}.
     */
    public Carrito getCarrito() {
        return carrito;
    }

    /**
     * Establece el objeto {@link Carrito} para esta vista.
     *
     * @param carrito El objeto {@link Carrito} a establecer.
     */
    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    /**
     * Obtiene la etiqueta para el campo de código de producto.
     *
     * @return El {@link JLabel} de código.
     */
    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    /**
     * Establece la etiqueta para el campo de código de producto.
     *
     * @param lblCodigo El {@link JLabel} a establecer.
     */
    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    /**
     * Obtiene la etiqueta para el campo de nombre de producto.
     *
     * @return El {@link JLabel} de nombre.
     */
    public JLabel getLblNombre() {
        return lblNombre;
    }

    /**
     * Establece la etiqueta para el campo de nombre de producto.
     *
     * @param lblNombre El {@link JLabel} a establecer.
     */
    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }

    /**
     * Obtiene la etiqueta para el campo de precio de producto.
     *
     * @return El {@link JLabel} de precio.
     */
    public JLabel getLblPrecio() {
        return lblPrecio;
    }

    /**
     * Establece la etiqueta para el campo de precio de producto.
     *
     * @param lblPrecio El {@link JLabel} a establecer.
     */
    public void setLblPrecio(JLabel lblPrecio) {
        this.lblPrecio = lblPrecio;
    }

    /**
     * Obtiene la etiqueta para el campo de cantidad.
     *
     * @return El {@link JLabel} de cantidad.
     */
    public JLabel getLblCantidad() {
        return lblCantidad;
    }

    /**
     * Establece la etiqueta para el campo de cantidad.
     *
     * @param lblCantidad El {@link JLabel} a establecer.
     */
    public void setLblCantidad(JLabel lblCantidad) {
        this.lblCantidad = lblCantidad;
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
     * Obtiene la etiqueta del título principal de la vista "Añadir Carrito".
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblCarritoAñadir() {
        return lblCarritoAñadir;
    }

    /**
     * Establece la etiqueta del título principal de la vista "Añadir Carrito".
     *
     * @param lblCarritoAñadir El {@link JLabel} a establecer como título.
     */
    public void setLblCarritoAñadir(JLabel lblCarritoAñadir) {
        this.lblCarritoAñadir = lblCarritoAñadir;
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
     * Limpia todos los campos de entrada de la vista y restablece la tabla de productos
     * a un estado inicial vacío.
     */
    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        cbxCantidad.setSelectedIndex(0);
        modelo.setRowCount(0);
        txtSubtotal.setText("");
        txtIva.setText("");
        txtTotal.setText("");
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y encabezados de la tabla.
     */
    public void cambiarIdioma() {
        // Se asegura de que el idioma esté actualizado en el handler (aunque ya viene del constructor)
        idioma.setLenguaje(idioma.getLocale().getLanguage(), idioma.getLocale().getCountry());

        setTitle(idioma.get("carrito.añadir.titulo")); // Actualiza el título de la ventana
        lblCarritoAñadir.setText(idioma.get("carrito.añadir.titulo"));
        lblCodigo.setText(idioma.get("carrito.añadir.codigo"));
        lblNombre.setText(idioma.get("carrito.añadir.nombre"));
        lblPrecio.setText(idioma.get("carrito.añadir.precio"));
        lblCantidad.setText(idioma.get("carrito.añadir.cantidad"));
        lblSubtotal.setText(idioma.get("carrito.añadir.subtotal"));
        lblIVA.setText(idioma.get("carrito.añadir.iva"));
        lblTotal.setText(idioma.get("carrito.añadir.total"));
        btnBuscar.setText(idioma.get("carrito.añadir.boton.buscar"));
        btnAnadir.setText(idioma.get("carrito.añadir.boton.anadir"));
        btnGuardar.setText(idioma.get("carrito.añadir.boton.guardar"));
        btnLimpiar.setText(idioma.get("carrito.añadir.boton.limpiar"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("carrito.añadir.tabla.codigo"),
                idioma.get("carrito.añadir.tabla.nombre"),
                idioma.get("carrito.añadir.tabla.precio"),
                idioma.get("carrito.añadir.tabla.cantidad"),
                idioma.get("carrito.añadir.tabla.subtotal")
        });
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar", "Añadir", "Limpiar" y "Guardar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    private void iconos() {
        URL botonGuardar = LoginView.class.getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonGuardar != null) {
            ImageIcon icono = new ImageIcon(botonGuardar);
            btnBuscar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonAnadir = LoginView.class.getClassLoader().getResource("imagenes/Añadir.svg.png");
        if (botonAnadir != null) {
            ImageIcon icono = new ImageIcon(botonAnadir);
            btnAnadir.setIcon(icono);
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
        URL botonGuardarCarrito = LoginView.class.getClassLoader().getResource("imagenes/Guardar.svg.png");
        if (botonGuardarCarrito != null) {
            ImageIcon icono = new ImageIcon(botonGuardarCarrito);
            btnGuardar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}