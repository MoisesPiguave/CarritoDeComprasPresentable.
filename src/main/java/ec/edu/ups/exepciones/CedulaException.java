package ec.edu.ups.exepciones;

/**
 * Excepción personalizada {@code CedulaException} que se lanza cuando una operación
 * relacionada con una cédula (documento de identidad en Ecuador) falla debido a una validación
 * incorrecta o a un formato inválido.
 * <p>
 * Esta es una excepción de tiempo de ejecución (hereda de {@link RuntimeException}),
 * lo que significa que no es necesario declararla en la cláusula {@code throws} de un método
 * ni capturarla explícitamente, aunque es buena práctica manejarla si se espera que ocurra.
 * </p>
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class CedulaException extends RuntimeException {
    /**
     * Construye una nueva {@code CedulaException} con el mensaje detallado especificado.
     *
     * @param message El mensaje detallado (que puede ser recuperado por el método {@link Throwable#getMessage()}).
     */
    public CedulaException(String message) {
        super(message);
    }
}