package ec.edu.ups.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase de utilidad {@code MensajeInternacionalizacionHandler} que facilita la gestión
 * de mensajes internacionalizados en la aplicación. Permite cargar y acceder a mensajes
 * específicos de un idioma y país ({@link Locale}) a través de archivos de propiedades
 * ({@link ResourceBundle}), normalmente nombrados "mensajes_xx_YY.properties".
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class MensajeInternacionalizacionHandler {

    private ResourceBundle bundle;
    private Locale locale;

    /**
     * Constructor de la clase MensajeInternacionalizacionHandler.
     * Inicializa el manejador cargando los mensajes para el lenguaje y país especificados.
     * El archivo de propiedades base esperado es "mensajes.properties" o sus variantes localizadas.
     *
     * @param lenguaje El código de lenguaje ISO 639-1 (ej., "es" para español, "en" para inglés).
     * @param pais El código de país ISO 3166-1 alpha-2 (ej., "EC" para Ecuador, "US" para Estados Unidos).
     */
    public MensajeInternacionalizacionHandler(String lenguaje, String pais) {
        this.locale = new Locale(lenguaje, pais);
        this.bundle = ResourceBundle.getBundle("mensajes", locale);
    }

    /**
     * Recupera un mensaje internacionalizado utilizando una clave.
     * La clave debe corresponder a una entrada definida en el archivo de propiedades
     * del {@link Locale} actualmente configurado.
     *
     * @param key La clave {@code String} del mensaje a recuperar.
     * @return El mensaje {@code String} asociado a la clave en el idioma actual.
     * @throws java.util.MissingResourceException Si la clave no se encuentra en el ResourceBundle.
     */
    public String get(String key) {
        return bundle.getString(key);
    }

    /**
     * Cambia el lenguaje y país de los mensajes internacionalizados.
     * Esto recarga el {@link ResourceBundle} con los mensajes correspondientes
     * al nuevo {@link Locale}, afectando los mensajes recuperados posteriormente.
     *
     * @param lenguaje El nuevo código de lenguaje ISO 639-1.
     * @param pais El nuevo código de país ISO 3166-1 alpha-2.
     */
    public void setLenguaje(String lenguaje, String pais) {
        this.locale = new Locale(lenguaje, pais);
        this.bundle = ResourceBundle.getBundle("mensajes", locale);
    }

    /**
     * Obtiene el {@link Locale} actualmente configurado en el manejador.
     *
     * @return El objeto {@link Locale} actual.
     */
    public Locale getLocale() {
        return locale;
    }
}