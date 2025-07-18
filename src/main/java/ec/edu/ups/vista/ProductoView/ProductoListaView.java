package ec.edu.ups.vista.ProductoView;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Vista interna (JInternalFrame) para listar y buscar productos.
 * Permite al usuario ver todos los productos disponibles en una tabla o buscar
 * productos por nombre. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoListaView extends JInternalFrame {

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JPanel panelPrincipal;
    private JButton btnListar;
    private JPanel JPanel; // Este campo parece ser un JPanel con nombre de clase
    private JLabel lblLista;
    private JLabel lblNombre;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista ProductoListaView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa el modelo de la tabla con las columnas apropiadas, cambia el idioma
     * de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public ProductoListaView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle(idioma.get("producto.lista.titulo"));
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {
                idioma.get("producto.lista.columna.codigo"),
                idioma.get("producto.lista.columna.nombre"),
                idioma.get("producto.lista.columna.precio")
        };
        modelo.setColumnIdentifiers(columnas);
        tblProductos.setModel(modelo);

        cambiarIdioma();
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y los encabezados de la tabla.
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("producto.lista.titulo"));

        lblLista.setText(idioma.get("producto.lista.etiqueta.lista"));
        lblNombre.setText(idioma.get("producto.lista.etiqueta.nombre"));

        btnBuscar.setText(idioma.get("producto.lista.boton.buscar"));
        btnListar.setText(idioma.get("producto.lista.boton.listar"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("producto.lista.columna.codigo"),
                idioma.get("producto.lista.columna.nombre"),
                idioma.get("producto.lista.columna.precio")
        });
    }

    /**
     * Obtiene el campo de texto para la búsqueda de productos por nombre.
     *
     * @return El {@link JTextField} de búsqueda.
     */
    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    /**
     * Establece el campo de texto para la búsqueda.
     *
     * @param txtBuscar El {@link JTextField} a establecer.
     */
    public void setTxtBuscar(JTextField txtBuscar) {
        this.txtBuscar = txtBuscar;
    }

    /**
     * Obtiene el botón de "Buscar".
     *
     * @return El {@link JButton} de búsqueda.
     */
    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    /**
     * Establece el botón de "Buscar".
     *
     * @param btnBuscar El {@link JButton} a establecer.
     */
    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    /**
     * Obtiene la tabla donde se muestran los productos.
     *
     * @return El {@link JTable} de productos.
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
     * Obtiene el panel principal de la vista.
     *
     * @return El {@link JPanel} principal.
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
     * Obtiene el botón de "Listar" todos los productos.
     *
     * @return El {@link JButton} de listar.
     */
    public JButton getBtnListar() {
        return btnListar;
    }

    /**
     * Establece el botón de "Listar".
     *
     * @param btnListar El {@link JButton} a establecer.
     */
    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
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
     * Obtiene la etiqueta del título principal de la lista de productos.
     *
     * @return El {@link JLabel} del título de la lista.
     */
    public JLabel getLblLista() {
        return lblLista;
    }

    /**
     * Establece la etiqueta del título principal de la lista de productos.
     *
     * @param lblLista El {@link JLabel} a establecer.
     */
    public void setLblLista(JLabel lblLista) {
        this.lblLista = lblLista;
    }

    /**
     * Obtiene la etiqueta del campo de nombre para la búsqueda.
     *
     * @return El {@link JLabel} del nombre.
     */
    public JLabel getLblNombre() {
        return lblNombre;
    }

    /**
     * Establece la etiqueta del campo de nombre para la búsqueda.
     *
     * @param lblNombre El {@link JLabel} a establecer.
     */
    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }

    /**
     * Carga una lista de objetos {@link Producto} en la tabla de la vista.
     * Los precios de los productos se formatean a moneda según el {@link Locale} actual.
     *
     * @param listaProductos La {@link List} de objetos {@link Producto} a cargar.
     */
    public void cargarDatos(List<Producto> listaProductos) {
        modelo.setNumRows(0);

        for (Producto producto : listaProductos) {
            Locale locale = idioma.getLocale();
            Object[] fila = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    FormateadorUtils.formatearMoneda(producto.getPrecio(), locale)
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar" y "Listar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos() {
        URL botonBuscar = LoginView.class.getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonBuscar != null) {
            ImageIcon icono = new ImageIcon(botonBuscar);
            btnBuscar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonListar = LoginView.class.getClassLoader().getResource("imagenes/ListarTodo.svg.png");
        if (botonListar != null) {
            ImageIcon icono = new ImageIcon(botonListar);
            btnListar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}