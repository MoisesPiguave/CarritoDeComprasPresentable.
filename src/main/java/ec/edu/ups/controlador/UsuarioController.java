package ec.edu.ups.controlador;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CuestionarioDAOMemoria;
import ec.edu.ups.modelo.*;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.AdministradorView.*;
import ec.edu.ups.vista.UsuarioView.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {

    public Usuario usuario;
    private final UsuarioDAO usuarioDAO;
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

    public UsuarioController(UsuarioDAO usuarioDAO, UsuarioCrearView usuarioCrearView,
                             UsuarioListarView usuarioListarView, UsuarioEliminarView usuarioEliminarView,
                             UsuarioModificarView usuarioModificarView, MensajeInternacionalizacionHandler idioma, RegistrarView registrarView) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioCrearView = usuarioCrearView;
        this.usuarioListarView = usuarioListarView;
        this.usuarioEliminarView = usuarioEliminarView;
        this.usuarioModificarView = usuarioModificarView;
        this.idioma = idioma;
        this.registrarView = registrarView;
        configurarEventosUsuarios();
    }

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
    }

    private void configurarEventosUsuarios() {
        usuarioCrearView.getBtnRegistrar().addActionListener(e -> registrarUsuario());
        usuarioListarView.getBtnBuscar().addActionListener(e -> buscarUsuario());
        usuarioListarView.getBtnListar().addActionListener(e -> listarUsuarios());
        usuarioEliminarView.getBtnBuscar().addActionListener(e -> buscarUsuarioParaEliminar());
        usuarioEliminarView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
        usuarioModificarView.getBtnBuscar().addActionListener(e -> buscarUsuarioParaModificar());
        usuarioModificarView.getBtnEditar().addActionListener(e -> modificarUsuario());
    }

    private void registrarUsuario() {
        String nombreCompleto = usuarioCrearView.getTxtNombreCompleto().getText().trim();
        String username = usuarioCrearView.getTxtUsername().getText().trim();
        String contrasenia = usuarioCrearView.getTxtPassword().getText().trim();
        String celular = usuarioCrearView.getTxtCelular().getText().trim();
        String correo = usuarioCrearView.getTxtCorreo().getText().trim();
        Object dia = usuarioCrearView.getCbxDia().getSelectedItem();
        Object mes = usuarioCrearView.getCbxMes().getSelectedItem();
        Object año = usuarioCrearView.getCbxAño().getSelectedItem();

        if (nombreCompleto.isEmpty() || username.isEmpty() || contrasenia.isEmpty()
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
        if (usuarioDAO.buscarPorUsuario(username) != null) {
            usuarioCrearView.mostrarMensaje(idioma.get("usuario.nombre.en.uso"));
            return;
        }

        String fechaNacimiento = dia + "/" + mes + "/" + año;
        Rol rol = usuarioCrearView.getRolSeleccionado();
        Usuario nuevoUsuario = new Usuario(username, contrasenia, rol);
        nuevoUsuario.setNombreCompleto(nombreCompleto);
        nuevoUsuario.setCelular(celular);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setFechaNacimiento(fechaNacimiento);

        usuarioDAO.crear(nuevoUsuario);
        usuarioCrearView.mostrarMensaje(idioma.get("usuario.creado") + ": " + username);
        usuarioCrearView.limpiarCampos();
    }

    private void buscarUsuario() {
        usuarioListarView.getModelo().setRowCount(0);
        String username = usuarioListarView.getTxtUsuario().getText().trim();
        Usuario usuario = usuarioDAO.buscarPorUsuario(username);
        if (usuario != null) {
            Object[] fila = {
                    usuario.getNombreCompleto(), usuario.getUsuario(), usuario.getContrasenia(),
                    usuario.getCorreo(), usuario.getCelular(), usuario.getFechaNacimiento(), usuario.getRol().toString()
            };
            usuarioListarView.getModelo().addRow(fila);
        }
    }

    private void listarUsuarios() {
        usuarioListarView.getModelo().setRowCount(0);
        for (Usuario usuario : usuarioDAO.listarTodos()) {
            Object[] fila = {
                    usuario.getNombreCompleto(), usuario.getUsuario(), usuario.getContrasenia(),
                    usuario.getCorreo(), usuario.getCelular(), usuario.getFechaNacimiento(), usuario.getRol().toString()
            };
            usuarioListarView.getModelo().addRow(fila);
        }
        usuarioListarView.mostrarMensaje(idioma.get("usuario.listado.exito"));
    }

    private void buscarUsuarioParaEliminar() {
        usuarioEliminarView.getModelo().setRowCount(0);
        String username = usuarioEliminarView.getTxtUsuario().getText().trim();
        Usuario usuario = usuarioDAO.buscarPorUsuario(username);
        if (usuario != null) {
            Object[] fila = {
                    usuario.getNombreCompleto(), usuario.getUsuario(), usuario.getContrasenia(),
                    usuario.getCorreo(), usuario.getCelular(), usuario.getFechaNacimiento(), usuario.getRol().toString()
            };
            usuarioEliminarView.getModelo().addRow(fila);
        } else {
            usuarioEliminarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
            usuarioEliminarView.getTxtUsuario().setText("");
        }
    }

    private void eliminarUsuario() {
        String username = usuarioEliminarView.getTxtUsuario().getText();
        Usuario usuario = usuarioDAO.buscarPorUsuario(username);
        if (usuario == null) {
            usuarioEliminarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
            return;
        }
        usuarioDAO.eliminar(username);
        usuarioEliminarView.mostrarMensaje(idioma.get("usuario.eliminado") + ": " + username);
        usuarioEliminarView.getTxtUsuario().setText("");
    }

    private void buscarUsuarioParaModificar() {
        String usernameBusqueda = usuarioModificarView.getTxtName().getText().trim();
        Usuario usuario = usuarioDAO.buscarPorUsuario(usernameBusqueda);
        if (usuario == null) {
            usuarioModificarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
            usuarioModificarView.getTxtName().setText("");
            return;
        }

        usuarioModificarView.getTxtUsername().setText(usuario.getUsuario());
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
    }

    private void modificarUsuario() {
        String nombreBusqueda = usuarioModificarView.getTxtName().getText().trim();
        Usuario usuario = usuarioDAO.buscarPorUsuario(nombreBusqueda);
        if (usuario == null) {
            usuarioModificarView.mostrarMensaje(idioma.get("usuario.no.encontrado"));
            return;
        }

        String username = usuarioModificarView.getTxtUsername().getText().trim();
        String contrasenia = usuarioModificarView.getTxtContrasenia().getText().trim();
        String nombreCompleto = usuarioModificarView.getTxtNombreCompleto().getText().trim();
        String correo = usuarioModificarView.getTxtCorreo().getText().trim();
        String celular = usuarioModificarView.getTxtCelular().getText().trim();
        Object dia = usuarioModificarView.getCbxDia().getSelectedItem();
        Object mes = usuarioModificarView.getCbxMes().getSelectedItem();
        Object año = usuarioModificarView.getCbxAño().getSelectedItem();

        if (username.isEmpty() || contrasenia.isEmpty() || nombreCompleto.isEmpty()
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
        String fechaNacimiento = dia + "/" + mes + "/" + año;
        usuario.setUsuario(username);
        usuario.setContrasenia(contrasenia);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setCorreo(correo);
        usuario.setCelular(celular);
        usuario.setFechaNacimiento(fechaNacimiento);

        usuarioDAO.actualizar(usuario);
        usuarioModificarView.mostrarMensaje(idioma.get("usuario.modificado") + ": " + username);
        usuarioModificarView.limpiarCampos();
    }

    private void configurarEventosPreguntas() {
        registrarView.getBtnRegistrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
                registrarView.setVisible(false);
                cuestionarioView.setVisible(true);
                System.out.println("llego hasta aqui");
            }
        });
        cuestionarioView.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obtenerRespuesta();
                cuestionarioView.limpiarCampos();

                if (preguntasRes.size() == 3) {
                    cuestionarioView.getBtnTerminar().setEnabled(true);
                }
            }
        });
        cuestionarioView.getBtnTerminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (preguntasRes.size() < 3) {
                    cuestionarioView.mostrarMensaje(idioma.get("mensaje.minimo.tres.preguntas"));
                    return;
                }

                obtenerPregunta(); // Guarda las preguntas en el usuario
                cuestionarioView.setVisible(false);
                loginView.setVisible(true);
            }
        });

    }

    public void obtenerPregunta() {
        if (userRegistrar != null) {
            userRegistrar.agregarPreguntas(preguntasRes);
            usuarioDAO.actualizar(userRegistrar);
        }
    }

    public void obtenerRespuesta() {
        Preguntas preguntas = (Preguntas) cuestionarioView.getCbxPreguntas().getSelectedItem();
        Respuesta respuesta = new Respuesta(cuestionarioView.getTxtRespuesta().getText());
        PreguntasRespuestas preguntasRespuestas = new PreguntasRespuestas(preguntas, respuesta);
        preguntasRes.add(preguntasRespuestas);
        cuestionarioView.mostrarMensaje(idioma.get("mensaje.respuesta.guardada") + ": " + preguntasRespuestas.getRespuesta());
    }

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
            }
        });
        cuestionarioRecuperarView.getBtnEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                    usuario.setContrasenia(nuevaContrasenia);
                    usuarioDAO.actualizar(usuario);
                    cuestionarioRecuperarView.mostrarMensaje(idioma.get("mensaje.contrasena.actualizada"));
                    cuestionarioRecuperarView.setVisible(false);
                    loginView.setVisible(true);
                }
            }
        });
    }

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

    private void salir() {
        loginView.dispose();
        System.exit(0);
    }

    private void autenticar() {
        String username = loginView.getTxtUsername().getText().trim();
        String contrasenia = loginView.getTxtContraseña().getText().trim();

        usuario = usuarioDAO.autenticar(username, contrasenia);
        if (usuario == null) {
            loginView.mostrarMensaje(idioma.get("login.mensaje.usuario_o_contrasena_incorrectos"));
        } else {
            loginView.dispose();
        }
    }

    private void crearUsuario() {
        String nombreCompleto = registrarView.getTxtNombreCompleto().getText().trim();
        String username = registrarView.getTxtUsuario().getText().trim();
        String contrasenia = registrarView.getTxtContraseña().getText().trim();
        String celular = registrarView.getTxtCelular().getText().trim();
        String correo = registrarView.getTxtCorreo().getText().trim();
        Object dia = registrarView.getCbxDia().getSelectedItem();
        Object mes = registrarView.getCbxMes().getSelectedItem();
        Object año = registrarView.getCbxAño().getSelectedItem();

        System.out.println("DEBUG -> Datos recibidos:");
        System.out.println("Nombre: " + nombreCompleto);
        System.out.println("Usuario: " + username);
        System.out.println("Contraseña: " + contrasenia);
        System.out.println("Celular: [" + celular + "]");
        System.out.println("Correo: " + correo);

        if (nombreCompleto.isEmpty() || username.isEmpty() || contrasenia.isEmpty()
                || celular.isEmpty() || correo.isEmpty() || dia == null || mes == null || año == null) {
            registrarView.mostrarMensaje(idioma.get("mensaje.campos.obligatorios"));
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

        if (usuarioDAO.buscarPorUsuario(username) != null) {
            registrarView.mostrarMensaje(idioma.get("usuario.nombre.en.uso"));
            return;
        }

        String fechaNacimiento = dia + "/" + mes + "/" + año;
        userRegistrar = new Usuario(username, contrasenia, Rol.USUARIO, nombreCompleto, fechaNacimiento, celular, correo);
        usuarioDAO.crear(userRegistrar);

        System.out.println("DEBUG -> Usuario creado correctamente: " + userRegistrar);

        registrarView.mostrarMensaje(idioma.get("usuario.creado"));
    }

}