package com.flyingpanda.main;

import com.flyingpanda.collectible.Collectible;
import com.flyingpanda.entity.*;
import com.flyingpanda.hud.HUD;
import com.flyingpanda.menu.GameOverMenu;
import com.flyingpanda.menu.MainMenu;
import com.flyingpanda.collision.CollissionChecker;
import com.flyingpanda.gameplay.GameplayManager;
import com.flyingpanda.sound.Sound;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {
    public static final int ORIGINAL_TILE_SIZE = 20;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * 3;
    private static final int MAX_SCREEN_COL = 16;
    private static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    transient Background bg;

    static final int FPS = 60;

    transient Thread gameThread;

    static Controller keyHandler = new Controller();

    transient Player player = new Player(keyHandler, this);

    private transient HUD hud = new HUD();
    private transient GameplayManager gameplayManager = new GameplayManager(this, hud, player);

    private final transient MainMenu mainMenu;

    private Sound backgroundMusic = new Sound();

    public GamePanel(MainMenu mainMenu) throws IOException {
        this.mainMenu = mainMenu;
        bg = new Background("/Sprites/bg/mountain_bg.png", "/Sprites/bg/trees.png", "/Sprites/bg/mount_far.png");
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startBackgroundMusic() {
        backgroundMusic = new Sound();
        backgroundMusic.setFile(5);
        backgroundMusic.loop();
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void stopGameThread() {
        Thread t = gameThread;
        gameThread = null;
        if (t != null && t != Thread.currentThread()) {
            try { t.join(150); } catch (InterruptedException ignored) {}
        }
    }

    public void gameOver() {
        stopBackgroundMusic();
        stopGameThread();

        SwingUtilities.invokeLater(() -> {
            try {
                GameOverMenu gameOverPanel = new GameOverMenu(hud.getScore(), this);
                gameOverPanel.showGameOverScreen(mainMenu);
            } catch (Exception e) {
                mainMenu.returnToMenu();
                dispose();
            }
        });
    }

    public void dispose() {
        this.removeKeyListener(keyHandler);

        if (keyHandler != null)
            keyHandler.reset();
        if (gameplayManager != null)
            gameplayManager.dispose();
        if (player != null)
            player.dispose();

        player = null;
        gameplayManager = null;
        hud = null;
        bg = null;

        setFocusable(false);
    }

    public void update() {
        bg.update();
        player.update(hud);
        gameplayManager.updateCurrentWave();
        CollissionChecker.checkAllCollisions(player, gameplayManager.getEnemies(), hud, gameplayManager, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        bg.draw(g2);
        player.draw(g2);
        for (Entity e: gameplayManager.getEnemies())
            e.draw(g2);
        for (Collectible c: gameplayManager.getCollectibles())
            c.draw(g2);
        hud.renderHUD(g2, player.getLives());
        hud.renderWaveCompletionInfo(g2);
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

            if (timer > 1_000_000_000)
                timer = 0;
        }
    }
}
