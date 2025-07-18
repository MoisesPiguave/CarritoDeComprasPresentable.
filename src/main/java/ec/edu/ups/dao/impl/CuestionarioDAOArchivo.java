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
import java.io.PrintWriter;
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

    private static final String PREGUNTAS_BIN_FILENAME = "preguntas.dat";
    private static final String PREGUNTAS_TXT_FILENAME = "preguntas.txt";

    public CuestionarioDAOArchivo(String directoryPath, MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        this.preguntas = new ArrayList<>();
        this.storageDirectoryPath = directoryPath + File.separator + "cuestionarios";
        File directory = new File(this.storageDirectoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Error al crear el directorio de almacenamiento de cuestionarios: " + storageDirectoryPath);
            }
        }

        cargarPreguntasDesdeArchivo();
        if (this.preguntas.isEmpty()) {
            cargarPreguntasPorDefecto();
            guardarPreguntasEnArchivo();
        }
    }

    private File getBinaryFile() {
        return new File(storageDirectoryPath + File.separator + PREGUNTAS_BIN_FILENAME);
    }

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

    private void guardarPreguntasEnArchivo() {
        File binaryFile = getBinaryFile();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryFile))) {
            oos.writeObject(new ArrayList<>(preguntas));
            System.out.println("Preguntas guardadas en archivo BINARIO.");
            guardarPreguntasEnTexto();
        } catch (IOException e) {
            System.err.println("Error al guardar preguntas en archivo BINARIO: " + e.getMessage());
        }
    }

    private void guardarPreguntasEnTexto() {
        File textFile = new File(storageDirectoryPath + File.separator + PREGUNTAS_TXT_FILENAME);
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(textFile))) {
            for (Preguntas pregunta : preguntas) {
                writer.println(pregunta.getCodigo() + " - " + pregunta.getPreguntas());
            }
            System.out.println("Preguntas también guardadas en archivo de TEXTO.");
        } catch (IOException e) {
            System.err.println("Error al guardar preguntas en archivo de TEXTO: " + e.getMessage());
        }
    }

    private void cargarPreguntasDesdeArchivo() {
        File binaryFile = getBinaryFile();
        if (binaryFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryFile))) {
                this.preguntas = (List<Preguntas>) ois.readObject();
                System.out.println("Preguntas cargadas desde archivo BINARIO.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar preguntas desde archivo BINARIO: " + e.getMessage());
            }
        }
    }

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

    @Override
    public List<Preguntas> listarPreguntas() {
        return new ArrayList<>(preguntas);
    }

    @Override
    public List<Preguntas> listarPreguntass() {
        return new ArrayList<>(preguntas);
    }

    public void actualizarIdioma(MensajeInternacionalizacionHandler nuevoMi) {
        this.idioma = nuevoMi;
        cargarPreguntasPorDefecto();
        guardarPreguntasEnArchivo();
        System.out.println("Idioma de cuestionario actualizado y preguntas recargadas.");
    }
}
