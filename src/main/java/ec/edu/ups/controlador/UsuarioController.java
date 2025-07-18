package ec.edu.ups.controlador;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CuestionarioDAOMemoria;
import ec.edu.ups.modelo.*;
import ec.edu.ups.exepciones.CedulaException;
import ec.edu.ups.exepciones.ContraseniaInvalidaException;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.*;
import ec.edu.ups.vista.UsuarioView.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de usuarios, incluyendo autenticación, registro,
 * CRUD de usuarios (Crear, Listar, Actualizar, Eliminar) y gestión de preguntas
 * de seguridad para recuperación de contraseña.
 * Este controlador maneja la interacción entre las vistas de usuario y los DAOs
 * correspondientes (UsuarioDAO, CuestionarioDAO).
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de 7 de 2025
 */
public class UsuarioController {

    public Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private final MensajeInternacionalizacionHandler idioma;

    private LoginView loginView;
    private RegistrarView registrarView;
    private UsuarioCrearView usuarioCrearView;
    private UsuarioListarView usuarioListarView;
    private UsuarioEliminarView usuarioEliminarView;
    private UsuarioModificarView usuarioModificarView;
    private CuestionarioView cuestionarioView;
    private CuestionarioRecuperarView cuestionarioRecuperarView;
    private List<PreguntasRespuestas> preguntasRes = new ArrayList<>();
    private Usuario userRegistrar;

    private CuestionarioDAO cuestionarioDAO;

