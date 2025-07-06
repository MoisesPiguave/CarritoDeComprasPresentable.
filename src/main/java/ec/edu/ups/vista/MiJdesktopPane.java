package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class MiJdesktopPane extends JDesktopPane {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo de la cancha
        g2d.setColor(new Color(255, 199, 44)); // Color madera
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Líneas de la cancha
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(50, 50, getWidth()-100, getHeight()-100);

        // Dibujar las gradas
        dibujarGradas(g2d);

        // Dibujar espectadores
        dibujarEspectadores(g2d);

        // Dibujar jugador principal
        dibujarJugador(g2d, getWidth()/2, getHeight()/2);

        // Dibujar balones
        dibujarBalones(g2d);
    }

    private void dibujarGradas(Graphics2D g2d) {
        // Gradas del lado izquierdo
        for(int i = 0; i < 5; i++) {
            g2d.setColor(new Color(150, 150, 150));
            g2d.fillRect(10, 100 + (i * 40), 100, 30);
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRect(10, 100 + (i * 40), 100, 30);
        }

        // Gradas del lado derecho
        for(int i = 0; i < 5; i++) {
            g2d.setColor(new Color(150, 150, 150));
            g2d.fillRect(getWidth()-110, 100 + (i * 40), 100, 30);
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRect(getWidth()-110, 100 + (i * 40), 100, 30);
        }
    }

    private void dibujarEspectadores(Graphics2D g2d) {
        // Dibuja espectadores pequeños en las gradas
        for(int i = 0; i < 8; i++) {
            // Espectadores izquierda
            dibujarEspectador(g2d, 30 + (i * 15), 110 + (i % 5 * 40),
                    new Color(random(150, 255), random(150, 255), random(150, 255)));
            // Espectadores derecha
            dibujarEspectador(g2d, getWidth()-90 + (i * 15), 110 + (i % 5 * 40),
                    new Color(random(150, 255), random(150, 255), random(150, 255)));
        }
    }

    private void dibujarEspectador(Graphics2D g2d, int x, int y, Color color) {
        // Cabeza
        g2d.setColor(color);
        g2d.fillOval(x, y, 10, 10);
        // Cuerpo
        g2d.fillRect(x+2, y+10, 6, 12);
    }

    private void dibujarJugador(Graphics2D g2d, int x, int y) {
        // Cabeza
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillOval(x-15, y-45, 30, 30);

        // Cuerpo
        g2d.setColor(new Color(255, 0, 0)); // Uniforme rojo
        g2d.fillRect(x-20, y-15, 40, 60);

        // Brazos
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(x-30, y-15, 10, 40); // Brazo izquierdo
        g2d.fillRect(x+20, y-15, 10, 40); // Brazo derecho

        // Piernas
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillRect(x-15, y+45, 12, 40); // Pierna izquierda
        g2d.fillRect(x+3, y+45, 12, 40);  // Pierna derecha

        // Zapatos
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x-18, y+85, 15, 10); // Zapato izquierdo
        g2d.fillRect(x+3, y+85, 15, 10);  // Zapato derecho
    }

    private void dibujarBalones(Graphics2D g2d) {
        // Balón principal
        dibujarBalon(g2d, getWidth()/2 + 50, getHeight()/2 - 30, 30);

        // Balones más pequeños dispersos
        dibujarBalon(g2d, 100, 300, 20);
        dibujarBalon(g2d, getWidth()-150, 200, 15);
        dibujarBalon(g2d, 200, getHeight()-100, 18);
        dibujarBalon(g2d, getWidth()-200, getHeight()-150, 22);
    }

    private void dibujarBalon(Graphics2D g2d, int x, int y, int size) {
        // Círculo naranja del balón
        g2d.setColor(new Color(238, 103, 48));
        g2d.fillOval(x-size/2, y-size/2, size, size);

        // Líneas negras del balón
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x-size/2, y-size/2, size, size);
        g2d.drawLine(x-size/2, y, x+size/2, y);
        g2d.drawLine(x, y-size/2, x, y+size/2);

        // Brillo del balón
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(x-size/4, y-size/4, size/3, size/3);
    }

    private int random(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }
}