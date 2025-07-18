package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.modelo.Preguntas;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link CuestionarioDAO} que gestiona la persistencia de objetos {@link Preguntas}
 * utilizando archivos binarios (serialización de objetos) en el sistema de ficheros.
 * Esta versión se adhiere al principio KISS, simplificando el manejo de archivos
 * mediante operaciones directas de {@code java.io} y bucles tradicionales,
 * eliminando el uso de {@code java.nio.file.Files} y {@code java.util.stream.Stream}.
 * Al iniciar, intenta cargar preguntas existentes; si no hay, carga un conjunto de preguntas por defecto.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CuestionarioDAOArchivo implements CuestionarioDAO {

    private final String storageDirectoryPath;
    private List<Preguntas> preguntas;
    private MensajeInternacionalizacionHandler idioma;

    // Nombre de archivo fijo para las preguntas, ya que son un conjunto único
    private static final String PREGUNTAS_BIN_FILENAME = "preguntas.dat";

    /**
     * Constructor de la clase CuestionarioDAOArchivo.
     * Inicializa el directorio de almacenamiento y carga las preguntas existentes o las crea por defecto.
     *
     * @param directoryPath La ruta base del directorio donde se almacenarán los archivos de cuestionarios.
     * @param idioma El manejador de internacionalización de mensajes para cargar las preguntas por defecto.
     */
    public CuestionarioDAOArchivo(String directoryPath, MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        this.preguntas = new ArrayList<>();
        // Creamos una subcarpeta 'cuestionarios' dentro del directorio seleccionado por el usuario
        this.storageDirectoryPath = directoryPath + File.separator + "cuestionarios";
        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) { // Crea el directorio si no existe
                System.err.println("Error al crear el directorio de almacenamiento de cuestionarios: " + storageDirectoryPath);
            }
        }

        cargarPreguntasDesdeArchivo(); // Intentar cargar preguntas al iniciar
        if (this.preguntas.isEmpty()) { // Si no se cargaron, crearlas por defecto
            cargarPreguntasPorDefecto();
            guardarPreguntasEnArchivo(); // Guardar las preguntas por defecto
        }
    }

    /**
     * Construye el objeto {@link File} para el archivo binario donde se guardan las preguntas.
     *
     * @return El objeto {@link File} que representa el archivo binario (.dat) de preguntas.
     */
    private File getBinaryFile() {
        return new File(storageDirectoryPath + File.separator + PREGUNTAS_BIN_FILENAME);
    }

    /**
     * Carga un conjunto de preguntas predefinidas en la lista de preguntas en memoria.
     * Este método se utiliza cuando no se encuentran preguntas persistidas al inicio.
     */
    private void cargarPreguntasPorDefecto() {
        preguntas.clear();
        preguntas.add(new Preguntas("1", idioma.get("pregunta.color_favorito")));
        preguntas.add(new Preguntas("2", idioma.get("pregunta.instrumento_musical_favorito")));
        preguntas.add(new Preguntas("3", idioma.get("pregunta.comida_favorita")));
        preguntas.add(new Preguntas("4", idioma.get("pregunta.deporte_favorito")));
        preguntas.add(new Preguntas("5", idioma.get("pregunta.nombre_de_tu_mascota")));
        preguntas.add(new Preguntas("6", idioma.get("pregunta.artista_favorito")));
        preguntas.add(new Preguntas("7", idioma.get("pregunta.comida_que_no_te_gusta")));
        preguntas.add(new Preguntas("8", idioma.get("pregunta.anime_favorito")));
        preguntas.add(new Preguntas("9", idioma.get("pregunta.messi_o_ronaldo")));
    }

    /**
     * Guarda la lista actual de preguntas en el sistema de archivos utilizando serialización binaria.
     */
    private void guardarPreguntasEnArchivo() {
        File binaryFile = getBinaryFile();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryFile))) {
            oos.writeObject(new ArrayList<>(preguntas));
            System.out.println("Preguntas guardadas en archivo BINARIO.");
        } catch (IOException e) {
            System.err.println("Error al guardar preguntas en archivo BINARIO: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de preguntas desde el sistema de archivos, priorizando el archivo binario.
     */
    private void cargarPreguntasDesdeArchivo() {
        File binaryFile = getBinaryFile();
        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                this.preguntas = (List<Preguntas>) ois.readObject();
                System.out.println("Preguntas cargadas desde archivo BINARIO.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar preguntas desde archivo BINARIO: " + e.getMessage());
                // Si falla la lectura binaria, la lista de preguntas quedará vacía y se cargarán por defecto.
            }
        }
    }

    /**
     * Crea y persiste una nueva pregunta en la lista en memoria y en el archivo.
     *
     * @param pregunta El objeto {@link Preguntas} a ser creado y persistido.
     * @throws IllegalArgumentException Si la pregunta o su código son nulos o vacíos.
     */
    @Override
    public void crear(Preguntas pregunta) {
        if (pregunta == null || pregunta.getCodigo() == null || pregunta.getCodigo().isEmpty()) {
            throw new IllegalArgumentException("La pregunta y su código no pueden ser nulos o vacíos.");
        }

        boolean existe = false;
        for (Preguntas p : preguntas) {
            if (p.getCodigo().equals(pregunta.getCodigo())) {
                existe = true;
                break;
            }
        }

        if (!existe) {
            preguntas.add(pregunta);
            guardarPreguntasEnArchivo();
            System.out.println("Pregunta creada y guardada: " + pregunta.getPreguntas());
        } else {
            System.out.println("La pregunta con código " + pregunta.getCodigo() + " ya existe.");
        }
    }

    /**
     * Lista todas las preguntas actualmente cargadas en memoria.
     * Retorna una **copia** de la lista para evitar modificaciones externas directas a la lista interna.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Preguntas}.
     */
    @Override
    public List<Preguntas> listarPreguntas() {
        return new ArrayList<>(preguntas);
    }

    /**
     * Lista todas las preguntas actualmente cargadas en memoria.
     * Este método es funcionalmente idéntico a {@link #listarPreguntas()} y podría
     * considerarse redundante en un diseño más simplificado.
     * Retorna una **copia** de la lista para evitar modificaciones externas directas a la lista interna.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Preguntas}.
     */
    @Override
    public List<Preguntas> listarPreguntass() {
        return new ArrayList<>(preguntas);
    }

    /**
     * Actualiza el manejador de internacionalización y recarga las preguntas por defecto
     * con los textos en el nuevo idioma, persistiendo estos cambios en los archivos.
     *
     * @param nuevoMi El nuevo {@link MensajeInternacionalizacionHandler} a utilizar.
     */
    public void actualizarIdioma(MensajeInternacionalizacionHandler nuevoMi) {
        this.idioma = nuevoMi;
        cargarPreguntasPorDefecto();
        guardarPreguntasEnArchivo();
        System.out.println("Idioma de cuestionario actualizado y preguntas recargadas.");
    }
}