    /**
     * Constructor principal para el controlador de usuario, enfocado en la autenticación
     * y recuperación de contraseña.
     *
     * @param usuarioDAO El DAO para la gestión de usuarios.
     * @param loginView La vista de inicio de sesión.
     * @param idioma El manejador de internacionalización de mensajes.
     * @param cuestionarioDAO El DAO para la gestión de cuestionarios/preguntas de seguridad.
     * @param cuestionarioView La vista para que el usuario establezca preguntas de seguridad al registrarse.
     * @param cuestionarioRecuperarView La vista para recuperar contraseña usando preguntas de seguridad.
     */
    public UsuarioController(UsuarioDAO usuarioDAO, LoginView loginView, MensajeInternacionalizacionHandler idioma,
                             CuestionarioDAO cuestionarioDAO, CuestionarioView cuestionarioView, CuestionarioRecuperarView cuestionarioRecuperarView) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.idioma = idioma;
        this.cuestionarioDAO = cuestionarioDAO;
        this.cuestionarioView = cuestionarioView;
        this.cuestionarioRecuperarView = cuestionarioRecuperarView;
        this.usuario = null;
        this.registrarView = new RegistrarView(idioma);
        this.registrarView.cambiarIdioma(idioma);
        configurarEventosEnVistas();
        configurarEventosPreguntas();
        configurarEventosRespuestas();
    }

    /**
     * Constructor para el controlador de usuario, enfocado en la administración de usuarios (CRUD)
     * desde una interfaz de administrador.
     *
     * @param usuarioDAO El DAO para la gestión de usuarios.
     * @param usuarioCrearView La vista para crear nuevos usuarios.
     * @param usuarioListarView La vista para listar y buscar usuarios.
     * @param usuarioEliminarView La vista para eliminar usuarios.
     * @param usuarioModificarView La vista para modificar usuarios existentes.
     * @param idioma El manejador de internacionalización de mensajes.
     * @param registrarView La vista de registro (puede ser compartida o específica para admin).
     * @param cuestionarioView La vista de cuestionario de seguridad.
     * @param cuestionarioRecuperarView La vista de recuperación de contraseña por cuestionario.
     */
    public UsuarioController(UsuarioDAO usuarioDAO, UsuarioCrearView usuarioCrearView,
                             UsuarioListarView usuarioListarView, UsuarioEliminarView usuarioEliminarView,
                             UsuarioModificarView usuarioModificarView, MensajeInternacionalizacionHandler idioma, RegistrarView registrarView,
                             CuestionarioView cuestionarioView, CuestionarioRecuperarView cuestionarioRecuperarView) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioCrearView = usuarioCrearView;
        this.usuarioListarView = usuarioListarView;
        this.usuarioEliminarView = usuarioEliminarView;
        this.usuarioModificarView = usuarioModificarView;
        this.idioma = idioma;
        this.registrarView = registrarView;
        this.cuestionarioView = cuestionarioView;
        this.cuestionarioRecuperarView = cuestionarioRecuperarView;
        configurarEventosUsuarios();
    }

    /**
     * Establece el DAO de usuario para este controlador.
     * @param usuarioDAO El DAO de usuario a establecer.
     */
    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Establece el usuario actual en el controlador.
     * @param usuario El objeto Usuario a establecer.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el usuario actual gestionado por el controlador.
     * @return El objeto Usuario actual.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Configura los oyentes de eventos para los botones y componentes de la vista de login.
     */
    private void configurarEventosEnVistas() {
        loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());
        loginView.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginView.setVisible(false);
                registrarView.setVisible(true);
            }
        });
        loginView.getBtnSalir().addActionListener(e -> salir());
        loginView.getCbxIdiomas().addActionListener(e -> cambiarIdioma());
        loginView.getBtnBuscarArchivo().addActionListener(e -> {
        });
    }

    /**
     * Configura los oyentes de eventos para los botones y componentes de las vistas
     * de administración de usuarios (Crear, Listar, Eliminar, Modificar).
     */
    private void configurarEventosUsuarios() {
        usuarioCrearView.getBtnRegistrar().addActionListener(e -> registrarUsuario());
        usuarioListarView.getBtnBuscar().addActionListener(e -> buscarUsuario());
        usuarioListarView.getBtnListar().addActionListener(e -> listarUsuarios());
        usuarioEliminarView.getBtnBuscar().addActionListener(e -> buscarUsuarioParaEliminar());
        usuarioEliminarView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
        usuarioModificarView.getBtnBuscar().addActionListener(e -> buscarUsuarioParaModificar());
        usuarioModificarView.getBtnEditar().addActionListener(e -> modificarUsuario());
    }

    /**
     * Registra un nuevo usuario en el sistema. Obtiene los datos de la vista de creación de usuario,
     * realiza validaciones (cédula, contraseña, formato de celular, correo) y lo persiste a través del DAO.
     * Muestra mensajes de éxito o error en la vista.
     */
    private void registrarUsuario() {
        try {
            String nombreCompleto = usuarioCrearView.getTxtNombreCompleto().getText().trim();
            String cedula = usuarioCrearView.getTxtUsername().getText().trim();
            String contrasenia = usuarioCrearView.getTxtPassword().getText().trim();
            String celular = usuarioCrearView.getTxtCelular().getText().trim();
            String correo = usuarioCrearView.getTxtCorreo().getText().trim();
            Object dia = usuarioCrearView.getCbxDia().getSelectedItem();
            Object mes = usuarioCrearView.getCbxMes().getSelectedItem();
            Object año = usuarioCrearView.getCbxAño().getSelectedItem();

            if (nombreCompleto.isEmpty() || cedula.isEmpty() || contrasenia.isEmpty()
                    || celular.isEmpty() || correo.isEmpty() || dia == null || mes == null || año == null) {
                usuarioCrearView.mostrarMensaje(idioma.get("mensaje.campos.obligatorios"));
                return;
            }
            if (!celular.matches("\\d+")) {
                usuarioCrearView.mostrarMensaje(idioma.get("usuario.celular.invalido"));
                return;
            }
            if (!correo.matches("^[\\w.-]+@gmail\\.com$")) {
                usuarioCrearView.mostrarMensaje(idioma.get("mensaje.correo.invalido"));
                return;
            }

            try {
                Usuario.validarCedulaEcuatoriana(cedula);
            } catch (CedulaException cedulaEx) {
                usuarioCrearView.mostrarMensaje(idioma.get("error.cedula.invalida") + ": " + cedulaEx.getMessage());
                return;
            }
            try {
                Usuario.validarContrasenia(contrasenia);
            } catch (ContraseniaInvalidaException contraseniaEx) {
                usuarioCrearView.mostrarMensaje(idioma.get("error.contrasenia.invalida") + ": " + contraseniaEx.getMessage());
                return;
            }

            if (usuarioDAO.buscarPorUsuario(cedula) != null) {
                usuarioCrearView.mostrarMensaje(idioma.get("usuario.nombre.en.uso"));
                return;
            }

            String fechaNacimiento = dia + "/" + mes + "/" + año;
            Rol rol = usuarioCrearView.getRolSeleccionado();
            Usuario nuevoUsuario = new Usuario(cedula, contrasenia, rol);
            nuevoUsuario.setNombreCompleto(nombreCompleto);
            nuevoUsuario.setCelular(celular);
            nuevoUsuario.setCorreo(correo);
            nuevoUsuario.setFechaNacimiento(fechaNacimiento);

            usuarioDAO.crear(nuevoUsuario);
            usuarioCrearView.mostrarMensaje(idioma.get("usuario.creado") + ": " + cedula);
            usuarioCrearView.limpiarCampos();
        } catch (Exception ex) {
            usuarioCrearView.mostrarMensaje(idioma.get("error.registro.usuario") + ": " + ex.getMessage());
        }
    }

    /**
     * Busca un usuario por su nombre de usuario (cédula) y muestra sus datos en la tabla de la vista de listado.
     * Si no se encuentra el usuario, muestra un mensaje.
     */
    private void buscarUsuario() {
        usuarioListarView.getModelo().setRowCount(0);
        try {
            String username = usuarioListarView.getTxtUsuario().getText().trim();
            Usuario usuario = usuarioDAO.buscarPorUsuario(username);
            if (usuario != null) {
                Object[] fila = {
                        usuario.getNombreCompleto(), usuario.getCedula(), usuario.getContrasenia(),
                        usuario.getCorreo(), usuario.getCelular(), usuario.getFechaNacimiento(), usuario.getRol().toString()
                };
                usuarioListarView.getModelo().addRow(fila);
            } else {
                usuarioListarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
            }
        } catch (Exception ex) {
            usuarioListarView.mostrarMensaje(idioma.get("error.buscar.usuario") + ": " + ex.getMessage());
        }
    }

    /**
     * Lista todos los usuarios registrados en el sistema y los carga en la tabla de la vista de listado.
     * Muestra un mensaje de éxito o error.
     */
    private void listarUsuarios() {
        usuarioListarView.getModelo().setRowCount(0);
        try {
            for (Usuario usuario : usuarioDAO.listarTodos()) {
                Object[] fila = {
                        usuario.getNombreCompleto(), usuario.getCedula(), usuario.getContrasenia(),
                        usuario.getCorreo(), usuario.getCelular(), usuario.getFechaNacimiento(), usuario.getRol().toString()
                };
                usuarioListarView.getModelo().addRow(fila);
            }
            usuarioListarView.mostrarMensaje(idioma.get("usuario.listado.exito"));
        } catch (Exception ex) {
            usuarioListarView.mostrarMensaje(idioma.get("error.listar.usuarios") + ": " + ex.getMessage());
        }
    }

    /**
     * Busca un usuario por su nombre de usuario (cédula) para mostrarlo en la vista de eliminación.
     * Si lo encuentra, carga sus datos en la tabla; de lo contrario, muestra un mensaje de no encontrado.
     */
    private void buscarUsuarioParaEliminar() {
        usuarioEliminarView.getModelo().setRowCount(0);
        try {
            String username = usuarioEliminarView.getTxtUsuario().getText().trim();
            Usuario usuario = usuarioDAO.buscarPorUsuario(username);
            if (usuario != null) {
                Object[] fila = {
                        usuario.getNombreCompleto(), usuario.getCedula(), usuario.getContrasenia(),
                        usuario.getCorreo(), usuario.getCelular(), usuario.getFechaNacimiento(), usuario.getRol().toString()
                };
                usuarioEliminarView.getModelo().addRow(fila);
            } else {
                usuarioEliminarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
                usuarioEliminarView.getTxtUsuario().setText("");
            }
        } catch (Exception ex) {
            usuarioEliminarView.mostrarMensaje(idioma.get("error.buscar.usuario.eliminar") + ": " + ex.getMessage());
        }
    }

    /**
     * Elimina un usuario de la base de datos basándose en el nombre de usuario (cédula)
     * ingresado en la vista de eliminación. Muestra mensajes de éxito o error.
     */
    private void eliminarUsuario() {
        try {
            String username = usuarioEliminarView.getTxtUsuario().getText();
            Usuario usuario = usuarioDAO.buscarPorUsuario(username);
            if (usuario == null) {
                usuarioEliminarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
                return;
            }
            usuarioDAO.eliminar(username);
            usuarioEliminarView.mostrarMensaje(idioma.get("usuario.eliminado") + ": " + username);
            usuarioEliminarView.getTxtUsuario().setText("");
            usuarioEliminarView.getModelo().setRowCount(0);
        } catch (Exception ex) {
            usuarioEliminarView.mostrarMensaje(idioma.get("error.eliminar.usuario") + ": " + ex.getMessage());
        }
    }

    /**
     * Busca un usuario por su cédula para cargar sus datos en la vista de modificación.
     * Permite pre-llenar los campos de la vista con la información actual del usuario.
     */
    private void buscarUsuarioParaModificar() {
        try {
            String cedulaBusqueda = usuarioModificarView.getTxtName().getText().trim();
            Usuario usuario = usuarioDAO.buscarPorUsuario(cedulaBusqueda);
            if (usuario == null) {
                usuarioModificarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
                usuarioModificarView.getTxtName().setText("");
                return;
            }

            usuarioModificarView.getTxtUsername().setText(usuario.getCedula());
            usuarioModificarView.getTxtContrasenia().setText(usuario.getContrasenia());
            usuarioModificarView.getTxtNombreCompleto().setText(usuario.getNombreCompleto());
            usuarioModificarView.getTxtCorreo().setText(usuario.getCorreo());
            usuarioModificarView.getTxtCelular().setText(usuario.getCelular());

            String[] fecha = usuario.getFechaNacimiento().split("/");
            if (fecha.length == 3) {
                usuarioModificarView.getCbxDia().setSelectedItem(Integer.parseInt(fecha[0]));
                usuarioModificarView.getCbxMes().setSelectedItem(fecha[1]);
                usuarioModificarView.getCbxAño().setSelectedItem(Integer.parseInt(fecha[2]));
            }
        } catch (Exception ex) {
            usuarioModificarView.mostrarMensaje(idioma.get("error.buscar.usuario.modificar") + ": " + ex.getMessage());
        }
    }

    /**
     * Modifica los datos de un usuario existente. Obtiene la información actualizada de la vista,
     * realiza validaciones y persiste los cambios a través del DAO.
     * Muestra mensajes de éxito o error.
     */
    private void modificarUsuario() {
        try {
            String cedulaBusqueda = usuarioModificarView.getTxtName().getText().trim();
            Usuario usuario = usuarioDAO.buscarPorUsuario(cedulaBusqueda);
            if (usuario == null) {
                usuarioModificarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
                return;
            }

            String nuevaCedula = usuarioModificarView.getTxtUsername().getText().trim();
            String nuevaContrasenia = usuarioModificarView.getTxtContrasenia().getText().trim();
            String nombreCompleto = usuarioModificarView.getTxtNombreCompleto().getText().trim();
            String correo = usuarioModificarView.getTxtCorreo().getText().trim();
            String celular = usuarioModificarView.getTxtCelular().getText().trim();
            Object dia = usuarioModificarView.getCbxDia().getSelectedItem();
            Object mes = usuarioModificarView.getCbxMes().getSelectedItem();
            Object año = usuarioModificarView.getCbxAño().getSelectedItem();

            if (nuevaCedula.isEmpty() || nuevaContrasenia.isEmpty() || nombreCompleto.isEmpty()
                    || correo.isEmpty() || celular.isEmpty() || dia == null || mes == null || año == null) {
                usuarioModificarView.mostrarMensaje(idioma.get("mensaje.campos.obligatorios"));
                return;
            }
            if (!correo.matches("^[\\w.-]+@gmail\\.com$")) {
                usuarioModificarView.mostrarMensaje(idioma.get("mensaje.correo.invalido"));
                return;
            }
            System.out.println("Celular ingresado: [" + celular + "]");
            if (!celular.matches("\\d{10}")) {
                registrarView.mostrarMensaje("Debe contener exactamente 10 dígitos");
                return;
            }

            try {
                Usuario.validarCedulaEcuatoriana(nuevaCedula);
            } catch (CedulaException cedulaEx) {
                usuarioModificarView.mostrarMensaje(idioma.get("error.cedula.invalida") + ": " + cedulaEx.getMessage());
                return;
            }
            try {
                Usuario.validarContrasenia(nuevaContrasenia);
            } catch (ContraseniaInvalidaException contraseniaEx) {
                usuarioModificarView.mostrarMensaje(idioma.get("error.contrasenia.invalida") + ": " + contraseniaEx.getMessage());
                return;
            }

            if (!nuevaCedula.equals(usuario.getCedula()) && usuarioDAO.buscarPorUsuario(nuevaCedula) != null) {
                usuarioModificarView.mostrarMensaje(idioma.get("usuario.nombre.en.uso"));
                return;
            }

            String fechaNacimiento = dia + "/" + mes + "/" + año;
            usuario.setCedula(nuevaCedula);
            usuario.setContrasenia(nuevaContrasenia);
            usuario.setNombreCompleto(nombreCompleto);
            usuario.setCorreo(correo);
            usuario.setCelular(celular);
            usuario.setFechaNacimiento(fechaNacimiento);

            usuarioDAO.actualizar(usuario);
            usuarioModificarView.mostrarMensaje(idioma.get("usuario.modificado") + ": " + nuevaCedula);
            usuarioModificarView.limpiarCampos();
        } catch (Exception ex) {
            usuarioModificarView.mostrarMensaje(idioma.get("error.modificar.usuario") + ": " + ex.getMessage());
        }
    }

    /**
     * Configura los oyentes de eventos para los botones de registro y el cuestionario de seguridad.
     * Maneja el flujo de registro de un nuevo usuario, incluyendo la recopilación de datos
     * y la configuración de preguntas de seguridad.
     */
    private void configurarEventosPreguntas() {
        registrarView.getBtnRegistrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombreCompleto = registrarView.getTxtNombreCompleto().getText().trim();
                    String cedula = registrarView.getTxtUsuario().getText().trim();
                    String contrasenia = registrarView.getTxtContraseña().getText().trim();
                    String celular = registrarView.getTxtCelular().getText().trim();
                    String correo = registrarView.getTxtCorreo().getText().trim();
                    Object dia = registrarView.getCbxDia().getSelectedItem();
                    Object mes = registrarView.getCbxMes().getSelectedItem();
                    Object año = registrarView.getCbxAño().getSelectedItem();

                    if (nombreCompleto.isEmpty() || cedula.isEmpty() || contrasenia.isEmpty()
                            || celular.isEmpty() || correo.isEmpty() || dia == null || mes == null || año == null) {
                        registrarView.mostrarMensaje(idioma.get("mensaje.campos.obligatorios"));
                        return;
                    }

                    try {
                        Usuario.validarCedulaEcuatoriana(cedula);
                    } catch (CedulaException cedulaEx) {
                        registrarView.mostrarMensaje(idioma.get("error.cedula.invalida") + ": " + cedulaEx.getMessage());
                        return;
                    }
                    try {
                        Usuario.validarContrasenia(contrasenia);
                    } catch (ContraseniaInvalidaException contraseniaEx) {
                        registrarView.mostrarMensaje(idioma.get("error.contrasenia.invalida") + ": " + contraseniaEx.getMessage());
                        return;
                    }

                    if (!celular.matches("09\\d{8}")) {
                        registrarView.mostrarMensaje("Celular inválido. Debe empezar con 09 y tener 10 dígitos.");
                        return;
                    }

                    if (!correo.matches("^[\\w.-]+@gmail\\.com$")) {
                        registrarView.mostrarMensaje("Correo inválido. Debe ser @gmail.com.");
                        return;
                    }

                    if (usuarioDAO.buscarPorUsuario(cedula) != null) {
                        registrarView.mostrarMensaje(idioma.get("usuario.nombre.en.uso"));
                        return;
                    }

                    String fechaNacimiento = dia + "/" + mes + "/" + año;
                    userRegistrar = new Usuario(cedula, contrasenia, Rol.USUARIO, nombreCompleto, fechaNacimiento, celular, correo);

                    registrarView.setVisible(false);
                    cuestionarioView.setVisible(true);
                } catch (Exception ex) {
                    registrarView.mostrarMensaje(idioma.get("error.crear.usuario") + ": " + ex.getMessage());
                    userRegistrar = null;
                }
            }
        });
        cuestionarioView.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    obtenerRespuesta();
                    cuestionarioView.limpiarCampos();

                    if (preguntasRes.size() == 3) {
                        cuestionarioView.getBtnTerminar().setEnabled(true);
                    }
                } catch (Exception ex) {
                    cuestionarioView.mostrarMensaje(idioma.get("error.guardar.respuesta") + ": " + ex.getMessage());
                }
            }
        });
        cuestionarioView.getBtnTerminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (preguntasRes.size() < 3) {
                        cuestionarioView.mostrarMensaje(idioma.get("mensaje.minimo.tres.preguntas"));
                        return;
                    }

                    obtenerPregunta();

                    cuestionarioView.setVisible(false);
                    loginView.setVisible(true);
                    userRegistrar = null;
                    preguntasRes.clear();
                } catch (Exception ex) {
                    cuestionarioView.mostrarMensaje(idioma.get("error.terminar.cuestionario") + ": " + ex.getMessage());
                }
            }
        });

    }

    /**
     * Asocia las preguntas y respuestas recopiladas del cuestionario de seguridad
     * al usuario que se está registrando y lo persiste en el DAO.
     */
    public void obtenerPregunta() {
        try {
            if (userRegistrar != null) {
                userRegistrar.agregarPreguntas(preguntasRes);
                usuarioDAO.crear(userRegistrar);
            } else {
                System.err.println("ERROR (UsuarioController): userRegistrar es null al intentar guardar preguntas.");
                cuestionarioView.mostrarMensaje(idioma.get("error.usuario.no_disponible"));
            }
        } catch (Exception ex) {
            System.err.println("ERROR (UsuarioController): Error al obtener y guardar preguntas en el usuario: " + ex.getMessage());
            cuestionarioView.mostrarMensaje(idioma.get("error.guardar.preguntas") + ": " + ex.getMessage());
        }
    }

    /**
     * Obtiene una pregunta y su respuesta de la vista del cuestionario y la añade a la lista
     * de preguntas y respuestas del usuario. Realiza validaciones y muestra mensajes.
     */
    public void obtenerRespuesta() {
        try {
            Preguntas preguntas = (Preguntas) cuestionarioView.getCbxPreguntas().getSelectedItem();
            String respuestaTexto = cuestionarioView.getTxtRespuesta().getText();

            if (preguntas == null || respuestaTexto.isEmpty()) {
                cuestionarioView.mostrarMensaje(idioma.get("mensaje.campos.obligatorios"));
                return;
            }

            Respuesta respuesta = new Respuesta(respuestaTexto);
            PreguntasRespuestas preguntasRespuestas = new PreguntasRespuestas(preguntas, respuesta);
            preguntasRes.add(preguntasRespuestas);
            cuestionarioView.mostrarMensaje(idioma.get("mensaje.respuesta.guardada") + ": " + preguntasRespuestas.getRespuesta().getTexto());
        } catch (Exception ex) {
            cuestionarioView.mostrarMensaje(idioma.get("error.obtener.respuesta") + ": " + ex.getMessage());
        }
    }

    /**
     * Configura los oyentes de eventos para la funcionalidad de recuperación de contraseña.
     * Permite al usuario buscar su cuenta, responder una pregunta de seguridad y establecer una nueva contraseña.
     */
    private void configurarEventosRespuestas() {
        loginView.getBtnOlvidar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginView.setVisible(false);
                cuestionarioRecuperarView.setVisible(true);
            }
        });
        cuestionarioRecuperarView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = cuestionarioRecuperarView.getTxtUsuario().getText().trim();

                    if (username.isEmpty()) {
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.usuario.vacio"));
                        return;
                    }

                    Usuario usuarioEncontrado = usuarioDAO.buscarPorUsuario(username);

                    if (usuarioEncontrado == null) {
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
                        return;
                    }

                    List<PreguntasRespuestas> preguntasUsuario = usuarioEncontrado.getPreguntasRespuestas();

                    if (preguntasUsuario == null || preguntasUsuario.isEmpty()) {
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.preguntas.no.registradas"));
                        return;
                    }

                    JComboBox<Preguntas> cbx = cuestionarioRecuperarView.getCbxPreguntas();
                    cbx.removeAllItems();

                    for (PreguntasRespuestas pr : preguntasUsuario) {
                        cbx.addItem(pr.getPreguntas());
                    }

                    usuario = usuarioEncontrado;
                    cuestionarioRecuperarView.getBtnEnviar().setEnabled(true);
                } catch (Exception ex) {
                    cuestionarioRecuperarView.mostrarMensaje(idioma.get("error.buscar.usuario.recuperar") + ": " + ex.getMessage());
                }
            }
        });
        cuestionarioRecuperarView.getBtnEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Preguntas preguntaSeleccionada = (Preguntas) cuestionarioRecuperarView.getCbxPreguntas().getSelectedItem();
                    String respuestaIngresada = cuestionarioRecuperarView.getTxtRespuesta1().getText().trim();

                    if (preguntaSeleccionada == null || respuestaIngresada.isEmpty()) {
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.campos.obligatorios"));
                        return;
                    }

                    if (usuario == null) {
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
                        return;
                    }

                    boolean esCorrecta = false;
                    for (PreguntasRespuestas pr : usuario.getPreguntasRespuestas()) {
                        if (pr.getPreguntas().getPreguntas().equals(preguntaSeleccionada.getPreguntas()) &&
                                pr.getRespuesta().getTexto().equalsIgnoreCase(respuestaIngresada)) {
                            esCorrecta = true;
                            break;
                        }
                    }

                    if (!esCorrecta) {
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.respuesta.incorrecta"));
                        return;
                    }

                    JPasswordField campoContraseña = new JPasswordField();
                    int opcion = JOptionPane.showConfirmDialog(
                            cuestionarioRecuperarView,
                            campoContraseña,
                            idioma.get("mensaje.contrasena.ingresar"),
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (opcion == JOptionPane.OK_OPTION) {
                        String nuevaContrasenia = new String(campoContraseña.getPassword()).trim();

                        if (nuevaContrasenia.isEmpty()) {
                            cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.contrasena.invalida"));
                            return;
                        }

                        try {
                            Usuario.validarContrasenia(nuevaContrasenia);
                        } catch (ContraseniaInvalidaException contraseniaEx) {
                            cuestionarioRecuperarView.mostrarMensaje(idioma.get("error.contrasenia.invalida") + ": " + contraseniaEx.getMessage());
                            return;
                        }

                        usuario.setContrasenia(nuevaContrasenia);
                        usuarioDAO.actualizar(usuario);
                        cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.contrasena.actualizada"));
                        cuestionarioRecuperarView.setVisible(false);
                        loginView.setVisible(true);
                    }
                } catch (Exception ex) {
                    cuestionarioRecuperarView.mostrarMensaje(idioma.get("error.verificar.respuesta") + ": " + ex.getMessage());
                }
            }
        });
    }

    /**
     * Cambia el idioma de la aplicación basándose en la selección del usuario.
     * Actualiza los textos de todas las vistas relevantes y recarga las preguntas
     * del cuestionario si el DAO es de tipo {@link CuestionarioDAOMemoria}.
     */
    private void cambiarIdioma() {
        String seleccion = (String) loginView.getCbxIdiomas().getSelectedItem();
        if (seleccion != null) {
            switch (seleccion) {
                case "Español": idioma.setLenguaje("es", "EC"); break;
                case "English": idioma.setLenguaje("en", "US"); break;
                case "Français": idioma.setLenguaje("fr", "FR"); break;
            }

            loginView.actualizarTextos(idioma);
            registrarView.cambiarIdioma(idioma);
            cuestionarioView.actualizarTextos(idioma);
            cuestionarioRecuperarView.actualizarTextos(idioma);

            if (cuestionarioDAO instanceof CuestionarioDAOMemoria) {
                ((CuestionarioDAOMemoria) cuestionarioDAO).actualizarIdioma(idioma);
            }
            cuestionarioView.cargarPreguntas();
        }
    }

    /**
     * Sale de la aplicación.
     */
    private void salir() {
        loginView.dispose();
        System.exit(0);
    }

    /**
     * Autentica al usuario usando el nombre de usuario (cédula) y la contraseña ingresados en la vista de login.
     * Si la autenticación es exitosa, cierra la vista de login; de lo contrario, muestra un mensaje de error.
     */
    private void autenticar() {
        try {
            String username = loginView.getTxtUsername().getText().trim();
            String contrasenia = String.valueOf(loginView.getTxtContraseña().getPassword()).trim();

            usuario = usuarioDAO.autenticar(username, contrasenia);
            if (usuario == null) {
                loginView.mostrarMensaje(idioma.get("login.mensaje.usuario_o_contrasena_incorrectos"));
            } else {
                loginView.dispose();
            }
        } catch (Exception ex) {
            loginView.mostrarMensaje(idioma.get("error.autenticacion") + ": " + ex.getMessage());
        }
    }
}