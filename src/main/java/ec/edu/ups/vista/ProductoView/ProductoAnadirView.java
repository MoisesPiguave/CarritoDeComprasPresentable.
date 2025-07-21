package ec.edu.ups.vista.ProductoView;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

/**
 * Vista interna (JInternalFrame) para la adición de nuevos productos al sistema.
 * Permite al usuario ingresar el código, nombre y precio de un producto,
 * y luego guardarlo o limpiar los campos. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoAnadirView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField txtPrecio;
    private JTextField txtNombre;
    private JTextField txtCodigo;
    private JButton btnAceptar;
    private JButton btnLimpiar;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblNuevoP;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista ProductoAnadirView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También carga los iconos y configura el ActionListener para el botón de limpiar.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public ProductoAnadirView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Datos del Producto");
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        cambiarIdioma();
        iconos();

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas y textos de botones.
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("producto.anadir.titulo"));
        lblNuevoP.setText(idioma.get("producto.anadir.encabezado"));
        lblCodigo.setText(idioma.get("producto.anadir.etiqueta.codigo"));
        lblNombre.setText(idioma.get("producto.anadir.etiqueta.nombre"));
        lblPrecio.setText(idioma.get("producto.anadir.etiqueta.precio"));
        btnAceptar.setText(idioma.get("producto.anadir.boton.aceptar"));
        btnLimpiar.setText(idioma.get("producto.anadir.boton.limpiar"));
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
     * Obtiene el campo de texto para el precio del producto.
     *
     * @return El {@link JTextField} del precio.
     */
    public JTextField getTxtPrecio() {
        return txtPrecio;
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
     * Obtiene el campo de texto para el código del producto.
     *
     * @return El {@link JTextField} del código.
     */
    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    /**
     * Obtiene el botón de "Aceptar" (usualmente para guardar el producto).
     *
     * @return El {@link JButton} de aceptar.
     */
    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    /**
     * Obtiene el botón de "Limpiar" los campos de la vista.
     *
     * @return El {@link JButton} de limpiar.
     */
    public JButton getBtnLimpiar() {
        return btnLimpiar;
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
     * Obtiene la etiqueta del título principal de la sección "Nuevo Producto".
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblNuevoP() {
        return lblNuevoP;
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
     * Limpia todos los campos de texto de la vista (código, nombre, precio).
     */
    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }

    /**
     * Imprime una lista de productos en la consola estándar.
     * Este método es para propósitos de depuración y muestra la representación
     * {@code toString()} de cada producto.
     *
     * @param productos La {@link List} de objetos {@link Producto} a mostrar.
     */
    public void mostrarProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    /**
     * Carga y establece los iconos para los botones de "Limpiar" y "Aceptar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos() {
        URL botonLimpiar = LoginView.class.getClassLoader().getResource("imagenes/LimpiarTodo.svg.png");
        if (botonLimpiar != null) {
            ImageIcon icono = new ImageIcon(botonLimpiar);
            btnLimpiar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonAceptar = LoginView.class.getClassLoader().getResource("imagenes/Añadir.svg.png");
        if (botonAceptar != null) {
            ImageIcon icono = new ImageIcon(botonAceptar);
            btnAceptar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}