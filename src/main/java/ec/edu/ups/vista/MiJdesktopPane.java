package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*; // Aunque no se usa Line2D o Rectangle2D directamente, se deja si es parte de un paquete de importaciones común.

/**
 * Una implementación personalizada de {@link JDesktopPane} que dibuja un fondo
 * temático de una cancha de baloncesto con espectadores, un jugador y balones.
 * Este panel se utiliza como el contenedor principal para las ventanas internas
 * de la aplicación, proporcionando un ambiente visual específico.
 *
 * @author Moises Piguave
 * @version 1.0
 * @since 17 de julio de 2025
 */
public class MiJdesktopPane extends JDesktopPane {

    /**
     * Sobrescribe el método {@code paintComponent} para realizar el dibujo personalizado
     * del fondo de la cancha de baloncesto.
     * Dibuja la cancha, las gradas, espectadores, un jugador principal y varios balones.
     *
     * @param g El contexto gráfico en el que se va a pintar.
     */
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

    /**
     * Dibuja las gradas a los lados izquierdo y derecho del panel.
     * Las gradas tienen un color gris y un borde más oscuro.
     *
     * @param g2d El contexto gráfico {@link Graphics2D} en el que se va a pintar.
     */
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

    /**
     * Dibuja pequeños espectadores aleatorios en las gradas a ambos lados del panel.
     *
     * @param g2d El contexto gráfico {@link Graphics2D} en el que se va a pintar.
     */
    private void dibujarEspectadores(Graphics2D g2d) {
        for(int i = 0; i < 8; i++) {
            // Espectadores izquierda
            dibujarEspectador(g2d, 30 + (i * 15), 110 + (i % 5 * 40),
                    new Color(random(150, 255), random(150, 255), random(150, 255)));
            // Espectadores derecha
            dibujarEspectador(g2d, getWidth()-90 + (i * 15), 110 + (i % 5 * 40),
                    new Color(random(150, 255), random(150, 255), random(150, 255)));
        }
    }

    /**
     * Dibuja un espectador individual como una cabeza (óvalo) y un cuerpo (rectángulo).
     *
     * @param g2d El contexto gráfico {@link Graphics2D} en el que se va a pintar.
     * @param x La coordenada X de la posición del espectador.
     * @param y La coordenada Y de la posición del espectador.
     * @param color El {@link Color} de la cabeza del espectador.
     */
    private void dibujarEspectador(Graphics2D g2d, int x, int y, Color color) {
        // Cabeza
        g2d.setColor(color);
        g2d.fillOval(x, y, 10, 10);
        // Cuerpo
        g2d.fillRect(x+2, y+10, 6, 12);
    }

    /**
     * Dibuja un jugador con un uniforme rojo en una posición central.
     * El jugador se compone de formas básicas para representar cabeza, cuerpo, brazos y piernas con zapatos.
     *
     * @param g2d El contexto gráfico {@link Graphics2D} en el que se va a pintar.
     * @param x La coordenada X central del jugador.
     * @param y La coordenada Y central del jugador.
     */
    private void dibujarJugador(Graphics2D g2d, int x, int y) {
        // Cabeza
        g2d.setColor(new Color(139, 69, 19)); // Color de piel
        g2d.fillOval(x-15, y-45, 30, 30);

        // Cuerpo
        g2d.setColor(new Color(255, 0, 0)); // Uniforme rojo
        g2d.fillRect(x-20, y-15, 40, 60);

        // Brazos
        g2d.setColor(new Color(139, 69, 19)); // Color de piel
        g2d.fillRect(x-30, y-15, 10, 40); // Brazo izquierdo
        g2d.fillRect(x+20, y-15, 10, 40); // Brazo derecho

        // Piernas
        g2d.setColor(new Color(255, 0, 0)); // Uniforme rojo
        g2d.fillRect(x-15, y+45, 12, 40); // Pierna izquierda
        g2d.fillRect(x+3, y+45, 12, 40);  // Pierna derecha

        // Zapatos
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x-18, y+85, 15, 10); // Zapato izquierdo
        g2d.fillRect(x+3, y+85, 15, 10);  // Zapato derecho
    }

    /**
     * Dibuja varios balones de baloncesto en diferentes posiciones y tamaños en el panel.
     *
     * @param g2d El contexto gráfico {@link Graphics2D} en el que se va a pintar.
     */
    private void dibujarBalones(Graphics2D g2d) {
        // Balón principal
        dibujarBalon(g2d, getWidth()/2 + 50, getHeight()/2 - 30, 30);

        // Balones más pequeños dispersos
        dibujarBalon(g2d, 100, 300, 20);
        dibujarBalon(g2d, getWidth()-150, 200, 15);
        dibujarBalon(g2d, 200, getHeight()-100, 18);
        dibujarBalon(g2d, getWidth()-200, getHeight()-150, 22);
    }

    /**
     * Dibuja un balón de baloncesto con un círculo naranja, líneas negras y un brillo blanco.
     *
     * @param g2d El contexto gráfico {@link Graphics2D} en el que se va a pintar.
     * @param x La coordenada X central del balón.
     * @param y La coordenada Y central del balón.
     * @param size El diámetro del balón.
     */
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

    /**
     * Genera un número entero aleatorio dentro de un rango especificado (inclusive).
     *
     * @param min El valor mínimo del rango.
     * @param max El valor máximo del rango.
     * @return Un número entero aleatorio entre min y max.
     */
    private int random(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }
}