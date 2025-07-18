package ec.edu.ups.vista.AdministradorView;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.net.URL;

/**
 * Vista (JFrame) para el registro de nuevos usuarios en la aplicación.
 * Permite a los usuarios ingresar sus datos personales y de acceso
 * para crear una nueva cuenta. Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class RegistrarView extends JFrame {
    private JLabel lblRegistrar;
    private JTextField txtNombreCompleto;
    private JTextField txtUsuario;
    private JTextField txtContraseña;
    private JTextField txtCelular;
    private JTextField txtCorreo;
    private JButton btnRegistrar;
    private JButton btnLimpiar;
    private JPanel panelPrincipal;
    private JLabel lblNombreCompleto;
    private JLabel lblFechaDeNacimiento;
    private JLabel lblUsuario;
    private JLabel lblContraseña;
    private JLabel lblCelular;
    private JLabel lblCorreo;
    private JComboBox cbxDia;
    private JComboBox cbxMes;
    private JComboBox cbxAño;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista RegistrarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre y la posición. También inicializa los JComboBox
     * para la fecha de nacimiento, carga los iconos y actualiza los textos según el idioma.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public RegistrarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Recuperar Contraseña"); // Este título se sobrescribe luego en cambiarIdioma
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        // Inicializar JComboBoxes de fecha
        for (int i = 1; i <= 31; i++) cbxDia.addItem(i);
        for (int i = 1980; i <= 2025; i++) cbxAño.addItem(i);

        icono();
        cambiarIdioma(idioma);
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * También actualiza las opciones del ComboBox para los meses.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} que contiene los textos en el idioma deseado.
     */
    public void cambiarIdioma(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        lblRegistrar.setText(idioma.get("registrar.titulo"));
        lblNombreCompleto.setText(idioma.get("registrar.nombre"));
        lblUsuario.setText(idioma.get("registrar.usuario"));
        lblContraseña.setText(idioma.get("registrar.contrasena"));
        lblCelular.setText(idioma.get("registrar.celular"));
        lblCorreo.setText(idioma.get("registrar.correo"));
        lblFechaDeNacimiento.setText(idioma.get("registrar.fechaNacimiento"));

        btnRegistrar.setText(idioma.get("registrar.boton.registrar"));
        btnLimpiar.setText(idioma.get("registrar.boton.limpiar"));

        cbxMes.removeAllItems();
        String[] meses = {
                idioma.get("mes.enero"), idioma.get("mes.febrero"), idioma.get("mes.marzo"),
                idioma.get("mes.abril"), idioma.get("mes.mayo"), idioma.get("mes.junio"),
                idioma.get("mes.julio"), idioma.get("mes.agosto"), idioma.get("mes.septiembre"),
                idioma.get("mes.octubre"), idioma.get("mes.noviembre"), idioma.get("mes.diciembre")
        };
        for (String mes : meses) {
            cbxMes.addItem(mes);
        }
    }

    /**
     * Obtiene la etiqueta del título principal de registro.
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblRegistrar() {
        return lblRegistrar;
    }

    /**
     * Establece la etiqueta del título principal de registro.
     *
     * @param lblRegistrar El {@link JLabel} a establecer.
     */
    public void setLblRegistrar(JLabel lblRegistrar) {
        this.lblRegistrar = lblRegistrar;
    }

    /**
     * Obtiene el campo de texto para el nombre completo.
     *
     * @return El {@link JTextField} del nombre completo.
     */
    public JTextField getTxtNombreCompleto() {
        return txtNombreCompleto;
    }

    /**
     * Establece el campo de texto para el nombre completo.
     *
     * @param txtNombreCompleto El {@link JTextField} a establecer.
     */
    public void setTxtNombreCompleto(JTextField txtNombreCompleto) {
        this.txtNombreCompleto = txtNombreCompleto;
    }

    /**
     * Obtiene el campo de texto para el nombre de usuario (cédula).
     *
     * @return El {@link JTextField} del nombre de usuario.
     */
    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    /**
     * Establece el campo de texto para el nombre de usuario (cédula).
     *
     * @param txtUsuario El {@link JTextField} a establecer.
     */
    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    /**
     * Obtiene el campo de texto para la contraseña.
     *
     * @return El {@link JTextField} de la contraseña.
     */
    public JTextField getTxtContraseña() {
        return txtContraseña;
    }

    /**
     * Establece el campo de texto para la contraseña.
     *
     * @param txtContraseña El {@link JTextField} a establecer.
     */
    public void setTxtContraseña(JTextField txtContraseña) {
        this.txtContraseña = txtContraseña;
    }

    /**
     * Obtiene el campo de texto para el número de celular.
     *
     * @return El {@link JTextField} del celular.
     */
    public JTextField getTxtCelular() {
        return txtCelular;
    }

    /**
     * Establece el campo de texto para el número de celular.
     *
     * @param txtCelular El {@link JTextField} a establecer.
     */
    public void setTxtCelular(JTextField txtCelular) {
        this.txtCelular = txtCelular;
    }

    /**
     * Obtiene el campo de texto para la dirección de correo electrónico.
     *
     * @return El {@link JTextField} del correo.
     */
    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    /**
     * Establece el campo de texto para la dirección de correo electrónico.
     *
     * @param txtCorreo El {@link JTextField} a establecer.
     */
    public void setTxtCorreo(JTextField txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    /**
     * Obtiene el botón de "Registrar".
     *
     * @return El {@link JButton} de registrar.
     */
    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    /**
     * Establece el botón de "Registrar".
     *
     * @param btnRegistrar El {@link JButton} a establecer.
     */
    public void setBtnRegistrar(JButton btnRegistrar) {
        this.btnRegistrar = btnRegistrar;
    }

    /**
     * Obtiene el botón de "Limpiar".
     *
     * @return El {@link JButton} de limpiar campos.
     */
    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    /**
     * Establece el botón de "Limpiar".
     *
     * @param btnLimpiar El {@link JButton} a establecer.
     */
    public void setBtnLimpiar(JButton btnLimpiar) {
        this.btnLimpiar = btnLimpiar;
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
     * Obtiene la etiqueta para el campo de nombre completo.
     *
     * @return El {@link JLabel} de nombre completo.
     */
    public JLabel getLblNombreCompleto() {
        return lblNombreCompleto;
    }

    /**
     * Establece la etiqueta para el campo de nombre completo.
     *
     * @param lblNombreCompleto El {@link JLabel} a establecer.
     */
    public void setLblNombreCompleto(JLabel lblNombreCompleto) {
        this.lblNombreCompleto = lblNombreCompleto;
    }

    /**
     * Obtiene la etiqueta para el campo de fecha de nacimiento.
     *
     * @return El {@link JLabel} de fecha de nacimiento.
     */
    public JLabel getLblFechaDeNacimiento() {
        return lblFechaDeNacimiento;
    }

    /**
     * Establece la etiqueta para el campo de fecha de nacimiento.
     *
     * @param lblFechaDeNacimiento El {@link JLabel} a establecer.
     */
    public void setLblFechaDeNacimiento(JLabel lblFechaDeNacimiento) {
        this.lblFechaDeNacimiento = lblFechaDeNacimiento;
    }

    /**
     * Obtiene la etiqueta para el campo de usuario (cédula).
     *
     * @return El {@link JLabel} de usuario.
     */
    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    /**
     * Establece la etiqueta para el campo de usuario (cédula).
     *
     * @param lblUsuario El {@link JLabel} a establecer.
     */
    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    /**
     * Obtiene la etiqueta para el campo de contraseña.
     *
     * @return El {@link JLabel} de contraseña.
     */
    public JLabel getLblContraseña() {
        return lblContraseña;
    }

    /**
     * Establece la etiqueta para el campo de contraseña.
     *
     * @param lblContraseña El {@link JLabel} a establecer.
     */
    public void setLblContraseña(JLabel lblContraseña) {
        this.lblContraseña = lblContraseña;
    }

    /**
     * Obtiene la etiqueta para el campo de celular.
     *
     * @return El {@link JLabel} de celular.
     */
    public JLabel getLblCelular() {
        return lblCelular;
    }

    /**
     * Establece la etiqueta para el campo de celular.
     *
     * @param lblCelular El {@link JLabel} a establecer.
     */
    public void setLblCelular(JLabel lblCelular) {
        this.lblCelular = lblCelular;
    }

    /**
     * Obtiene la etiqueta para el campo de correo electrónico.
     *
     * @return El {@link JLabel} de correo.
     */
    public JLabel getLblCorreo() {
        return lblCorreo;
    }

    /**
     * Establece la etiqueta para el campo de correo electrónico.
     *
     * @param lblCorreo El {@link JLabel} a establecer.
     */
    public void setLblCorreo(JLabel lblCorreo) {
        this.lblCorreo = lblCorreo;
    }

    /**
     * Obtiene el JComboBox para seleccionar el día de nacimiento.
     *
     * @return El {@link JComboBox} de día.
     */
    public JComboBox getCbxDia() {
        return cbxDia;
    }

    /**
     * Establece el JComboBox para el día de nacimiento.
     *
     * @param cbxDia El {@link JComboBox} a establecer.
     */
    public void setCbxDia(JComboBox cbxDia) {
        this.cbxDia = cbxDia;
    }

    /**
     * Obtiene el JComboBox para seleccionar el mes de nacimiento.
     *
     * @return El {@link JComboBox} de mes.
     */
    public JComboBox getCbxMes() {
        return cbxMes;
    }

    /**
     * Establece el JComboBox para el mes de nacimiento.
     *
     * @param cbxMes El {@link JComboBox} a establecer.
     */
    public void setCbxMes(JComboBox cbxMes) {
        this.cbxMes = cbxMes;
    }

    /**
     * Obtiene el JComboBox para seleccionar el año de nacimiento.
     *
     * @return El {@link JComboBox} de año.
     */
    public JComboBox getCbxAño() {
        return cbxAño;
    }

    /**
     * Establece el JComboBox para el año de nacimiento.
     *
     * @param cbxAño El {@link JComboBox} a establecer.
     */
    public void setCbxAño(JComboBox cbxAño) {
        this.cbxAño = cbxAño;
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
     * Limpia todos los campos de entrada de la vista y restablece
     * la selección de los JComboBoxes de fecha a sus valores iniciales (primeros elementos).
     */
    public void limpiarCampos() {
        txtNombreCompleto.setText("");
        txtUsuario.setText("");
        txtContraseña.setText("");
        txtCelular.setText("");
        txtCorreo.setText("");
        cbxDia.setSelectedIndex(0);
        cbxMes.setSelectedIndex(0);
        cbxAño.setSelectedIndex(0);

    }

    /**
     * Carga y establece los iconos para los botones de "Registrar" y "Limpiar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola.
     */
    private void icono() {
        URL botonRegistrar = LoginView.class.getClassLoader().getResource("imagenes/Register.svg.png");
        if (botonRegistrar != null) {
            ImageIcon icono = new ImageIcon(botonRegistrar);
            btnRegistrar.setIcon(icono);
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