package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * Representa un par de pregunta y respuesta, comúnmente utilizado para almacenar
 * las preguntas de seguridad y sus respuestas correspondientes para un usuario.
 * Esta clase asocia una {@link Preguntas} con su {@link Respuesta}.
 * Implementa {@link Serializable} para permitir la persistencia del objeto
 * en archivos o su transmisión a través de la red.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class PreguntasRespuestas implements Serializable {
    private static final long serialVersionUID = 1L;

    private Preguntas preguntas;
    private Respuesta respuesta;

    /**
     * Constructor de la clase PreguntasRespuestas.
     * Inicializa un nuevo par de pregunta y respuesta.
     *
     * @param preguntas El objeto {@link Preguntas} asociado.
     * @param respuesta El objeto {@link Respuesta} correspondiente a la pregunta.
     */
    public PreguntasRespuestas( Preguntas preguntas, Respuesta respuesta) {
        this.preguntas = preguntas;
        this.respuesta = respuesta;
    }

    /**
     * Obtiene el objeto {@link Preguntas} de este par.
     *
     * @return El objeto {@link Preguntas}.
     */
    public Preguntas getPreguntas() {
        return preguntas;
    }

    /**
     * Establece el objeto {@link Preguntas} para este par.
     *
     * @param preguntas El objeto {@link Preguntas} a establecer.
     */
    public void setPreguntas(Preguntas preguntas) {
        this.preguntas = preguntas;
    }

    /**
     * Obtiene el objeto {@link Respuesta} de este par.
     *
     * @return El objeto {@link Respuesta}.
     */
    public Respuesta getRespuesta() {
        return respuesta;
    }

    /**
     * Establece el objeto {@link Respuesta} para este par.
     *
     * @param respuesta El objeto {@link Respuesta} a establecer.
     */
    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * Retorna una representación en cadena de texto del objeto PreguntasRespuestas.
     * Incluye las representaciones en cadena de la pregunta y la respuesta asociadas.
     *
     * @return Una cadena formateada que describe el par pregunta-respuesta.
     */
    @Override
    public String toString() {
        return "PreguntasRespuestas{" +
                "preguntas=" + preguntas +
                ", respuesta=" + respuesta +
                '}';
    }
}