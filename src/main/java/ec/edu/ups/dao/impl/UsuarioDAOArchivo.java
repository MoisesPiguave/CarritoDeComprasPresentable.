package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.PreguntasRespuestas;
import ec.edu.ups.modelo.Preguntas;
import ec.edu.ups.modelo.Respuesta;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementación de {@link UsuarioDAO} que gestiona la persistencia de objetos {@link Usuario}
 * utilizando dos formatos de archivo en el sistema de ficheros:
 * <ul>
 * <li>Archivo binario (.dat): para el objeto completo serializado.</li>
 * <li>Archivo de texto (.txt): para metadatos clave como cédula, contraseña, rol, etc., con longitud fija,
 * incluyendo ahora una representación de las preguntas y respuestas de seguridad.</li>
 * </ul>
 * Esta versión ha sido modificada para almacenar **todos los usuarios en un único archivo binario**
 * y **un único archivo de texto**, sobrescribiendo el contenido completo en cada operación de guardado.
 * Utiliza operaciones básicas de {@code java.io} y bucles tradicionales,
 * evitando las APIs de {@code java.nio} y {@code java.util.stream}, para mantener el código más directo.
 * También incluye la lógica para la creación de un usuario administrador por defecto si no existe.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class UsuarioDAOArchivo implements UsuarioDAO {

    private final String storageDirectoryPath;
    private final CuestionarioDAO cuestionarioDAO;

    // --- RUTAS DE ARCHIVO GLOBALES ---
    private final String ALL_USERS_BINARY_FILE_PATH;
    private final String ALL_USERS_TEXT_FILE_PATH;

    // --- Definición de longitudes fijas para el archivo de texto ---
    private static final int CEDULA_LENGTH = 10;
    private static final int CONTRASENIA_LENGTH = 20;
    private static final int ROL_LENGTH = 15;
    private static final int NOMBRE_COMPLETO_LENGTH = 50;
    private static final int FECHA_NACIMIENTO_LENGTH = 10;
    private static final int CELULAR_LENGTH = 10;
    private static final int CORREO_LENGTH = 40;
    private static final int PREGUNTAS_RESPUESTAS_LENGTH = 200; // Longitud combinada para todas las P/R

    private static final int TOTAL_TEXT_RECORD_LENGTH =
            CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH +
                    FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH + CORREO_LENGTH + PREGUNTAS_RESPUESTAS_LENGTH;

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
        this.storageDirectoryPath = directoryPath + File.separator + "usuarios";
        this.cuestionarioDAO = cuestionarioDAO;

        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Error al crear el directorio de almacenamiento de usuarios: " + storageDirectoryPath);
            }
        }

        this.ALL_USERS_BINARY_FILE_PATH = this.storageDirectoryPath + File.separator + "all_users.dat";
        this.ALL_USERS_TEXT_FILE_PATH = this.storageDirectoryPath + File.separator + "all_users.txt";

        String adminCedula = "2450097775";
        String adminPassword = "Admin@";
        String adminNombre = "Administrador Principal";
        String adminCelular = "0998765432";
        String adminCorreo = "admin@example.com";
        String adminFechaNacimiento = "01/Enero/1990";

        Usuario adminExistente = buscarPorUsuario(adminCedula);
        if (adminExistente == null) {
            Usuario nuevoAdmin = new Usuario(adminCedula, adminPassword, Rol.ADMINISTRADOR,
                    adminNombre, adminFechaNacimiento, adminCelular, adminCorreo);
            crear(nuevoAdmin);
        }
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
     * Convierte la lista de preguntas y respuestas de un usuario en una única cadena de texto
     * formateada para ser guardada con longitud fija.
     * Formato: "P1:R1;P2:R2;..."
     *
     * @param preguntasRespuestas La lista de {@link PreguntasRespuestas} del usuario.
     * @return Una cadena que representa todas las preguntas y respuestas.
     */
    private String formatPreguntasRespuestasForText(List<PreguntasRespuestas> preguntasRespuestas) {
        if (preguntasRespuestas == null || preguntasRespuestas.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (PreguntasRespuestas pr : preguntasRespuestas) {
            String preguntaTexto = "";
            String respuestaTexto = "";

            if (pr != null) {
                if (pr.getPreguntas() != null && pr.getPreguntas().getPreguntas() != null) {
                    preguntaTexto = pr.getPreguntas().getPreguntas();
                }
                if (pr.getRespuesta() != null && pr.getRespuesta().getTexto() != null) { // Corregido getTexto()
                    respuestaTexto = pr.getRespuesta().getTexto();
                }
            }

            if (!preguntaTexto.isEmpty() || !respuestaTexto.isEmpty()) {
                sb.append(preguntaTexto.replace(";", ",").replace(":", "-"))
                        .append(":")
                        .append(respuestaTexto.replace(";", ",").replace(":", "-"))
                        .append(";");
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Guarda la lista completa de usuarios en el archivo binario y en el archivo de texto.
     * Este método sobrescribe el contenido anterior de ambos archivos.
     *
     * @param usuarios La lista de {@link Usuario} a guardar.
     */
    private void saveAllUsers(List<Usuario> usuarios) {
        // 1. Guardar en archivo BINARIO
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ALL_USERS_BINARY_FILE_PATH))) {
            oos.writeObject(usuarios);
            System.out.println("Todos los usuarios guardados en archivo BINARIO: " + ALL_USERS_BINARY_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar TODOS los usuarios en archivo BINARIO: " + e.getMessage());
        }

        // 2. Guardar en archivo de TEXTO (formato de longitud fija, cada usuario en una línea)
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ALL_USERS_TEXT_FILE_PATH), StandardCharsets.UTF_8))) {
            for (Usuario usuario : usuarios) {
                StringBuilder sb = new StringBuilder();
                sb.append(padRight(usuario.getCedula(), CEDULA_LENGTH));
                sb.append(padRight(usuario.getContrasenia(), CONTRASENIA_LENGTH));
                sb.append(padRight(usuario.getRol().name(), ROL_LENGTH));
                sb.append(padRight(usuario.getNombreCompleto(), NOMBRE_COMPLETO_LENGTH));
                sb.append(padRight(usuario.getFechaNacimiento(), FECHA_NACIMIENTO_LENGTH));
                sb.append(padRight(usuario.getCelular(), CELULAR_LENGTH));
                sb.append(padRight(usuario.getCorreo(), CORREO_LENGTH));
                sb.append(padRight(formatPreguntasRespuestasForText(usuario.getPreguntasRespuestas()), PREGUNTAS_RESPUESTAS_LENGTH));

                writer.write(sb.toString());
                writer.newLine();
            }
            System.out.println("Todos los usuarios guardados en archivo de TEXTO: " + ALL_USERS_TEXT_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar TODOS los usuarios en archivo de TEXTO: " + e.getMessage());
        }
    }

    /**
     * Crea y persiste un nuevo usuario en el sistema de archivos.
     * El usuario se añade a la colección existente y se guarda la lista completa
     * en un único archivo binario (serializado) y un único archivo de texto.
     *
     * @param usuario El objeto {@link Usuario} a ser creado y persistido.
     * @throws IllegalArgumentException Si el usuario o su cédula son nulos o vacíos.
     */
    @Override
    public void crear(Usuario usuario) {
        if (usuario == null || usuario.getCedula() == null || usuario.getCedula().isEmpty()) {
            throw new IllegalArgumentException("Usuario y su cédula no pueden ser nulos o vacíos para la creación.");
        }

        List<Usuario> usuarios = listarTodos();
        boolean existe = usuarios.stream().anyMatch(u -> u.getCedula().equals(usuario.getCedula()));
        if (existe) {
            System.err.println("Error: El usuario con cédula " + usuario.getCedula() + " ya existe. No se creará.");
            return;
        }

        usuarios.add(usuario);
        System.out.println("Usuario añadido a la colección en memoria: " + usuario.getCedula());
        saveAllUsers(usuarios);
    }

    /**
     * Busca y recupera un usuario por su nombre de usuario (cédula) de la colección completa de usuarios.
     *
     * @param username El nombre de usuario (cédula) del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no existe un usuario con esa cédula.
     */
    @Override
    public Usuario buscarPorUsuario(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return listarTodos().stream()
                .filter(u -> u.getCedula().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Actualiza un usuario existente en el sistema de archivos.
     * La operación implica cargar todos los usuarios, encontrar y reemplazar el usuario a actualizar,
     * y luego volver a guardar la lista completa en los archivos.
     *
     * @param usuario El objeto {@link Usuario} con los datos actualizados.
     * @throws IllegalArgumentException Si el usuario o su cédula son nulos o vacíos.
     */
    @Override
    public void actualizar(Usuario usuario) {
        if (usuario == null || usuario.getCedula() == null || usuario.getCedula().isEmpty()) {
            throw new IllegalArgumentException("Usuario y su cédula no pueden ser nulos o vacíos para la actualización.");
        }

        List<Usuario> usuarios = listarTodos();
        boolean encontrado = false;

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCedula().equals(usuario.getCedula())) {
                usuarios.set(i, usuario);
                encontrado = true;
                System.out.println("Usuario actualizado en la colección en memoria: " + usuario.getCedula());
                break;
            }
        }

        if (!encontrado) {
            System.err.println("Error: El usuario con cédula " + usuario.getCedula() + " no fue encontrado para actualizar.");
            return;
        }

        saveAllUsers(usuarios);
    }

    /**
     * Elimina un usuario del sistema de archivos, removiendo su entrada de la colección completa.
     * La operación implica cargar todos los usuarios, eliminar el usuario especificado,
     * y luego volver a guardar la lista reducida en los archivos.
     *
     * @param username El nombre de usuario (cédula) del usuario a eliminar.
     * @throws IllegalArgumentException Si el nombre de usuario es nulo o vacío.
     */
    @Override
    public void eliminar(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío para la eliminación.");
        }

        List<Usuario> usuarios = listarTodos();
        boolean removido = usuarios.removeIf(u -> u.getCedula().equals(username));

        if (removido) {
            System.out.println("Usuario eliminado de la colección en memoria: " + username);
            saveAllUsers(usuarios);
        } else {
            System.err.println("Error: El usuario con cédula " + username + " no fue encontrado para eliminar.");
        }
    }

    /**
     * Lista todos los usuarios persistidos en el sistema de archivos.
     * Lee la lista completa de usuarios del único archivo binario (.dat)
     * y la deserializa. Si el archivo binario no existe o hay un error de lectura,
     * intenta reconstruir la lista desde el archivo de texto.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Usuario} encontrados.
     * Retorna una lista vacía si no hay usuarios o si ocurren errores de lectura en ambos formatos.
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        File binaryFile = new File(ALL_USERS_BINARY_FILE_PATH);

        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    usuarios.addAll((List<Usuario>) obj);
                } else {
                    System.err.println("El contenido del archivo binario no es una lista de usuarios. Intentando leer del archivo de texto.");
                    usuarios = readAllUsersFromTextFile();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer la lista de usuarios del archivo BINARIO principal: " + e.getMessage());
                System.out.println("Intentando reconstruir la lista desde el archivo de texto principal...");
                usuarios = readAllUsersFromTextFile();
            }
        } else {
            System.out.println("Archivo binario de usuarios principal no encontrado. Intentando leer del archivo de texto principal...");
            usuarios = readAllUsersFromTextFile();
        }
        return usuarios;
    }

    /**
     * Método auxiliar para reconstruir una lista de objetos {@link Usuario} a partir de su representación
     * en un único archivo de texto plano con formato de longitud fija, una línea por usuario.
     *
     * @return La {@link List} de objetos {@link Usuario} reconstruidos con los datos disponibles en el archivo de texto,
     * o una lista vacía si falla la lectura o el parseo.
     */
    private List<Usuario> readAllUsersFromTextFile() {
        List<Usuario> usuarios = new ArrayList<>();
        File textFile = new File(ALL_USERS_TEXT_FILE_PATH);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.length() >= TOTAL_TEXT_RECORD_LENGTH) {
                        String parsedCedula = line.substring(0, CEDULA_LENGTH).trim();
                        String parsedContrasenia = line.substring(CEDULA_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH).trim();
                        String parsedRolName = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH).trim();
                        String parsedNombreCompleto = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH).trim();
                        String parsedFechaNacimiento = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH).trim();
                        String parsedCelular = line.substring(CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH).trim();
                        String parsedCorreo = line.substring(CELULAR_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH, CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH + FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH + CORREO_LENGTH).trim();
                        String parsedPreguntasRespuestasStr = line.substring(
                                CEDULA_LENGTH + CONTRASENIA_LENGTH + ROL_LENGTH + NOMBRE_COMPLETO_LENGTH +
                                        FECHA_NACIMIENTO_LENGTH + CELULAR_LENGTH + CORREO_LENGTH, TOTAL_TEXT_RECORD_LENGTH).trim();

                        Rol parsedRol = null;
                        try {
                            parsedRol = Rol.valueOf(parsedRolName);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Rol desconocido en archivo de texto para usuario con cédula " + parsedCedula + ": " + parsedRolName + ". Se asignará rol de USUARIO.");
                            parsedRol = Rol.USUARIO;
                        }

                        Usuario usuario = new Usuario(parsedCedula, parsedContrasenia, parsedRol,
                                parsedNombreCompleto, parsedFechaNacimiento, parsedCelular, parsedCorreo);

                        List<PreguntasRespuestas> preguntasRecuperadas = new ArrayList<>();
                        if (!parsedPreguntasRespuestasStr.isEmpty()) {
                            String[] pares = parsedPreguntasRespuestasStr.split(";");
                            for (String par : pares) {
                                // --- CORRECCIÓN CLAVE AQUÍ ---
                                // Asegúrate de que 'par' no sea nulo o vacío después de trim() antes de split
                                if (par != null && !par.trim().isEmpty()) {
                                    String[] partes = par.split(":");
                                    // Verifica que se encontraron exactamente dos partes (pregunta y respuesta)
                                    if (partes.length == 2) {
                                        String preguntaContent = partes[0].trim();
                                        String respuestaContent = partes[1].trim();

                                        // Asegúrate de que el contenido no esté vacío después de trim()
                                        if (!preguntaContent.isEmpty() && !respuestaContent.isEmpty()) {
                                            preguntasRecuperadas.add(new PreguntasRespuestas(
                                                    new Preguntas("TEMP_CODE", preguntaContent), // "TEMP_CODE" porque el código no se guardó individualmente
                                                    new Respuesta(respuestaContent)
                                            ));
                                        } else {
                                            System.err.println("Advertencia: Se encontró un par P/R con contenido vacío después de parseo en el texto: '" + par + "'");
                                        }
                                    } else {
                                        System.err.println("Advertencia: Formato inesperado para un par P/R en el texto (no tiene ':') o tiene múltiples: '" + par + "'");
                                    }
                                }
                            }
                        }
                        usuario.setPreguntasRespuestas(preguntasRecuperadas);

                        usuarios.add(usuario);
                    } else {
                        System.err.println("Línea de texto incompleta o corrupta: " + line);
                    }
                }
            } catch (IOException | IndexOutOfBoundsException e) {
                System.err.println("Error al leer o parsear usuarios de archivo de TEXTO principal: " + e.getMessage());
            }
        }
        return usuarios;
    }

    /**
     * Lista usuarios que coinciden exactamente con el nombre de usuario (cédula) proporcionado.
     *
     * @param username El nombre de usuario (cédula) para filtrar.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran usuarios que coincidan.
     */
    @Override
    public List<Usuario> listarPorUsuario(String username) {
        List<Usuario> resultados = new ArrayList<>();
        if (username == null || username.isEmpty()) {
            return listarTodos();
        }

        Usuario usuarioExacto = buscarPorUsuario(username);
        if (usuarioExacto != null) {
            resultados.add(usuarioExacto);
        }
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
        Usuario usuario = buscarPorUsuario(username);
        if (usuario != null && usuario.getContrasenia() != null && usuario.getContrasenia().equals(password)) {
            return usuario;
        }
        return null;
    }
}