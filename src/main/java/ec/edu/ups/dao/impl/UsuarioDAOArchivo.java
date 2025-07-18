package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.modelo.Rol;

import java.io.*;
import java.nio.charset.StandardCharsets; // A pesar de no usar java.nio.file.Files, StandardCharsets sigue siendo útil para especificar la codificación.
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link UsuarioDAO} que gestiona la persistencia de objetos {@link Usuario}
 * utilizando dos formatos de archivo en el sistema de ficheros:
 * <ul>
 * <li>Archivo binario (.dat): para el objeto completo serializado.</li>
 * <li>Archivo de texto (.txt): para metadatos clave como cédula, contraseña, rol, etc., con longitud fija.</li>
 * </ul>
 * Esta versión utiliza operaciones básicas de {@code java.io} y bucles tradicionales,
 * evitando las APIs de {@code java.nio} y {@code java.util.stream}, para mantener el código más directo.
 * También incluye la lógica para la creación de un usuario administrador por defecto si no existe.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioDAOArchivo implements UsuarioDAO {

    private final String storageDirectoryPath; // Ruta del directorio como String para File
    private final CuestionarioDAO cuestionarioDAO; // Se mantiene por compatibilidad en el constructor

    // --- Definición de longitudes fijas para el archivo de texto ---
    private static final int CEDULA_LENGTH = 10;
    private static final int CONTRASENIA_LENGTH = 20;
    private static final int ROL_LENGTH = 15;
    private static final int NOMBRE_COMPLETO_LENGTH = 50;
    private static final int FECHA_NACIMIENTO_LENGTH = 10;
    private static final int CELULAR_LENGTH = 10;
    private static final int CORREO_LENGTH = 40;
    private static final int TOTAL_TEXT_RECORD_LENGTH =
            CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH +
                    FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH + CORREO_LENGTH;

    /**
     * Constructor de la clase UsuarioDAOArchivo.
     * Inicializa el directorio de almacenamiento para usuarios y crea la carpeta si no existe.
     * Al inicializar, verifica si el usuario administrador por defecto existe y lo crea si es necesario.
     *
     * @param directoryPath La ruta base del directorio donde se almacenarán los archivos de usuarios.
     * @param cuestionarioDAO Una instancia de {@link CuestionarioDAO} (aunque no se usa directamente en este DAO,
     * se mantiene para compatibilidad con el constructor de UsuarioController).
     */
    public UsuarioDAOArchivo(String directoryPath, CuestionarioDAO cuestionarioDAO) {
        // Concatenamos para crear la ruta específica para usuarios, usando File.separator para compatibilidad.
        this.storageDirectoryPath = directoryPath + File.separator + "usuarios";
        this.cuestionarioDAO = cuestionarioDAO; // Se asigna, pero no se usa en este DAO directamente.

        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) { // Crea el directorio si no existe. Devuelve false si falla.
                System.err.println("Error al crear el directorio de almacenamiento de usuarios: " + storageDirectoryPath);
            }
        }

        // Lógica para crear el administrador por defecto si no existe.
        String adminCedula = "2450097775";
        String adminPassword = "Admin@";
        String adminNombre = "Administrador Principal";
        String adminCelular = "0998765432";
        String adminCorreo = "admin@example.com";
        String adminFechaNacimiento = "01/Enero/1990";

        Usuario adminExistente = buscarPorUsuario(adminCedula); // Usa el propio método de búsqueda
        if (adminExistente == null) {
            Usuario nuevoAdmin = new Usuario(adminCedula, adminPassword, Rol.ADMINISTRADOR,
                    adminNombre, adminFechaNacimiento, adminCelular, adminCorreo);
            crear(nuevoAdmin); // Usa el propio método de creación para persistirlo
        }
    }

    /**
     * Construye el objeto {@link File} para el archivo binario de un usuario dado su cédula.
     * El archivo binario guarda el objeto {@link Usuario} completo serializado.
     *
     * @param cedula La cédula del usuario.
     * @return El objeto {@link File} que representa el archivo binario (.dat) del usuario.
     */
    private File getBinaryFile(String cedula) {
        return new File(storageDirectoryPath + File.separator + cedula + ".dat");
    }

    /**
     * Construye el objeto {@link File} para el archivo de texto de un usuario dado su cédula.
     * El archivo de texto guarda metadatos en un formato de longitud fija.
     *
     * @param cedula La cédula del usuario.
     * @return El objeto {@link File} que representa el archivo de texto (.txt) del usuario.
     */
    private File getTextFile(String cedula) {
        return new File(storageDirectoryPath + File.separator + cedula + ".txt");
    }

    /**
     * Rellena una cadena con espacios a la derecha para alcanzar una longitud fija,
     * o la trunca si es más larga que la longitud especificada.
     *
     * @param s La cadena de texto a formatear.
     * @param n La longitud deseada para la cadena.
     * @return La cadena formateada con longitud fija.
     */
    private String padRight(String s, int n) {
        if (s == null) s = "";
        if (s.length() > n) {
            return s.substring(0, n);
        }
        return String.format("%-" + n + "s", s);
    }

    /**
     * Crea y persiste un nuevo usuario en el sistema de archivos.
     * El usuario se guarda en un archivo binario (serializado) y en un archivo de texto
     * con formato de longitud fija para sus atributos clave.
     *
     * @param usuario El objeto {@link Usuario} a ser creado y persistido.
     * @throws IllegalArgumentException Si el usuario o su cédula son nulos o vacíos.
     */
    @Override
    public void crear(Usuario usuario) {
        if (usuario == null || usuario.getCedula() == null || usuario.getCedula().isEmpty()) {
            throw new IllegalArgumentException("Usuario y su cédula no pueden ser nulos o vacíos para la creación.");
        }

        // 1. Guardar en archivo binario (serialización de objeto)
        File binaryFile = getBinaryFile(usuario.getCedula());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryFile))) {
            oos.writeObject(usuario);
            System.out.println("Usuario creado y guardado BINARIO: " + usuario.getCedula());
        } catch (IOException e) {
            System.err.println("Error al guardar usuario en archivo BINARIO: " + e.getMessage());
        }

        // 2. Guardar en archivo de texto (formato de longitud fija)
        File textFile = getTextFile(usuario.getCedula());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFile), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            sb.append(padRight(usuario.getCedula(), CEDULA_LENGTH));
            sb.append(padRight(usuario.getContrasenia(), CONTRASENIA_LENGTH));
            sb.append(padRight(usuario.getRol().name(), ROL_LENGTH));
            sb.append(padRight(usuario.getNombreCompleto(), NOMBRE_COMPLETO_LENGTH));
            sb.append(padRight(usuario.getFechaNacimiento(), FECHA_NACIMIENTO_LENGTH));
            sb.append(padRight(usuario.getCelular(), CELULAR_LENGTH));
            sb.append(padRight(usuario.getCorreo(), CORREO_LENGTH));

            writer.write(sb.toString());
            System.out.println("Usuario creado y guardado TEXTO: " + usuario.getCedula());
        } catch (IOException e) {
            System.err.println("Error al guardar usuario en archivo de TEXTO: " + e.getMessage());
        }
    }

    /**
     * Busca y recupera un usuario por su nombre de usuario (cédula).
     * Prioriza la lectura del archivo binario. Si este no existe o hay un error de lectura,
     * intenta leer el usuario desde su representación en archivo de texto plano como fallback.
     *
     * @param username El nombre de usuario (cédula) del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no existe un usuario con ese nombre de usuario
     * o si ocurre un error durante la lectura en ambos formatos.
     */
    @Override
    public Usuario buscarPorUsuario(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        File binaryFile = getBinaryFile(username);
        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                return (Usuario) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer usuario de archivo BINARIO para '" + username + "', intentando archivo de texto: " + e.getMessage());
                // Si falla la lectura binaria, intenta leer del archivo de texto
                return readUserFromTextFile(username);
            }
        } else {
            // Si el binario no existe, intentar leer del archivo de texto
            return readUserFromTextFile(username);
        }
    }

    /**
     * Método auxiliar para reconstruir un objeto {@link Usuario} a partir de su representación
     * en un archivo de texto plano con formato de longitud fija.
     *
     * @param cedula La cédula del usuario a leer.
     * @return El objeto {@link Usuario} reconstruido con los datos disponibles en el archivo de texto,
     * o {@code null} si falla la lectura o el parseo.
     */
    private Usuario readUserFromTextFile(String cedula) {
        File textFile = getTextFile(cedula);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                if (line != null && line.length() >= TOTAL_TEXT_RECORD_LENGTH) {
                    // Parsear los campos de la línea de longitud fija
                    String parsedCedula = line.substring(0, CEDULA_LENGTH).trim();
                    String parsedContrasenia = line.substring(CEDULA_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH).trim();
                    String parsedRolName = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH).trim();
                    String parsedNombreCompleto = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH).trim();
                    String parsedFechaNacimiento = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH).trim();
                    String parsedCelular = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH).trim();
                    String parsedCorreo = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH, TOTAL_TEXT_RECORD_LENGTH).trim();

                    Rol parsedRol = null;
                    try {
                        parsedRol = Rol.valueOf(parsedRolName);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Rol desconocido en archivo de texto para usuario " + cedula + ": " + parsedRolName + ". Se asignará rol de USUARIO.");
                        parsedRol = Rol.USUARIO; // Asignar un rol por defecto si el leído es inválido
                    }

                    // Se crea un objeto Usuario con los datos leídos del archivo de texto.
                    // Nota: Las preguntas de seguridad no se guardan en el archivo de texto y serán nulas/vacías.
                    Usuario usuario = new Usuario(parsedCedula, parsedContrasenia, parsedRol,
                            parsedNombreCompleto, parsedFechaNacimiento, parsedCelular, parsedCorreo);
                    return usuario;
                }
            } catch (IOException | IndexOutOfBoundsException e) {
                System.err.println("Error al leer o parsear usuario de archivo de TEXTO para '" + cedula + "': " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Actualiza un usuario existente en el sistema de archivos.
     * La operación de actualización se implementa eliminando las versiones anteriores del usuario
     * (binaria y de texto) y luego creando nuevas con los datos actualizados.
     *
     * @param usuario El objeto {@link Usuario} con los datos actualizados.
     * @throws IllegalArgumentException Si el usuario o su cédula son nulos o vacíos.
     */
    @Override
    public void actualizar(Usuario usuario) {
        if (usuario == null || usuario.getCedula() == null || usuario.getCedula().isEmpty()) {
            throw new IllegalArgumentException("Usuario y su cédula no pueden ser nulos o vacíos para la actualización.");
        }
        eliminar(usuario.getCedula()); // Elimina los archivos existentes (binario y texto)
        crear(usuario); // Crea nuevos archivos con la información actualizada
    }

    /**
     * Elimina un usuario del sistema de archivos, removiendo tanto su archivo binario como de texto.
     *
     * @param username El nombre de usuario (cédula) del usuario a eliminar.
     * @throws IllegalArgumentException Si el nombre de usuario es nulo o vacío.
     */
    @Override
    public void eliminar(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío para la eliminación.");
        }

        File binaryFile = getBinaryFile(username);
        File textFile = getTextFile(username);

        boolean binaryDeleted = false;
        boolean textDeleted = false;

        // Intentar eliminar el archivo binario
        if (binaryFile.exists()) {
            binaryDeleted = binaryFile.delete();
            if (!binaryDeleted) {
                System.err.println("Error al eliminar el archivo BINARIO del usuario '" + username + "'");
            }
        } else {
            binaryDeleted = true; // Si no existe, se considera "eliminado" exitosamente
        }

        // Intentar eliminar el archivo de texto
        if (textFile.exists()) {
            textDeleted = textFile.delete();
            if (!textDeleted) {
                System.err.println("Error al eliminar el archivo de TEXTO del usuario '" + username + "'");
            }
        } else {
            textDeleted = true; // Si no existe, se considera "eliminado" exitosamente
        }

        if (binaryDeleted && textDeleted) {
            System.out.println("Usuario eliminado (binario y texto), usuario: " + username);
        } else {
            System.err.println("Advertencia: No se pudieron eliminar todos los archivos del usuario '" + username + "'");
        }
    }

    /**
     * Lista todos los usuarios persistidos en el sistema de archivos.
     * Lee los archivos binarios (.dat) y los deserializa para reconstruir los objetos {@link Usuario} completos.
     * Si hay un error al leer un archivo binario, ese usuario se omite de la lista.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Usuario} encontrados.
     * Retorna una lista vacía si no hay usuarios o si ocurren errores de lectura.
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        File directory = new File(storageDirectoryPath);
        File[] files = directory.listFiles(); // Lista todos los archivos en el directorio

        if (files != null) {
            for (File file : files) {
                // Solo consideramos los archivos binarios para listar, ya que son los que tienen el objeto completo.
                if (file.isFile() && file.getName().endsWith(".dat")) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        Usuario usuario = (Usuario) ois.readObject();
                        if (usuario != null) { // Asegurarse de que el objeto deserializado no sea nulo
                            usuarios.add(usuario);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("Error al listar usuario desde archivo BINARIO: " + file.getName() + " - " + e.getMessage());
                        // Si hay un error al leer un archivo, simplemente lo omitimos y continuamos con los demás.
                    }
                }
            }
        }
        return usuarios;
    }

    /**
     * Lista usuarios que coinciden exactamente con el nombre de usuario (cédula) proporcionado.
     * Si el nombre de usuario es nulo o vacío, devuelve todos los usuarios.
     *
     * @param username El nombre de usuario (cédula) para filtrar.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran usuarios que coincidan.
     */
    @Override
    public List<Usuario> listarPorUsuario(String username) {
        List<Usuario> resultados = new ArrayList<>();
        if (username == null || username.isEmpty()) {
            return listarTodos(); // Si el nombre de usuario es vacío, listamos todos.
        }

        Usuario usuarioExacto = buscarPorUsuario(username); // Intentamos buscar por coincidencia exacta primero.
        if (usuarioExacto != null) {
            resultados.add(usuarioExacto);
        }
        // Nota: Si se deseara una búsqueda "LIKE" (que contenga el username) en lugar de exacta,
        // se debería iterar sobre listarTodos() y filtrar por el nombre/cédula que contenga el `username` dado.
        return resultados;
    }

    /**
     * Autentica a un usuario verificando su nombre de usuario (cédula) y contraseña.
     *
     * @param username El nombre de usuario (cédula) a autenticar.
     * @param password La contraseña a verificar.
     * @return El objeto {@link Usuario} si la autenticación es exitosa, o {@code null} si las credenciales son incorrectas
     * o el usuario no existe.
     */
    @Override
    public Usuario autenticar(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        Usuario usuario = buscarPorUsuario(username); // Busca el usuario por nombre de usuario (cédula)
        if (usuario != null && usuario.getContrasenia() != null && usuario.getContrasenia().equals(password)) {
            return usuario;
        }
        return null;
    }
}