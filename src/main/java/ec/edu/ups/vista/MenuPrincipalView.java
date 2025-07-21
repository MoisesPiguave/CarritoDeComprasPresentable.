package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.net.URL;

/**
 * Vista principal (JFrame) de la aplicación, que actúa como la interfaz principal
 * después del inicio de sesión. Contiene una barra de menú con opciones para
 * gestionar productos, carritos, usuarios, cambiar el idioma y salir de la aplicación.
 * Utiliza un {@link JDesktopPane} como panel de contenido para albergar ventanas internas.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class MenuPrincipalView extends JFrame {
    private JMenuBar menuBar;

    private JMenu menuProducto;
    private JMenu menuCarrito;
    private JMenu menuUsuario;
    private JMenu menuIdioma;
    private JMenu menuSalir;

    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemActualizarProducto;
    private JMenuItem menuItemBuscarProducto;

    private JMenuItem menuItemCrearCarrito;
    private JMenuItem menuItemBuscarCarrito;
    private JMenuItem menuItemModificarCarrito;
    private JMenuItem menuItemEliminarCarrito;

    private JMenuItem menuItemCrearUsuario;
    private JMenuItem menuItemListarUsuario;
    private JMenuItem menuItemActualizarUsuario;
    private JMenuItem menuItemEliminarUsuario;

    private JMenuItem menuItemEspanol;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemFrances;

    private JMenuItem menuItemCerrarSesion;
    private JMenuItem menuItemSalir;

    // CAMBIO CLAVE AQUÍ: Usamos JDesktopPane estándar
    private JDesktopPane jDesktopPane;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la vista MenuPrincipalView.
     * Inicializa la barra de menú y sus componentes (menús y elementos de menú),
     * los añade a la jerarquía, y configura las propiedades básicas del JFrame.
     * También carga los textos y los iconos de los menús y elementos.
     *
     * @param idioma El {@link MensajeInternacionalizacionHandler} para gestionar los textos de la interfaz.
     */
    public MenuPrincipalView( MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;

        // CAMBIO CLAVE AQUÍ: Inicializamos con el JDesktopPane estándar
        jDesktopPane = new JDesktopPane();
        menuBar = new JMenuBar();

        menuProducto = new JMenu("Producto");
        menuCarrito = new JMenu("Carrito");
        menuUsuario = new JMenu("Usuario");
        menuIdioma = new JMenu("Idioma");
        menuSalir = new JMenu("Salir");

        menuItemCrearProducto = new JMenuItem("Crear Producto");
        menuItemEliminarProducto = new JMenuItem("Eliminar Producto");
        menuItemActualizarProducto = new JMenuItem("Actualizar Producto");
        menuItemBuscarProducto = new JMenuItem("Buscar Producto");

        menuItemCrearCarrito = new JMenuItem("Crear Carrito");
        menuItemBuscarCarrito = new JMenuItem("Buscar Carrito");
        menuItemModificarCarrito = new JMenuItem("Actualizar Carrito");
        menuItemEliminarCarrito = new JMenuItem("Eliminar Carrito");

        menuItemCrearUsuario = new JMenuItem("Crear Usuario");
        menuItemListarUsuario = new JMenuItem("Buscar Usuario");
        menuItemActualizarUsuario = new JMenuItem("Actualizar Usuario");
        menuItemEliminarUsuario = new JMenuItem("Eliminar Usuario");

        menuItemEspanol = new JMenuItem("Español");
        menuItemIngles = new JMenuItem("Inglés");
        menuItemFrances = new JMenuItem("Francés");

        menuItemCerrarSesion = new JMenuItem("Cerrar Sesión");
        menuItemSalir = new JMenuItem("Salir");

        menuBar.add(menuProducto);
        menuBar.add(menuCarrito);
        menuBar.add(menuUsuario);
        menuBar.add(menuIdioma);
        menuBar.add(menuSalir);

        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);

        menuCarrito.add(menuItemCrearCarrito);
        menuCarrito.add(menuItemBuscarCarrito);
        menuCarrito.add(menuItemModificarCarrito);
        menuCarrito.add(menuItemEliminarCarrito);

        menuUsuario.add(menuItemCrearUsuario);
        menuUsuario.add(menuItemListarUsuario);
        menuUsuario.add(menuItemActualizarUsuario);
        menuUsuario.add(menuItemEliminarUsuario);

        menuIdioma.add(menuItemEspanol);
        menuIdioma.add(menuItemIngles);
        menuIdioma.add(menuItemFrances);

        menuSalir.add(menuItemCerrarSesion);
        menuSalir.add(menuItemSalir);

        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Carrito de Compras En Línea");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        cambiarIdioma();
    }

    /**
     * Obtiene el JMenuItem para "Crear Producto".
     * @return El {@link JMenuItem} de crear producto.
     */
    public JMenuItem getMenuItemCrearProducto() {
        return menuItemCrearProducto;
    }

    /**
     * Establece el JMenuItem para "Crear Producto".
     * @param menuItemCrearProducto El {@link JMenuItem} a establecer.
     */
    public void setMenuItemCrearProducto(JMenuItem menuItemCrearProducto) {
        this.menuItemCrearProducto = menuItemCrearProducto;
    }

    /**
     * Obtiene el JMenuItem para "Eliminar Producto".
     * @return El {@link JMenuItem} de eliminar producto.
     */
    public JMenuItem getMenuItemEliminarProducto() {
        return menuItemEliminarProducto;
    }

    /**
     * Establece el JMenuItem para "Eliminar Producto".
     * @param menuItemEliminarProducto El {@link JMenuItem} a establecer.
     */
    public void setMenuItemEliminarProducto(JMenuItem menuItemEliminarProducto) {
        this.menuItemEliminarProducto = menuItemEliminarProducto;
    }

    /**
     * Obtiene el JMenuItem para "Actualizar Producto".
     * @return El {@link JMenuItem} de actualizar producto.
     */
    public JMenuItem getMenuItemActualizarProducto() {
        return menuItemActualizarProducto;
    }

    /**
     * Establece el JMenuItem para "Actualizar Producto".
     * @param menuItemActualizarProducto El {@link JMenuItem} a establecer.
     */
    public void setMenuItemActualizarProducto(JMenuItem menuItemActualizarProducto) {
        this.menuItemActualizarProducto = menuItemActualizarProducto;
    }

    /**
     * Obtiene el JMenuItem para "Buscar Producto".
     * @return El {@link JMenuItem} de buscar producto.
     */
    public JMenuItem getMenuItemBuscarProducto() {
        return menuItemBuscarProducto;
    }

    /**
     * Establece el JMenuItem para "Buscar Producto".
     * @param menuItemBuscarProducto El {@link JMenuItem} a establecer.
     */
    public void setMenuItemBuscarProducto(JMenuItem menuItemBuscarProducto) {
        this.menuItemBuscarProducto = menuItemBuscarProducto;
    }

    /**
     * Obtiene el JMenu "Producto".
     * @return El {@link JMenu} de producto.
     */
    public JMenu getMenuProducto() {
        return menuProducto;
    }

    /**
     * Establece el JMenu "Producto".
     * @param menuProducto El {@link JMenu} a establecer.
     */
    public void setMenuProducto(JMenu menuProducto) {
        this.menuProducto = menuProducto;
    }

    /**
     * Obtiene el JMenu "Carrito".
     * @return El {@link JMenu} de carrito.
     */
    public JMenu getMenuCarrito() {
        return menuCarrito;
    }

    /**
     * Establece el JMenu "Carrito".
     * @param menuCarrito El {@link JMenu} a establecer.
     */
    public void setMenuCarrito(JMenu menuCarrito) {
        this.menuCarrito = menuCarrito;
    }

    /**
     * Obtiene el JMenuItem para "Crear Carrito".
     * @return El {@link JMenuItem} de crear carrito.
     */
    public JMenuItem getMenuItemCrearCarrito() {
        return menuItemCrearCarrito;
    }

    /**
     * Establece el JMenuItem para "Crear Carrito".
     * @param menuItemCrearCarrito El {@link JMenuItem} a establecer.
     */
    public void setMenuItemCrearCarrito(JMenuItem menuItemCrearCarrito) {
        this.menuItemCrearCarrito = menuItemCrearCarrito;
    }

    /**
     * Obtiene el JDesktopPane donde se mostrarán las ventanas internas.
     * @return El {@link JDesktopPane}.
     */
    // ELIMINA LA LÍNEA DE ABAJO: @Override
    public JDesktopPane getjDesktopPane() {
        return jDesktopPane;
    }

    /**
     * Muestra un mensaje en un cuadro de diálogo emergente.
     * @param mensaje El texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Obtiene el JMenuItem para "Buscar Carrito".
     * @return El {@link JMenuItem} de buscar carrito.
     */
    public JMenuItem getMenuItemBuscarCarrito() {
        return menuItemBuscarCarrito;
    }

    /**
     * Establece el JMenuItem para "Buscar Carrito".
     * @param menuItemBuscarCarrito El {@link JMenuItem} a establecer.
     */
    public void setMenuItemBuscarCarrito(JMenuItem menuItemBuscarCarrito) {
        this.menuItemBuscarCarrito = menuItemBuscarCarrito;
    }

    /**
     * Obtiene el JMenu "Usuario".
     * @return El {@link JMenu} de usuario.
     */
    public JMenu getMenuUsuario() {
        return menuUsuario;
    }

    /**
     * Establece el JMenu "Usuario".
     * @param menuUsuario El {@link JMenu} a establecer.
     */
    public void setMenuUsuario(JMenu menuUsuario) {
        this.menuUsuario = menuUsuario;
    }

    /**
     * Obtiene el JMenu "Salir".
     * @return El {@link JMenu} de salir.
     */
    public JMenu getMenuSalir() {
        return menuSalir;
    }

    /**
     * Establece el JMenu "Salir".
     * @param menuSalir El {@link JMenu} a establecer.
     */
    public void setMenuSalir(JMenu menuSalir) {
        this.menuSalir = menuSalir;
    }

    /**
     * Obtiene el JMenuItem para "Crear Usuario".
     * @return El {@link JMenuItem} de crear usuario.
     */
    public JMenuItem getMenuItemCrearUsuario() {
        return menuItemCrearUsuario;
    }

    /**
     * Establece el JMenuItem para "Crear Usuario".
     * @param menuItemCrearUsuario El {@link JMenuItem} a establecer.
     */
    public void setMenuItemCrearUsuario(JMenuItem menuItemCrearUsuario) {
        this.menuItemCrearUsuario = menuItemCrearUsuario;
    }

    /**
     * Obtiene el JMenuItem para "Listar/Buscar Usuario".
     * @return El {@link JMenuItem} de listar/buscar usuario.
     */
    public JMenuItem getMenuItemListarUsuario() {
        return menuItemListarUsuario;
    }

    /**
     * Establece el JMenuItem para "Listar/Buscar Usuario".
     * @param menuItemBuscarUsuario El {@link JMenuItem} a establecer.
     */
    public void setMenuItemListarUsuario(JMenuItem menuItemBuscarUsuario) {
        this.menuItemListarUsuario = menuItemBuscarUsuario;
    }

    /**
     * Obtiene el JMenuItem para "Actualizar Usuario".
     * @return El {@link JMenuItem} de actualizar usuario.
     */
    public JMenuItem getMenuItemActualizarUsuario() {
        return menuItemActualizarUsuario;
    }

    /**
     * Establece el JMenuItem para "Actualizar Usuario".
     * @param menuItemActualizarUsuario El {@link JMenuItem} a establecer.
     */
    public void setMenuItemActualizarUsuario(JMenuItem menuItemActualizarUsuario) {
        this.menuItemActualizarUsuario = menuItemActualizarUsuario;
    }

    /**
     * Obtiene el JMenuItem para "Eliminar Usuario".
     * @return El {@link JMenuItem} de eliminar usuario.
     */
    public JMenuItem getMenuItemEliminarUsuario() {
        return menuItemEliminarUsuario;
    }

    /**
     * Establece el JMenuItem para "Eliminar Usuario".
     * @param menuItemEliminarUsuario El {@link JMenuItem} a establecer.
     */
    public void setMenuItemEliminarUsuario(JMenuItem menuItemEliminarUsuario) {
        this.menuItemEliminarUsuario = menuItemEliminarUsuario;
    }

    /**
     * Obtiene el JMenuItem para "Cerrar Sesión".
     * @return El {@link JMenuItem} de cerrar sesión.
     */
    public JMenuItem getMenuItemCerrarSesion() {
        return menuItemCerrarSesion;
    }

    /**
     * Establece el JMenuItem para "Cerrar Sesión".
     * @param menuItemCerrarSesion El {@link JMenuItem} a establecer.
     */
    public void setMenuItemCerrarSesion(JMenuItem menuItemCerrarSesion) {
        this.menuItemCerrarSesion = menuItemCerrarSesion;
    }

    /**
     * Obtiene el JMenuItem para "Modificar Carrito".
     * @return El {@link JMenuItem} de modificar carrito.
     */
    public JMenuItem getMenuItemModificarCarrito() {
        return menuItemModificarCarrito;
    }

    /**
     * Establece el JMenuItem para "Modificar Carrito".
     * @param menuItemModificarCarrito El {@link JMenuItem} a establecer.
     */
    public void setMenuItemModificarCarrito(JMenuItem menuItemModificarCarrito) {
        this.menuItemModificarCarrito = menuItemModificarCarrito;
    }

    /**
     * Obtiene el JMenuItem para "Eliminar Carrito".
     * @return El {@link JMenuItem} de eliminar carrito.
     */
    public JMenuItem getMenuItemEliminarCarrito() {
        return menuItemEliminarCarrito;
    }

    /**
     * Establece el JMenuItem para "Eliminar Carrito".
     * @param menuItemEliminarCarrito El {@link JMenuItem} a establecer.
     */
    public void setMenuItemEliminarCarrito(JMenuItem menuItemEliminarCarrito) {
        this.menuItemEliminarCarrito = menuItemEliminarCarrito;
    }

    /**
     * Obtiene el JMenuItem para "Salir" (de la aplicación).
     * @return El {@link JMenuItem} de salir.
     */
    public JMenuItem getMenuItemSalir() {
        return menuItemSalir;
    }

    /**
     * Establece el JMenuItem para "Salir".
     * @param menuItemSalir El {@link JMenuItem} a establecer.
     */
    public void setMenuItemSalir(JMenuItem menuItemSalir) {
        this.menuItemSalir = menuItemSalir;
    }

    /**
     * Obtiene el JMenu "Idioma".
     * @return El {@link JMenu} de idioma.
     */
    public JMenu getMenuIdioma() {
        return menuIdioma;
    }

    /**
     * Establece el JMenu "Idioma".
     * @param menuIdioma El {@link JMenu} a establecer.
     */
    public void setMenuIdioma(JMenu menuIdioma) {
        this.menuIdioma = menuIdioma;
    }

    /**
     * Obtiene el JMenuItem para seleccionar el idioma "Español".
     * @return El {@link JMenuItem} para español.
     */
    public JMenuItem getMenuItemEspanol() {
        return menuItemEspanol;
    }

    /**
     * Establece el JMenuItem para el idioma "Español".
     * @param menuItemEspanol El {@link JMenuItem} a establecer.
     */
    public void setMenuItemEspanol(JMenuItem menuItemEspanol) {
        this.menuItemEspanol = menuItemEspanol;
    }

    /**
     * Obtiene el JMenuItem para seleccionar el idioma "Inglés".
     * @return El {@link JMenuItem} para inglés.
     */
    public JMenuItem getMenuItemIngles() {
        return menuItemIngles;
    }

    /**
     * Establece el JMenuItem para el idioma "Inglés".
     * @param menuItemIngles El {@link JMenuItem} a establecer.
     */
    public void setMenuItemIngles(JMenuItem menuItemIngles) {
        this.menuItemIngles = menuItemIngles;
    }

    /**
     * Obtiene el JMenuItem para seleccionar el idioma "Francés".
     * @return El {@link JMenuItem} para francés.
     */
    public JMenuItem getMenuItemFrances() {
        return menuItemFrances;
    }

    /**
     * Establece el JMenuItem para el idioma "Francés".
     * @param menuItemFrances El {@link JMenuItem} a establecer.
     */
    public void setMenuItemFrances(JMenuItem menuItemFrances) {
        this.menuItemFrances = menuItemFrances;
    }

    /**
     * Muestra un mensaje de pregunta al usuario con opciones de "Sí" y "No".
     *
     * @param mensaje El texto de la pregunta a mostrar.
     * @return {@code true} si el usuario selecciona "Sí", {@code false} si selecciona "No".
     */
    public boolean mostrarMensajePregunta(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    /**
     * Deshabilita los elementos de menú que solo son accesibles para un rol de administrador.
     * Esto se utiliza cuando un usuario con un rol no administrativo inicia sesión.
     */
    public void deshabilitarMenusAdministrador() {
        getMenuItemCrearProducto().setEnabled(false);
        getMenuItemBuscarProducto().setEnabled(false);
        getMenuItemActualizarProducto().setEnabled(false);
        getMenuItemEliminarProducto().setEnabled(false);
        getMenuItemCrearUsuario().setEnabled(false);
        getMenuItemListarUsuario().setEnabled(false);
        getMenuItemActualizarUsuario().setEnabled(false);
        getMenuItemEliminarUsuario().setEnabled(false);
    }

    /**
     * Actualiza todos los textos de los componentes de la barra de menú para reflejar el idioma actual
     * gestionado por el {@link MensajeInternacionalizacionHandler}.
     * También carga y establece los iconos para todos los menús y elementos de menú.
     */
    public void cambiarIdioma() {
        if (idioma == null) return;

        menuProducto.setText(idioma.get("menu.producto"));
        menuCarrito.setText(idioma.get("menu.carrito"));
        menuUsuario.setText(idioma.get("menu.usuario"));
        menuIdioma.setText(idioma.get("menu.idioma"));
        menuSalir.setText(idioma.get("menu.salir"));
        menuItemCrearProducto.setText(idioma.get("menu.producto.crear"));
        menuItemEliminarProducto.setText(idioma.get("menu.producto.eliminar"));
        menuItemActualizarProducto.setText(idioma.get("menu.producto.actualizar"));
        menuItemBuscarProducto.setText(idioma.get("menu.producto.buscar"));
        menuItemCrearCarrito.setText(idioma.get("menu.carrito.crear"));
        menuItemBuscarCarrito.setText(idioma.get("menu.carrito.buscar"));
        menuItemModificarCarrito.setText(idioma.get("menu.carrito.actualizar"));
        menuItemEliminarCarrito.setText(idioma.get("menu.carrito.eliminar"));
        menuItemCrearUsuario.setText(idioma.get("menu.usuario.crear"));
        menuItemListarUsuario.setText(idioma.get("menu.usuario.buscar"));
        menuItemActualizarUsuario.setText(idioma.get("menu.usuario.actualizar"));
        menuItemEliminarUsuario.setText(idioma.get("menu.usuario.eliminar"));
        menuItemEspanol.setText(idioma.get("Español"));
        menuItemIngles.setText(idioma.get("Inglés"));
        menuItemFrances.setText(idioma.get("Frances"));
        menuItemCerrarSesion.setText(idioma.get("menu.salir.cerrar"));
        menuItemSalir.setText(idioma.get("menu.salir.salir"));

        setTitle(idioma.get("titulo.ventana"));

        // Carga y asignación de iconos (asegúrate de que las rutas de las imágenes son correctas)
        // He añadido una comprobación para evitar NullPointerException si getResource devuelve null
        // y mensajes de error más específicos.

        URL EcuadorURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Ecuador.svg.png");
        if (EcuadorURL != null) {
            ImageIcon icono = new ImageIcon(EcuadorURL);
            menuItemEspanol.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Ecuador (imagenes/Ecuador.svg.png)");
        }
        URL USAURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/EstadosUnidos.svg.png");
        if (USAURL != null) {
            ImageIcon icono = new ImageIcon(USAURL);
            menuItemIngles.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Estados Unidos (imagenes/EstadosUnidos.svg.png)");
        }
        URL FranceURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Francia.svg.png");
        if (FranceURL != null) {
            ImageIcon icono = new ImageIcon(FranceURL);
            menuItemFrances.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Francia (imagenes/Francia.svg.png)");
        }
        URL iconoMenuCarritoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Carrito.svg.png");
        if (iconoMenuCarritoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuCarritoURL);
            menuCarrito.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Carrito (imagenes/Carrito.svg.png)");
        }
        URL iconoMenuProductoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Producto.svg.png");
        if (iconoMenuProductoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuProductoURL);
            menuProducto.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Producto (imagenes/Producto.svg.png)");
        }
        URL iconoMenuUsuarioURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Usuario.svg.png.png"); // Nota: doble .png aquí
        if (iconoMenuUsuarioURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuUsuarioURL);
            menuUsuario.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Usuario (imagenes/Usuario.svg.png.png)");
        }
        URL iconoMenuSalirURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Salir.svg.png");
        if (iconoMenuSalirURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuSalirURL);
            menuSalir.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Salir (imagenes/Salir.svg.png)");
        }
        URL iconoMenuIdiomaURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Idioma.svg.png");
        if (iconoMenuIdiomaURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuIdiomaURL);
            menuIdioma.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Idioma (imagenes/Idioma.svg.png)");
        }
        URL iconoMenuCrearCarritoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Crear.svg.png");
        if (iconoMenuCrearCarritoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuCrearCarritoURL);
            menuItemCrearCarrito.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Crear Carrito (imagenes/Crear.svg.png)");
        }
        URL iconoMenuCrearProductoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Crear.svg.png");
        if (iconoMenuCrearProductoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuCrearProductoURL);
            menuItemCrearProducto.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Crear Producto (imagenes/Crear.svg.png)");
        }
        URL iconoMenuCrearUsuarioURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Crear.svg.png");
        if (iconoMenuCrearUsuarioURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuCrearUsuarioURL);
            menuItemCrearUsuario.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Crear Usuario (imagenes/Crear.svg.png)");
        }
        URL iconoMenuBuscarCarritoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Buscar.svg.png");
        if (iconoMenuBuscarCarritoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuBuscarCarritoURL);
            menuItemBuscarCarrito.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Buscar Carrito (imagenes/Buscar.svg.png)");
        }
        URL iconoMenuBuscarProductoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Buscar.svg.png");
        if (iconoMenuBuscarProductoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuBuscarProductoURL);
            menuItemBuscarProducto.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Buscar Producto (imagenes/Buscar.svg.png)");
        }
        URL iconoMenuBuscarUsuarioURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Buscar.svg.png");
        if (iconoMenuBuscarUsuarioURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuBuscarUsuarioURL);
            menuItemListarUsuario.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Buscar Usuario (imagenes/Buscar.svg.png)");
        }
        URL iconoMenuEliminarCarritoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Eliminar.svg.png");
        if (iconoMenuEliminarCarritoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuEliminarCarritoURL);
            menuItemEliminarCarrito.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Eliminar Carrito (imagenes/Eliminar.svg.png)");
        }
        URL iconoMenuEliminarProductoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Eliminar.svg.png");
        if (iconoMenuEliminarProductoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuEliminarProductoURL);
            menuItemEliminarProducto.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Eliminar Producto (imagenes/Eliminar.svg.png)");
        }
        URL iconoMenuEliminarUsuarioURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Eliminar.svg.png");
        if (iconoMenuEliminarUsuarioURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuEliminarUsuarioURL);
            menuItemEliminarUsuario.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Eliminar Usuario (imagenes/Eliminar.svg.png)");
        }
        URL iconoMenuActualizarCarritoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Actualizar.svg.png");
        if (iconoMenuActualizarCarritoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuActualizarCarritoURL);
            menuItemModificarCarrito.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Actualizar Carrito (imagenes/Actualizar.svg.png)");
        }
        URL iconoMenuActualizarProductoURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Actualizar.svg.png");
        if (iconoMenuActualizarProductoURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuActualizarProductoURL);
            menuItemActualizarProducto.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Actualizar Producto (imagenes/Actualizar.svg.png)");
        }
        URL iconoMenuActualizarUsuarioURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Actualizar.svg.png");
        if (iconoMenuActualizarUsuarioURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuActualizarUsuarioURL);
            menuItemActualizarUsuario.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Actualizar Usuario (imagenes/Actualizar.svg.png)");
        }
        URL iconoMenuCerrarSesionURL = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Salir.svg.png");
        if (iconoMenuCerrarSesionURL != null) {
            ImageIcon icono = new ImageIcon(iconoMenuCerrarSesionURL);
            menuItemCerrarSesion.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Cerrar Sesión (imagenes/Salir.svg.png)");
        }
        URL iconoMenuItemURl = MenuPrincipalView.class.getClassLoader().getResource("imagenes/Exit.svg.png");
        if (iconoMenuItemURl != null) {
            ImageIcon icono = new ImageIcon(iconoMenuItemURl);
            menuItemSalir.setIcon(icono);
        } else {
            System.err.println("Error: No se ha cargado el icono de Salir (imagenes/Exit.svg.png)");
        }
    }
}