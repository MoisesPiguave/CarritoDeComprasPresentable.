package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets; // Sigue siendo útil para especificar la codificación en archivos de texto
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // Necesario para String.format de double

/**
 * Implementación de {@link CarritoDAO} que gestiona la persistencia de objetos {@link Carrito}
 * utilizando dos formatos de archivo en el sistema de ficheros:
 * <ul>
 * <li>Archivo binario (.dat): para el objeto completo serializado.</li>
 * <li>Archivo de texto (.txt): para metadatos clave como código, cédula de usuario y total, con longitud fija.</li>
 * </ul>
 * Esta versión utiliza operaciones básicas de {@code java.io} y bucles tradicionales,
 * evitando las APIs de {@code java.nio} y {@code java.util.stream}, para mantener el código más directo.
 * Utiliza un esquema de numeración secuencial para los códigos de carrito.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CarritoDAOArchivo implements CarritoDAO {

    private final String storageDirectoryPath;
    private int nextCodigo = 1;

    // --- Definición de longitudes fijas para el archivo de texto ---
    private static final int CODIGO_LENGTH = 5;
    private static final int USUARIO_CEDULA_LENGTH = 10;
    private static final int TOTAL_LENGTH_LENGTH = 10;
    private static final int TOTAL_TEXT_RECORD_LENGTH =
            CODIGO_LENGTH + USUARIO_CEDULA_LENGTH + TOTAL_LENGTH_LENGTH;

    /**
     * Constructor de la clase CarritoDAOArchivo.
     * Inicializa el directorio de almacenamiento y determina el siguiente código disponible para un nuevo carrito.
     *
     * @param directoryPath La ruta base del directorio donde se almacenarán los archivos de carritos.
     */
    public CarritoDAOArchivo(String directoryPath) {
        // Concatenamos para crear la ruta específica para carritos, usando File.separator para compatibilidad.
        this.storageDirectoryPath = directoryPath + File.separator + "carritos";
        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) { // Crea el directorio si no existe. Devuelve false si falla.
                System.err.println("Error al crear el directorio de almacenamiento de carritos: " + storageDirectoryPath);
            }
        }
        this.nextCodigo = getNextAvailableCodigo();
    }

    /**
     * Construye el objeto {@link File} para el archivo binario de un carrito dado su código.
     * El archivo binario guarda el objeto {@link Carrito} completo serializado.
     *
     * @param codigo El código del carrito.
     * @return El objeto {@link File} que representa el archivo binario (.dat) del carrito.
     */
    private File getBinaryFile(int codigo) {
        return new File(storageDirectoryPath + File.separator + codigo + ".dat");
    }

    /**
     * Construye el objeto {@link File} para el archivo de texto de un carrito dado su código.
     * El archivo de texto guarda metadatos en un formato de longitud fija.
     *
     * @param codigo El código del carrito.
     * @return El objeto {@link File} que representa el archivo de texto (.txt) del carrito.
     */
    private File getTextFile(int codigo) {
        return new File(storageDirectoryPath + File.separator + codigo + ".txt");
    }

    /**
     * Formatea una cadena de texto para que tenga una longitud fija.
     * Si la cadena es más larga que la longitud especificada, se trunca.
     * Si es más corta, se rellena con espacios a la derecha.
     *
     * @param text La cadena de texto a formatear.
     * @param length La longitud deseada para la cadena.
     * @return La cadena formateada con longitud fija.
     */
    private String fixedLengthString(String text, int length) {
        if (text == null) {
            text = "";
        }
        if (text.length() > length) {
            return text.substring(0, length); // Trunca si es demasiado largo
        }
        return String.format("%-" + length + "s", text); // Rellena con espacios a la derecha
    }

    /**
     * Determina el siguiente código disponible para un nuevo carrito,
     * basándose en el código más alto de los archivos .dat existentes en el directorio de almacenamiento.
     *
     * @return El siguiente código entero disponible para un carrito.
     */
    private int getNextAvailableCodigo() {
        int maxCodigo = 0;
        File directory = new File(storageDirectoryPath);
        File[] files = directory.listFiles(); // Lista todos los archivos en el directorio

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".dat")) {
                    try {
                        String fileName = file.getName();
                        // Asume que el nombre de archivo es "codigo.dat"
                        int codigo = Integer.parseInt(fileName.substring(0, fileName.indexOf(".")));
                        if (codigo > maxCodigo) {
                            maxCodigo = codigo;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato de nombre de archivo al buscar código: " + file.getName());
                    }
                }
            }
        }
        return maxCodigo + 1;
    }

    /**
     * Crea y persiste un nuevo carrito en el sistema de archivos.
     * El carrito se guarda en un archivo binario (serializado) y en un archivo de texto
     * con formato de longitud fija para sus metadatos clave. Se le asigna un nuevo código secuencial.
     *
     * @param carrito El objeto Carrito a ser creado y persistido.
     * @throws IllegalArgumentException Si el carrito proporcionado es nulo.
     */
    @Override
    public void crear(Carrito carrito) {
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo para la creación.");
        }

        carrito.setCodigo(nextCodigo);
        nextCodigo++;
        Carrito copia = carrito.copiar(); // Se usa copia para asegurar independencia

        // 1. Guardar en archivo binario (serialización de objeto)
        File binaryFile = getBinaryFile(copia.getCodigo());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryFile))) {
            oos.writeObject(copia);
            System.out.println("Carrito creado y guardado BINARIO, codigo: " + copia.getCodigo());
        } catch (IOException e) {
            System.err.println("Error al guardar carrito en archivo BINARIO: " + e.getMessage());
        }

        // 2. Guardar en archivo de texto (formato de longitud fija)
        File textFile = getTextFile(copia.getCodigo());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFile), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            sb.append(fixedLengthString(String.valueOf(copia.getCodigo()), CODIGO_LENGTH));

            String cedulaUsuario = "";
            if (copia.getUsuario() != null) {
                cedulaUsuario = copia.getUsuario().getCedula();
            }
            sb.append(fixedLengthString(cedulaUsuario, USUARIO_CEDULA_LENGTH));

            // Formatea el total a 2 decimales con Locale.US para asegurar el punto como separador decimal.
            String totalAsString = String.format(Locale.US, "%.2f", copia.getTotal());
            sb.append(fixedLengthString(totalAsString, TOTAL_LENGTH_LENGTH));

            writer.write(sb.toString());
            System.out.println("Carrito creado y guardado TEXTO, codigo: " + copia.getCodigo());
        } catch (IOException e) {
            System.err.println("Error al guardar carrito en archivo de TEXTO: " + e.getMessage());
        }
    }

    /**
     * Busca y recupera un carrito por su código.
     * Prioriza la lectura del archivo binario. Si este no existe o hay un error de lectura,
     * intenta leer el carrito desde su representación en archivo de texto plano como fallback.
     *
     * @param codigo El código del carrito a buscar.
     * @return El objeto Carrito si se encuentra, o null si no existe o hay un error de lectura en ambos formatos.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        if (codigo <= 0) {
            return null;
        }

        File binaryFile = getBinaryFile(codigo);
        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                return (Carrito) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer carrito de archivo BINARIO para código " + codigo + ", intentando archivo de texto: " + e.getMessage());
                // Si falla la lectura binaria, intenta leer del archivo de texto
                return readCarritoFromTextFile(codigo);
            }
        } else {
            // Si el binario no existe, intenta leer del archivo de texto
            return readCarritoFromTextFile(codigo);
        }
    }

    /**
     * Lee un objeto Carrito desde su representación en archivo de texto plano.
     * Este método se utiliza como fallback si el archivo binario no está disponible o es ilegible.
     * El carrito recuperado de texto tendrá un objeto Usuario 'dummy' con solo la cédula (si está disponible)
     * y el total, ya que la información completa de ítems no se guarda en el archivo de texto.
     *
     * @param codigo El código del carrito a leer.
     * @return El objeto Carrito reconstruido desde el archivo de texto, o null si falla la lectura o el parseo.
     */
    private Carrito readCarritoFromTextFile(int codigo) {
        File textFile = getTextFile(codigo);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                if (line != null && line.length() >= TOTAL_TEXT_RECORD_LENGTH) {
                    // Parsear los campos de la línea de longitud fija
                    String parsedCodigoStr = line.substring(0, CODIGO_LENGTH).trim();
                    String parsedUsuarioCedula = line.substring(CODIGO_LENGTH, CODIGO_LENGTH + USUARIO_CEDULA_LENGTH).trim();
                    String parsedTotalStr = line.substring(CODIGO_LENGTH + USUARIO_CEDULA_LENGTH, TOTAL_TEXT_RECORD_LENGTH).trim();

                    int parsedCodigo = Integer.parseInt(parsedCodigoStr);
                    double parsedTotal = Double.parseDouble(parsedTotalStr);

                    Usuario dummyUsuario = null;
                    if (!parsedUsuarioCedula.isEmpty()) {
                        // Creamos un usuario "dummy" solo con la cédula, ya que el resto de datos no están en el TXT.
                        // El resto de los campos del usuario (contraseña, rol, etc.) serán nulos o por defecto.
                        dummyUsuario = new Usuario(parsedUsuarioCedula, "", null);
                    } else {
                        // Si no hay cédula, se puede crear un usuario con una cédula por defecto o dejarlo nulo.
                        // Depende de si el Carrito modelo permite usuario nulo o requiere una cédula válida.
                        dummyUsuario = new Usuario("0000000000", "", null); // Ejemplo: cédula por defecto
                    }

                    // Se crea el carrito. Su total se calculará dinámicamente en base a sus ítems.
                    // Si el Carrito modelo no tiene un constructor para setear el total, este valor no se usará.
                    // Aquí, el Carrito constructor con (codigo, usuario) no acepta el total,
                    // así que el 'parsedTotal' de la línea TXT se ignora para la construcción del objeto Carrito,
                    // lo cual es coherente si el modelo de Carrito calcula su total dinámicamente.
                    Carrito carrito = new Carrito(parsedCodigo, dummyUsuario);
                    // carrito.setTotal(parsedTotal); // Si el modelo de Carrito tuviera un setTotal y el total fuera un campo persistido
                    return carrito;
                }
            } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error al leer o parsear carrito de archivo de TEXTO para código " + codigo + ": " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Actualiza un carrito existente en el sistema de archivos.
     * La operación de actualización se implementa eliminando las versiones anteriores del carrito
     * (binaria y de texto) y luego creando nuevas con los datos actualizados.
     *
     * @param carrito El objeto Carrito con los datos actualizados.
     * @throws IllegalArgumentException Si el carrito proporcionado es nulo.
     */
    @Override
    public void actualizar(Carrito carrito) {
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo para la actualización.");
        }
        eliminar(carrito.getCodigo()); // Elimina los archivos existentes (binario y texto)
        crear(carrito); // Crea nuevos archivos con la información actualizada
    }

    /**
     * Elimina un carrito del sistema de archivos, removiendo tanto su archivo binario como de texto.
     *
     * @param codigo El código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        File binaryFile = getBinaryFile(codigo);
        File textFile = getTextFile(codigo);

        boolean binaryDeleted = false;
        boolean textDeleted = false;

        // Intentar eliminar el archivo binario
        if (binaryFile.exists()) {
            binaryDeleted = binaryFile.delete();
            if (!binaryDeleted) {
                System.err.println("Error al eliminar el archivo BINARIO del carrito con código " + codigo);
            }
        } else {
            binaryDeleted = true; // Si no existe, se considera "eliminado" exitosamente
        }

        // Intentar eliminar el archivo de texto
        if (textFile.exists()) {
            textDeleted = textFile.delete();
            if (!textDeleted) {
                System.err.println("Error al eliminar el archivo de TEXTO del carrito con código " + codigo);
            }
        } else {
            textDeleted = true; // Si no existe, se considera "eliminado" exitosamente
        }

        if (binaryDeleted && textDeleted) {
            System.out.println("Carrito eliminado (binario y texto), codigo: " + codigo);
        } else {
            System.err.println("Advertencia: No se pudieron eliminar todos los archivos del carrito con código " + codigo);
        }
    }

    /**
     * Lista todos los carritos persistidos en el sistema de archivos.
     * Lee los archivos binarios (.dat) y los deserializa para reconstruir los objetos Carrito completos.
     * Si hay un error al leer un archivo binario, ese carrito se omite de la lista.
     *
     * @return Una lista de todos los objetos Carrito encontrados.
     * Retorna una lista vacía si no hay carritos o si ocurren errores de lectura.
     */
    @Override
    public List<Carrito> listarTodos() {
        List<Carrito> carritos = new ArrayList<>();
        File directory = new File(storageDirectoryPath);
        File[] files = directory.listFiles(); // Lista todos los archivos en el directorio

        if (files != null) {
            for (File file : files) {
                // Solo consideramos los archivos binarios para listar, ya que son los que tienen el objeto completo.
                if (file.isFile() && file.getName().endsWith(".dat")) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        Carrito carrito = (Carrito) ois.readObject();
                        if (carrito != null) { // Asegurarse de que el objeto deserializado no sea nulo
                            carritos.add(carrito);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("Error al listar carrito desde archivo BINARIO: " + file.getName() + " - " + e.getMessage());
                        // Si hay un error al leer un archivo, simplemente lo omitimos y continuamos con los demás.
                    }
                }
            }
        }
        return carritos;
    }

    /**
     * Busca y devuelve una lista de carritos asociados a un usuario específico.
     *
     * @param usuario El objeto Usuario cuyos carritos se desean buscar.
     * @return Una lista de carritos que pertenecen al usuario especificado.
     * Retorna una lista vacía si el usuario o su cédula son nulos o vacíos.
     */
    @Override
    public List<Carrito> buscarPorUsuario(Usuario usuario) {
        if (usuario == null || usuario.getCedula() == null || usuario.getCedula().isEmpty()) {
            return new ArrayList<>();
        }
        List<Carrito> carritosUsuario = new ArrayList<>();
        for (Carrito carrito : listarTodos()) { // Reutilizamos listarTodos y luego filtramos
            if (carrito != null && carrito.getUsuario() != null && carrito.getUsuario().getCedula() != null && carrito.getUsuario().getCedula().equals(usuario.getCedula())) {
                carritosUsuario.add(carrito);
            }
        }
        return carritosUsuario;
    }
}