package ec.edu.ups.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase de utilidad {@code FormateadorUtils} que proporciona métodos estáticos
 * para formatear cantidades monetarias y fechas de acuerdo a un {@link Locale} específico.
 * Esto facilita la internacionalización de la presentación de datos en la aplicación.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class FormateadorUtils {

    /**
     * Formatea una cantidad numérica como una cadena de moneda utilizando el {@link Locale} proporcionado.
     * Por ejemplo, para un locale de Ecuador (es-EC), 123.45 se podría formatear como "$123.45".
     *
     * @param cantidad La cantidad {@code double} a formatear.
     * @param locale El {@link Locale} que define las convenciones de formato de moneda (símbolo, separadores).
     * @return Una {@code String} que representa la cantidad formateada como moneda.
     */
    public static String formatearMoneda(double cantidad, Locale locale) {
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(locale);
        return formatoMoneda.format(cantidad);
    }

    /**
     * Formatea un objeto {@link Date} como una cadena de fecha de acuerdo al {@link Locale} proporcionado.
     * El formato de la fecha será {@link DateFormat#MEDIUM}, que suele ser un formato legible
     * como "17 de julio de 2025" o "Jul 17, 2025" dependiendo del locale.
     *
     * @param fecha El objeto {@link Date} a formatear.
     * @param locale El {@link Locale} que define las convenciones de formato de fecha.
     * @return Una {@code String} que representa la fecha formateada.
     */
    public static String formatearFecha(Date fecha, Locale locale) {
        DateFormat formato = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        return formato.format(fecha);
    }
}