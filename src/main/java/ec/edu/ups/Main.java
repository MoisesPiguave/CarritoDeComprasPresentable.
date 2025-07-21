package ec.edu.ups;

import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.dao.impl.CuestionarioDAOMemoria;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.dao.impl.CarritoDAOArchivo;
import ec.edu.ups.dao.impl.CuestionarioDAOArchivo;
import ec.edu.ups.dao.impl.ProductoDAOArchivo;
import ec.edu.ups.dao.impl.UsuarioDAOArchivo;


import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.*;
import ec.edu.ups.vista.AdministradorView.CuestionarioRecuperarView;
import ec.edu.ups.vista.AdministradorView.CuestionarioView;
import ec.edu.ups.vista.AdministradorView.LoginView;
import ec.edu.ups.vista.AdministradorView.RegistrarView;
import ec.edu.ups.vista.CarritoView.CarritoAnadirView;
import ec.edu.ups.vista.CarritoView.CarritoEliminarView;
import ec.edu.ups.vista.CarritoView.CarritoListarView;
import ec.edu.ups.vista.CarritoView.CarritoModificarView;
import ec.edu.ups.vista.ProductoView.ProductoActualizarView;
import ec.edu.ups.vista.ProductoView.ProductoAnadirView;
import ec.edu.ups.vista.ProductoView.ProductoEliminarView;
import ec.edu.ups.vista.ProductoView.ProductoListaView;
import ec.edu.ups.vista.UsuarioView.UsuarioCrearView;
import ec.edu.ups.vista.UsuarioView.UsuarioEliminarView;
import ec.edu.ups.vista.UsuarioView.UsuarioListarView;
import ec.edu.ups.vista.UsuarioView.UsuarioModificarView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Clase principal que inicia la aplicación del sistema de carrito de compras.
 * Gestiona la inicialización de las vistas, los controladores y las implementaciones DAO
 * (tanto en memoria como en archivo), basándose en la selección del usuario en la pantalla de login.
 * También maneja la autenticación de usuarios y el cambio de idioma global de la aplicación.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class Main {
    /**
     * Método principal de la aplicación.
     * Inicializa el manejador de internacionalización, la vista de login,
     * y configura el flujo de autenticación y la selección del tipo de almacenamiento de datos.
     * Una vez que el usuario se autentica, se instancian y configuran las vistas y controladores
     * principales de la aplicación con la implementación de persistencia elegida (memoria o archivo).
     *
     * @param args Argumentos de la línea de comandos (no se utilizan en esta aplicación).
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            MensajeInternacionalizacionHandler idioma = new MensajeInternacionalizacionHandler("es", "EC");

            // DAOs temporales (en memoria) para el proceso de login inicial.
            // Siempre comenzaremos con DAOs de memoria para el registro/login.
            CuestionarioDAO tempCuestionarioDAO = new CuestionarioDAOMemoria(idioma);
            UsuarioDAO tempUsuarioDAO = new UsuarioDAOMemoria(tempCuestionarioDAO);

            // ÚNICAS INSTANCIAS DE VISTAS DE LOGIN/REGISTRO/CUESTIONARIO
            // Estas instancias se crean una sola vez al inicio.
            LoginView loginView = new LoginView(idioma);
            RegistrarView registrarView = new RegistrarView(idioma);
            CuestionarioView cuestionarioView = new CuestionarioView(idioma, tempCuestionarioDAO);
            CuestionarioRecuperarView cuestionarioRecuperarView = new CuestionarioRecuperarView(idioma);

            // El UsuarioController principal se inicializa con estas únicas instancias.
            UsuarioController usuarioController = new UsuarioController(tempUsuarioDAO, loginView, idioma, tempCuestionarioDAO, cuestionarioView, cuestionarioRecuperarView);
            usuarioController.setRegistrarView(registrarView); // Aseguramos que el controlador tenga la referencia a registrarView

            loginView.setVisible(true); // Muestra la ventana de login al inicio

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario usuarioAutenticado = usuarioController.getUsuario();

                    if (usuarioAutenticado != null) {
                        String tipoAlmacenamientoSeleccionado = (String) loginView.getCbxTipoAlmacenamiento().getSelectedItem();
                        String rutaArchivoSeleccionada = loginView.getTxtRutaArchivo().getText();

                        ProductoDAO actualProductoDAO;
                        CarritoDAO actualCarritoDAO;
                        CuestionarioDAO actualCuestionarioDAO;
                        UsuarioDAO actualUsuarioDAO;

                        // Lógica para decidir qué DAOs instanciar: Memoria o Archivo
                        if (tipoAlmacenamientoSeleccionado.equals(idioma.get("login.opcion_archivo"))) {
                            System.out.println("DEBUG (Main): Almacenamiento seleccionado: Archivo en " + rutaArchivoSeleccionada);
                            File storageDirectory = new File(rutaArchivoSeleccionada);
                            if (!storageDirectory.exists()) {
                                storageDirectory.mkdirs();
                            }

                            actualCuestionarioDAO = new CuestionarioDAOArchivo(storageDirectory.getAbsolutePath(), idioma);
                            actualUsuarioDAO = new UsuarioDAOArchivo(storageDirectory.getAbsolutePath(), actualCuestionarioDAO);
                            actualProductoDAO = new ProductoDAOArchivo(storageDirectory.getAbsolutePath());
                            actualCarritoDAO = new CarritoDAOArchivo(storageDirectory.getAbsolutePath());

                        } else { // Almacenamiento en Memoria
                            System.out.println("DEBUG (Main): Almacenamiento seleccionado: Memoria.");
                            actualCuestionarioDAO = new CuestionarioDAOMemoria(idioma);
                            actualUsuarioDAO = new UsuarioDAOMemoria(actualCuestionarioDAO);
                            actualProductoDAO = new ProductoDAOMemoria();
                            actualCarritoDAO = new CarritoDAOMemoria();
                        }

                        // **** PASO CRÍTICO: ACTUALIZAR EL DAO EN LAS VISTAS Y EN EL CONTROLADOR ****
                        // Las vistas de cuestionario ya existen, solo hay que actualizarles el DAO.
                        cuestionarioView.setCuestionarioDAO(actualCuestionarioDAO);
                        // El cuestionarioRecuperarView no necesita el DAO directamente, lo usa el controlador.

                        // El UsuarioController principal necesita los DAOs actualizados.
                        usuarioController.setUsuarioDAO(actualUsuarioDAO);
                        usuarioController.setCuestionarioDAO(actualCuestionarioDAO); // También el DAO del cuestionario
                        // Las vistas de registro/cuestionario ya fueron pasadas al constructor del controlador,
                        // no es necesario volver a llamarlos con setCuestionarioView, setRegistrarView, etc.
                        // El controlador ya tiene las referencias correctas.

                        // Vistas CRUD de Usuario (estas sí se crean/inyectan después de la autenticación)
                        UsuarioCrearView usuarioCrearView = new UsuarioCrearView(idioma);
                        UsuarioListarView usuarioListarView = new UsuarioListarView(idioma);
                        UsuarioEliminarView usuarioEliminarView = new UsuarioEliminarView(idioma);
                        UsuarioModificarView usuarioModificarView = new UsuarioModificarView(idioma);
                        // RegistrarView ya fue creada al inicio y pasada al controlador.

                        // Inyectar las vistas CRUD al UsuarioController
                        usuarioController.setUsuarioCrearView(usuarioCrearView);
                        usuarioController.setUsuarioListarView(usuarioListarView);
                        usuarioController.setUsuarioEliminarView(usuarioEliminarView);
                        usuarioController.setUsuarioModificarView(usuarioModificarView);


                        // Instanciar todas las demás vistas de la aplicación principal
                        MenuPrincipalView principalView = new MenuPrincipalView(idioma);
                        ProductoAnadirView productoAnadirView = new ProductoAnadirView(idioma);
                        ProductoListaView productoListaView = new ProductoListaView(idioma);
                        ProductoActualizarView productoActualizarView = new ProductoActualizarView(idioma);
                        ProductoEliminarView productoEliminarView = new ProductoEliminarView(idioma);

                        CarritoAnadirView carritoAnadirView = new CarritoAnadirView(idioma);
                        CarritoListarView carritoListarView = new CarritoListarView(idioma);
                        CarritoModificarView carritoModificarView = new CarritoModificarView(idioma);
                        CarritoEliminarView carritoEliminarView = new CarritoEliminarView(idioma);

                        // Añadir TODAS las vistas (incluyendo las de cuestionario y registro) al JDesktopPane
                        principalView.getjDesktopPane().add(productoAnadirView);
                        principalView.getjDesktopPane().add(productoListaView);
                        principalView.getjDesktopPane().add(productoActualizarView);
                        principalView.getjDesktopPane().add(productoEliminarView);

                        principalView.getjDesktopPane().add(carritoAnadirView);
                        principalView.getjDesktopPane().add(carritoListarView);
                        principalView.getjDesktopPane().add(carritoModificarView);
                        principalView.getjDesktopPane().add(carritoEliminarView);

                        principalView.getjDesktopPane().add(usuarioCrearView);
                        principalView.getjDesktopPane().add(usuarioListarView);
                        principalView.getjDesktopPane().add(usuarioEliminarView);
                        principalView.getjDesktopPane().add(usuarioModificarView);

                        principalView.getjDesktopPane().add(registrarView); // Asegura que RegistrarView esté en el desktop
                        principalView.getjDesktopPane().add(cuestionarioView); // Asegura que CuestionarioView esté en el desktop
                        principalView.getjDesktopPane().add(cuestionarioRecuperarView); // Asegura que CuestionarioRecuperarView esté en el desktop


                        // Instanciar otros controladores con los DAOs que el usuario seleccionó
                        ProductoController productoController = new ProductoController(actualProductoDAO, productoAnadirView, productoListaView, carritoAnadirView, productoEliminarView, productoActualizarView, idioma);
                        CarritoController carritoController = new CarritoController(actualCarritoDAO, carritoAnadirView, actualProductoDAO, carritoListarView, usuarioAutenticado, carritoModificarView, carritoEliminarView, idioma);

                        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getCedula());

                        if (usuarioAutenticado.getRol().equals(Rol.ADMINISTRADOR)) {
                            // Los menús de administrador están habilitados por defecto.
                        } else if (usuarioAutenticado.getRol().equals(Rol.USUARIO)) {
                            principalView.deshabilitarMenusAdministrador();
                        }

                        principalView.setVisible(true);

                        // Configuración de ActionListeners para el menú principal
                        principalView.getMenuItemCrearProducto().addActionListener(e1 -> {
                            if (!productoAnadirView.isVisible()) {
                                productoAnadirView.setVisible(true);
                            }
                            productoAnadirView.toFront();
                            productoAnadirView.requestFocus();
                        });
                        principalView.getMenuItemBuscarProducto().addActionListener(e1 -> {
                            if (!productoListaView.isVisible()) {
                                productoListaView.setVisible(true);
                                productoController.listarProductos();
                            }
                            productoListaView.toFront();
                            productoListaView.requestFocus();
                        });
                        principalView.getMenuItemCrearCarrito().addActionListener(e1 -> {
                            if (!carritoAnadirView.isVisible()) {
                                carritoAnadirView.setVisible(true);
                            }
                            carritoAnadirView.toFront();
                            carritoAnadirView.requestFocus();
                        });
                        principalView.getMenuItemEliminarProducto().addActionListener(e1 -> {
                            if (!productoEliminarView.isVisible()) {
                                productoEliminarView.setVisible(true);
                            }
                            productoEliminarView.toFront();
                            productoEliminarView.requestFocus();
                        });
                        principalView.getMenuItemActualizarProducto().addActionListener(e1 -> {
                            if (!productoActualizarView.isVisible()) {
                                productoActualizarView.setVisible(true);
                            }
                            productoActualizarView.toFront();
                            productoActualizarView.requestFocus();
                        });
                        principalView.getMenuItemBuscarCarrito().addActionListener(e1 -> {
                            if (!carritoListarView.isVisible()) {
                                carritoListarView.setVisible(true);
                                carritoController.listarCarritos();
                            }
                            carritoListarView.toFront();
                            carritoListarView.requestFocus();
                        });
                        principalView.getMenuItemCerrarSesion().addActionListener(e1 -> {
                            boolean confirmado = principalView.mostrarMensajePregunta(idioma.get("login.main_cerrarSesion"));
                            if (confirmado) {
                                principalView.dispose();
                                loginView.limpiarCampos();
                                loginView.setVisible(true);

                                // Al cerrar sesión, reinicializamos los DAOs temporales
                                // y los asignamos de nuevo al controlador y a las vistas de cuestionario
                                CuestionarioDAO newTempCuestionarioDAO = new CuestionarioDAOMemoria(idioma);
                                UsuarioDAO newTempUsuarioDAO = new UsuarioDAOMemoria(newTempCuestionarioDAO);

                                usuarioController.setUsuarioDAO(newTempUsuarioDAO);
                                usuarioController.setCuestionarioDAO(newTempCuestionarioDAO);
                                usuarioController.setUsuario(null);

                                cuestionarioView.setCuestionarioDAO(newTempCuestionarioDAO); // Actualiza el DAO de la CuestionarioView
                                cuestionarioView.limpiarCampos(); // Limpia los campos del cuestionario si quedó abierto

                                // Asegúrate de que las vistas de administración también se limpien si es necesario
                                usuarioCrearView.limpiarCampos();
                                usuarioListarView.getModelo().setRowCount(0); // Limpia la tabla
                                usuarioEliminarView.getTxtUsuario().setText("");
                                usuarioEliminarView.getModelo().setRowCount(0);
                                usuarioModificarView.limpiarCampos();
                            }
                        });
                        principalView.getMenuItemCrearUsuario().addActionListener(e1 -> {
                            if (!usuarioCrearView.isVisible()) {
                                usuarioCrearView.setVisible(true);
                            }
                            usuarioCrearView.toFront();
                            usuarioCrearView.requestFocus();
                        });
                        principalView.getMenuItemListarUsuario().addActionListener(e1 -> {
                            if (!usuarioListarView.isVisible()) {
                                usuarioListarView.setVisible(true);
                                usuarioController.listarUsuarios();
                            }
                            usuarioListarView.toFront();
                            usuarioListarView.requestFocus();
                        });
                        principalView.getMenuItemEliminarUsuario().addActionListener(e1 -> {
                            if (!usuarioEliminarView.isVisible()) {
                                usuarioEliminarView.setVisible(true);
                            }
                            usuarioEliminarView.toFront();
                            usuarioEliminarView.requestFocus();
                        });
                        principalView.getMenuItemModificarCarrito().addActionListener(e1 -> {
                            if (!carritoModificarView.isVisible()) {
                                carritoModificarView.setVisible(true);
                            }
                            carritoModificarView.toFront();
                            carritoModificarView.requestFocus();
                        });
                        principalView.getMenuItemEliminarCarrito().addActionListener(e1 -> {
                            if (!carritoEliminarView.isVisible()) {
                                carritoEliminarView.setVisible(true);
                            }
                            carritoEliminarView.toFront();
                            carritoEliminarView.requestFocus();
                        });
                        principalView.getMenuItemSalir().addActionListener(e1 -> {
                            boolean confirmado = principalView.mostrarMensajePregunta(idioma.get("login.main_salir"));
                            if (confirmado) {
                                principalView.dispose();
                                System.exit(0);
                            }
                        });
                        principalView.getMenuItemActualizarUsuario().addActionListener(e1 -> {
                            if (!usuarioModificarView.isVisible()) {
                                usuarioModificarView.setVisible(true);
                            }
                            usuarioModificarView.toFront();
                            usuarioModificarView.requestFocus();
                        });
                        principalView.getMenuItemEspanol().addActionListener(e1 -> {
                            idioma.setLenguaje("es", "EC");
                            principalView.cambiarIdioma();
                            carritoAnadirView.cambiarIdioma();
                            carritoEliminarView.cambiarIdioma();
                            carritoListarView.cambiarIdioma();
                            carritoModificarView.cambiarIdioma();
                            productoActualizarView.cambiarIdioma();
                            productoAnadirView.cambiarIdioma();
                            productoEliminarView.cambiarIdioma();
                            productoListaView.cambiarIdioma();
                            usuarioModificarView.cambiarIdioma(idioma);
                            usuarioEliminarView.cambiarIdioma(idioma);
                            usuarioCrearView.cambiarIdioma(idioma);
                            usuarioListarView.cambiarIdioma(idioma);
                            loginView.actualizarTextos(idioma);
                            registrarView.cambiarIdioma(idioma);
                            cuestionarioView.actualizarTextos(idioma);
                            cuestionarioRecuperarView.actualizarTextos(idioma);
                            if (actualCuestionarioDAO instanceof CuestionarioDAOArchivo) {
                                ((CuestionarioDAOArchivo) actualCuestionarioDAO).actualizarIdioma(idioma);
                            } else if (actualCuestionarioDAO instanceof CuestionarioDAOMemoria) {
                                ((CuestionarioDAOMemoria) actualCuestionarioDAO).actualizarIdioma(idioma);
                            }
                            cuestionarioView.cargarPreguntas();
                        });
                        principalView.getMenuItemIngles().addActionListener(e1 -> {
                            idioma.setLenguaje("en", "US");
                            principalView.cambiarIdioma();
                            carritoAnadirView.cambiarIdioma();
                            carritoEliminarView.cambiarIdioma();
                            carritoListarView.cambiarIdioma();
                            carritoModificarView.cambiarIdioma();
                            productoActualizarView.cambiarIdioma();
                            productoAnadirView.cambiarIdioma();
                            productoEliminarView.cambiarIdioma();
                            productoListaView.cambiarIdioma();
                            usuarioModificarView.cambiarIdioma(idioma);
                            usuarioEliminarView.cambiarIdioma(idioma);
                            usuarioCrearView.cambiarIdioma(idioma);
                            usuarioListarView.cambiarIdioma(idioma);
                            loginView.actualizarTextos(idioma);
                            registrarView.cambiarIdioma(idioma);
                            cuestionarioView.actualizarTextos(idioma);
                            cuestionarioRecuperarView.actualizarTextos(idioma);
                            if (actualCuestionarioDAO instanceof CuestionarioDAOArchivo) {
                                ((CuestionarioDAOArchivo) actualCuestionarioDAO).actualizarIdioma(idioma);
                            } else if (actualCuestionarioDAO instanceof CuestionarioDAOMemoria) {
                                ((CuestionarioDAOMemoria) actualCuestionarioDAO).actualizarIdioma(idioma);
                            }
                            cuestionarioView.cargarPreguntas();
                        });
                        principalView.getMenuItemFrances().addActionListener(e1 -> {
                            idioma.setLenguaje("fr", "FR");
                            principalView.cambiarIdioma();
                            carritoAnadirView.cambiarIdioma();
                            carritoEliminarView.cambiarIdioma();
                            carritoListarView.cambiarIdioma();
                            carritoModificarView.cambiarIdioma();
                            productoActualizarView.cambiarIdioma();
                            productoAnadirView.cambiarIdioma();
                            productoEliminarView.cambiarIdioma();
                            productoListaView.cambiarIdioma();
                            usuarioModificarView.cambiarIdioma(idioma);
                            usuarioEliminarView.cambiarIdioma(idioma);
                            usuarioCrearView.cambiarIdioma(idioma);
                            usuarioListarView.cambiarIdioma(idioma);
                            loginView.actualizarTextos(idioma);
                            registrarView.cambiarIdioma(idioma);
                            cuestionarioView.actualizarTextos(idioma);
                            cuestionarioRecuperarView.actualizarTextos(idioma);
                            if (actualCuestionarioDAO instanceof CuestionarioDAOArchivo) {
                                ((CuestionarioDAOArchivo) actualCuestionarioDAO).actualizarIdioma(idioma);
                            } else if (actualCuestionarioDAO instanceof CuestionarioDAOMemoria) {
                                ((CuestionarioDAOMemoria) actualCuestionarioDAO).actualizarIdioma(idioma);
                            }
                            cuestionarioView.cargarPreguntas();
                        });
                    }
                }
            });
        });
    }
}