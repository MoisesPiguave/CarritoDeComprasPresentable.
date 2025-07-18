package ec.edu.ups.dao;

import ec.edu.ups.modelo.Preguntas;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para la entidad {@link Preguntas}.
 * Define el contrato para las operaciones de persistencia relacionadas con
 * las preguntas utilizadas en el sistema de cuestionarios, por ejemplo,
 * para recuperación de contraseñas.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public interface CuestionarioDAO {
    /**
     * Crea y persiste una nueva pregunta en el medio de almacenamiento.
     *
     * @param preguntas El objeto {@link Preguntas} que se desea persistir.
     */
    void crear(Preguntas preguntas);

    /**
     * Recupera y retorna una lista de todas las preguntas disponibles en el medio de almacenamiento.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Preguntas} almacenados.
     * Retorna una lista vacía si no hay preguntas.
     */
    List<Preguntas> listarPreguntas();

    /**
     * Recupera y retorna una lista de todas las preguntas disponibles en el medio de almacenamiento.
     * Este método es funcionalmente idéntico a {@link #listarPreguntas()}.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Preguntas} almacenados.
     * Retorna una lista vacía si no hay preguntas.
     */
    List<Preguntas> listarPreguntass();
}