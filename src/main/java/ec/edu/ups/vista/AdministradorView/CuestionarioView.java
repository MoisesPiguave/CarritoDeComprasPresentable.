package ec.edu.ups.vista.AdministradorView;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.impl.CuestionarioDAOMemoria;
import ec.edu.ups.modelo.Preguntas;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.net.URL;

/**
 * Vista (JFrame) para la gestión del cuestionario de seguridad.
 * Permite al usuario seleccionar preguntas y guardar sus respuestas,
 * y finalizar el cuestionario una vez que se han registrado suficientes preguntas.
 * Soporta internacionalización de textos.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
// ¡¡¡CAMBIO CRÍTICO AQUÍ: DEBE EXTENDER JInternalFrame!!!
public class CuestionarioView extends JInternalFrame { // <-- ¡ESTO ES LO QUE DEBES CAMBIAR!
    private JComboBox cbxPreguntas;
    private JTextField txtRespuesta;
    private JButton btnGuardar;
    private JPanel panelPrincipal; // Este JPanel contendrá el contenido del formulario .form
    private JButton btnTerminar;
    private JLabel lblTitulo;
    private JLabel lblPregunta;
    private MensajeInternacionalizacionHandler idioma;
    private CuestionarioDAO cuestionarioDAO;
    private CuestionarioDAOMemoria cuestionarioDAOMemoria; // No utilizado directamente en este código

    /**
     * Constructor de la vista CuestionarioView.
     * Inicializa la vista con el manejador de internacionalización y el DAO de cuestionario,
     * configura el contenido, el título, la operación de cierre, la posición,
     * carga los iconos y las preguntas en el combo box.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     * @param cuestionarioDAO El {@link CuestionarioDAO} para acceder a las preguntas del cuestionario.
     */
    public CuestionarioView( MensajeInternacionalizacionHandler idioma, CuestionarioDAO cuestionarioDAO) {
        this.idioma = idioma;
        this.cuestionarioDAO = cuestionarioDAO;

        // Cuando cambias a JInternalFrame, algunas propiedades de JFrame pueden necesitar ajustes.
        // setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Esto se convierte en setClosable(true) para JInternalFrame

        // Propiedades comunes de JInternalFrame que puedes querer establecer:
        setClosable(true); // Permite cerrar la ventana interna
        setMaximizable(true); // Permite maximizar
        setResizable(true); // Permite redimensionar
        setIconifiable(true); // Permite minimizar a un icono en el desktop

        setContentPane(panelPrincipal);
        setTitle("Cuestionario");
        setSize(550, 350);
        // setLocationRelativeTo(null); // Esto no se usa en JInternalFrame directamente para el posicionamiento
        // Su posición inicial se establece al añadirla al JDesktopPane o con setLocation(x, y)
        icono();
        // Solo cargar preguntas si el DAO NO es nulo al inicio
        if (this.cuestionarioDAO != null) {
            cargarPreguntas();
        }
    }

    // ... (El resto de tus métodos getters, setters y lógicos permanecen IGUAL)
    // No he modificado nada más que la herencia y comentado setDefaultCloseOperation.

    /**
     * Obtiene el JComboBox que muestra las preguntas del cuestionario.
     *
     * @return El {@link JComboBox} de preguntas.
     */
    public JComboBox getCbxPreguntas() {
        return cbxPreguntas;
    }

    /**
     * Establece el JComboBox para las preguntas del cuestionario.
     *
     * @param cbxPreguntas El {@link JComboBox} a establecer.
     */
    public void setCbxPreguntas(JComboBox cbxPreguntas) {
        this.cbxPreguntas = cbxPreguntas;
    }

    /**
     * Obtiene el JTextField donde el usuario ingresa su respuesta a la pregunta.
     *
     * @return El {@link JTextField} para la respuesta.
     */
    public JTextField getTxtRespuesta() {
        return txtRespuesta;
    }

    /**
     * Establece el JTextField para la respuesta.
     *
     * @param txtRespuesta El {@link JTextField} a establecer.
     */
    public void setTxtRespuesta(JTextField txtRespuesta) {
        this.txtRespuesta = txtRespuesta;
    }

    /**
     * Obtiene el botón para guardar la respuesta a la pregunta actual.
     *
     * @return El {@link JButton} de guardar.
     */
    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    /**
     * Establece el botón de guardar.
     *
     * @param btnGuardar El {@link JButton} a establecer.
     */
    public void setBtnGuardar(JButton btnGuardar) {
        this.btnGuardar = btnGuardar;
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
     * Obtiene el botón para terminar el cuestionario y guardar el conjunto de preguntas/respuestas.
     *
     * @return El {@link JButton} de terminar.
     */
    public JButton getBtnTerminar() {
        return btnTerminar;
    }

    /**
     * Establece el botón de terminar.
     *
     * @param btnTerminar El {@link JButton} a establecer.
     */
    public void setBtnTerminar(JButton btnTerminar) {
        this.btnTerminar = btnTerminar;
    }

    /**
     * Obtiene la etiqueta del título principal de la vista.
     *
     * @return El {@link JLabel} del título.
     */
    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    /**
     * Establece la etiqueta del título principal de la vista.
     *
     * @param lblTitulo El {@link JLabel} a establecer como título.
     */
    public void setLblTitulo(JLabel lblTitulo) {
        this.lblTitulo = lblTitulo;
    }

    /**
     * Obtiene la etiqueta para el texto de la pregunta.
     *
     * @return El {@link JLabel} de la pregunta.
     */
    public JLabel getLblPregunta() {
        return lblPregunta;
    }

    /**
     * Establece la etiqueta para el texto de la pregunta.
     *
     * @param lblPregunta El {@link JLabel} a establecer.
     */
    public void setLblPregunta(JLabel lblPregunta) {
        this.lblPregunta = lblPregunta;
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
     * Carga y establece los iconos para los botones de "Guardar" y "Terminar"
     * desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola.
     */
    private void icono() {
        // Asumiendo que LoginView.class.getClassLoader() es un punto de partida válido para recursos
        // Si las imágenes están en un módulo diferente o la clase cargadora cambia, esto puede fallar.
        URL botonGuardarCuestionario = LoginView.class.getClassLoader().getResource("imagenes/Guardar.svg.png");
        if (botonGuardarCuestionario != null) {
            ImageIcon icono = new ImageIcon(botonGuardarCuestionario);
            btnGuardar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado: imagenes/Guardar.svg.png");
        }
        URL botonTerminarCuestionario = LoginView.class.getClassLoader().getResource("imagenes/Terminar.svg.png");
        if (botonTerminarCuestionario != null) {
            ImageIcon icono = new ImageIcon(botonTerminarCuestionario);
            btnTerminar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado: imagenes/Terminar.svg.png");
        }
    }

    /**
     * Limpia los campos de entrada de la vista, restableciendo
     * la selección del combo box de preguntas y vaciando el campo de respuesta.
     */
    public void limpiarCampos() {
        cbxPreguntas.setSelectedIndex(0);
        txtRespuesta.setText("");
    }

    /**
     * Establece el DAO de cuestionario para esta vista.
     * Esto es crucial para que la vista pueda cargar las preguntas correctas.
     * @param cuestionarioDAO El DAO de cuestionario a establecer.
     */
    public void setCuestionarioDAO(CuestionarioDAO cuestionarioDAO) {
        this.cuestionarioDAO = cuestionarioDAO;
        // Recarga las preguntas cada vez que el DAO cambie para asegurar que los datos son los correctos.
        if (this.cuestionarioDAO != null) {
            cargarPreguntas();
        }
    }

    /**
     * Carga las preguntas desde el {@link CuestionarioDAO} y las añade al JComboBox.
     * Limpia los ítems existentes en el combo box antes de cargar los nuevos.
     */
    public void cargarPreguntas() {
        if (cbxPreguntas != null && cuestionarioDAO != null) {
            cbxPreguntas.removeAllItems();
            try {
                for (Preguntas p : cuestionarioDAO.listarPreguntas()) {
                    cbxPreguntas.addItem(p);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar preguntas en CuestionarioView: " + e.getMessage());
                JOptionPane.showMessageDialog(this, idioma.get("error.cargar.preguntas") + ": " + e.getMessage(), idioma.get("error.titulo"), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.err.println("Advertencia: No se pueden cargar preguntas. ComboBox o CuestionarioDAO es nulo.");
        }
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} que contiene los textos en el idioma deseado.
     */
    public void actualizarTextos(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setTitle(idioma.get("cuestionario.titulo"));
        lblTitulo.setText(idioma.get("cuestionario.titulo"));
        lblPregunta.setText(idioma.get("cuestionario.pregunta"));
        btnGuardar.setText(idioma.get("cuestionario.boton.guardar"));
        btnTerminar.setText(idioma.get("cuestionario.boton.terminar"));
    }
}