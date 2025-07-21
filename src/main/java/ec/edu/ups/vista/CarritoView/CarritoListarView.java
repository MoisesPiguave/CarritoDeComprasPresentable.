package ec.edu.ups.vista.CarritoView;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Vista interna (JInternalFrame) para listar y buscar carritos de compras.
 * Permite mostrar todos los carritos, buscar uno por código y ver el detalle
 * de un carrito seleccionado en la tabla. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CarritoListarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable tblProductos;
    private JTextField txtCarrito;
    private JButton btnMostrarDetalle;
    private JButton btnMostrar;
    private JButton btnListar;
    private JLabel lblCodigo;
    private JLabel lblListar;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista CarritoListarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre y el tamaño. Inicializa el modelo de la tabla
     * con las columnas apropiadas y las configura como no editables.
     * También cambia el idioma de los textos y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public CarritoListarView(MensajeInternacionalizacionHandler idioma) {
        super(idioma.get("carrito.listar.titulo"), true, true, false, true);
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);

        modelo = new DefaultTableModel(new Object[]{
                idioma.get("carrito.listar.columna.codigo"),
                idioma.get("carrito.listar.columna.fecha"),
                idioma.get("carrito.listar.columna.subtotal"),
                idioma.get("carrito.listar.columna.iva"),
                idioma.get("carrito.listar.columna.total")
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        setTitle(idioma.get("carrito.listar.titulo"));
        lblListar.setText(idioma.get("carrito.listar.etiqueta"));
        lblCodigo.setText(idioma.get("carrito.listar.codigo"));
        btnMostrar.setText(idioma.get("carrito.listar.boton.mostrar"));
        btnMostrarDetalle.setText(idioma.get("carrito.listar.boton.detalle"));
        btnListar.setText(idioma.get("carrito.listar.boton.listar"));

        modelo.setColumnIdentifiers(new Object[]{
                idioma.get("carrito.listar.columna.codigo"),
                idioma.get("carrito.listar.columna.fecha"),
                idioma.get("carrito.listar.columna.subtotal"),
                idioma.get("carrito.listar.columna.iva"),
                idioma.get("carrito.listar.columna.total")
        });
    }

    /**
     * Carga una lista de objetos {@link Carrito} en la tabla de la vista.
     * Cada carrito se muestra con su código, fecha de creación, subtotal, IVA y total con IVA.
     * Los valores numéricos y fechas se formatean según el {@link Locale} actual.
     *
     * @param carritos La {@link List} de objetos {@link Carrito} a cargar en la tabla.
     */
    public void cargarDatos(List<Carrito> carritos) {
        modelo.setRowCount(0);
        Locale locale = idioma.getLocale();
        for (Carrito carrito : carritos) {
            String fecha = FormateadorUtils.formatearFecha(carrito.getFechaCreacion().getTime(), locale);
            modelo.addRow(new Object[]{
                    carrito.getCodigo(),
                    fecha,
                    FormateadorUtils.formatearMoneda(carrito.calcularTotal(), locale),
                    FormateadorUtils.formatearMoneda(carrito.calcularIVA(), locale),
                    FormateadorUtils.formatearMoneda(carrito.calcularTotalConIVA(), locale)
            });
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
     * Limpia el campo de texto de búsqueda de carrito y vacía la tabla de productos.
     */
    public void limpiarCampos() {
        txtCarrito.setText("");
        modelo.setNumRows(0);
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
     * Obtiene la tabla donde se muestran los carritos.
     *
     * @return El {@link JTable} de carritos.
     */
    public JTable getTblProductos() {
        return tblProductos;
    }

    /**
     * Establece la tabla de productos (carritos).
     *
     * @param tblProductos El {@link JTable} a establecer.
     */
    public void setTblProductos(JTable tblProductos) {
        this.tblProductos = tblProductos;
    }

    /**
     * Obtiene el campo de texto para ingresar el código de carrito a buscar.
     *
     * @return El {@link JTextField} del código del carrito.
     */
    public JTextField getTxtCarrito() {
        return txtCarrito;
    }

    /**
     * Establece el campo de texto para el código del carrito.
     *
     * @param txtCarrito El {@link JTextField} a establecer.
     */
    public void setTxtCarrito(JTextField txtCarrito) {
        this.txtCarrito = txtCarrito;
    }

    /**
     * Obtiene el botón para mostrar el detalle de un carrito seleccionado.
     *
     * @return El {@link JButton} de mostrar detalle.
     */
    public JButton getBtnMostrarDetalle() {
        return btnMostrarDetalle;
    }

    /**
     * Establece el botón de mostrar detalle.
     *
     * @param btnMostrarDetalle El {@link JButton} a establecer.
     */
    public void setBtnMostrarDetalle(JButton btnMostrarDetalle) {
        this.btnMostrarDetalle = btnMostrarDetalle;
    }

    /**
     * Obtiene el botón para mostrar un carrito específico (generalmente por código).
     *
     * @return El {@link JButton} de mostrar.
     */
    public JButton getBtnMostrar() {
        return btnMostrar;
    }

    /**
     * Establece el botón de mostrar.
     *
     * @param btnMostrar El {@link JButton} a establecer.
     */
    public void setBtnMostrar(JButton btnMostrar) {
        this.btnMostrar = btnMostrar;
    }

    /**
     * Obtiene el botón para listar todos los carritos.
     *
     * @return El {@link JButton} de listar.
     */
    public JButton getBtnListar() {
        return btnListar;
    }

    /**
     * Establece el botón de listar.
     *
     * @param btnListar El {@link JButton} a establecer.
     */
    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
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
     * Obtiene la etiqueta del título principal de la vista de listado de carritos.
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblListar() {
        return lblListar;
    }

    /**
     * Establece la etiqueta del título principal de la vista de listado de carritos.
     *
     * @param lblListar El {@link JLabel} a establecer como título.
     */
    public void setLblListar(JLabel lblListar) {
        this.lblListar = lblListar;
    }

    /**
     * Obtiene el modelo de tabla asociado a {@link #tblProductos}.
     *
     * @return El {@link DefaultTableModel} de la tabla de carritos.
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
     * Obtiene el manejador de internacionalización asociado a esta vista.
     *
     * @return El {@link MensajeInternacionalizacionHandler} de la vista.
     */
    public MensajeInternacionalizacionHandler getMi() {
        return idioma;
    }

    /**
     * Establece el manejador de internacionalización para esta vista.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} a establecer.
     */
    public void setMi(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
    }

    /**
     * Carga y establece los iconos para los botones de "Listar", "Mostrar" y "Mostrar Detalle"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos() {
        URL botonListar = LoginView.class.getClassLoader().getResource("imagenes/ListarTodo.svg.png");
        if (botonListar != null) {
            ImageIcon icono = new ImageIcon(botonListar);
            btnListar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonMostrar = LoginView.class.getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonMostrar != null) {
            ImageIcon icono = new ImageIcon(botonMostrar);
            btnMostrar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonMostrarDetalle = LoginView.class.getClassLoader().getResource("imagenes/Terminar.svg.png");
        if (botonMostrarDetalle != null) {
            ImageIcon icono = new ImageIcon(botonMostrarDetalle);
            btnMostrarDetalle.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}