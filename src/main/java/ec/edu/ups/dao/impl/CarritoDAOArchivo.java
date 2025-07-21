package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.ItemCarrito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implementación de {@link CarritoDAO} que gestiona la persistencia de objetos {@link Carrito}
 * utilizando dos formatos de archivo en el sistema de ficheros:
 * <ul>
 * <li>Archivo binario (.dat): para la lista completa de objetos {@link Carrito} serializada.</li>
 * <li>Archivo de texto (.txt): para metadatos clave de cada carrito, con formato de longitud fija y mejor legibilidad.</li>
 * </ul>
 * Esta versión ha sido modificada para almacenar **todos los carritos en un único archivo binario**
 * y **un único archivo de texto**, sobrescribiendo el contenido completo en cada operación de guardado.
 * Utiliza operaciones básicas de {@code java.io} y bucles tradicionales,
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

    // --- NUEVAS RUTAS DE ARCHIVO GLOBALES para el almacenamiento único de TODOS los carritos ---
    private final String ALL_CARTS_BINARY_FILE_PATH;
    private final String ALL_CARTS_TEXT_FILE_PATH;

    // --- Definición de longitudes fijas para el archivo de texto (para cada campo del carrito) ---
    private static final int CODIGO_LENGTH = 5;
    private static final int USUARIO_CEDULA_LENGTH = 10;
    private static final int TOTAL_LENGTH = 12;
    private static final int ITEMS_SUMMARY_LENGTH = 100; // Ajusta según el tamaño esperado del resumen de ítems

    private static final int BASE_TEXT_RECORD_LENGTH =
            CODIGO_LENGTH + USUARIO_CEDULA_LENGTH + TOTAL_LENGTH + ITEMS_SUMMARY_LENGTH;

    /**
     * Constructor de la clase CarritoDAOArchivo.
     * Inicializa el directorio de almacenamiento y determina el siguiente código disponible para un nuevo carrito.
     *
     * @param directoryPath La ruta base del directorio donde se almacenarán los archivos de carritos.
     */
    public CarritoDAOArchivo(String directoryPath) {
        this.storageDirectoryPath = directoryPath + File.separator + "carritos";
        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Error al crear el directorio de almacenamiento de carritos: " + storageDirectoryPath);
            }
        }

        this.ALL_CARTS_BINARY_FILE_PATH = this.storageDirectoryPath + File.separator + "all_carritos.dat";
        this.ALL_CARTS_TEXT_FILE_PATH = this.storageDirectoryPath + File.separator + "all_carritos.txt";

        this.nextCodigo = getNextAvailableCodigoFromAllCarritos();
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
            return text.substring(0, length);
        }
        return String.format("%-" + length + "s", text);
    }

    /**
     * Determina el siguiente código disponible para un nuevo carrito,
     * basándose en el código más alto de los carritos cargados desde el archivo único.
     *
     * @return El siguiente código entero disponible para un carrito.
     */
    private int getNextAvailableCodigoFromAllCarritos() {
        int maxCodigo = 0;
        List<Carrito> allCarritos = listarTodos();
        for (Carrito carrito : allCarritos) {
            if (carrito.getCodigo() > maxCodigo) {
                maxCodigo = carrito.getCodigo();
            }
        }
        return maxCodigo + 1;
    }

    /**
     * Genera una cadena de texto que resume los ítems de un carrito para el archivo de texto.
     * Formato: "Producto1(xCantidad);Producto2(xCantidad);"
     *
     * @param items La lista de {@link ItemCarrito} del carrito.
     * @return Una cadena resumida de los ítems.
     */
    private String formatItemsForText(List<ItemCarrito> items) {
        if (items == null || items.isEmpty()) {
            return "No items";
        }
        StringBuilder sb = new StringBuilder();
        for (ItemCarrito item : items) {
            // --- VERIFICACIONES DE NULIDAD PARA CADA ITEM Y SUS PROPIEDADES ---
            if (item != null) {
                String productName = "";
                int quantity = 0; // Se asume int, si fuera Integer, necesitaría null check

                if (item.getProducto() != null && item.getProducto().getNombre() != null) {
                    productName = item.getProducto().getNombre();
                } else {
                    // Si el producto o su nombre son nulos, se usa un marcador de posición
                    productName = "Producto Desconocido";
                }

                quantity = item.getCantidad(); // Asumiendo que getCantidad() devuelve un int primitivo

                // Solo añade el ítem si el nombre del producto no está vacío después de las verificaciones
                if (!productName.trim().isEmpty()) {
                    sb.append(productName.replace(";", ",").replace(":", "-"))
                            .append("(x")
                            .append(quantity)
                            .append(");");
                } else {
                    System.err.println("Advertencia: ItemCarrito con nombre de producto vacío encontrado.");
                }
            } else {
                System.err.println("Advertencia: Se encontró un ItemCarrito nulo en la lista de ítems del carrito.");
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * Guarda la lista completa de carritos en el archivo binario y en el archivo de texto.
     * Este método sobrescribe el contenido anterior de ambos archivos.
     *
     * @param carritos La lista de {@link Carrito} a guardar.
     */
    private void saveAllCarritos(List<Carrito> carritos) {
        // 1. Guardar en archivo BINARIO
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ALL_CARTS_BINARY_FILE_PATH))) {
            oos.writeObject(carritos);
            System.out.println("Todos los carritos guardados en archivo BINARIO: " + ALL_CARTS_BINARY_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar TODOS los carritos en archivo BINARIO: " + e.getMessage());
        }

        // 2. Guardar en archivo de TEXTO (formato mejorado para legibilidad)
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ALL_CARTS_TEXT_FILE_PATH), StandardCharsets.UTF_8))) {
            for (Carrito carrito : carritos) {
                writer.write("--- INICIO CARRITO ---");
                writer.newLine();

                writer.write(fixedLengthString("Código: " + carrito.getCodigo(), BASE_TEXT_RECORD_LENGTH));
                writer.newLine();

                String cedulaUsuario = (carrito.getUsuario() != null && carrito.getUsuario().getCedula() != null) ? carrito.getUsuario().getCedula() : "N/A";
                writer.write(fixedLengthString("Usuario (Cédula): " + cedulaUsuario, BASE_TEXT_RECORD_LENGTH));
                writer.newLine();

                String totalAsString = String.format(Locale.US, "%.2f", carrito.getTotal());
                writer.write(fixedLengthString("Total: $" + totalAsString, BASE_TEXT_RECORD_LENGTH));
                writer.newLine();

                // Aquí se llama al método formatItemsForText
                writer.write(fixedLengthString("Items: " + formatItemsForText(carrito.obtenerItems()), BASE_TEXT_RECORD_LENGTH));
                writer.newLine();

                writer.write("--- FIN CARRITO ---");
                writer.newLine();
                writer.newLine();
            }
            System.out.println("Todos los carritos guardados en archivo de TEXTO: " + ALL_CARTS_TEXT_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar TODOS los carritos en archivo de TEXTO: " + e.getMessage());
        }
    }

    /**
     * Crea y persiste un nuevo carrito en el sistema de archivos.
     * El carrito se añade a la colección existente y se guarda la lista completa
     * en un único archivo binario (serializado) y un único archivo de texto.
     * Se le asigna un nuevo código secuencial.
     *
     * @param carrito El objeto Carrito a ser creado y persistido.
     * @throws IllegalArgumentException Si el carrito proporcionado es nulo.
     */
    @Override
    public void crear(Carrito carrito) {
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo para la creación.");
        }

        // Asigna el siguiente código disponible y lo incrementa
        carrito.setCodigo(nextCodigo);
        nextCodigo++;

        List<Carrito> carritos = listarTodos();

        // Puedes añadir una verificación si el código ya existe, aunque `nextCodigo` debería prevenirlo.
        // boolean existe = carritos.stream().anyMatch(c -> c.getCodigo() == carrito.getCodigo());
        // if (existe) { ... }

        carritos.add(carrito);
        System.out.println("Carrito añadido a la colección en memoria: " + carrito.getCodigo());

        saveAllCarritos(carritos);
    }

    /**
     * Busca y recupera un carrito por su código de la colección completa de carritos.
     *
     * @param codigo El código del carrito a buscar.
     * @return El objeto Carrito si se encuentra, o null si no existe un carrito con ese código.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        if (codigo <= 0) {
            return null;
        }
        return listarTodos().stream()
                .filter(c -> c.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    /**
     * Actualiza un carrito existente en el sistema de archivos.
     * La operación implica cargar todos los carritos, encontrar y reemplazar el carrito a actualizar,
     * y luego volver a guardar la lista completa en los archivos.
     *
     * @param carrito El objeto Carrito con los datos actualizados.
     * @throws IllegalArgumentException Si el carrito proporcionado es nulo.
     */
    @Override
    public void actualizar(Carrito carrito) {
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo para la actualización.");
        }

        List<Carrito> carritos = listarTodos();
        boolean encontrado = false;

        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                encontrado = true;
                System.out.println("Carrito actualizado en la colección en memoria: " + carrito.getCodigo());
                break;
            }
        }

        if (!encontrado) {
            System.err.println("Error: El carrito con código " + carrito.getCodigo() + " no fue encontrado para actualizar.");
            return;
        }

        saveAllCarritos(carritos);
    }

    /**
     * Elimina un carrito del sistema de archivos, removiendo su entrada de la colección completa.
     * La operación implica cargar todos los carritos, eliminar el carrito especificado,
     * y luego volver a guardar la lista reducida en los archivos.
     *
     * @param codigo El código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        List<Carrito> carritos = listarTodos();

        boolean removido = carritos.removeIf(c -> c.getCodigo() == codigo);

        if (removido) {
            System.out.println("Carrito eliminado de la colección en memoria: " + codigo);
            saveAllCarritos(carritos);
        } else {
            System.err.println("Error: El carrito con código " + codigo + " no fue encontrado para eliminar.");
        }
    }

    /**
     * Lista todos los carritos persistidos en el sistema de archivos.
     * Lee la lista completa de carritos del único archivo binario (.dat)
     * y la deserializa. Si el archivo binario no existe o hay un error de lectura,
     * intenta reconstruir la lista desde el archivo de texto.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Carrito} encontrados.
     * Retorna una lista vacía si no hay carritos o si ocurren errores de lectura en ambos formatos.
     */
    @Override
    public List<Carrito> listarTodos() {
        List<Carrito> carritos = new ArrayList<>();
        File binaryFile = new File(ALL_CARTS_BINARY_FILE_PATH);

        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    carritos.addAll((List<Carrito>) obj);
                } else {
                    System.err.println("El contenido del archivo binario no es una lista de carritos. Intentando leer del archivo de texto.");
                    carritos = readAllCarritosFromTextFile();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer la lista de carritos del archivo BINARIO principal: " + e.getMessage());
                System.out.println("Intentando reconstruir la lista desde el archivo de texto principal...");
                carritos = readAllCarritosFromTextFile();
            }
        } else {
            System.out.println("Archivo binario de carritos principal no encontrado. Intentando leer del archivo de texto principal...");
            carritos = readAllCarritosFromTextFile();
        }
        return carritos;
    }

    /**
     * Método auxiliar para reconstruir una lista de objetos {@link Carrito} a partir de su representación
     * en un único archivo de texto plano con formato de longitud fija, cada carrito separado por delimitadores.
     * Debido a la complejidad de reconstruir la lista de ítems de carrito desde una sola línea de texto de longitud fija,
     * los carritos recuperados de texto tendrán una lista de ítems vacía y el total recalculado si el modelo lo permite.
     * Este método es principalmente un respaldo.
     *
     * @return La {@link List} de objetos {@link Carrito} reconstruidos con los datos disponibles en el archivo de texto,
     * o una lista vacía si falla la lectura o el parseo.
     */
    private List<Carrito> readAllCarritosFromTextFile() {
        List<Carrito> carritos = new ArrayList<>();
        File textFile = new File(ALL_CARTS_TEXT_FILE_PATH);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals("--- INICIO CARRITO ---")) {
                        try {
                            int parsedCodigo = 0;
                            String parsedUsuarioCedula = "";
                            double parsedTotal = 0.0;

                            line = reader.readLine(); // Código
                            if (line != null && line.startsWith("Código: ")) {
                                parsedCodigo = Integer.parseInt(line.substring("Código: ".length()).trim());
                            }
                            line = reader.readLine(); // Usuario
                            if (line != null && line.startsWith("Usuario (Cédula): ")) {
                                parsedUsuarioCedula = line.substring("Usuario (Cédula): ".length()).trim();
                            }
                            line = reader.readLine(); // Total
                            if (line != null && line.startsWith("Total: $")) {
                                parsedTotal = Double.parseDouble(line.substring("Total: $".length()).trim());
                            }
                            line = reader.readLine(); // Items (solo se lee la línea, no se parsean los ítems para objetos)

                            // Saltar la línea "--- FIN CARRITO ---" y la línea en blanco
                            reader.readLine(); // --- FIN CARRITO ---
                            reader.readLine(); // línea en blanco

                            Usuario dummyUsuario = null;
                            if (!parsedUsuarioCedula.isEmpty() && !parsedUsuarioCedula.equals("N/A")) {
                                dummyUsuario = new Usuario(parsedUsuarioCedula, "", null);
                            } else {
                                dummyUsuario = new Usuario("0000000000", "", null);
                            }

                            Carrito carrito = new Carrito(parsedCodigo, dummyUsuario);
                            carritos.add(carrito);
                        } catch (NumberFormatException | IndexOutOfBoundsException | IOException | NullPointerException ex) { // Captura NPE aquí también
                            System.err.println("Error al procesar bloque de carrito desde archivo de TEXTO. Se omitirá este carrito. Error: " + ex.getMessage());
                            // Consumir las líneas restantes del bloque para no afectar el siguiente carrito
                            while((line = reader.readLine()) != null && !line.trim().equals("--- FIN CARRITO ---")) { /* consumir líneas */ }
                            reader.readLine(); // consumir la línea en blanco después de FIN CARRITO
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer carritos desde archivo de TEXTO principal: " + e.getMessage());
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
        for (Carrito carrito : listarTodos()) {
            if (carrito != null && carrito.getUsuario() != null && carrito.getUsuario().getCedula() != null && carrito.getUsuario().getCedula().equals(usuario.getCedula())) {
                carritosUsuario.add(carrito);
            }
        }
        return carritosUsuario;
    }
}