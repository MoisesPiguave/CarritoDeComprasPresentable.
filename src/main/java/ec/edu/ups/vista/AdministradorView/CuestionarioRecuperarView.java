package ec.edu.ups.vista.AdministradorView;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.net.URL;

public class CuestionarioRecuperarView extends JFrame {
    private JButton btnEnviar;
    private JPanel panelPrincipal;
    private JButton terminarButton;
    private JLabel lblTitulo;
    private JTextField txtRespuesta1;
    private JLabel lblPregunta1;
    private JComboBox cbxPreguntas;
    private JLabel lblRespuesta;
    private JTextField txtUsuario;
    private JButton btnBuscar;
    private JLabel lblUsuario;
    private final MensajeInternacionalizacionHandler idioma;

    public CuestionarioRecuperarView(MensajeInternacionalizacionHandler idioma) {
        this.idioma = idioma;
        setContentPane(panelPrincipal);
        setTitle("Recuperar Contraseña");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        icono();
    }
    public void actualizarTextos(MensajeInternacionalizacionHandler idioma) {
        setTitle(idioma.get("recuperar.titulo"));

        lblTitulo.setText(idioma.get("recuperar.titulo"));
        lblUsuario.setText(idioma.get("recuperar.usuario"));
        lblPregunta1.setText(idioma.get("recuperar.pregunta"));
        lblRespuesta.setText(idioma.get("recuperar.respuesta"));

        btnBuscar.setText(idioma.get("recuperar.boton.buscar"));
        btnEnviar.setText(idioma.get("recuperar.boton.enviar"));
        terminarButton.setText(idioma.get("recuperar.boton.terminar"));
    }

    public JButton getBtnEnviar() {
        return btnEnviar;
    }

    public void setBtnEnviar(JButton btnEnviar) {
        this.btnEnviar = btnEnviar;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getTerminarButton() {
        return terminarButton;
    }

    public void setTerminarButton(JButton terminarButton) {
        this.terminarButton = terminarButton;
    }

    public JTextField getTxtRespuesta1() {
        return txtRespuesta1;
    }

    public void setTxtRespuesta1(JTextField txtRespuesta1) {
        this.txtRespuesta1 = txtRespuesta1;
    }

    public JLabel getLblPregunta1() {
        return lblPregunta1;
    }

    public void setLblPregunta1(JLabel lblPregunta1) {
        this.lblPregunta1 = lblPregunta1;
    }

    public MensajeInternacionalizacionHandler getMi() {
        return idioma;
    }

    public void setLblTitulo(JLabel lblTitulo) {
        this.lblTitulo = lblTitulo;
    }

    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    public JComboBox getCbxPreguntas() {
        return cbxPreguntas;
    }

    public void setCbxPreguntas(JComboBox cbxPreguntas) {
        this.cbxPreguntas = cbxPreguntas;
    }

    public JLabel getLblRespuesta() {
        return lblRespuesta;
    }

    public void setLblRespuesta(JLabel lblRespuesta) {
        this.lblRespuesta = lblRespuesta;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }


    public void icono() {
        URL botonTerminarCuestionario = LoginView.class.getClassLoader().getResource("imagenes/Terminar.svg.png");
        if (botonTerminarCuestionario != null) {
            ImageIcon icono = new ImageIcon(botonTerminarCuestionario);
            terminarButton.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
        URL botonEnviarCuestionario = LoginView.class.getClassLoader().getResource("imagenes/Enviar.svg.png");
        if (botonEnviarCuestionario != null) {
            ImageIcon icono = new ImageIcon(botonEnviarCuestionario);
            btnEnviar.setIcon(icono);
        } else {
            System.err.println("Icono no encontrado");
        }
    }

    public void limpiarCampos() {

    }

}
