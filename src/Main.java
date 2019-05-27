import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.*;

public class Main extends Canvas implements Runnable {

    private static final long serialVersionUID = 9131249716373496859L;

    public static final int WIDTH = 512;
    private static final String TITLE = "World Generation Using Cellular Automata";
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private Thread thread;
    private boolean running = false;

    private static final int TICKS_PER_SECOND = 20;
    private static final int NANOSECONDS_PER_TICK = 1000000000 / TICKS_PER_SECOND;

    public static void main(String[] args) {
        JFrame f = new JFrame(TITLE);
        Dimension d = new Dimension(WIDTH + 6, WIDTH + 29);
        Main m = new Main();
        f.add(m);
        f.setSize(d);
        f.setPreferredSize(d);
        f.setMaximumSize(d);
        f.setMinimumSize(d);
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.requestFocus();
        f.pack();
        m.start();
    }

    private synchronized void start() {
        if(!running){
            thread = new Thread(this);
            thread.start();
            running = true;
        }
    }

    private synchronized void stop() {
        if(running){
            try {
                thread.join();
            } catch (InterruptedException e) { e.printStackTrace(); }
            running = false;
        }
    }

    private void init() {
        createBufferStrategy(3);
        Chunk.randomlyPopulate(0.5);

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_T) Chunk.tick();
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        });
    }

    @Override
    public void run() {
        init();
        long lastTime = System.nanoTime(), now, delta = 0;
        while(running){
            now = System.nanoTime();
            delta += now - lastTime;
            if(delta >= NANOSECONDS_PER_TICK){
                tick();
                delta -= NANOSECONDS_PER_TICK;
            }
            render();
            lastTime = now;
        }
        stop();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, WIDTH, WIDTH);

        Chunk.render(g);

        g.dispose();
        bs.show();
    }

    private void tick() {}
}