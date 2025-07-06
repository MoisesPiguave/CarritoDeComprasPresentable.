package ec.edu.ups.modelo;

public class Preguntas {
    private String id;
    private String preguntas;

    public Preguntas(String id, String preguntas) {
        this.id = id;
        this.preguntas = preguntas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }

    @Override
    public String toString() {
        return "Preguntas{" +
                "id='" + id + '\'' +
                ", preguntas='" + preguntas + '\'' +
                '}';
    }
}
