package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.io.*;
import java.nio.charset.StandardCharsets; // A pesar de no usar java.nio.file.Files, StandardCharsets sigue siendo útil para especificar la codificación.
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // Necesario para String.format de double

/**
 * Implementación de {@link ProductoDAO} que gestiona la persistencia de objetos {@link Producto}
 * utilizando dos formatos de archivo en el sistema de ficheros:
 * <ul>
 * <li>Archivo binario (.dat): para el objeto completo serializado.</li>
 * <li>Archivo de texto (.txt): para metadatos clave como código, nombre y precio, con longitud fija.</li>
 * </ul>
 * Esta versión utiliza operaciones básicas de {@code java.io} y bucles tradicionales,
 * evitando las APIs de {@code java.nio} y {@code java.util.stream}, para mantener el código más directo.
 * Al inicializar, verifica si hay productos existentes y precarga uno si el almacenamiento está vacío.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ProductoDAOArchivo implements ProductoDAO {

    private final String storageDirectoryPath;

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
        // Concatenamos para crear la ruta específica para productos, usando File.separator para compatibilidad.
        this.storageDirectoryPath = directoryPath + File.separator + "productos";
        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) { // Crea el directorio si no existe. Devuelve false si falla.
                System.err.println("Error al crear el directorio de almacenamiento de productos: " + storageDirectoryPath);
            }
        }
        // Puedes precargar un producto inicial aquí si lo deseas, como en ProductoDAOMemoria
        if (listarTodos().isEmpty()) {
            crear(new Producto(1, "Balon Molten 7", 30));
        }
    }

    /**
     * Construye el objeto {@link File} para el archivo binario de un producto dado su código.
     * El archivo binario guarda el objeto {@link Producto} completo serializado.
     *
     * @param codigo El código del producto.
     * @return El objeto {@link File} que representa el archivo binario (.dat) del producto.
     */
    private File getBinaryFile(int codigo) {
        return new File(storageDirectoryPath + File.separator + codigo + ".dat");
    }

    /**
     * Construye el objeto {@link File} para el archivo de texto de un producto dado su código.
     * El archivo de texto guarda metadatos en un formato de longitud fija.
     *
     * @param codigo El código del producto.
     * @return El objeto {@link File} que representa el archivo de texto (.txt) del producto.
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
     * Crea y persiste un nuevo producto en el sistema de archivos.
     * El producto se guarda en un archivo binario (serializado) y en un archivo de texto
     * con formato de longitud fija para sus atributos clave.
     *
     * @param producto El objeto {@link Producto} a ser creado y persistido.
     * @throws IllegalArgumentException Si el producto proporcionado es nulo.
     */
    @Override
    public void crear(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo para la creación.");
        }

        File binaryFile = getBinaryFile(producto.getCodigo());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryFile))) {
            oos.writeObject(producto);
            System.out.println("Producto creado y guardado BINARIO, codigo: " + producto.getCodigo());
        } catch (IOException e) {
            System.err.println("Error al guardar producto en archivo BINARIO: " + e.getMessage());
        }

        File textFile = getTextFile(producto.getCodigo());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFile), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            sb.append(fixedLengthString(String.valueOf(producto.getCodigo()), CODIGO_LENGTH));
            sb.append(fixedLengthString(producto.getNombre(), NOMBRE_LENGTH));

            // Formatea el precio a 2 decimales y luego lo ajusta a la longitud fija
            String precioAsString = String.format(Locale.US, "%.2f", producto.getPrecio());
            sb.append(fixedLengthString(precioAsString, PRECIO_LENGTH));

            writer.write(sb.toString());
            System.out.println("Producto creado y guardado TEXTO, codigo: " + producto.getCodigo());
        } catch (IOException e) {
            System.err.println("Error al guardar producto en archivo de TEXTO: " + e.getMessage());
        }
    }

    /**
     * Busca y recupera un producto por su código.
     * Prioriza la lectura del archivo binario. Si este no existe o hay un error de lectura,
     * intenta leer el producto desde su representación en archivo de texto plano como fallback.
     *
     * @param codigo El código entero del producto a buscar.
     * @return El objeto {@link Producto} si se encuentra, o {@code null} si no existe un producto con ese código
     * o si ocurre un error durante la lectura en ambos formatos.
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        if (codigo <= 0) {
            return null;
        }

        File binaryFile = getBinaryFile(codigo);
        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                return (Producto) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al leer producto de archivo BINARIO para código " + codigo + ", intentando archivo de texto: " + e.getMessage());
                // Si falla la lectura binaria, intenta leer del archivo de texto
                return readProductoFromTextFile(codigo);
            }
        } else {
            // Si el binario no existe, intentar leer del archivo de texto
            return readProductoFromTextFile(codigo);
        }
    }

    /**
     * Método auxiliar para reconstruir un objeto {@link Producto} a partir de su representación
     * en un archivo de texto plano con formato de longitud fija.
     *
     * @param codigo El código del producto a leer.
     * @return El objeto {@link Producto} reconstruido, o {@code null} si falla la lectura o el parseo.
     */
    private Producto readProductoFromTextFile(int codigo) {
        File textFile = getTextFile(codigo);
        if (textFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                if (line != null && line.length() >= TOTAL_TEXT_RECORD_LENGTH) {
                    // Parsear los campos de la línea de longitud fija
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
        for (Producto producto : listarTodos()) {
            if (producto != null && producto.getNombre() != null && producto.getNombre().toLowerCase().startsWith(nombre.toLowerCase())) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    /**
     * Actualiza un producto existente en el sistema de archivos.
     * La operación de actualización se implementa eliminando las versiones anteriores del producto
     * (binaria y de texto) y luego creando nuevas con los datos actualizados.
     *
     * @param producto El objeto {@link Producto} con los datos actualizados.
     * @throws IllegalArgumentException Si el producto proporcionado es nulo.
     */
    @Override
    public void actualizar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo para la actualización.");
        }
        eliminar(producto.getCodigo()); // Elimina los archivos existentes (binario y texto)
        crear(producto); // Crea nuevos archivos con la información actualizada
    }

    /**
     * Elimina un producto del sistema de archivos, removiendo tanto su archivo binario como de texto.
     *
     * @param codigo El código entero del producto a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        File binaryFile = getBinaryFile(codigo);
        File textFile = getTextFile(codigo);

        boolean binaryDeleted = false;
        boolean textDeleted = false;

        if (binaryFile.exists()) {
            binaryDeleted = binaryFile.delete();
            if (!binaryDeleted) {
                System.err.println("Error al eliminar el archivo BINARIO del producto con código " + codigo);
            }
        } else {
            binaryDeleted = true; // Si no existe, se considera "eliminado"
        }

        if (textFile.exists()) {
            textDeleted = textFile.delete();
            if (!textDeleted) {
                System.err.println("Error al eliminar el archivo de TEXTO del producto con código " + codigo);
            }
        } else {
            textDeleted = true; // Si no existe, se considera "eliminado"
        }

        if (binaryDeleted && textDeleted) {
            System.out.println("Producto eliminado (binario y texto), codigo: " + codigo);
        } else {
            System.err.println("Advertencia: No se pudieron eliminar todos los archivos del producto con código " + codigo);
        }
    }

    /**
     * Lista todos los productos persistidos en el sistema de archivos.
     * Prioriza la lectura de los archivos binarios (.dat) para reconstruir los objetos {@link Producto} completos.
     * Si un archivo binario no se puede leer, se imprime un error y ese producto no se incluye en la lista.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Producto} encontrados.
     * Retorna una lista vacía si no hay productos o si ocurren errores de lectura.
     */
    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        File directory = new File(storageDirectoryPath);
        File[] files = directory.listFiles(); // Lista todos los archivos en el directorio

        if (files != null) {
            for (File file : files) {
                // Solo consideramos los archivos binarios para listar, ya que son los que tienen el objeto completo.
                if (file.isFile() && file.getName().endsWith(".dat")) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        Producto producto = (Producto) ois.readObject();
                        if (producto != null) {
                            productos.add(producto);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("Error al listar producto desde archivo BINARIO: " + file.getName() + " - " + e.getMessage());
                        // Si hay un error al leer un archivo, simplemente lo omitimos y continuamos con los demás.
                    }
                }
            }
        }
        return productos;
    }
}