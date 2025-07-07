package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CuestionarioDAO;
import ec.edu.ups.modelo.Preguntas;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import java.util.List;
import java.util.ArrayList;

public class CuestionarioDAOMemoria implements CuestionarioDAO {

    private List<Preguntas> preguntas;
    private MensajeInternacionalizacionHandler idioma;

    public CuestionarioDAOMemoria(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        this.preguntas = new ArrayList<>();
        cargarPreguntas();
    }

    public void cargarPreguntas() {
        preguntas.clear();
        preguntas.add(new Preguntas("1", idioma.get("pregunta.color_favorito")));
        preguntas.add(new Preguntas("2", idioma.get("pregunta.instrumento_musical_favorito")));
        preguntas.add(new Preguntas("3", idioma.get("pregunta.comida_favorita")));
        preguntas.add(new Preguntas("4", idioma.get("pregunta.deporte_favorito")));
        preguntas.add(new Preguntas("5", idioma.get("pregunta.nombre_de_tu_mascota")));
        preguntas.add(new Preguntas("6", idioma.get("pregunta.artista_favorito")));
        preguntas.add(new Preguntas("7", idioma.get("pregunta.comida_que_no_te_gusta")));
        preguntas.add(new Preguntas("8", idioma.get("pregunta.anime_favorito")));
        preguntas.add(new Preguntas("9", idioma.get("pregunta.messi_o_ronaldo")));
    }


    @Override
    public void crear(Preguntas pregunta) {
        preguntas.add(pregunta);
    }

    @Override
    public List<Preguntas> listarPreguntas() {
        return preguntas;
    }

    @Override
    public List<Preguntas> listarPreguntass() {
        return preguntas;
    }

    public void actualizarIdioma(MensajeInternacionalizacionHandler nuevoMi) {
        this.idioma = nuevoMi;
        cargarPreguntas();
    }
}

