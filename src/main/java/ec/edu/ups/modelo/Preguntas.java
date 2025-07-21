package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * Representa una pregunta dentro de un cuestionario de seguridad,
 * típicamente usada para la recuperación de contraseñas u otros procesos de verificación.
 * Contiene un código único y el texto de la pregunta.
 * Implementa {@link Serializable} para permitir la persistencia del objeto en archivos
 * o su transmisión a través de la red.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class Preguntas implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String preguntas;

    /**
     * Constructor de la clase Preguntas.
     * Inicializa una nueva pregunta con un código y un texto de pregunta específicos.
     *
     * @param codigo El código único que identifica esta pregunta.
     * @param preguntas El texto de la pregunta.
     */
    public Preguntas(String codigo, String preguntas) {
        this.codigo = codigo;
        this.preguntas = preguntas;
    }

    /**
     * Obtiene el código único de la pregunta.
     *
     * @return El código de la pregunta como una cadena de texto.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único de la pregunta.
     *
     * @param codigo El código de la pregunta a establecer.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el texto completo de la pregunta.
     *
     * @return El texto de la pregunta.
     */
    public String getPreguntas() {
        return preguntas;
    }

    /**
     * Establece el texto de la pregunta.
     *
     * @param preguntas El texto de la pregunta a establecer.
     */
    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }

    /**
     * Retorna una representación en cadena de texto de la pregunta,
     * que en este caso es simplemente el texto de la pregunta.
     * Esto es útil para mostrar la pregunta directamente en componentes de UI como JComboBox.
     *
     * @return El texto de la pregunta.
     */
    @Override
    public String toString() {
        return preguntas;
    }
}