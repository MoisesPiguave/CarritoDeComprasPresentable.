package ec.edu.ups.vista.UsuarioView;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import java.net.URL;

/**
 * Vista interna (JInternalFrame) para la modificación de usuarios existentes.
 * Permite al administrador buscar un usuario por su cédula y actualizar sus
 * datos personales, credenciales y fecha de nacimiento.
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioModificarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JButton btnEditar;
    private JTextField txtUsername;
    private JTextField txtContrasenia;
    private JTextField txtName; // Campo para buscar por nombre de usuario (cédula)
    private JButton btnBuscar;
    private JLabel lblActualizar; // Título de la sección de actualización
    private JTextField txtNombreCompleto;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JComboBox cbxDia;
    private JComboBox cbxMes;
    private JComboBox cbxAño;
    private JLabel lblFechaN;
    private JLabel lblCelular;
    private JLabel lblCorreo;
    private JLabel lblNombreC;
    private JLabel lblUsuario; // Etiqueta para el campo de username (cédula) a editar
    private JLabel lblContraseña;
    private JLabel lblUser; // Etiqueta para el campo de búsqueda de usuario
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista UsuarioModificarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * También inicializa los JComboBox para la fecha de nacimiento, carga los iconos y
     * actualiza los textos según el idioma.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public UsuarioModificarView( MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Modificar Usuario"); // Este título se actualizará con el idioma en cambiarIdioma()
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 350);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializar JComboBoxes de fecha
        for (int i = 1; i <= 31; i++) cbxDia.addItem(i);
        for (int i = 1980; i <= 2025; i++) cbxAño.addItem(i);

        cambiarIdioma(idioma);
        iconos();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas, textos de botones y las opciones
     * del ComboBox para los meses.
     */
    public void cambiarIdioma(MensajeInternacionalizacionHandler idioma) {
        setTitle(this.idioma.get("usuario.modificar.titulo"));
        lblUser.setText(this.idioma.get("usuario.modificar.usuario_buscar"));
        lblActualizar.setText(this.idioma.get("usuario.modificar.actualizar"));
        lblUsuario.setText(this.idioma.get("usuario.modificar.usuario"));
        lblContraseña.setText(this.idioma.get("usuario.modificar.contrasena"));
        lblNombreC.setText(this.idioma.get("usuario.modificar.nombre_completo"));
        lblCorreo.setText(this.idioma.get("usuario.modificar.correo"));
        lblCelular.setText(this.idioma.get("usuario.modificar.celular"));
        lblFechaN.setText(this.idioma.get("usuario.modificar.fecha_nacimiento"));
        btnBuscar.setText(this.idioma.get("boton.usuario.modificar.buscar"));
        btnEditar.setText(this.idioma.get("boton.usuario.modificar.editar"));

        cbxMes.removeAllItems();

        for (int i = 1; i <= 12; i++) {
            cbxMes.addItem(this.idioma.get("mes." + i)); // Usar claves como "mes.1" para enero, etc.
        }
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
     * Obtiene el botón de "Editar" (para guardar los cambios del usuario).
     * @return El {@link JButton} de editar.
     */
    public JButton getBtnEditar() {
        return btnEditar;
    }

    /**
     * Establece el botón de "Editar".
     * @param btnEditar El {@link JButton} a establecer.
     */
    public void setBtnEditar(JButton btnEditar) {
        this.btnEditar = btnEditar;
    }

    /**
     * Obtiene el campo de texto para el nombre de usuario (cédula) a modificar.
     * @return El {@link JTextField} del nombre de usuario.
     */
    public JTextField getTxtUsername() {
        return txtUsername;
    }

    /**
     * Establece el campo de texto para el nombre de usuario (cédula) a modificar.
     * @param txtUsername El {@link JTextField} a establecer.
     */
    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    /**
     * Obtiene el campo de texto para la contraseña a modificar.
     * @return El {@link JTextField} de la contraseña.
     */
    public JTextField getTxtContrasenia() {
        return txtContrasenia;
    }

    /**
     * Establece el campo de texto para la contraseña a modificar.
     * @param txtContrasenia El {@link JTextField} a establecer.
     */
    public void setTxtContrasenia(JTextField txtContrasenia) {
        this.txtContrasenia = txtContrasenia;
    }

    /**
     * Obtiene el campo de texto para el nombre de usuario (cédula) utilizado en la búsqueda inicial.
     * @return El {@link JTextField} para la búsqueda por nombre.
     */
    public JTextField getTxtName() {
        return txtName;
    }

    /**
     * Establece el campo de texto para el nombre de usuario (cédula) utilizado en la búsqueda inicial.
     * @param txtName El {@link JTextField} a establecer.
     */
    public void setTxtName(JTextField txtName) {
        this.txtName = txtName;
    }

    /**
     * Obtiene el botón de "Buscar" usuario.
     * @return El {@link JButton} de buscar.
     */
    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    /**
     * Establece el botón de "Buscar".
     * @param btnBuscar El {@link JButton} a establecer.
     */
    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    /**
     * Obtiene la etiqueta del título de la sección de actualización.
     * @return El {@link JLabel} del título de actualización.
     */
    public JLabel getLblActualizar() {
        return lblActualizar;
    }

    /**
     * Establece la etiqueta del título de la sección de actualización.
     * @param lblActualizar El {@link JLabel} a establecer.
     */
    public void setLblActualizar(JLabel lblActualizar) {
        this.lblActualizar = lblActualizar;
    }

    /**
     * Obtiene el campo de texto para el nombre completo del usuario.
     * @return El {@link JTextField} del nombre completo.
     */
    public JTextField getTxtNombreCompleto() {
        return txtNombreCompleto;
    }

    /**
     * Establece el campo de texto para el nombre completo del usuario.
     * @param txtNombreCompleto El {@link JTextField} a establecer.
     */
    public void setTxtNombreCompleto(JTextField txtNombreCompleto) {
        this.txtNombreCompleto = txtNombreCompleto;
    }

    /**
     * Obtiene el campo de texto para el correo electrónico del usuario.
     * @return El {@link JTextField} del correo.
     */
    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    /**
     * Establece el campo de texto para el correo electrónico del usuario.
     * @param txtCorreo El {@link JTextField} a establecer.
     */
    public void setTxtCorreo(JTextField txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    /**
     * Obtiene el campo de texto para el número de celular del usuario.
     * @return El {@link JTextField} del celular.
     */
    public JTextField getTxtCelular() {
        return txtCelular;
    }

    /**
     * Establece el campo de texto para el número de celular del usuario.
     * @param txtCelular El {@link JTextField} a establecer.
     */
    public void setTxtCelular(JTextField txtCelular) {
        this.txtCelular = txtCelular;
    }

    /**
     * Obtiene el JComboBox para el día de nacimiento.
     * @return El {@link JComboBox} del día.
     */
    public JComboBox getCbxDia() {
        return cbxDia;
    }

    /**
     * Establece el JComboBox para el día de nacimiento.
     * @param cbxDia El {@link JComboBox} a establecer.
     */
    public void setCbxDia(JComboBox cbxDia) {
        this.cbxDia = cbxDia;
    }

    /**
     * Obtiene el JComboBox para el mes de nacimiento.
     * @return El {@link JComboBox} del mes.
     */
    public JComboBox getCbxMes() {
        return cbxMes;
    }

    /**
     * Establece el JComboBox para el mes de nacimiento.
     * @param cbxMes El {@link JComboBox} a establecer.
     */
    public void setCbxMes(JComboBox cbxMes) {
        this.cbxMes = cbxMes;
    }

    /**
     * Obtiene el JComboBox para el año de nacimiento.
     * @return El {@link JComboBox} del año.
     */
    public JComboBox getCbxAño() {
        return cbxAño;
    }

    /**
     * Establece el JComboBox para el año de nacimiento.
     * @param cbxAño El {@link JComboBox} a establecer.
     */
    public void setCbxAño(JComboBox cbxAño) {
        this.cbxAño = cbxAño;
    }

    /**
     * Obtiene la etiqueta de la fecha de nacimiento.
     * @return El {@link JLabel} de la fecha de nacimiento.
     */
    public JLabel getLblFechaN() {
        return lblFechaN;
    }

    /**
     * Establece la etiqueta de la fecha de nacimiento.
     * @param lblFechaN El {@link JLabel} a establecer.
     */
    public void setLblFechaN(JLabel lblFechaN) {
        this.lblFechaN = lblFechaN;
    }

    /**
     * Obtiene la etiqueta del celular.
     * @return El {@link JLabel} del celular.
     */
    public JLabel getLblCelular() {
        return lblCelular;
    }

    /**
     * Establece la etiqueta del celular.
     * @param lblCelular El {@link JLabel} a establecer.
     */
    public void setLblCelular(JLabel lblCelular) {
        this.lblCelular = lblCelular;
    }

    /**
     * Obtiene la etiqueta del correo electrónico.
     * @return El {@link JLabel} del correo.
     */
    public JLabel getLblCorreo() {
        return lblCorreo;
    }

    /**
     * Establece la etiqueta del correo electrónico.
     * @param lblCorreo El {@link JLabel} a establecer.
     */
    public void setLblCorreo(JLabel lblCorreo) {
        this.lblCorreo = lblCorreo;
    }

    /**
     * Obtiene la etiqueta del nombre completo.
     * @return El {@link JLabel} del nombre completo.
     */
    public JLabel getLblNombreC() {
        return lblNombreC;
    }

    /**
     * Establece la etiqueta del nombre completo.
     * @param lblNombreC El {@link JLabel} a establecer.
     */
    public void setLblNombreC(JLabel lblNombreC) {
        this.lblNombreC = lblNombreC;
    }

    /**
     * Obtiene la etiqueta del campo de usuario (cédula) a modificar.
     * @return El {@link JLabel} del usuario.
     */
    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    /**
     * Establece la etiqueta del campo de usuario (cédula) a modificar.
     * @param lblUsuario El {@link JLabel} a establecer.
     */
    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    /**
     * Obtiene la etiqueta de la contraseña.
     * @return El {@link JLabel} de la contraseña.
     */
    public JLabel getLblContraseña() {
        return lblContraseña;
    }

    /**
     * Establece la etiqueta de la contraseña.
     * @param lblContraseña El {@link JLabel} a establecer.
     */
    public void setLblContraseña(JLabel lblContraseña) {
        this.lblContraseña = lblContraseña;
    }

    /**
     * Obtiene el manejador de internacionalización asociado a esta vista.
     * @return El {@link MensajeInternacionalizacionHandler} de la vista.
     */
    public MensajeInternacionalizacionHandler getMi() {
        return idioma;
    }

    /**
     * Establece el manejador de internacionalización para esta vista.
     * @param idioma El {@link MensajeInternacionalizacionHandler} a establecer.
     */
    public void setMi(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
    }

    /**
     * Obtiene la etiqueta para el campo de búsqueda de usuario (cédula).
     * @return El {@link JLabel} para el usuario.
     */
    public JLabel getLblUser() {
        return lblUser;
    }

    /**
     * Establece la etiqueta para el campo de búsqueda de usuario (cédula).
     * @param lblUser El {@link JLabel} a establecer.
     */
    public void setLblUser(JLabel lblUser) {
        this.lblUser = lblUser;
    }

    /**
     * Limpia todos los campos de entrada de la vista, incluyendo el de búsqueda,
     * y restablece la selección de los JComboBoxes de fecha a sus valores iniciales.
     */
    public void limpiarCampos() {
        txtName.setText("");
        txtUsername.setText("");
        txtContrasenia.setText("");
        txtNombreCompleto.setText("");
        txtCorreo.setText("");
        txtCelular.setText("");
        cbxDia.setSelectedIndex(0);
        cbxMes.setSelectedIndex(0);
        cbxAño.setSelectedIndex(0);
    }

    /**
     * Muestra un mensaje en un cuadro de diálogo emergente.
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Muestra un mensaje de pregunta al usuario con opciones de "Sí" y "No".
     *
     * @param mensaje El texto de la pregunta a mostrar.
     * @return {@code true} si el usuario selecciona "Sí", {@code false} si selecciona "No".
     */
    public boolean mostrarMensajePregunta(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    /**
     * Carga y establece los iconos para los botones de "Buscar" y "Editar"
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
        URL botonEditar = LoginView.class.getClassLoader().getResource("imagenes/Actualizar.svg.png");
        if (botonEditar != null) {
            ImageIcon icono = new ImageIcon(botonEditar);
            btnEditar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}