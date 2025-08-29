package com.FlyingPanda.main;

import com.FlyingPanda.entity.Player;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 20;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    Background bg;

    final int FPS = 60;

    static Thread gameThread;

    Controller keyHandler = new Controller();

    Player player = new Player(keyHandler, this);

    public GamePanel() {
        bg = new Background("/Sprites/bg/mountain_bg.png", "/Sprites/bg/trees.png", "/Sprites/bg/mount_far.png");
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        bg.update(this);
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        bg.draw(g2, this);
        player.draw(g2);
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double deltaTime = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (deltaTime >= 1) {
                update();
                repaint();
                deltaTime--;
                drawCount++;
            }

            if (timer > 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }
}
