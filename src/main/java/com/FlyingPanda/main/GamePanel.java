package com.FlyingPanda.main;

import com.FlyingPanda.collectible.Collectible;
import com.FlyingPanda.entity.Bee;
import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.entity.Player;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.menu.GameOverMenu;
import com.FlyingPanda.menu.MainMenu;
import com.FlyingPanda.utils.CollissionChecker;
import com.FlyingPanda.gameplay.GameplayManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private volatile boolean running;

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
    public transient GameplayManager gameplayManager = new GameplayManager(this, hud);

    public transient MainMenu mainMenu;

    public GamePanel(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        bg = new Background("/Sprites/bg/mountain_bg.png", "/Sprites/bg/trees.png", "/Sprites/bg/mount_far.png");
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void stopGameThread() {
        running = false;
        Thread t = gameThread;
        gameThread = null;
        if (t != null && t != Thread.currentThread()) {
            try { t.join(150); } catch (InterruptedException ignored) {}
        }
    }

    public void gameOver() {
        stopGameThread();
        GameOverMenu gameOverPanel = new GameOverMenu(hud.getScore(), this);
        gameOverPanel.showGameOverScreen(mainMenu);
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
        hud.update();
        gameplayManager.updateCurrentWave();
        CollissionChecker.checkAllCollisions(player, gameplayManager.getEagles(), gameplayManager.getBees(), hud, gameplayManager, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        bg.draw(g2);
        player.draw(g2);
        for (Eagle e : gameplayManager.getEagles())
            e.draw(g2);
        for (Bee b: gameplayManager.getBees())
            b.draw(g2);
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
