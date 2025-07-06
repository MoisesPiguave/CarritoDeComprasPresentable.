package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioDAOMemoria implements UsuarioDAO {

    private List<Usuario> usuarios;
    private CuestionarioDAO cuestionarioDAO;

    public UsuarioDAOMemoria(CuestionarioDAO cuestionarioDAO) {
        this.usuarios = new ArrayList<>();
        this.cuestionarioDAO = cuestionarioDAO;

        crear(new Usuario("", "", Rol.ADMINISTRADOR));
        Usuario admin = new Usuario("Moises", "12345", Rol.ADMINISTRADOR);
        admin.setNombreCompleto("Moises Piguave");
        admin.setCorreo("moi.emanuelsps@gmail.com");
        admin.setCelular("0981468221");
        admin.setFechaNacimiento("14/Diciembre/2005");
        crear(admin);
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().equals(username) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void crear(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("DEBUG -> Usuario creado: " + usuario);
    }

    @Override
    public Usuario buscarPorUsuario(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void eliminar(String username) {
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getUsuario().equals(username)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.getUsuario().equals(usuario.getUsuario())) {
                u.setContrasenia(usuario.getContrasenia());
                u.setNombreCompleto(usuario.getNombreCompleto());
                u.setCorreo(usuario.getCorreo());
                u.setCelular(usuario.getCelular());
                u.setFechaNacimiento(usuario.getFechaNacimiento());
                u.setRol(usuario.getRol());
                u.setPreguntasRespuestas(usuario.getPreguntasRespuestas());
                System.out.println("DEBUG -> Usuario actualizado: " + u);
                break;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarios;
    }

    @Override
    public List<Usuario> listarPorUsuario(String username) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().startsWith(username)) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }
}
