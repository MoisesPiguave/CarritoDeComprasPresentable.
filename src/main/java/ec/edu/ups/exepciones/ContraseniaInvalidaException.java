package ec.edu.ups.exepciones;

/**
 * Excepción personalizada {@code ContraseniaInvalidaException} que se lanza cuando
 * una contraseña no cumple con los criterios de seguridad o formato requeridos.
 * <p>
 * Esta es una excepción de tiempo de ejecución (hereda de {@link RuntimeException}),
 * lo que significa que no es obligatorio declararla en la cláusula {@code throws} de un método
 * ni capturarla explícitamente, aunque se recomienda manejarla para proporcionar retroalimentación
 * adecuada al usuario.
 * </p>
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class ContraseniaInvalidaException extends RuntimeException {
    /**
     * Construye una nueva {@code ContraseniaInvalidaException} con el mensaje detallado especificado.
     *
     * @param message El mensaje detallado (que puede ser recuperado por el método {@link Throwable#getMessage()}).
     */
    public ContraseniaInvalidaException(String message) {
        super(message);
    }
}