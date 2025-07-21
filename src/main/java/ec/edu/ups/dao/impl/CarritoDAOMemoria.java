package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementación de {@link CarritoDAO} que gestiona la persistencia de objetos {@link Carrito}
 * en memoria, utilizando una lista {@link ArrayList}.
 * Los carritos son almacenados temporalmente y no persisten al reiniciar la aplicación.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de 7 de 2025
 */
public class CarritoDAOMemoria implements CarritoDAO {

    private List<Carrito> carritos;

    /**
     * Constructor de la clase CarritoDAOMemoria.
     * Inicializa la lista interna de carritos como una nueva instancia de {@link ArrayList}.
     */
    public CarritoDAOMemoria() {
        this.carritos = new ArrayList<Carrito>();
    }

    /**
     * Crea un nuevo carrito en memoria.
     * Asigna un código secuencial al carrito y agrega una copia del mismo a la lista.
     *
     * @param carrito El objeto {@link Carrito} a ser creado.
     */
    @Override
    public void crear(Carrito carrito) {
        carrito.setCodigo(carritos.size() + 1);
        Carrito copia = carrito.copiar();
        carritos.add(copia);
        System.out.println("Carrito creado, codigo del carrito : : " + copia.getCodigo());
    }

    /**
     * Busca un carrito en memoria por su código.
     *
     * @param codigo El código entero del carrito a buscar.
     * @return El objeto {@link Carrito} si se encuentra, o {@code null} si no existe un carrito con ese código.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        for (Carrito carrito : carritos) {
            if (carrito.getCodigo() == codigo) {
                return carrito;
            }
        }
        return null;
    }

    /**
     * Actualiza un carrito existente en memoria.
     * Itera sobre la lista de carritos y, si encuentra una coincidencia por código, reemplaza el objeto existente
     * con el carrito proporcionado.
     *
     * @param carrito El objeto {@link Carrito} con los datos actualizados y el código de un carrito existente.
     */
    @Override
    public void actualizar(Carrito carrito) {
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                break;
            }
        }
    }

    /**
     * Elimina un carrito de la memoria por su código.
     * Utiliza un {@link Iterator} para recorrer y eliminar el carrito de manera segura de la lista.
     *
     * @param codigo El código entero del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        Iterator<Carrito> iterator = carritos.iterator();
        while (iterator.hasNext()) {
            Carrito carrito = iterator.next();
            if (carrito.getCodigo() == codigo) {
                iterator.remove();
            }
        }
    }

    /**
     * Lista todos los carritos actualmente almacenados en memoria.
     *
     * @return Una {@link List} que contiene todos los objetos {@link Carrito} en memoria.
     */
    @Override
    public List<Carrito> listarTodos() {
        return carritos;
    }

    /**
     * Busca y devuelve una lista de carritos asociados a un usuario específico.
     *
     * @param usuario El objeto {@link Usuario} cuyos carritos se desean buscar.
     * @return Una {@link List} de {@link Carrito} que pertenecen al usuario especificado.
     * Retorna una lista vacía si no se encuentran carritos para el usuario o si el usuario es nulo.
     */
    @Override
    public List<Carrito> buscarPorUsuario(Usuario usuario) {
        List<Carrito> carritosUsuario = new ArrayList<>();
        if (usuario != null && usuario.getCedula() != null) {
            for (Carrito carrito : carritos) {
                // Se asume que getUsuario() no retorna null, o se manejaría con un null check adicional.
                // Si getUsuario() puede ser null, se debería agregar un 'carrito.getUsuario() != null'
                // antes de acceder a carrito.getUsuario().getCedula().
                if (carrito.getUsuario().getCedula().equals(usuario.getCedula())) {
                    carritosUsuario.add(carrito);
                }
            }
        }
        return carritosUsuario;
    }
}