package ec.edu.ups.vista.UsuarioView;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.URL;
import java.util.List;
import ec.edu.ups.modelo.Usuario; // Se añadió esta importación para el método cargarDatos

/**
 * Vista interna (JInternalFrame) para listar y buscar usuarios.
 * Permite al administrador ver todos los usuarios registrados en una tabla
 * o buscar usuarios específicos por su nombre de usuario (cédula).
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioListarView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField TxtUsuario;
    private JButton BtnBuscar;
    private JButton BtnListar;
    private JTable tblUsuario;
    private JLabel lblListar;
    private JLabel lblUser;
    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista UsuarioListarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa el modelo de la tabla con las columnas apropiadas y las configura
     * como no editables. Carga los iconos y cambia el idioma de los textos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public UsuarioListarView( MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Listar Usuarios"); // Este título se actualizará con el idioma en cambiarIdioma()
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
        tblUsuario.setModel(modelo);
        cambiarIdioma(idioma);
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y los encabezados de la tabla.
     */
    public void cambiarIdioma(MensajeInternacionalizacionHandler idioma) {
        setTitle(this.idioma.get("usuario.listar.titulo"));
        lblListar.setText(this.idioma.get("usuario.listar.tituloTabla"));
        lblUser.setText(this.idioma.get("usuario.listar.usuario"));
        BtnBuscar.setText(this.idioma.get("usuario.listar.boton.buscar"));
        BtnListar.setText(this.idioma.get("usuario.listar.boton.listar"));

        modelo.setColumnIdentifiers(new Object[]{
                this.idioma.get("usuario.listar.columna.nombre"),
                this.idioma.get("usuario.listar.columna.usuario"),
                this.idioma.get("usuario.listar.columna.contrasena"),
                this.idioma.get("usuario.listar.columna.correo"),
                this.idioma.get("usuario.listar.columna.celular"),
                this.idioma.get("usuario.listar.columna.fechaNacimiento"),
                this.idioma.get("usuario.listar.columna.rol")
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
     * Obtiene el campo de texto para el nombre de usuario (cédula) a buscar.
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
     * Obtiene el botón de "Listar" todos los usuarios.
     * @return El {@link JButton} de listar.
     */
    public JButton getBtnListar() {
        return BtnListar;
    }

    /**
     * Establece el botón de "Listar".
     * @param btnListar El {@link JButton} a establecer.
     */
    public void setBtnListar(JButton btnListar) {
        BtnListar = btnListar;
    }

    /**
     * Obtiene la tabla donde se muestran los usuarios.
     * @return El {@link JTable} de usuarios.
     */
    public JTable getTblUsuario() {
        return tblUsuario;
    }

    /**
     * Establece la tabla de usuarios.
     * @param tblUsuario El {@link JTable} a establecer.
     */
    public void setTblUsuario(JTable tblUsuario) {
        this.tblUsuario = tblUsuario;
    }

    /**
     * Obtiene el modelo de tabla asociado a {@link #tblUsuario}.
     * @return El {@link DefaultTableModel} de la tabla de usuarios.
     */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo de tabla para {@link #tblUsuario}.
     * @param modelo El {@link DefaultTableModel} a establecer.
     */
    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene la etiqueta del título principal de la vista de listado de usuarios.
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblListar() {
        return lblListar;
    }

    /**
     * Establece la etiqueta del título principal de la vista de listado de usuarios.
     * @param lblListar El {@link JLabel} a establecer.
     */
    public void setLblListar(JLabel lblListar) {
        this.lblListar = lblListar;
    }

    /**
     * Obtiene la etiqueta del campo de usuario (cédula).
     * @return El {@link JLabel} de usuario.
     */
    public JLabel getLblUser() {
        return lblUser;
    }

    /**
     * Establece la etiqueta del campo de usuario (cédula).
     * @param lblUser El {@link JLabel} a establecer.
     */
    public void setLblUser(JLabel lblUser) {
        this.lblUser = lblUser;
    }

    /**
     * Muestra un mensaje en un cuadro de diálogo emergente.
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar" y "Listar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos(){
        URL botonBuscar = LoginView.class.getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (botonBuscar != null) {
            ImageIcon icono = new ImageIcon(botonBuscar);
            BtnBuscar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonListar = LoginView.class.getClassLoader().getResource("imagenes/ListarTodo.svg.png");
        if (botonListar != null) {
            ImageIcon icono = new ImageIcon(botonListar);
            BtnListar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }

    /**
     * Carga una lista de objetos {@link Usuario} en la tabla de la vista.
     * Los datos mostrados incluyen nombre, cédula, contraseña, correo, celular, fecha de nacimiento y rol.
     *
     * @param usuarios La {@link List} de objetos {@link Usuario} a cargar en la tabla.
     */
    public void cargarDatos(List<ec.edu.ups.modelo.Usuario> usuarios) {
        modelo.setRowCount(0); // Limpiar filas existentes

        for (ec.edu.ups.modelo.Usuario usuario : usuarios) {
            Object[] fila = {
                    usuario.getNombreCompleto(),
                    usuario.getCedula(),
                    usuario.getContrasenia(),
                    usuario.getCorreo(),
                    usuario.getCelular(),
                    usuario.getFechaNacimiento(),
                    usuario.getRol().toString()
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Limpia los campos de entrada de la vista y vacía la tabla de usuarios.
     */
    public void limpiarCampos() {
        TxtUsuario.setText("");
        modelo.setRowCount(0);
    }
}