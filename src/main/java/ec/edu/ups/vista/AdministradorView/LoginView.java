package ec.edu.ups.vista.AdministradorView;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

/**
 * Vista (JFrame) para la pantalla de inicio de sesión de la aplicación.
 * Permite a los usuarios ingresar sus credenciales (nombre de usuario y contraseña),
 * registrarse, recuperar la contraseña, seleccionar el idioma de la interfaz
 * y elegir el tipo de almacenamiento de datos (memoria o archivo), incluyendo la ruta.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtContraseña;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JPanel panelPrincipal;
    private JButton btnOlvidar;
    private JButton btnSalir;
    private JComboBox<String> cbxIdiomas;
    private JLabel lblUsuario;
    private JLabel lblContraseña;
    private JLabel lblIniciarSesion;

    private JComboBox<String> cbxTipoAlmacenamiento;
    private JLabel lblRutaArchivo;
    private JTextField txtRutaArchivo;
    private JButton btnBuscarArchivo;

    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista LoginView.
     * Inicializa la vista con el manejador de internacionalización, configura el contenido,
     * el título, la operación de cierre y la posición. También inicializa los componentes,
     * carga los iconos y actualiza los textos según el idioma.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public LoginView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle(idioma.get("login.titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        inicializarComponentes();
        iconos();
        actualizarTextos(idioma);
        System.out.println("DEBUG (LoginView): LoginView inicializada.");
    }

    /**
     * Inicializa los componentes interactivos de la vista, como los JComboBox
     * para idiomas y tipo de almacenamiento, y configura sus oyentes de eventos.
     * Habilita/deshabilita campos de ruta de archivo según la selección del tipo de almacenamiento.
     */
    private void inicializarComponentes() {
        if (cbxIdiomas != null) {
            cbxIdiomas.removeAllItems();
            cbxIdiomas.addItem("Español");
            cbxIdiomas.addItem("English");
            cbxIdiomas.addItem("Français");
            if (idioma.getLocale().getLanguage().equals("es")) {
                cbxIdiomas.setSelectedItem("Español");
            } else if (idioma.getLocale().getLanguage().equals("en")) {
                cbxIdiomas.setSelectedItem("English");
            } else if (idioma.getLocale().getLanguage().equals("fr")) {
                cbxIdiomas.setSelectedItem("Français");
            }
        } else {
            System.err.println("DEBUG (LoginView): cbxIdiomas es null en inicializarComponentes.");
        }

        if (cbxTipoAlmacenamiento != null) {
            cbxTipoAlmacenamiento.removeAllItems();
            cbxTipoAlmacenamiento.addItem(idioma.get("login.opcion_memoria"));
            cbxTipoAlmacenamiento.addItem(idioma.get("login.opcion_archivo"));

            cbxTipoAlmacenamiento.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String seleccion = (String) cbxTipoAlmacenamiento.getSelectedItem();
                    boolean esArchivo = idioma.get("login.opcion_archivo").equals(seleccion);

                    if (txtRutaArchivo != null) {
                        txtRutaArchivo.setEnabled(esArchivo);
                        if (!esArchivo) {
                            txtRutaArchivo.setText("");
                        }
                    } else {
                        System.err.println("DEBUG (LoginView): txtRutaArchivo es null en actionPerformed del ComboBox.");
                    }

                    if (btnBuscarArchivo != null) {
                        btnBuscarArchivo.setEnabled(esArchivo);
                    } else {
                        System.err.println("DEBUG (LoginView): btnBuscarArchivo es null en actionPerformed del ComboBox.");
                    }
                    System.out.println("DEBUG (LoginView): Tipo de almacenamiento seleccionado: " + seleccion);
                }
            });

            cbxTipoAlmacenamiento.setSelectedItem(idioma.get("login.opcion_memoria"));
        } else {
            System.err.println("DEBUG (LoginView): cbxTipoAlmacenamiento es null en inicializarComponentes.");
        }

        if (txtRutaArchivo != null) {
            txtRutaArchivo.setEnabled(false);
        } else {
            System.err.println("DEBUG (LoginView): txtRutaArchivo es null al final de inicializarComponentes.");
        }
        if (btnBuscarArchivo != null) {
            btnBuscarArchivo.setEnabled(false);
        } else {
            System.err.println("DEBUG (LoginView): btnBuscarArchivo es null al final de inicializarComponentes.");
        }

        if (btnBuscarArchivo != null) {
            btnBuscarArchivo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setDialogTitle(idioma.get("login.seleccionar_ruta_archivo"));

                    int userSelection = fileChooser.showSaveDialog(LoginView.this);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File selectedDirectory = fileChooser.getSelectedFile();
                        if (txtRutaArchivo != null) {
                            txtRutaArchivo.setText(selectedDirectory.getAbsolutePath());
                        }
                        System.out.println("DEBUG (LoginView): Carpeta seleccionada: " + selectedDirectory.getAbsolutePath());
                    } else {
                        System.out.println("DEBUG (LoginView): Selección de carpeta cancelada.");
                    }
                }
            });
        } else {
            System.err.println("DEBUG (LoginView): btnBuscarArchivo es null al añadir listener.");
        }
    }

    /**
     * Obtiene el campo de texto para el nombre de usuario.
     *
     * @return El {@link JTextField} del nombre de usuario.
     */
    public JTextField getTxtUsername() {
        return txtUsername;
    }

    /**
     * Obtiene el campo de texto para la contraseña.
     *
     * @return El {@link JPasswordField} de la contraseña.
     */
    public JPasswordField getTxtContraseña() {
        return (JPasswordField) txtContraseña;
    }

    /**
     * Obtiene el botón de "Iniciar Sesión".
     *
     * @return El {@link JButton} de iniciar sesión.
     */
    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    /**
     * Obtiene el botón de "Registrarse".
     *
     * @return El {@link JButton} de registrarse.
     */
    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
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
     * Obtiene el botón de "Olvidé mi Contraseña".
     *
     * @return El {@link JButton} de olvidar contraseña.
     */
    public JButton getBtnOlvidar() {
        return btnOlvidar;
    }

    /**
     * Obtiene el botón de "Salir" de la aplicación.
     *
     * @return El {@link JButton} de salir.
     */
    public JButton getBtnSalir() {
        return btnSalir;
    }

    /**
     * Obtiene el ComboBox para la selección de idiomas.
     *
     * @return El {@link JComboBox} de idiomas.
     */
    public JComboBox<String> getCbxIdiomas() {
        return cbxIdiomas;
    }

    /**
     * Obtiene el ComboBox para la selección del tipo de almacenamiento de datos.
     *
     * @return El {@link JComboBox} del tipo de almacenamiento.
     */
    public JComboBox<String> getCbxTipoAlmacenamiento() {
        return cbxTipoAlmacenamiento;
    }

    /**
     * Obtiene el campo de texto que muestra la ruta del archivo de almacenamiento.
     *
     * @return El {@link JTextField} de la ruta del archivo.
     */
    public JTextField getTxtRutaArchivo() {
        return txtRutaArchivo;
    }

    /**
     * Obtiene el botón para buscar y seleccionar una ruta de archivo.
     *
     * @return El {@link JButton} para buscar archivo.
     */
    public JButton getBtnBuscarArchivo() {
        return btnBuscarArchivo;
    }

    /**
     * Obtiene la etiqueta del campo de usuario.
     *
     * @return El {@link JLabel} de usuario.
     */
    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    /**
     * Obtiene la etiqueta del campo de contraseña.
     *
     * @return El {@link JLabel} de contraseña.
     */
    public JLabel getLblContraseña() {
        return lblContraseña;
    }

    /**
     * Obtiene la etiqueta del título principal de la sección de inicio de sesión.
     *
     * @return El {@link JLabel} del título de inicio de sesión.
     */
    public JLabel getLblIniciarSesion() {
        return lblIniciarSesion;
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
     * Muestra un mensaje de pregunta al usuario con opciones de "Sí" y "No".
     *
     * @param mensaje El texto de la pregunta a mostrar.
     * @return {@code true} si el usuario selecciona "Sí", {@code false} si selecciona "No".
     */
    public boolean mostrarMensajePregunta(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, idioma.get("confirmacion.titulo"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    /**
     * Limpia los campos de entrada de la vista y restablece el estado de los componentes
     * relacionados con el tipo de almacenamiento, volviendo a la opción "Memoria" por defecto.
     */
    public void limpiarCampos() {
        if (txtUsername != null) {
            txtUsername.setText("");
        }
        if (txtContraseña != null) {
            txtContraseña.setText("");
        }
        if (txtRutaArchivo != null) {
            txtRutaArchivo.setText("");
        }
        if (cbxTipoAlmacenamiento != null) {
            cbxTipoAlmacenamiento.setSelectedItem(idioma.get("login.opcion_memoria"));
        }
        if (txtRutaArchivo != null) {
            txtRutaArchivo.setEnabled(false);
        }
        if (btnBuscarArchivo != null) {
            btnBuscarArchivo.setEnabled(false);
        }
        System.out.println("DEBUG (LoginView): Campos limpiados y estado de almacenamiento reseteado.");
    }

    /**
     * Actualiza todos los textos de los componentes de la vista para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * También actualiza las opciones del ComboBox de tipo de almacenamiento.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} que contiene los textos en el idioma deseado.
     */
    public void actualizarTextos(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;

        if (lblIniciarSesion != null) {
            lblIniciarSesion.setText(idioma.get("login.titulo"));
        } else {
            System.err.println("DEBUG (LoginView): lblIniciarSesion es null en actualizarTextos.");
        }
        if (lblUsuario != null) {
            lblUsuario.setText(idioma.get("login.usuario"));
        } else {
            System.err.println("DEBUG (LoginView): lblUsuario es null en actualizarTextos.");
        }
        if (lblContraseña != null) {
            lblContraseña.setText(idioma.get("login.contrasenia"));
        } else {
            System.err.println("DEBUG (LoginView): lblContraseña es null en actualizarTextos.");
        }

        if (btnIniciarSesion != null) {
            btnIniciarSesion.setText(idioma.get("login.iniciar"));
        } else {
            System.err.println("DEBUG (LoginView): btnIniciarSesion es null en actualizarTextos.");
        }
        if (btnRegistrarse != null) {
            btnRegistrarse.setText(idioma.get("login.registrar"));
        } else {
            System.err.println("DEBUG (LoginView): btnRegistrarse es null en actualizarTextos.");
        }
        if (btnOlvidar != null) {
            btnOlvidar.setText(idioma.get("login.olvidar"));
        } else {
            System.err.println("DEBUG (LoginView): btnOlvidar es null en actualizarTextos.");
        }
        if (btnSalir != null) {
            btnSalir.setText(idioma.get("login.salir"));
        } else {
            System.err.println("DEBUG (LoginView): btnSalir es null en actualizarTextos.");
        }

        if (cbxTipoAlmacenamiento != null) {
            String selectedStorage = (String) cbxTipoAlmacenamiento.getSelectedItem();
            cbxTipoAlmacenamiento.removeAllItems();
            cbxTipoAlmacenamiento.addItem(idioma.get("login.opcion_memoria"));
            cbxTipoAlmacenamiento.addItem(idioma.get("login.opcion_archivo"));
            if (selectedStorage != null && (selectedStorage.equals(idioma.get("login.opcion_memoria")) || selectedStorage.equals(idioma.get("login.opcion_archivo")))) {
                cbxTipoAlmacenamiento.setSelectedItem(selectedStorage);
            } else {
                cbxTipoAlmacenamiento.setSelectedItem(idioma.get("login.opcion_memoria"));
            }
            if (cbxTipoAlmacenamiento.getActionListeners().length > 0) {
                cbxTipoAlmacenamiento.getActionListeners()[0].actionPerformed(new ActionEvent(cbxTipoAlmacenamiento, ActionEvent.ACTION_PERFORMED, null));
            }
        } else {
            System.err.println("DEBUG (LoginView): cbxTipoAlmacenamiento es null en actualizarTextos.");
        }

        if (lblRutaArchivo != null) {
            lblRutaArchivo.setText(idioma.get("login.lbl_ruta_archivo"));
        } else {
            System.err.println("DEBUG (LoginView): lblRutaArchivo es null en actualizarTextos.");
        }
        if (btnBuscarArchivo != null) {
            btnBuscarArchivo.setText(idioma.get("login.btn_seleccionar_ruta"));
        } else {
            System.err.println("DEBUG (LoginView): btnBuscarArchivo es null en actualizarTextos.");
        }

        setTitle(idioma.get("titulo.ventana"));
        System.out.println("DEBUG (LoginView): Textos actualizados a " + idioma.getLocale().getDisplayLanguage());
    }

    /**
     * Carga y establece los iconos para los botones de "Iniciar Sesión", "Registrarse",
     * "Olvidé mi Contraseña", "Salir" y "Buscar Archivo" desde los recursos de la aplicación.
     * Si un icono no se encuentra, imprime un mensaje de error en la consola.
     */
    private void iconos() {
        URL iconLogin = getClass().getClassLoader().getResource("imagenes/Login.svg.png");
        if (iconLogin != null) {
            ImageIcon icono = new ImageIcon(iconLogin);
            if (btnIniciarSesion != null) btnIniciarSesion.setIcon(icono);
            if (btnRegistrarse != null) btnRegistrarse.setIcon(icono);
        } else {
            System.err.println("ERROR (LoginView): Icono Login.svg.png no encontrado.");
        }

        URL iconOlvidar = getClass().getClassLoader().getResource("imagenes/Olvidarrr.svg.png");
        if (iconOlvidar != null) {
            ImageIcon icono = new ImageIcon(iconOlvidar);
            if (btnOlvidar != null) btnOlvidar.setIcon(icono);
        } else {
            System.err.println("ERROR (LoginView): Icono Olvidarrr.svg.png no encontrado.");
        }

        URL iconSalir = getClass().getClassLoader().getResource("imagenes/Salir.svg.png");
        if (iconSalir != null) {
            ImageIcon icono = new ImageIcon(iconSalir);
            if (btnSalir != null) btnSalir.setIcon(icono);
        } else {
            System.err.println("ERROR (LoginView): Icono Salir.svg.png no encontrado.");
        }

        URL iconBuscar = getClass().getClassLoader().getResource("imagenes/BuscarTodo.svg.png");
        if (iconBuscar != null) {
            ImageIcon icono = new ImageIcon(iconBuscar);
            if (btnBuscarArchivo != null) btnBuscarArchivo.setIcon(icono);
        } else {
            System.err.println("ERROR (LoginView): Icono BuscarTodo.svg.png no encontrado.");
        }
        System.out.println("DEBUG (LoginView): Íconos cargados.");
    }
}