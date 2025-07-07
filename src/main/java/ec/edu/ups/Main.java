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

    public class Main {
        public static void main(String[] args) {
            java.awt.EventQueue.invokeLater(() -> {
                MensajeInternacionalizacionHandler idioma = new MensajeInternacionalizacionHandler("es", "EC");
                ProductoDAO productoDAO = new ProductoDAOMemoria();
                CarritoDAO carritoDAO = new CarritoDAOMemoria();
                CuestionarioDAO cuestionarioDAO = new CuestionarioDAOMemoria(idioma);
                UsuarioDAO usuarioDAO = new UsuarioDAOMemoria(cuestionarioDAO);


                LoginView loginView = new LoginView(idioma);
                loginView.setVisible(true);
                CuestionarioView cuestionarioView = new CuestionarioView(idioma, cuestionarioDAO);
                CuestionarioRecuperarView cuestionarioRecuperarView = new CuestionarioRecuperarView(idioma);


                UsuarioController usuarioController = new UsuarioController(usuarioDAO, loginView, idioma, cuestionarioDAO, cuestionarioView, cuestionarioRecuperarView);

                loginView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Usuario usuarioAutenticado = usuarioController.usuario;

                        if (usuarioAutenticado != null) {
                            // Instanciar vistas
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


                            ProductoController productoController = new ProductoController(productoDAO, productoAnadirView, productoListaView, carritoAnadirView, productoEliminarView, productoActualizarView, idioma);
                            CarritoController carritoController = new CarritoController(carritoDAO, carritoAnadirView, productoDAO, carritoListarView, usuarioAutenticado, carritoModificarView, carritoEliminarView, idioma);
                            UsuarioController usuarioController2 = new UsuarioController(usuarioDAO, usuarioCrearView, usuarioListarView, usuarioEliminarView, usuarioModificarView, idioma, registrarView);

                            principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getUsuario());

                            if (usuarioAutenticado.getRol().equals(Rol.USUARIO)) {
                                principalView.deshabilitarMenusAdministrador();
                            }

                            principalView.setVisible(true);
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
