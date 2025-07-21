package ec.edu.ups.vista.AdministradorView;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.net.URL;

/**
 * Vista (JFrame) para la recuperación de contraseña a través de un cuestionario de seguridad.
 * Permite al usuario buscar su cuenta, seleccionar una pregunta de seguridad, ingresar una respuesta
 * y, si es correcta, establecer una nueva contraseña.
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CuestionarioRecuperarView extends JFrame {
    private JButton btnEnviar;
    private JPanel panelPrincipal;
    private JButton terminarButton;
    private JLabel lblTitulo;
    private JTextField txtRespuesta1;
    private JLabel lblPregunta1;
    private JComboBox cbxPreguntas;
    private JLabel lblRespuesta;
    private JTextField txtUsuario;
    private JButton btnBuscar;
    private JLabel lblUsuario;
    private final MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista CuestionarioRecuperarView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre y la posición, y carga los iconos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public CuestionarioRecuperarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Recuperar Contraseña");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        icono();
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} que contiene los textos en el idioma deseado.
     */
    public void actualizarTextos(MensajeInternacionalizacionHandler idioma) {
        setTitle(idioma.get("recuperar.titulo"));

        lblTitulo.setText(idioma.get("recuperar.titulo"));
        lblUsuario.setText(idioma.get("recuperar.usuario"));
        lblPregunta1.setText(idioma.get("recuperar.pregunta"));
        lblRespuesta.setText(idioma.get("recuperar.respuesta"));

        btnBuscar.setText(idioma.get("recuperar.boton.buscar"));
        btnEnviar.setText(idioma.get("recuperar.boton.enviar"));
        terminarButton.setText(idioma.get("recuperar.boton.terminar"));
    }

    /**
     * Obtiene el botón de "Enviar" de la vista.
     *
     * @return El {@link JButton} para enviar la respuesta.
     */
    public JButton getBtnEnviar() {
        return btnEnviar;
    }

    /**
     * Establece el botón de "Enviar" de la vista.
     *
     * @param btnEnviar El {@link JButton} a establecer como botón de envío.
     */
    public void setBtnEnviar(JButton btnEnviar) {
        this.btnEnviar = btnEnviar;
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
     * @param panelPrincipal El {@link JPanel} a establecer como panel principal.
     */
    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    /**
     * Obtiene el botón de "Terminar" de la vista.
     *
     * @return El {@link JButton} para terminar el proceso (puede no estar siempre habilitado).
     */
    public JButton getTerminarButton() {
        return terminarButton;
    }

    /**
     * Establece el botón de "Terminar" de la vista.
     *
     * @param terminarButton El {@link JButton} a establecer como botón de terminar.
     */
    public void setTerminarButton(JButton terminarButton) {
        this.terminarButton = terminarButton;
    }

    /**
     * Obtiene el campo de texto para la respuesta a la pregunta de seguridad.
     *
     * @return El {@link JTextField} para ingresar la respuesta.
     */
    public JTextField getTxtRespuesta1() {
        return txtRespuesta1;
    }

    /**
     * Establece el campo de texto para la respuesta a la pregunta de seguridad.
     *
     * @param txtRespuesta1 El {@link JTextField} a establecer para la respuesta.
     */
    public void setTxtRespuesta1(JTextField txtRespuesta1) {
        this.txtRespuesta1 = txtRespuesta1;
    }

    /**
     * Obtiene la etiqueta para la primera pregunta de seguridad.
     *
     * @return El {@link JLabel} que muestra la pregunta.
     */
    public JLabel getLblPregunta1() {
        return lblPregunta1;
    }

    /**
     * Establece la etiqueta para la primera pregunta de seguridad.
     *
     * @param lblPregunta1 El {@link JLabel} a establecer para la pregunta.
     */
    public void setLblPregunta1(JLabel lblPregunta1) {
        this.lblPregunta1 = lblPregunta1;
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
     * Establece la etiqueta para el título principal de la vista.
     *
     * @param lblTitulo El {@link JLabel} a establecer como título.
     */
    public void setLblTitulo(JLabel lblTitulo) {
        this.lblTitulo = lblTitulo;
    }

    /**
     * Obtiene la etiqueta para el título principal de la vista.
     *
     * @return El {@link JLabel} que muestra el título.
     */
    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    /**
     * Obtiene el combo box que contiene las preguntas de seguridad.
     *
     * @return El {@link JComboBox} de preguntas.
     */
    public JComboBox getCbxPreguntas() {
        return cbxPreguntas;
    }

    /**
     * Establece el combo box que contiene las preguntas de seguridad.
     *
     * @param cbxPreguntas El {@link JComboBox} a establecer para las preguntas.
     */
    public void setCbxPreguntas(JComboBox cbxPreguntas) {
        this.cbxPreguntas = cbxPreguntas;
    }

    /**
     * Obtiene la etiqueta para el campo de respuesta.
     *
     * @return El {@link JLabel} para la etiqueta de respuesta.
     */
    public JLabel getLblRespuesta() {
        return lblRespuesta;
    }

    /**
     * Establece la etiqueta para el campo de respuesta.
     *
     * @param lblRespuesta El {@link JLabel} a establecer para la etiqueta de respuesta.
     */
    public void setLblRespuesta(JLabel lblRespuesta) {
        this.lblRespuesta = lblRespuesta;
    }

    /**
     * Obtiene el campo de texto para ingresar el nombre de usuario (cédula).
     *
     * @return El {@link JTextField} para el usuario.
     */
    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    /**
     * Establece el campo de texto para ingresar el nombre de usuario (cédula).
     *
     * @param txtUsuario El {@link JTextField} a establecer para el usuario.
     */
    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    /**
     * Obtiene el botón de "Buscar" para buscar el usuario por su nombre de usuario.
     *
     * @return El {@link JButton} de búsqueda.
     */
    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    /**
     * Establece el botón de "Buscar".
     *
     * @param btnBuscar El {@link JButton} a establecer como botón de búsqueda.
     */
    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    /**
     * Obtiene la etiqueta para el campo de usuario.
     *
     * @return El {@link JLabel} para la etiqueta de usuario.
     */
    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    /**
     * Establece la etiqueta para el campo de usuario.
     *
     * @param lblUsuario El {@link JLabel} a establecer para la etiqueta de usuario.
     */
    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
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
     * Limpia los campos de entrada de la vista.
     * Actualmente, este método está vacío y no realiza ninguna limpieza.
     */
    public void limpiarCampos() {
        // Implementación pendiente: Vaciar txtUsuario, txtRespuesta1, etc.
    }

    /**
     * Carga y establece los iconos para los botones de "Terminar" y "Enviar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola.
     */
    public void icono() {
        URL botonTerminarCuestionario = LoginView.class.getClassLoader().getResource("imagenes/Terminar.svg.png");
        if (botonTerminarCuestionario != null) {
            ImageIcon icono = new ImageIcon(botonTerminarCuestionario);
            terminarButton.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonEnviarCuestionario = LoginView.class.getClassLoader().getResource("imagenes/Enviar.svg.png");
        if (botonEnviarCuestionario != null) {
            ImageIcon icono = new ImageIcon(botonEnviarCuestionario);
            btnEnviar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }
}