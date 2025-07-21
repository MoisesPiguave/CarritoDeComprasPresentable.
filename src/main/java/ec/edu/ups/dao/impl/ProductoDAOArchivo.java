package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implementación de {@link ProductoDAO} que gestiona la persistencia de objetos {@link Producto}
 * utilizando dos formatos de archivo en el sistema de ficheros:
 * <ul>
 * <li>Archivo binario (.dat): para el objeto completo serializado.</li>
 * <li>Archivo de texto (.txt): para metadatos clave como código, nombre y precio, con longitud fija.</li>
 * </ul>
 * Esta versión ha sido modificada para almacenar **todos los productos en un único archivo binario**
 * y **un único archivo de texto**, sobrescribiendo el contenido completo en cada operación de guardado.
 * Utiliza operaciones básicas de {@code java.io} y bucles tradicionales,
 * evitando las APIs de {@code java.nio} y {@code java.util.stream}, para mantener el código más directo.
 * Al inicializar, verifica si hay productos existentes y precarga uno si el almacenamiento está vacío.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoDAOArchivo implements ProductoDAO {

    private final String storageDirectoryPath;

    // --- NUEVAS RUTAS DE ARCHIVO GLOBALES para el almacenamiento único de TODOS los productos ---
    private final String ALL_PRODUCTS_BINARY_FILE_PATH;
    private final String ALL_PRODUCTS_TEXT_FILE_PATH;

    // --- Definición de longitudes fijas para el archivo de texto ---
    private static final int CODIGO_LENGTH = 5; // Para códigos como "1", "12345"
    private static final int NOMBRE_LENGTH = 50; // Longitud para el nombre del producto
    private static final int PRECIO_LENGTH = 10; // Para el precio, ej: "12345.67"
    private static final int TOTAL_TEXT_RECORD_LENGTH =
            CODIGO_LENGTH + NOMBRE_LENGTH + PRECIO_LENGTH;

    /**
     * Constructor de la clase ProductoDAOArchivo.
     * Inicializa el directorio de almacenamiento para productos y crea la carpeta si no existe.
     * Si no se encuentran productos al inicio, crea un producto predeterminado para asegurar un estado inicial.
     *
     * @param directoryPath La ruta base del directorio donde se almacenarán los archivos de productos.
     */
    public ProductoDAOArchivo(String directoryPath) {
        this.storageDirectoryPath = directoryPath + File.separator + "productos";
        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Error al crear el directorio de almacenamiento de productos: " + storageDirectoryPath);
            }
        }

        // --- Inicializar las rutas de archivo globales ---
        this.ALL_PRODUCTS_BINARY_FILE_PATH = this.storageDirectoryPath + File.separator + "all_products.dat";
        this.ALL_PRODUCTS_TEXT_FILE_PATH = this.storageDirectoryPath + File.separator + "all_products.txt";

        // Puedes precargar un producto inicial aquí si lo deseas, como en ProductoDAOMemoria
        // Ahora usarás listarTodos() que lee del archivo único.
        if (listarTodos().isEmpty()) {
            crear(new Producto(1, "Balon Molten 7", 30));
        }
    }

    // Los métodos 'getBinaryFile(int codigo)' y 'getTextFile(int codigo)' ya no son necesarios
    // porque ahora hay un solo archivo para todos los productos.
    /*
    private File getBinaryFile(int codigo) {
        return new File(storageDirectoryPath + File.separator + codigo + ".dat");
    }

    private File getTextFile(int codigo) {
        return new File(storageDirectoryPath + File.separator + codigo + ".txt");
    }
    */

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

    // --- NUEVO MÉTODO AUXILIAR: Guarda toda la lista de productos ---
    /**
     * Guarda la lista completa de productos en el archivo binario y en el archivo de texto.
     * Este método sobrescribe el contenido anterior de ambos archivos.
     *
     * @param productos La lista de {@link Producto} a guardar.
     */
    private void saveAllProducts(List<Producto> productos) {
        // 1. Guardar en archivo BINARIO
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ALL_PRODUCTS_BINARY_FILE_PATH))) {
            oos.writeObject(productos); // Escribe la lista completa de objetos Producto
            System.out.println("Todos los productos guardados en archivo BINARIO: " + ALL_PRODUCTS_BINARY_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar TODOS los productos en archivo BINARIO: " + e.getMessage());
        }

        // 2. Guardar en archivo de TEXTO (formato de longitud fija, cada producto en una línea)
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ALL_PRODUCTS_TEXT_FILE_PATH), StandardCharsets.UTF_8))) {
            for (Producto producto : productos) {
                StringBuilder sb = new StringBuilder();
                sb.append(fixedLengthString(String.valueOf(producto.getCodigo()), CODIGO_LENGTH));
                sb.append(fixedLengthString(producto.getNombre(), NOMBRE_LENGTH));

                String precioAsString = String.format(Locale.US, "%.2f", producto.getPrecio());
                sb.append(fixedLengthString(precioAsString, PRECIO_LENGTH));

                writer.write(sb.toString());
                writer.newLine(); // Cada producto en una nueva línea
            }
            System.out.println("Todos los productos guardados en archivo de TEXTO: " + ALL_PRODUCTS_TEXT_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error al guardar TODOS los productos en archivo de TEXTO: " + e.getMessage());
        }
    }

    /**
     * Crea y persiste un nuevo producto en el sistema de archivos.
     * El producto se añade a la colección existente y se guarda la lista completa
     * en un único archivo binario (serializado) y un único archivo de texto.
     *
     * @param producto El objeto {@link Producto} a ser creado y persistido.
     * @throws IllegalArgumentException Si el producto proporcionado es nulo.
     */
    @Override
    public void crear(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo para la creación.");
        }

        // Paso 1: Leer todos los productos existentes
        List<Producto> productos = listarTodos();

        // Paso 2: Verificar si el producto ya existe por código
        boolean existe = productos.stream().anyMatch(p -> p.getCodigo() == producto.getCodigo());
        if (existe) {
            System.err.println("Error: El producto con código " + producto.getCodigo() + " ya existe. No se creará.");
            return;
        }

        // Paso 3: Añadir el nuevo producto a la lista
        productos.add(producto);
        System.out.println("Producto añadido a la colección en memoria: " + producto.getCodigo());

        // Paso 4: Guardar la lista completa de productos en los archivos
        saveAllProducts(productos);
    }

    /**
     * Busca y recupera un producto por su código de la colección completa de productos.
     *
     * @param codigo El código entero del producto a buscar.
     * @return El objeto {@link Producto} si se encuentra, o {@code null} si no existe un producto con ese código.
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        if (codigo <= 0) {
            return null;
        }
        // Buscar en la lista completa de productos cargada desde los archivos.
        return listarTodos().stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    // El método 'readProductoFromTextFile(int codigo)' ya no es necesario como fallback individual
    // porque la lógica de lectura ahora está centralizada en listarTodos() y readAllProductsFromTextFile().
    /*
    private Producto readProductoFromTextFile(int codigo) {
        File textFile = getTextFile(codigo);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                if (line != null && line.length() >= TOTAL_TEXT_RECORD_LENGTH) {
                    String parsedCodigoStr = line.substring(0, CODIGO_LENGTH).trim();
                    String parsedNombre = line.substring(CODIGO_LENGTH, CODIGO_LENGTH + NOMBRE_LENGTH).trim();
                    String parsedPrecioStr = line.substring(CODIGO_LENGTH + NOMBRE_LENGTH, TOTAL_TEXT_RECORD_LENGTH).trim();

                    int parsedCodigo = Integer.parseInt(parsedCodigoStr);
                    double parsedPrecio = Double.parseDouble(parsedPrecioStr);

                    return new Producto(parsedCodigo, parsedNombre, parsedPrecio);
                }
            } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error al leer o parsear producto de archivo de TEXTO para código " + codigo + ": " + e.getMessage());
            }
        }
        return null;
    }
    */

    /**
     * Busca productos por su nombre (o una parte inicial de él) en la lista de todos los productos.
     * Realiza una búsqueda insensible a mayúsculas y minúsculas.
     *
     * @param nombre El nombre o la parte inicial del nombre del producto a buscar.
     * @return Una {@link List} de objetos {@link Producto} que coinciden con el criterio de búsqueda.
     * Retorna una lista vacía si no se encuentran productos con el nombre especificado.
     */
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        if (nombre == null || nombre.isEmpty()) {
            return productosEncontrados;
        }
        // Se itera sobre todos los productos y se filtra por nombre, sin usar Streams
        for (Producto producto : listarTodos()) { // Ahora listarTodos() lee del archivo único
            if (producto != null && producto.getNombre() != null && producto.getNombre().toLowerCase().startsWith(nombre.toLowerCase())) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    /**
     * Actualiza un producto existente en el sistema de archivos.
     * La operación implica cargar todos los productos, encontrar y reemplazar el producto a actualizar,
     * y luego volver a guardar la lista completa en los archivos.
     *
     * @param producto El objeto {@link Producto} con los datos actualizados.
     * @throws IllegalArgumentException Si el producto proporcionado es nulo.
     */
    @Override
    public void actualizar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo para la actualización.");
        }

        // Paso 1: Cargar todos los productos existentes
        List<Producto> productos = listarTodos();
        boolean encontrado = false;

        // Paso 2: Iterar y actualizar el producto si existe
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto); // Reemplaza el producto en la lista
                encontrado = true;
                System.out.println("Producto actualizado en la colección en memoria: " + producto.getCodigo());
                break;
            }
        }

        if (!encontrado) {
            System.err.println("Error: El producto con código " + producto.getCodigo() + " no fue encontrado para actualizar.");
            return;
        }

        // Paso 3: Guardar la lista completa de productos actualizada
        saveAllProducts(productos);
    }

    /**
     * Elimina un producto del sistema de archivos, removiendo su entrada de la colección completa.
     * La operación implica cargar todos los productos, eliminar el producto especificado,
     * y luego volver a guardar la lista reducida en los archivos.
     *
     * @param codigo El código entero del producto a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        // Paso 1: Cargar todos los productos existentes
        List<Producto> productos = listarTodos();

        // Paso 2: Eliminar el producto de la lista
        boolean removido = productos.removeIf(p -> p.getCodigo() == codigo);

        if (removido) {
            System.out.println("Producto eliminado de la colección en memoria: " + codigo);
            // Paso 3: Guardar la lista completa de productos actualizada (sin el eliminado)
            saveAllProducts(productos);
        } else {
            System.err.println("Error: El producto con código " + codigo + " no fue encontrado para eliminar.");
        }
    }

    /**
     * Lista todos los productos persistidos en el sistema de archivos.
     * Lee la lista completa de productos del único archivo binario (.dat)
     * y la deserializa. Si el archivo binario no existe o hay un error de lectura,
     * intenta reconstruir la lista desde el archivo de texto.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Producto} encontrados.
     * Retorna una lista vacía si no hay productos o si ocurren errores de lectura en ambos formatos.
     */
    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        File binaryFile = new File(ALL_PRODUCTS_BINARY_FILE_PATH);

        // Intentar leer del archivo binario principal
        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    productos.addAll((List<Producto>) obj);
                } else {
                    System.err.println("El contenido del archivo binario no es una lista de productos. Intentando leer del archivo de texto.");
                    // Si el archivo binario no contiene una lista, intenta leer del texto como fallback.
                    productos = readAllProductsFromTextFile();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer la lista de productos del archivo BINARIO principal: " + e.getMessage());
                System.out.println("Intentando reconstruir la lista desde el archivo de texto principal...");
                // Si falla la lectura binaria, intentar leer del archivo de texto
                productos = readAllProductsFromTextFile();
            }
        } else {
            System.out.println("Archivo binario de productos principal no encontrado. Intentando leer del archivo de texto principal...");
            // Si el binario no existe, intentar leer del archivo de texto
            productos = readAllProductsFromTextFile();
        }
        return productos;
    }

    // --- NUEVO MÉTODO AUXILIAR PARA LEER LA LISTA COMPLETA DE PRODUCTOS DESDE EL ARCHIVO DE TEXTO ---
    /**
     * Método auxiliar para reconstruir una lista de objetos {@link Producto} a partir de su representación
     * en un único archivo de texto plano con formato de longitud fija, una línea por producto.
     *
     * @return La {@link List} de objetos {@link Producto} reconstruidos con los datos disponibles en el archivo de texto,
     * o una lista vacía si falla la lectura o el parseo.
     */
    private List<Producto> readAllProductsFromTextFile() {
        List<Producto> productos = new ArrayList<>();
        File textFile = new File(ALL_PRODUCTS_TEXT_FILE_PATH);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.length() >= TOTAL_TEXT_RECORD_LENGTH) {
                        // Parsear los campos de la línea de longitud fija
                        String parsedCodigoStr = line.substring(0, CODIGO_LENGTH).trim();
                        String parsedNombre = line.substring(CODIGO_LENGTH, CODIGO_LENGTH + NOMBRE_LENGTH).trim();
                        String parsedPrecioStr = line.substring(CODIGO_LENGTH + NOMBRE_LENGTH, TOTAL_TEXT_RECORD_LENGTH).trim();

                        int parsedCodigo = Integer.parseInt(parsedCodigoStr);
                        double parsedPrecio = Double.parseDouble(parsedPrecioStr);

                        productos.add(new Producto(parsedCodigo, parsedNombre, parsedPrecio));
                    } else {
                        System.err.println("Línea de texto de producto incompleta o corrupta: " + line);
                    }
                }
            } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Error al leer o parsear productos de archivo de TEXTO principal: " + e.getMessage());
            }
        }
        return productos;
    }
}