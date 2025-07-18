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

            // DAOs iniciales/temporales. Estos se usarán para el login y el registro inicial.
            // Serán reemplazados por los DAOs de archivo si el usuario selecciona esa opción.
            CuestionarioDAO initialCuestionarioDAO = new CuestionarioDAOMemoria(idioma);
            UsuarioDAO initialUsuarioDAO = new UsuarioDAOMemoria(initialCuestionarioDAO);

            LoginView loginView = new LoginView(idioma);
            loginView.setVisible(true);

            // Vistas de Cuestionario y Recuperación de Contraseña, inicializadas con el DAO temporal.
            CuestionarioView cuestionarioView = new CuestionarioView(idioma, initialCuestionarioDAO);
            CuestionarioRecuperarView cuestionarioRecuperarView = new CuestionarioRecuperarView(idioma);

            // El `usuarioController` principal, inicializado con los DAOs iniciales.
            // Sus DAOs internos se actualizarán cuando el usuario elija el tipo de almacenamiento.
            UsuarioController usuarioController = new UsuarioController(initialUsuarioDAO, loginView, idioma, initialCuestionarioDAO, cuestionarioView, cuestionarioRecuperarView);

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
                                storageDirectory.mkdirs(); // Crear directorios si no existen
                            }

                            // ¡Instanciar los DAOs de ARCHIVO!
                            actualCuestionarioDAO = new CuestionarioDAOArchivo(storageDirectory.getAbsolutePath(), idioma);
                            actualUsuarioDAO = new UsuarioDAOArchivo(storageDirectory.getAbsolutePath(), actualCuestionarioDAO);
                            actualProductoDAO = new ProductoDAOArchivo(storageDirectory.getAbsolutePath());
                            actualCarritoDAO = new CarritoDAOArchivo(storageDirectory.getAbsolutePath());

                        } else { // Si es "Memoria" (u otra opción por defecto)
                            System.out.println("DEBUG (Main): Almacenamiento seleccionado: Memoria.");
                            actualCuestionarioDAO = new CuestionarioDAOMemoria(idioma);
                            actualUsuarioDAO = new UsuarioDAOMemoria(actualCuestionarioDAO);
                            actualProductoDAO = new ProductoDAOMemoria();
                            actualCarritoDAO = new CarritoDAOMemoria();
                        }

                        // Actualizar el UsuarioController principal con el DAO de usuario seleccionado
                        usuarioController.setUsuarioDAO(actualUsuarioDAO);
                        // Re-instanciar CuestionarioView y CuestionarioRecuperarView con el DAO de Cuestionario correcto
                        CuestionarioView finalCuestionarioView = new CuestionarioView(idioma, actualCuestionarioDAO);
                        CuestionarioRecuperarView finalCuestionarioRecuperarView = new CuestionarioRecuperarView(idioma);


                        // Instanciar todas las vistas de la aplicación principal
                        MenuPrincipalView principalView = new MenuPrincipalView(idioma);
                        ProductoAnadirView productoAnadirView = new ProductoAnadirView(idioma);
                        ProductoListaView productoListaView = new ProductoListaView(idioma);
                        ProductoActualizarView productoActualizarView = new ProductoActualizarView(idioma);
                        ProductoEliminarView productoEliminarView = new ProductoEliminarView(idioma);

                        CarritoAnadirView carritoAnadirView = new CarritoAnadirView(idioma);
                        CarritoListarView carritoListarView = new CarritoListarView(idioma);
                        CarritoModificarView carritoModificarView = new CarritoModificarView(idioma);
                        CarritoEliminarView carritoEliminarView = new CarritoEliminarView(idioma);

                        UsuarioCrearView usuarioCrearView = new UsuarioCrearView(idioma);
                        UsuarioListarView usuarioListarView = new UsuarioListarView(idioma);
                        UsuarioEliminarView usuarioEliminarView = new UsuarioEliminarView(idioma);
                        UsuarioModificarView usuarioModificarView = new UsuarioModificarView(idioma);

                        RegistrarView registrarView = new RegistrarView(idioma);

                        // Añadir vistas al panel de escritorio
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

                        // Instanciar controladores con los DAOs que el usuario seleccionó
                        ProductoController productoController = new ProductoController(actualProductoDAO, productoAnadirView, productoListaView, carritoAnadirView, productoEliminarView, productoActualizarView, idioma);
                        CarritoController carritoController = new CarritoController(actualCarritoDAO, carritoAnadirView, actualProductoDAO, carritoListarView, usuarioAutenticado, carritoModificarView, carritoEliminarView, idioma);
                        // Pasar las vistas de Cuestionario que usan el DAO de Cuestionario correcto
                        UsuarioController usuarioController2 = new UsuarioController(actualUsuarioDAO, usuarioCrearView, usuarioListarView, usuarioEliminarView, usuarioModificarView, idioma, registrarView, finalCuestionarioView, finalCuestionarioRecuperarView);

                        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getCedula());

                        if (usuarioAutenticado.getRol().equals(Rol.ADMINISTRADOR)) {
                            // Los menús de administrador están habilitados por defecto, no necesitas hacer nada aquí.
                            // Si deseas habilitar menús específicos para ADMINISTRADOR, asegúrate de que estén deshabilitados al inicio
                            // y luego los habilitas aquí.
                        } else if (usuarioAutenticado.getRol().equals(Rol.USUARIO)) {
                            principalView.deshabilitarMenusAdministrador();
                        }

                        principalView.setVisible(true);

                        // Configuración de los ActionListeners para los elementos del menú principal
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
                                // Restablecer el `usuarioController` principal a su estado inicial (memoria) para el próximo login
                                usuarioController.setUsuarioDAO(new UsuarioDAOMemoria(new CuestionarioDAOMemoria(idioma)));
                                usuarioController.setUsuario(null);
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
                            usuarioModificarView.cambiarIdioma();
                            usuarioEliminarView.cambiarIdioma();
                            usuarioCrearView.cambiarIdioma();
                            usuarioListarView.cambiarIdioma();
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
                            usuarioModificarView.cambiarIdioma();
                            usuarioEliminarView.cambiarIdioma();
                            usuarioCrearView.cambiarIdioma();
                            usuarioListarView.cambiarIdioma();
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
                            usuarioModificarView.cambiarIdioma();
                            usuarioEliminarView.cambiarIdioma();
                            usuarioCrearView.cambiarIdioma();
                            usuarioListarView.cambiarIdioma();
                        });
                    }
                }
            });
        });
    }
}