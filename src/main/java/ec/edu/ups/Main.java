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
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.*;
import ec.edu.ups.vista.AdministracionView.CuestionarioRecuperarView;
import ec.edu.ups.vista.AdministracionView.CuestionarioView;
import ec.edu.ups.vista.AdministracionView.LoginView;
import ec.edu.ups.vista.AdministracionView.RegistrarView;
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

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {

            // Internacionalización
            MensajeInternacionalizacionHandler mi = new MensajeInternacionalizacionHandler("es", "EC");

            // DAOs
            ProductoDAO productoDAO = new ProductoDAOMemoria();
            CarritoDAO carritoDAO = new CarritoDAOMemoria();
            CuestionarioDAO cuestionarioDAO = new CuestionarioDAOMemoria(mi);
            UsuarioDAO usuarioDAO = new UsuarioDAOMemoria(cuestionarioDAO);

            // Vistas de login y recuperación
            LoginView loginView = new LoginView(mi);
            loginView.setVisible(true);
            CuestionarioView cuestionarioView = new CuestionarioView(mi, cuestionarioDAO);
            CuestionarioRecuperarView cuestionarioRecuperarView = new CuestionarioRecuperarView(mi);

            // Controlador de usuario para login
            UsuarioController usuarioController = new UsuarioController(usuarioDAO, loginView, mi, cuestionarioDAO, cuestionarioView, cuestionarioRecuperarView);

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario usuarioAutenticado = usuarioController.usuario; // usuario autenticado

                    if (usuarioAutenticado != null) {
                        // Instanciar vistas
                        MenuPrincipalView principalView = new MenuPrincipalView(mi);
                        ProductoAnadirView productoAnadirView = new ProductoAnadirView(mi);
                        ProductoListaView productoListaView = new ProductoListaView(mi);
                        ProductoActualizarView productoActualizarView = new ProductoActualizarView(mi);
                        ProductoEliminarView productoEliminarView = new ProductoEliminarView(mi);

                        CarritoAnadirView carritoAnadirView = new CarritoAnadirView(mi);
                        CarritoListarView carritoListarView = new CarritoListarView(mi);
                        CarritoModificarView carritoModificarView = new CarritoModificarView(mi);
                        CarritoEliminarView carritoEliminarView = new CarritoEliminarView(mi);

                        UsuarioCrearView usuarioCrearView = new UsuarioCrearView(mi);
                        UsuarioListarView usuarioListarView = new UsuarioListarView(mi);
                        UsuarioEliminarView usuarioEliminarView = new UsuarioEliminarView(mi);
                        UsuarioModificarView usuarioModificarView = new UsuarioModificarView(mi);

                        RegistrarView registrarView = new RegistrarView(mi);

                        // Agregar todos los InternalFrames al jDesktopPane ANTES de mostrarlos
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

                        // Controladores
                        ProductoController productoController = new ProductoController(productoDAO, productoAnadirView, productoListaView, carritoAnadirView, productoEliminarView, productoActualizarView, mi);
                        CarritoController carritoController = new CarritoController(carritoDAO, carritoAnadirView, productoDAO, carritoListarView, usuarioAutenticado, carritoModificarView, carritoEliminarView, mi);
                        UsuarioController usuarioController2 = new UsuarioController(usuarioDAO, usuarioCrearView, usuarioListarView, usuarioEliminarView, usuarioModificarView, mi, registrarView);

                        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getUsuario());

                        if (usuarioAutenticado.getRol().equals(Rol.USUARIO)) {
                            principalView.deshabilitarMenusAdministrador();
                        }

                        principalView.setVisible(true);

                        // Listeners para menú: solo mostrar y traer al frente las ventanas ya agregadas
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
                            boolean confirmado = principalView.mostrarMensajePregunta(mi.get("login.main_cerrarSesion"));
                            if (confirmado) {
                                principalView.dispose();
                                loginView.limpiarCampos();
                                loginView.setVisible(true);
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
                            boolean confirmado = principalView.mostrarMensajePregunta(mi.get("login.main_salir"));
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
                            mi.setLenguaje("es", "EC");
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
                            mi.setLenguaje("en", "US");
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
                            mi.setLenguaje("fr", "FR");
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
