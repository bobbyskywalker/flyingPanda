package com.FlyingPanda.main;

import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.entity.Player;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.utils.CollissionChecker;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    public static final int originalTileSize = 20;
    static final int scale = 3;

    public static final int tileSize = originalTileSize * scale;
    static final int maxScreenCol = 16;
    static final int maxScreenRow = 12;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;

    transient Background bg;

    static final int FPS = 60;

    transient Thread gameThread;

    static Controller keyHandler = new Controller();

    transient Player player = new Player(keyHandler, this);
    public transient HUD hud = new HUD();

    static ArrayList<Eagle> eagles = new ArrayList<>();

    public GamePanel() {
        bg = new Background("/Sprites/bg/mountain_bg.png", "/Sprites/bg/trees.png", "/Sprites/bg/mount_far.png");
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        eagles.add(new Eagle(this, hud));
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        bg.update();
        player.update(hud);
        hud.update();
        for (Eagle e : eagles)
            e.update();

        CollissionChecker.checkAllCollisions(player, eagles);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        bg.draw(g2);
        player.draw(g2);
        for (Eagle e : eagles)
            e.draw(g2);
        hud.renderHUD(g2);
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double deltaTime = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (deltaTime >= 1) {
                update();
                repaint();
                deltaTime--;
            }

            if (timer > 1000000000)
                timer = 0;
        }
    }
}
