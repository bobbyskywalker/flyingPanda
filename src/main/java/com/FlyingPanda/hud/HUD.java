package com.FlyingPanda.hud;

import com.FlyingPanda.main.GamePanel;

import java.awt.*;

public class HUD {
    private int score;
    private int waveNumber;

    private boolean showingWaveCompletion = false;
    private String waveCompletionMessage = "";
    private String nextWaveMessage = "";
    private long waveInfoStartTime = 0;
    private long waveInfoDuration = 0;

    private static final int hudHeight = 60;
    private static final int hudWidth = GamePanel.screenWidth;

    public HUD() {
        score = 0;
        waveNumber = 1;
    }

    public void update() { }

    public void renderHUD(Graphics2D g2, int playerLives) {
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
        String livesText = "Lives: " + playerLives;
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

    public void renderWaveCompletionInfo(Graphics2D g2) {
        if (showingWaveCompletion) {
            long currentTime = System.currentTimeMillis();
            long timeRemaining = waveInfoDuration - (currentTime - waveInfoStartTime);

            Font originalFont = g2.getFont();
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            FontMetrics fm = g2.getFontMetrics();

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, GamePanel.screenHeight / 2 - 80, GamePanel.screenWidth, 160);

            g2.setColor(Color.GREEN);
            int messageWidth = fm.stringWidth(waveCompletionMessage);
            g2.drawString(waveCompletionMessage,
                    (GamePanel.screenWidth - messageWidth) / 2,
                    GamePanel.screenHeight / 2 - 20);

            g2.setColor(Color.YELLOW);
            messageWidth = fm.stringWidth(nextWaveMessage);
            g2.drawString(nextWaveMessage,
                    (GamePanel.screenWidth - messageWidth) / 2,
                    GamePanel.screenHeight / 2 + 20);

            String countdownText = "Starting in: " + (timeRemaining / 1000 + 1);
            g2.setColor(Color.WHITE);
            messageWidth = fm.stringWidth(countdownText);
            g2.drawString(countdownText,
                    (GamePanel.screenWidth - messageWidth) / 2,
                    GamePanel.screenHeight / 2 + 60);

            g2.setFont(originalFont);
        }
    }

    public void setWaveCompletionInfo(int completedWave, int nextWave, long delayMs) {
        showingWaveCompletion = true;
        waveCompletionMessage = "Wave " + completedWave + " Completed!";
        nextWaveMessage = "Wave " + nextWave + " incoming...";
        waveInfoStartTime = System.currentTimeMillis();
        waveInfoDuration = delayMs;
    }

    public void clearWaveCompletionInfo() {
        showingWaveCompletion = false;
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

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }
}
