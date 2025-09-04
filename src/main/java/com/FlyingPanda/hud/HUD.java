package com.FlyingPanda.hud;

import com.FlyingPanda.main.GamePanel;

import java.awt.*;

public class HUD {
    private int score;
    private int lives;
    private int waveNumber;

    private static final int hudHeight = 60;
    private static final int hudWidth = GamePanel.screenWidth;
    Rectangle hudArea = new Rectangle(0, 0, hudWidth, hudHeight);

    public HUD() {
        score = 0;
        lives = 3;
        waveNumber = 1;
    }

    public void update() { }

    public void renderHUD(Graphics2D g2) {
        Color originalColor = g2.getColor();
        Font originalFont = g2.getFont();

        // background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, hudWidth, 60);

        // border
        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, hudWidth - 1, 60 - 1);

        // font
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.WHITE);

        // score
        g2.drawString("Score: " + score, 20, 30);

        // lives
        g2.drawString("Lives: " + lives, 200, 30);

        // wave
        g2.drawString("Wave: " + waveNumber, 380, 30);

        g2.setColor(originalColor);
        g2.setFont(originalFont);
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }
}
