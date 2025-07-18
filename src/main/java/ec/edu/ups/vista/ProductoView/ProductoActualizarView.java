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
 * Vista interna (JInternalFrame) para la actualización de productos.
 * Permite al usuario buscar un producto por su código y modificar su información (nombre y precio).
 * La vista muestra el producto actual en una tabla antes de la actualización.
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoActualizarView extends JInternalFrame {
    private JPanel panelActualizar;
    private JButton buscarButton;
    private JTextField textField1;
    private JTable table1;
    private JButton actualizarButton;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblActualizar;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista ProductoActualizarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa el modelo de la tabla, cambia el idioma de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public ProductoActualizarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelActualizar);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
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
        setTitle(idioma.get("producto.actualizar.titulo"));
        lblActualizar.setText(idioma.get("producto.actualizar.encabezado"));
        lblCodigo.setText(idioma.get("producto.actualizar.etiqueta.codigo"));
        lblNombre.setText(idioma.get("producto.actualizar.etiqueta.nombre"));
        lblPrecio.setText(idioma.get("producto.actualizar.etiqueta.precio"));
        buscarButton.setText(idioma.get("producto.actualizar.boton.buscar"));
        actualizarButton.setText(idioma.get("producto.actualizar.boton.actualizar"));

        String[] columnas = {
                idioma.get("producto.actualizar.columna.codigo"),
                idioma.get("producto.actualizar.columna.nombre"),
                idioma.get("producto.actualizar.columna.precio")
        };
        modelo.setColumnIdentifiers(columnas);
    }

    /**
     * Obtiene el panel principal de la vista de actualización.
     *
     * @return El {@link JPanel} principal.
     */
    public JPanel getPanelActualizar() {
        return panelActualizar;
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
     * Obtiene el primer campo de texto, típicamente usado para el código de búsqueda del producto.
     *
     * @return El {@link JTextField} para el código.
     */
    public JTextField getTextField1() {
        return textField1;
    }

    /**
     * Obtiene la tabla donde se muestran los datos del producto encontrado.
     *
     * @return El {@link JTable} de productos.
     */
    public JTable getTable1() {
        return table1;
    }

    /**
     * Obtiene el botón de "Actualizar".
     *
     * @return El {@link JButton} de actualización.
     */
    public JButton getActualizarButton() {
        return actualizarButton;
    }

    /**
     * Obtiene el segundo campo de texto, típicamente usado para el nuevo nombre del producto.
     *
     * @return El {@link JTextField} para el nombre.
     */
    public JTextField getTextField2() {
        return textField2;
    }

    /**
     * Obtiene el tercer campo de texto, típicamente usado para el nuevo precio del producto.
     *
     * @return El {@link JTextField} para el precio.
     */
    public JTextField getTextField3() {
        return textField3;
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
     * Obtiene la etiqueta del código del producto.
     *
     * @return El {@link JLabel} del código.
     */
    public JLabel getLblCodigo() {
        return lblCodigo;
    }

    /**
     * Obtiene la etiqueta del nombre del producto.
     *
     * @return El {@link JLabel} del nombre.
     */
    public JLabel getLblNombre() {
        return lblNombre;
    }

    /**
     * Obtiene la etiqueta del precio del producto.
     *
     * @return El {@link JLabel} del precio.
     */
    public JLabel getLblPrecio() {
        return lblPrecio;
    }

    /**
     * Obtiene la etiqueta del título principal de la sección de actualización.
     *
     * @return El {@link JLabel} del título de actualización.
     */
    public JLabel getLblActualizar() {
        return lblActualizar;
    }

    /**
     * Carga una lista de objetos {@link Producto} en la tabla de la vista.
     * Los precios de los productos se formatean a moneda según el {@link Locale} actual.
     *
     * @param listaProductos La {@link List} de objetos {@link Producto} a cargar.
     */
    public void cargarDatos(List<Producto> listaProductos) {
        Locale locale = idioma.getLocale();
        modelo.setNumRows(0);
        for (Producto producto : listaProductos) {
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
     * Limpia los campos de entrada y vacía la tabla de productos en la vista.
     */
    public void limpiarCampos() {
        textField1.setText("");
        modelo.setNumRows(0);
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar" y "Actualizar"
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
        URL botonActualizar = LoginView.class.getClassLoader().getResource("imagenes/Actualizar.svg.png");
        if (botonActualizar != null) {
            ImageIcon icono = new ImageIcon(botonActualizar);
            actualizarButton.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}