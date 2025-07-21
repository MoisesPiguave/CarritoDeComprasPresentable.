package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.modelo.Preguntas;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementación de {@link CuestionarioDAO} que gestiona las preguntas de un cuestionario
 * en memoria. Las preguntas son predefinidas y cargadas al inicializar la clase,
 * y no persisten al finalizar la ejecución de la aplicación. Permite la actualización
 * del idioma de las preguntas cargadas.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CuestionarioDAOMemoria implements CuestionarioDAO {

    private List<Preguntas> preguntas;
    private MensajeInternacionalizacionHandler idioma;

    /**
     * Constructor de la clase CuestionarioDAOMemoria.
     * Inicializa la lista de preguntas y carga el conjunto de preguntas por defecto
     * utilizando el manejador de internacionalización proporcionado.
     *
     * @param idioma El manejador de internacionalización {@link MensajeInternacionalizacionHandler}
     * que se utilizará para obtener los textos de las preguntas.
     */
    public CuestionarioDAOMemoria(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        this.preguntas = new ArrayList<>();
        cargarPreguntas();
    }

    /**
     * Carga un conjunto predefinido de preguntas en la lista en memoria.
     * Este método vacía la lista actual y la rellena con preguntas cuyos textos
     * se obtienen a través del manejador de internacionalización, permitiendo
     * que las preguntas se muestren en el idioma configurado.
     */
    public void cargarPreguntas() {
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
     * Añade una nueva pregunta a la lista de preguntas en memoria.
     * Como esta implementación es en memoria, la pregunta solo existirá
     * durante la ejecución actual de la aplicación.
     *
     * @param pregunta El objeto {@link Preguntas} a añadir.
     */
    @Override
    public void crear(Preguntas pregunta) {
        preguntas.add(pregunta);
    }

    /**
     * Recupera todas las preguntas actualmente almacenadas en memoria.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Preguntas} cargados.
     */
    @Override
    public List<Preguntas> listarPreguntas() {
        return preguntas;
    }

    /**
     * Recupera todas las preguntas actualmente almacenadas en memoria.
     * Este método es funcionalmente idéntico a {@link #listarPreguntas()}.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Preguntas} cargados.
     */
    @Override
    public List<Preguntas> listarPreguntass() {
        return preguntas;
    }

    /**
     * Actualiza el manejador de internacionalización utilizado por esta clase
     * y recarga las preguntas para que sus textos se presenten en el nuevo idioma.
     *
     * @param nuevoMi El nuevo manejador de internacionalización {@link MensajeInternacionalizacionHandler}
     * a utilizar.
     */
    public void actualizarIdioma(MensajeInternacionalizacionHandler nuevoMi) {
        this.idioma = nuevoMi;
        cargarPreguntas();
    }
}