import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JuegoComida extends JFrame implements KeyListener, ActionListener, Runnable {
    private int anchoMapa = 500;
    private int altoMapa = 500;

    private int jugador1X = anchoMapa / 4;
    private int jugador1Y = altoMapa / 2;
    private int velocidadJugador1 = 3;

    private int jugador2X = 3 * anchoMapa / 4;
    private int jugador2Y = altoMapa / 2;
    private int velocidadJugador2 = 3;

    private int jugador1Puntuacion = 0;
    private int jugador2Puntuacion = 0;

    private JLabel puntuacionJugador1;
    private JLabel puntuacionJugador2;
    private JLabel mensajeGanador;

    private List<Point> comidas;
    private List<Point> estructuras;

    private boolean[] keysJugador1;
    private boolean[] keysJugador2;

    private ZonaEspecial zonaEspecial;
    private Timer timer;

    public JuegoComida() {
        setTitle("Juego de Comida");
        setSize(800, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        puntuacionJugador1 = new JLabel("Puntuacion:\nJugador 1: " + jugador1Puntuacion);
        puntuacionJugador2 = new JLabel("Puntuacion:\nJugador 2: " + jugador2Puntuacion);
        mensajeGanador = new JLabel("");

        JPanel panelPuntuacion = new JPanel(new GridLayout(3, 1));
        panelPuntuacion.add(puntuacionJugador1);
        panelPuntuacion.add(puntuacionJugador2);
        panelPuntuacion.add(mensajeGanador);

        add(panelPuntuacion, BorderLayout.NORTH);

        comidas = new ArrayList<>();
        colocarNuevasComidas(10);

        estructuras = new ArrayList<>();
        colocarNuevasEstructuras(5);

        keysJugador1 = new boolean[4];
        keysJugador2 = new boolean[4];

        zonaEspecial = new ZonaEspecial();
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zonaEspecial.aparecerAleatoriamente();
            }
        });
        timer.start();

        JButton reiniciarButton = new JButton("Reiniciar");
        reiniciarButton.addActionListener(this);

        add(reiniciarButton, BorderLayout.SOUTH);

        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        setVisible(true);

        new Thread(this).start();
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(50, 50, 50));
        g.fillRect((getWidth() - anchoMapa) / 2, (getHeight() - altoMapa) / 2, anchoMapa, altoMapa);

        g.setColor(Color.RED);
        g.fillRect(jugador1X, jugador1Y, 20, 20);

        g.setColor(Color.BLUE);
        g.fillRect(jugador2X, jugador2Y, 20, 20);

        g.setColor(Color.GREEN);
        for (Point comida : comidas) {
            g.fillRect(comida.x, comida.y, 10, 10);
        }

        g.setColor(Color.WHITE);
        for (Point estructura : estructuras) {
            g.fillRect(estructura.x, estructura.y, 10, 4);
        }

        if (zonaEspecial.estaActiva()) {
            g.setColor(new Color(255, 255, 0, 150));
            g.fillRect(zonaEspecial.getX(), zonaEspecial.getY(), zonaEspecial.getAncho(), zonaEspecial.getAlto());
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            keysJugador1[0] = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            keysJugador1[1] = true;
        } else if (keyCode == KeyEvent.VK_UP) {
            keysJugador1[2] = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            keysJugador1[3] = true;
        }

        if (keyCode == KeyEvent.VK_A) {
            keysJugador2[0] = true;
        } else if (keyCode == KeyEvent.VK_D) {
            keysJugador2[1] = true;
        } else if (keyCode == KeyEvent.VK_W) {
            keysJugador2[2] = true;
        } else if (keyCode == KeyEvent.VK_S) {
            keysJugador2[3] = true;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            keysJugador1[0] = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            keysJugador1[1] = false;
        } else if (keyCode == KeyEvent.VK_UP) {
            keysJugador1[2] = false;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            keysJugador1[3] = false;
        }

        if (keyCode == KeyEvent.VK_A) {
            keysJugador2[0] = false;
        } else if (keyCode == KeyEvent.VK_D) {
            keysJugador2[1] = false;
        } else if (keyCode == KeyEvent.VK_W) {
            keysJugador2[2] = false;
        } else if (keyCode == KeyEvent.VK_S) {
            keysJugador2[3] = false;
        }
    }

    private void actualizarMovimiento() {
        if (keysJugador1[0] && jugador1X > (getWidth() - anchoMapa) / 2) {
            jugador1X -= velocidadJugador1;
        }
        if (keysJugador1[1] && jugador1X < (getWidth() + anchoMapa) / 2 - 20) {
            jugador1X += velocidadJugador1;
        }
        if (keysJugador1[2] && jugador1Y > (getHeight() - altoMapa) / 2) {
            jugador1Y -= velocidadJugador1;
        }
        if (keysJugador1[3] && jugador1Y < (getHeight() + altoMapa) / 2 - 20) {
            jugador1Y += velocidadJugador1;
        }

        if (keysJugador2[0] && jugador2X > (getWidth() - anchoMapa) / 2) {
            jugador2X -= velocidadJugador2;
        }
        if (keysJugador2[1] && jugador2X < (getWidth() + anchoMapa) / 2 - 20) {
            jugador2X += velocidadJugador2;
        }
        if (keysJugador2[2] && jugador2Y > (getHeight() - altoMapa) / 2) {
            jugador2Y -= velocidadJugador2;
        }
        if (keysJugador2[3] && jugador2Y < (getHeight() + altoMapa) / 2 - 20) {
            jugador2Y += velocidadJugador2;
        }

        for (Point estructura : estructuras) {
            if (jugador1X < estructura.x + 10 && jugador1X + 20 > estructura.x &&
                    jugador1Y < estructura.y + 4 && jugador1Y + 20 > estructura.y) {
                if (keysJugador1[0]) jugador1X += velocidadJugador1;
                if (keysJugador1[1]) jugador1X -= velocidadJugador1;
                if (keysJugador1[2]) jugador1Y += velocidadJugador1;
                if (keysJugador1[3]) jugador1Y -= velocidadJugador1;
            }

            if (jugador2X < estructura.x + 10 && jugador2X + 20 > estructura.x &&
                    jugador2Y < estructura.y + 4 && jugador2Y + 20 > estructura.y) {
                if (keysJugador2[0]) jugador2X += velocidadJugador2;
                if (keysJugador2[1]) jugador2X -= velocidadJugador2;
                if (keysJugador2[2]) jugador2Y += velocidadJugador2;
                if (keysJugador2[3]) jugador2Y -= velocidadJugador2;
            }
        }

        if (zonaEspecial.estaActiva()) {
            if (zonaEspecial.contiene(jugador1X, jugador1Y)) {
                velocidadJugador1 = 1;
            } else {
                velocidadJugador1 = 5;
            }

            if (zonaEspecial.contiene(jugador2X, jugador2Y)) {
                velocidadJugador2 = 1;
            } else {
                velocidadJugador2 = 4;
            }
        }
    }

    private void colocarNuevasComidas(int cantidad) {
        Random random = new Random();
        for (int i = 0; i < cantidad; i++) {
            int x = random.nextInt(anchoMapa) + (getWidth() - anchoMapa) / 2;
            int y = random.nextInt(altoMapa) + (getHeight() - altoMapa) / 2;
            comidas.add(new Point(x, y));
        }
    }

    private void colocarNuevasEstructuras(int cantidad) {
        Random random = new Random();
        for (int i = 0; i < cantidad; i++) {
            int x = random.nextInt(anchoMapa - 10) + (getWidth() - anchoMapa) / 2;
            int y = random.nextInt(altoMapa - 4) + (getHeight() - altoMapa) / 2;
            estructuras.add(new Point(x, y));
        }
    }

    private void actualizarPuntuacion() {
        puntuacionJugador1.setText("Puntuacion:\nJugador 1: " + jugador1Puntuacion);
        puntuacionJugador2.setText("Puntuacion:\nJugador 2: " + jugador2Puntuacion);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoComida());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        reiniciarJuego();
    }

    private void reiniciarJuego() {
        jugador1X = anchoMapa / 4;
        jugador1Y = altoMapa / 2;
        jugador2X = 3 * anchoMapa / 4;
        jugador2Y = altoMapa / 2;
        jugador1Puntuacion = 0;
        jugador2Puntuacion = 0;

        comidas.clear();
        colocarNuevasComidas(10);

        estructuras.clear();
        colocarNuevasEstructuras(5);

        mensajeGanador.setText("");

    zonaEspecial.aparecerAleatoriamente();

    repaint();

    requestFocusInWindow();
    }

    @Override
    public void run() {
        while (true) {
            actualizarMovimiento();

            for (int i = 0; i < comidas.size(); i++) {
                Point comida = comidas.get(i);
                if (jugador1X < comida.x + 10 && jugador1X + 20 > comida.x &&
                jugador1Y < comida.y + 10 && jugador1Y + 20 > comida.y) {
                    jugador1Puntuacion++;
                    comidas.remove(i);
                    colocarNuevasComidas(1);
                }
            }

            for (int i = 0; i < comidas.size(); i++) {
                Point comida = comidas.get(i);
                if (jugador2X < comida.x + 10 && jugador2X + 20 > comida.x &&
                        jugador2Y < comida.y + 10 && jugador2Y + 20 > comida.y) {
                    jugador2Puntuacion++;
                    comidas.remove(i);
                    colocarNuevasComidas(1);
                }
            }

            actualizarPuntuacion();
            repaint();

            if (jugador1Puntuacion >= 5) {
                JOptionPane.showMessageDialog(null, "¡Jugador 1 ha ganado!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
                reiniciarJuego();
            } else if (jugador2Puntuacion >= 5) {
                JOptionPane.showMessageDialog(null, "¡Jugador 2 ha ganado!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
                reiniciarJuego();
            }
            

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ZonaEspecial {
        private int x;
        private int y;
        private int ancho = 200;
        private int alto = 200;
        private boolean activa = false;

        public void aparecerAleatoriamente() {
            Random random = new Random();
            x = random.nextInt(anchoMapa - ancho) + (getWidth() - anchoMapa) / 2;
            y = random.nextInt(altoMapa - alto) + (getHeight() - altoMapa) / 2;
            activa = true;
        }

        public boolean estaActiva() {
            return activa;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getAncho() {
            return ancho;
        }

        public int getAlto() {
            return alto;
        }

        public boolean contiene(int xJugador, int yJugador) {
            return xJugador >= x && xJugador <= x + ancho && yJugador >= y && yJugador <= y + alto;
        }
    }
}

