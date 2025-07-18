package ec.edu.ups.vista.UsuarioView;

import ec.edu.ups.modelo.Rol;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Vista interna (JInternalFrame) para la creación de nuevos usuarios.
 * Permite a los administradores registrar usuarios con sus datos personales,
 * credenciales (cédula y contraseña), y la aplicación fija su rol como 'USUARIO'.
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioCrearView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField TxtUsername;
    private JTextField TxtPassword;
    private JButton BtnRegistrar;
    private JButton BtnLimpiar;
    private JLabel lblNuevoUsuario;
    private JLabel lblUsuario;
    private JLabel lblContraseña;
    private JLabel lblRol;
    private JTextField TxtCorreo;
    private JTextField TxtCelular;
    private JTextField TxtNombreCompleto;
    private JLabel lblNombreC;
    private JLabel lblCorreo;
    private JLabel lblCelular;
    private JLabel lblFechaN;
    private JComboBox cbxDia;
    private JComboBox cbxMes;
    private JComboBox cbxAño;
    private final Rol rolFijo = Rol.USUARIO; // Define el rol por defecto para los usuarios creados aquí
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista UsuarioCrearView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, el comportamiento de cierre, el tamaño y las propiedades de ventana interna.
     * Carga los iconos, configura el ActionListener para el botón de limpiar, e inicializa
     * los JComboBox para la fecha de nacimiento.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public UsuarioCrearView ( MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Crear Usuario"); // Este título se actualizará con el idioma en cambiarIdioma()
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        iconos();

        BtnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        for (int i = 1; i <= 31; i++) cbxDia.addItem(i);
        for (int i = 1980; i <= 2025; i++) cbxAño.addItem(i);
        String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        // Se carga inicialmente con nombres de meses en español (o inglés por defecto)
        // luego se actualiza con el idioma en cambiarIdioma()
        for (String mes : meses) {
            cbxMes.addItem(mes);
        }
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * Esto incluye el título de la ventana, etiquetas y textos de botones, así como las opciones
     * del ComboBox para los meses (obteniendo los nombres de los meses del archivo de propiedades).
     */
    public void cambiarIdioma() {
        setTitle(idioma.get("usuario.crear.titulo"));
        lblNuevoUsuario.setText(idioma.get("usuario.crear.tituloEtiqueta"));
        lblUsuario.setText(idioma.get("usuario.crear.usuario"));
        lblContraseña.setText(idioma.get("usuario.crear.contrasena"));
        lblRol.setText(idioma.get("usuario.crear.rol"));
        lblCorreo.setText(idioma.get("usuario.crear.correo"));
        lblCelular.setText(idioma.get("usuario.crear.celular"));
        lblNombreC.setText(idioma.get("usuario.crear.nombreCompleto"));
        lblFechaN.setText(idioma.get("usuario.crear.fechaNacimiento"));
        BtnRegistrar.setText(idioma.get("usuario.crear.boton.registrar"));
        BtnLimpiar.setText(idioma.get("usuario.crear.boton.limpiar"));

        cbxMes.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            cbxMes.addItem(idioma.get("mess." + i)); // Usa claves como "mess.1", "mess.2" para los meses
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
     * Obtiene el rol predefinido para los usuarios creados a través de esta vista.
     * En esta vista, el rol está fijo como {@link Rol#USUARIO}.
     * @return El {@link Rol#USUARIO}.
     */
    public Rol getRolSeleccionado() {
        return rolFijo;
    }

    /**
     * Establece el panel principal de la vista.
     * @param panelPrincipal El {@link JPanel} a establecer.
     */
    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    /**
     * Obtiene el campo de texto para el nombre de usuario (cédula).
     * @return El {@link JTextField} del nombre de usuario.
     */
    public JTextField getTxtUsername() {
        return TxtUsername;
    }

    /**
     * Establece el campo de texto para el nombre de usuario (cédula).
     * @param txtUsername El {@link JTextField} a establecer.
     */
    public void setTxtUsername(JTextField txtUsername) {
        TxtUsername = txtUsername;
    }

    /**
     * Obtiene el campo de texto para la contraseña.
     * @return El {@link JTextField} de la contraseña.
     */
    public JTextField getTxtPassword() {
        return TxtPassword;
    }

    /**
     * Establece el campo de texto para la contraseña.
     * @param txtPassword El {@link JTextField} a establecer.
     */
    public void setTxtPassword(JTextField txtPassword) {
        TxtPassword = txtPassword;
    }

    /**
     * Obtiene el botón de "Registrar".
     * @return El {@link JButton} de registrar.
     */
    public JButton getBtnRegistrar() {
        return BtnRegistrar;
    }

    /**
     * Establece el botón de "Registrar".
     * @param btnRegistrar El {@link JButton} a establecer.
     */
    public void setBtnRegistrar(JButton btnRegistrar) {
        BtnRegistrar = btnRegistrar;
    }

    /**
     * Obtiene el botón de "Limpiar" campos.
     * @return El {@link JButton} de limpiar.
     */
    public JButton getBtnLimpiar() {
        return BtnLimpiar;
    }

    /**
     * Establece el botón de "Limpiar" campos.
     * @param btnLimpiar El {@link JButton} a establecer.
     */
    public void setBtnLimpiar(JButton btnLimpiar) {
        BtnLimpiar = btnLimpiar;
    }

    /**
     * Obtiene la etiqueta del título principal de la vista "Nuevo Usuario".
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblNuevoUsuario() {
        return lblNuevoUsuario;
    }

    /**
     * Establece la etiqueta del título principal de la vista "Nuevo Usuario".
     * @param lblNuevoUsuario El {@link JLabel} a establecer.
     */
    public void setLblNuevoUsuario(JLabel lblNuevoUsuario) {
        this.lblNuevoUsuario = lblNuevoUsuario;
    }

    /**
     * Obtiene la etiqueta del campo de usuario (cédula).
     * @return El {@link JLabel} de usuario.
     */
    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    /**
     * Establece la etiqueta del campo de usuario (cédula).
     * @param lblUsuario El {@link JLabel} a establecer.
     */
    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    /**
     * Obtiene la etiqueta del campo de contraseña.
     * @return El {@link JLabel} de contraseña.
     */
    public JLabel getLblContraseña() {
        return lblContraseña;
    }

    /**
     * Establece la etiqueta del campo de contraseña.
     * @param lblContraseña El {@link JLabel} a establecer.
     */
    public void setLblContraseña(JLabel lblContraseña) {
        this.lblContraseña = lblContraseña;
    }

    /**
     * Obtiene la etiqueta del rol del usuario.
     * @return El {@link JLabel} del rol.
     */
    public JLabel getLblRol() {
        return lblRol;
    }

    /**
     * Establece la etiqueta del rol del usuario.
     * @param lblRol El {@link JLabel} a establecer.
     */
    public void setLblRol(JLabel lblRol) {
        this.lblRol = lblRol;
    }

    /**
     * Obtiene el campo de texto para la dirección de correo electrónico.
     * @return El {@link JTextField} del correo.
     */
    public JTextField getTxtCorreo() {
        return TxtCorreo;
    }

    /**
     * Establece el campo de texto para la dirección de correo electrónico.
     * @param txtCorreo El {@link JTextField} a establecer.
     */
    public void setTxtCorreo(JTextField txtCorreo) {
        TxtCorreo = txtCorreo;
    }

    /**
     * Obtiene el campo de texto para el número de celular.
     * @return El {@link JTextField} del celular.
     */
    public JTextField getTxtCelular() {
        return TxtCelular;
    }

    /**
     * Establece el campo de texto para el número de celular.
     * @param txtCelular El {@link JTextField} a establecer.
     */
    public void setTxtCelular(JTextField txtCelular) {
        TxtCelular = txtCelular;
    }

    /**
     * Obtiene el campo de texto para el nombre completo del usuario.
     * @return El {@link JTextField} del nombre completo.
     */
    public JTextField getTxtNombreCompleto() {
        return TxtNombreCompleto;
    }

    /**
     * Establece el campo de texto para el nombre completo del usuario.
     * @param txtNombreCompleto El {@link JTextField} a establecer.
     */
    public void setTxtNombreCompleto(JTextField txtNombreCompleto) {
        TxtNombreCompleto = txtNombreCompleto;
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
     * Obtiene la etiqueta del número de celular.
     * @return El {@link JLabel} del celular.
     */
    public JLabel getLblCelular() {
        return lblCelular;
    }

    /**
     * Establece la etiqueta del número de celular.
     * @param lblCelular El {@link JLabel} a establecer.
     */
    public void setLblCelular(JLabel lblCelular) {
        this.lblCelular = lblCelular;
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
     * Obtiene el JComboBox para seleccionar el día de nacimiento.
     * @return El {@link JComboBox} para el día.
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
     * Obtiene el JComboBox para seleccionar el mes de nacimiento.
     * @return El {@link JComboBox} para el mes.
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
     * Obtiene el JComboBox para seleccionar el año de nacimiento.
     * @return El {@link JComboBox} para el año.
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
     * Limpia todos los campos de entrada de la vista y restablece
     * la selección de los JComboBoxes de fecha a sus valores iniciales.
     */
    public void limpiarCampos() {
        TxtUsername.setText("");
        TxtPassword.setText("");
        TxtCorreo.setText("");
        TxtCelular.setText("");
        TxtNombreCompleto.setText("");
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
     * Carga y establece los iconos para los botones de "Limpiar" y "Registrar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola estándar.
     */
    public void iconos() {
        URL botonLimpiar = LoginView.class.getClassLoader().getResource("imagenes/LimpiarTodo.svg.png");
        if (botonLimpiar != null) {
            ImageIcon icono = new ImageIcon(botonLimpiar);
            BtnLimpiar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonRegistrar = LoginView.class.getClassLoader().getResource("imagenes/Register.svg.png");
        if (botonRegistrar != null) {
            ImageIcon icono = new ImageIcon(botonRegistrar);
            BtnRegistrar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}