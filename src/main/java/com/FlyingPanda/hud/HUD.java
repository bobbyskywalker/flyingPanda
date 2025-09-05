package com.FlyingPanda.hud;

import com.FlyingPanda.main.GamePanel;

import java.awt.*;

public class HUD {
    private int score;
    private int lives;
    private int waveNumber;

    private static final int hudHeight = 60;
    private static final int hudWidth = GamePanel.screenWidth;

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
        g2.setColor(new Color(40, 0, 80, 150));
        g2.fillRect(0, 0, hudWidth, 60);

        // border
        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, hudWidth - 1, 60 - 1);

        // font
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2.setColor(Color.PINK);

        int textY = 35;

        String scoreText = "Score: " + score;
        String livesText = "Lives: " + lives;
        String waveText = "Wave: " + waveNumber;

        FontMetrics fm = g2.getFontMetrics();

        int scoreWidth = fm.stringWidth(scoreText);
        int livesWidth = fm.stringWidth(livesText);
        int waveWidth = fm.stringWidth(waveText);

        int sectionWidth = hudWidth / 3;

        int scoreX = (sectionWidth - scoreWidth) / 2;
        int livesX = sectionWidth + (sectionWidth - livesWidth) / 2;
        int waveX = (2 * sectionWidth) + (sectionWidth - waveWidth) / 2;

        g2.drawString(scoreText, scoreX, textY);
        g2.drawString(livesText, livesX, textY);
        g2.drawString(waveText, waveX, textY);

        g2.setColor(originalColor);
        g2.setFont(originalFont);
    }

    public int getHudHeight() {
        return hudHeight;
    }

    public int getHudWidth() {
        return hudWidth;
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
