package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * Enumeración {@code Rol} que define los diferentes tipos de roles
 * que un {@link Usuario} puede tener dentro del sistema.
 * Implementa {@link Serializable} para permitir la persistencia de los objetos
 * que contengan este tipo de enumeración (por ejemplo, el objeto {@link Usuario}).
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public enum Rol implements Serializable {

    /**
     * Representa un usuario con privilegios administrativos.
     * Los administradores suelen tener acceso a funcionalidades de gestión y configuración del sistema.
     */
    ADMINISTRADOR,

    /**
     * Representa un usuario estándar del sistema.
     * Los usuarios normales tienen acceso a las funcionalidades básicas del sistema.
     */
    USUARIO
}