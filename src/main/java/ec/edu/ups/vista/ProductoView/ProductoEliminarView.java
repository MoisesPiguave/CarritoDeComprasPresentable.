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
 * Vista interna (JInternalFrame) para la eliminación de productos.
 * Permite al usuario buscar un producto por su código y visualizar su información
 * antes de proceder con la eliminación. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoEliminarView extends JInternalFrame {
    private JTable table1;
    private JPanel panelEliminar;
    private JTextField textField1;
    private JButton buscarButton;
    private JButton eliminarButton;
    private JLabel lblCodigo;
    private JLabel lblEliminar;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista ProductoEliminarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa el modelo de la tabla con las columnas apropiadas, cambia el idioma
     * de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public ProductoEliminarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelEliminar);
        setTitle(idioma.get("producto.eliminar.titulo"));
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {
                idioma.get("producto.eliminar.columna.codigo"),
                idioma.get("producto.eliminar.columna.nombre"),
                idioma.get("producto.eliminar.columna.precio")
        };
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);

        cambiarIdioma();
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y los encabezados de la tabla.
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("producto.eliminar.titulo"));

        lblCodigo.setText(idioma.get("producto.eliminar.etiqueta.codigo"));
        lblEliminar.setText(idioma.get("producto.eliminar.etiqueta.eliminar"));

        buscarButton.setText(idioma.get("producto.eliminar.boton.buscar"));
        eliminarButton.setText(idioma.get("producto.eliminar.boton.eliminar"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("producto.eliminar.columna.codigo"),
                idioma.get("producto.eliminar.columna.nombre"),
                idioma.get("producto.eliminar.columna.precio")
        });
    }

    /**
     * Obtiene la tabla donde se muestran los datos del producto a eliminar.
     *
     * @return El {@link JTable} de productos.
     */
    public JTable getTable1() {
        return table1;
    }

    /**
     * Establece la tabla de productos.
     *
     * @param table1 El {@link JTable} a establecer.
     */
    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    /**
     * Obtiene el panel principal de la vista de eliminación.
     *
     * @return El {@link JPanel} principal.
     */
    public JPanel getPanelEliminar() {
        return panelEliminar;
    }

    /**
     * Establece el panel principal de la vista de eliminación.
     *
     * @param panelEliminar El {@link JPanel} a establecer.
     */
    public void setPanelEliminar(JPanel panelEliminar) {
        this.panelEliminar = panelEliminar;
    }

    /**
     * Obtiene el campo de texto para ingresar el código de búsqueda del producto.
     *
     * @return El {@link JTextField} para el código.
     */
    public JTextField getTextField1() {
        return textField1;
    }

    /**
     * Establece el campo de texto para el código de búsqueda.
     *
     * @param textField1 El {@link JTextField} a establecer.
     */
    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
    }

    /**
     * Obtiene el botón de "Buscar".
     *
     * @return El {@link JButton} de búsqueda.
     */
    public JButton getBuscarButton() {
        return buscarButton;
    }

    /**
     * Establece el botón de "Buscar".
     *
     * @param buscarButton El {@link JButton} a establecer.
     */
    public void setBuscarButton(JButton buscarButton) {
        this.buscarButton = buscarButton;
    }

    /**
     * Obtiene el botón de "Eliminar".
     *
     * @return El {@link JButton} de eliminación.
     */
    public JButton getEliminarButton() {
        return eliminarButton;
    }

    /**
     * Establece el botón de "Eliminar".
     *
     * @param eliminarButton El {@link JButton} a establecer.
     */
    public void setEliminarButton(JButton eliminarButton) {
        this.eliminarButton = eliminarButton;
    }

    /**
     * Obtiene el modelo de tabla asociado a {@link #table1}.
     *
     * @return El {@link DefaultTableModel} de la tabla de productos.
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
     * Obtiene la etiqueta del código del producto.
     *
     * @return El {@link JLabel} del código.
     */
    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    /**
     * Establece la etiqueta del código del producto.
     *
     * @param lblCodigo El {@link JLabel} a establecer.
     */
    public void setLblCodigo(JLabel lblCodigo) {
        this.lblCodigo = lblCodigo;
    }

    /**
     * Obtiene la etiqueta del título principal de la sección de eliminación.
     *
     * @return El {@link JLabel} del título de eliminación.
     */
    public JLabel getLblEliminar() {
        return lblEliminar;
    }

    /**
     * Establece la etiqueta del título principal de la sección de eliminación.
     *
     * @param lblEliminar El {@link JLabel} a establecer.
     */
    public void setLblEliminar(JLabel lblEliminar) {
        this.lblEliminar = lblEliminar;
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
     * Muestra un mensaje en un cuadro de diálogo emergente.
     *
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Limpia el campo de texto de búsqueda y vacía la tabla de productos en la vista.
     */
    public void limpiarCampos() {
        textField1.setText("");
        modelo.setNumRows(0);
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar" y "Eliminar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos() {
        URL botonBuscar = LoginView.class.getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonBuscar != null) {
            ImageIcon icono = new ImageIcon(botonBuscar);
            buscarButton.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonEliminar = LoginView.class.getClassLoader().getResource("imagenes/EliminarTodo.svg.png");
        if (botonEliminar != null) {
            ImageIcon icono = new ImageIcon(botonEliminar);
            eliminarButton.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}