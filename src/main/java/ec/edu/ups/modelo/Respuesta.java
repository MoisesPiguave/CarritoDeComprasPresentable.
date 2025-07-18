package ec.edu.ups.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la respuesta a una pregunta de seguridad.
 * Contiene el texto de la respuesta.
 * Implementa {@link Serializable} para permitir la persistencia del objeto
 * en archivos o su transmisión a través de la red.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class Respuesta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String texto;

    /**
     * Constructor de la clase Respuesta.
     * Inicializa una nueva respuesta con el texto especificado.
     *
     * @param texto El texto de la respuesta.
     */
    public Respuesta(String texto){
        this.texto = texto;
    }

    /**
     * Obtiene el texto de la respuesta.
     *
     * @return El texto de la respuesta.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Establece el texto de la respuesta.
     *
     * @param texto El texto de la respuesta a establecer.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Retorna una representación en cadena de texto del objeto Respuesta,
     * que en este caso es simplemente el texto de la respuesta.
     *
     * @return El texto de la respuesta.
     */
    @Override
    public String toString(){
        return texto;
    }
}