package ec.edu.ups.vista.UsuarioView;

import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import java.util.List;

/**
 * Vista interna (JInternalFrame) para la eliminación de usuarios.
 * Permite al administrador buscar un usuario por su nombre de usuario (cédula)
 * y visualizar sus datos en una tabla antes de proceder con la eliminación.
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioEliminarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField TxtUsuario;
    private JTable tblUser;
    private JButton BtnEliminar;
    private JButton BtnBuscar;
    private JLabel lblEliminar;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista UsuarioEliminarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa el modelo de la tabla con las columnas apropiadas y las configura
     * como no editables. Carga los iconos y cambia el idioma de los textos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public UsuarioEliminarView( MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Datos del Producto"); // Este título se actualizará con el idioma en cambiarIdioma()
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel(new Object[]{"Nombre", "Usuario", "Contraseña", "Correo", "Celular", "Fcha de Nacimiento", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUser.setModel(modelo);
        cambiarIdioma(idioma);
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y los encabezados de la tabla.
     */
    public void cambiarIdioma(MensajeInternacionalizacionHandler idioma) {
        setTitle(this.idioma.get("usuario.eliminar.titulo"));
        lblEliminar.setText(this.idioma.get("usuario.eliminar.tituloEtiqueta"));
        BtnBuscar.setText(this.idioma.get("usuario.eliminar.boton.buscar"));
        BtnEliminar.setText(this.idioma.get("usuario.eliminar.boton.eliminar"));

        modelo.setColumnIdentifiers(new Object[]{
                this.idioma.get("usuario.eliminar.columna.nombre"),
                this.idioma.get("usuario.eliminar.columna.usuario"),
                this.idioma.get("usuario.eliminar.columna.contrasena"),
                this.idioma.get("usuario.eliminar.columna.correo"),
                this.idioma.get("usuario.eliminar.columna.celular"),
                this.idioma.get("usuario.eliminar.columna.fechaNacimiento"),
                this.idioma.get("usuario.eliminar.columna.rol")
        });
    }

    /**
     * Obtiene el panel principal de la vista.
     * @return El {@link JPanel} principal.
     */
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    /**
     * Establece el panel principal de la vista.
     * @param panelPrincipal El {@link JPanel} a establecer.
     */
    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    /**
     * Obtiene el campo de texto para el nombre de usuario (cédula) a buscar o eliminar.
     * @return El {@link JTextField} del usuario.
     */
    public JTextField getTxtUsuario() {
        return TxtUsuario;
    }

    /**
     * Establece el campo de texto para el nombre de usuario (cédula).
     * @param txtUsuario El {@link JTextField} a establecer.
     */
    public void setTxtUsuario(JTextField txtUsuario) {
        TxtUsuario = txtUsuario;
    }

    /**
     * Obtiene la tabla donde se muestran los datos del usuario.
     * @return El {@link JTable} de usuarios.
     */
    public JTable getTblUser() {
        return tblUser;
    }

    /**
     * Establece la tabla de usuarios.
     * @param tblUser El {@link JTable} a establecer.
     */
    public void setTblUser(JTable tblUser) {
        this.tblUser = tblUser;
    }

    /**
     * Obtiene el botón de "Eliminar".
     * @return El {@link JButton} de eliminación.
     */
    public JButton getBtnEliminar() {
        return BtnEliminar;
    }

    /**
     * Establece el botón de "Eliminar".
     * @param btnEliminar El {@link JButton} a establecer.
     */
    public void setBtnEliminar(JButton btnEliminar) {
        BtnEliminar = btnEliminar;
    }

    /**
     * Obtiene el botón de "Buscar".
     * @return El {@link JButton} de búsqueda.
     */
    public JButton getBtnBuscar() {
        return BtnBuscar;
    }

    /**
     * Establece el botón de "Buscar".
     * @param btnBuscar El {@link JButton} a establecer.
     */
    public void setBtnBuscar(JButton btnBuscar) {
        BtnBuscar = btnBuscar;
    }

    /**
     * Obtiene el modelo de tabla asociado a {@link #tblUser}.
     * @return El {@link DefaultTableModel} de la tabla de usuarios.
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo de tabla para {@link #tblUser}.
     * @param modelo El {@link DefaultTableModel} a establecer.
     */
    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene la etiqueta del título principal de la vista de eliminación de usuario.
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblEliminar() {
        return lblEliminar;
    }

    /**
     * Establece la etiqueta del título principal de la vista de eliminación de usuario.
     * @param lblEliminar El {@link JLabel} a establecer.
     */
    public void setLblEliminar(JLabel lblEliminar) {
        this.lblEliminar = lblEliminar;
    }

    /**
     * Muestra un mensaje en un cuadro de diálogo emergente.
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Carga una lista de objetos {@link Usuario} en la tabla de la vista.
     * Muestra la cédula, contraseña y rol de cada usuario.
     *
     * @param usuarios La {@link List} de objetos {@link Usuario} a cargar.
     */
    public void cargarUsuario(List<Usuario> usuarios) {
        modelo.setRowCount(0);

        for (Usuario usuario : usuarios) {
            Object[] fila = {
                    usuario.getCedula(),
                    usuario.getContrasenia(),
                    usuario.getRol()
            };
            modelo.addRow(fila);
        }
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
            BtnBuscar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonEliminar = LoginView.class.getClassLoader().getResource("imagenes/EliminarTodo.svg.png");
        if (botonEliminar != null) {
            ImageIcon icono = new ImageIcon(botonEliminar);
            BtnEliminar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}