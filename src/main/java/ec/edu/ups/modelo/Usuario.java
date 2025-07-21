package ec.edu.ups.modelo;

import ec.edu.ups.exepciones.CedulaException;
import ec.edu.ups.exepciones.ContraseniaInvalidaException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Representa un usuario en el sistema, encapsulando sus datos personales,
 * credenciales de acceso (cédula y contraseña), rol, y preguntas de seguridad.
 * Implementa {@link Serializable} para permitir la persistencia del objeto
 * en archivos o su transmisión a través de la red.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cedula;
    private String contrasenia;
    private Rol rol;
    private String nombreCompleto;
    private String fechaNacimiento;
    private String celular;
    private String correo;
    private List<PreguntasRespuestas> preguntasRespuestas;

    /**
     * Constructor básico de la clase Usuario.
     * Inicializa un nuevo usuario con su cédula, contraseña y rol.
     * La lista de preguntas y respuestas se inicializa vacía.
     * La validación de cédula y contraseña se espera que se realice
     * antes de la creación del objeto o a través de los setters.
     *
     * @param cedula La cédula (identificador único) del usuario.
     * @param contrasenia La contraseña del usuario.
     * @param rol El {@link Rol} del usuario (ADMINISTRADOR o USUARIO).
     */
    public Usuario(String cedula, String contrasenia, Rol rol) {
        this.cedula = cedula;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.preguntasRespuestas = new ArrayList<>();
    }

    /**
     * Constructor completo de la clase Usuario.
     * Inicializa un nuevo usuario con todos sus datos personales, credenciales y rol.
     * La lista de preguntas y respuestas se inicializa vacía.
     *
     * @param cedula La cédula (identificador único) del usuario.
     * @param contrasenia La contraseña del usuario.
     * @param rol El {@link Rol} del usuario (ADMINISTRADOR o USUARIO).
     * @param nombreCompleto El nombre completo del usuario.
     * @param fechaNacimiento La fecha de nacimiento del usuario en formato de texto (ej., "01/Enero/1990").
     * @param celular El número de celular del usuario.
     * @param correo La dirección de correo electrónico del usuario.
     */
    public Usuario(String cedula, String contrasenia, Rol rol, String nombreCompleto, String fechaNacimiento, String celular, String correo) {
        this.cedula = cedula;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.celular = celular;
        this.correo = correo;
        this.preguntasRespuestas = new ArrayList<>();
    }

    /**
     * Obtiene la cédula del usuario.
     * @return La cédula del usuario.
     */
    public String getCedula() { return cedula; }

    /**
     * Establece la cédula del usuario.
     * Realiza una validación de la cédula ecuatoriana antes de asignarla.
     * @param cedula La cédula a establecer.
     * @throws CedulaException Si la cédula no cumple con el formato o es inválida.
     */
    public void setCedula(String cedula) throws CedulaException {
        validarCedulaEcuatoriana(cedula);
        this.cedula = cedula;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña del usuario.
     */
    public String getContrasenia() { return contrasenia; }

    /**
     * Establece la contraseña del usuario.
     * Realiza una validación de la contraseña según los criterios de seguridad antes de asignarla.
     * @param contrasenia La contraseña a establecer.
     * @throws ContraseniaInvalidaException Si la contraseña no cumple con los requisitos.
     */
    public void setContrasenia(String contrasenia) throws ContraseniaInvalidaException {
        validarContrasenia(contrasenia);
        this.contrasenia = contrasenia;
    }

    /**
     * Obtiene el rol del usuario.
     * @return El {@link Rol} del usuario.
     */
    public Rol getRol() { return rol; }

    /**
     * Establece el rol del usuario.
     * @param rol El {@link Rol} a establecer para el usuario.
     */
    public void setRol(Rol rol) { this.rol = rol; }

    /**
     * Obtiene el nombre completo del usuario.
     * @return El nombre completo del usuario.
     */
    public String getNombreCompleto() { return nombreCompleto; }

    /**
     * Establece el nombre completo del usuario.
     * @param nombreCompleto El nombre completo a establecer.
     */
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    /**
     * Obtiene la fecha de nacimiento del usuario.
     * @return La fecha de nacimiento del usuario en formato de texto.
     */
    public String getFechaNacimiento() { return fechaNacimiento; }

    /**
     * Establece la fecha de nacimiento del usuario.
     * @param fechaNacimiento La fecha de nacimiento a establecer.
     */
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    /**
     * Obtiene el número de celular del usuario.
     * @return El número de celular del usuario.
     */
    public String getCelular() { return celular; }

    /**
     * Establece el número de celular del usuario.
     * @param celular El número de celular a establecer.
     */
    public void setCelular(String celular) { this.celular = celular; }

    /**
     * Obtiene la dirección de correo electrónico del usuario.
     * @return La dirección de correo electrónico del usuario.
     */
    public String getCorreo() { return correo; }

    /**
     * Establece la dirección de correo electrónico del usuario.
     * @param correo La dirección de correo electrónico a establecer.
     */
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * Obtiene la lista de pares de preguntas y respuestas de seguridad asociadas al usuario.
     * @return Una {@link List} de {@link PreguntasRespuestas}.
     */
    public List<PreguntasRespuestas> getPreguntasRespuestas() { return preguntasRespuestas; }

    /**
     * Agrega una colección de nuevas preguntas y respuestas a la lista existente del usuario.
     * Si la lista de preguntas y respuestas es nula, se inicializa antes de añadir.
     * @param nuevasPreguntas La {@link List} de {@link PreguntasRespuestas} a añadir.
     */
    public void agregarPreguntas(List<PreguntasRespuestas> nuevasPreguntas) {
        if (this.preguntasRespuestas == null) {
            this.preguntasRespuestas = new ArrayList<>();
        }
        this.preguntasRespuestas.addAll(nuevasPreguntas);
    }

    /**
     * Establece la lista completa de preguntas y respuestas de seguridad para el usuario.
     * Esto reemplaza cualquier lista existente.
     * @param preguntasRespuestas La {@link List} de {@link PreguntasRespuestas} a establecer.
     */
    public void setPreguntasRespuestas(List<PreguntasRespuestas> preguntasRespuestas) {
        this.preguntasRespuestas = preguntasRespuestas;
    }

    /**
     * Retorna una representación en cadena de texto del objeto Usuario,
     * incluyendo todos sus atributos.
     * @return Una cadena formateada que describe el usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "cedula='" + cedula + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", rol=" + rol +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", celular='" + celular + '\'' +
                ", correo='" + correo + '\'' +
                ", preguntasRespuestas=" + preguntasRespuestas +
                '}';
    }

    /**
     * Valida una cédula ecuatoriana según el algoritmo del dígito verificador y formato.
     * Esta es una validación estática que puede ser llamada independientemente de una instancia de Usuario.
     *
     * @param cedula La cadena de la cédula a validar.
     * @throws CedulaException Si la cédula no cumple el formato (longitud, solo números) o es inválida según el algoritmo.
     */
    public static void validarCedulaEcuatoriana(String cedula) throws CedulaException {
        if (cedula == null || cedula.trim().isEmpty() || cedula.length() != 10) {
            throw new CedulaException("La cédula debe tener exactamente 10 dígitos.");
        }
        if (!cedula.matches("\\d{10}")) {
            throw new CedulaException("La cédula debe contener solo números.");
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            throw new CedulaException("Los dos primeros dígitos de la cédula no corresponden a una provincia válida.");
        }

        int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
        int suma = 0;
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};

        for (int i = 0; i < 9; i++) {
            int digito = Integer.parseInt(cedula.substring(i, i + 1));
            int valor = digito * coeficientes[i];
            if (valor >= 10) {
                valor -= 9;
            }
            suma += valor;
        }

        int digitoObtenido = suma % 10;
        if (digitoObtenido != 0) {
            digitoObtenido = 10 - digitoObtenido;
        }

        if (digitoObtenido != digitoVerificador) {
            throw new CedulaException("La cédula es inválida según el algoritmo del dígito verificador.");
        }
    }

    /**
     * Valida una contraseña según un conjunto de requisitos de seguridad:
     * - Debe tener un mínimo de 6 caracteres.
     * - Debe contener al menos uno de los siguientes caracteres especiales: '@', '-', o '.'.
     * Esta es una validación estática que puede ser llamada independientemente de una instancia de Usuario.
     *
     * @param contrasenia La contraseña a validar.
     * @throws ContraseniaInvalidaException Si la contraseña no cumple con los requisitos de seguridad.
     */
    public static void validarContrasenia(String contrasenia) throws ContraseniaInvalidaException {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new ContraseniaInvalidaException("La contraseña no puede estar vacía.");
        }
        if (contrasenia.length() < 6) {
            throw new ContraseniaInvalidaException("La contraseña debe tener al menos 6 caracteres.");
        }

        if (!Pattern.matches(".*[@.-].*", contrasenia)) {
            throw new ContraseniaInvalidaException("La contraseña debe contener al menos uno de los siguientes caracteres: @, . o -");
        }
    }
}