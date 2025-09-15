package com.FlyingPanda.hud;

import com.FlyingPanda.entity.Bee;
import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.entity.Entity;
import com.FlyingPanda.entity.Spider;
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

    private long waveStartTime = 0;
    private static final long WAVE_TIME_LIMIT = 45_000_000_000L;

    private boolean showingTimeoutMessage = false;
    private long timeoutMessageStartTime = 0;
    private static final long TIMEOUT_MESSAGE_DURATION = 2000;


    private static final int hudHeight = 60;
    private static final int hudWidth = GamePanel.screenWidth;

    public HUD() {
        score = 0;
        waveNumber = 1;
    }

    public void renderHUD(Graphics2D g2, int playerLives) {
        Color originalColor = g2.getColor();
        Font originalFont = g2.getFont();

        // background
        g2.setColor(new Color(40, 0, 80, 150));
        g2.fillRect(0, 0, hudWidth, 90);

        // border
        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, hudWidth - 1, 90 - 1);

        // font
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2.setColor(Color.PINK);

        int firstRowY = 25;
        int secondRowY = 55;

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

        g2.drawString(scoreText, scoreX, firstRowY);
        g2.drawString(livesText, livesX, firstRowY);
        g2.drawString(waveText, waveX, firstRowY);

        if (waveStartTime > 0) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - waveStartTime;
            long remainingTime = WAVE_TIME_LIMIT - elapsedTime;

            if (remainingTime < 0) remainingTime = 0;

            double remainingSeconds = remainingTime / 1_000_000_000.0;
            String timerText = String.format("Time: %.1fs", remainingSeconds);

            if (remainingSeconds <= 3.0) {
                g2.setColor(Color.RED);
            } else if (remainingSeconds <= 5.0) {
                g2.setColor(Color.ORANGE);
            } else {
                g2.setColor(Color.WHITE);
            }

            int timerWidth = fm.stringWidth(timerText);
            int timerX = (hudWidth - timerWidth) / 2;
            g2.drawString(timerText, timerX, secondRowY);
        }

        g2.setColor(originalColor);
        g2.setFont(originalFont);
    }


    public void renderWaveCompletionInfo(Graphics2D g2) {
        if (showingWaveCompletion) {
            long currentTime = System.currentTimeMillis();
            long timeRemaining = waveInfoDuration - (currentTime - waveInfoStartTime);

            Font originalFont = g2.getFont();
            g2.setFont(new Font("Monospaced", Font.BOLD, 26));
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

        if (showingTimeoutMessage) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeoutMessageStartTime < TIMEOUT_MESSAGE_DURATION) {
                Font originalFont = g2.getFont();
                g2.setFont(new Font("Monospaced", Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();

                String timeoutText = "TIME'S UP! -1 LIFE";
                g2.setColor(Color.RED);
                int messageWidth = fm.stringWidth(timeoutText);
                int messageX = (GamePanel.screenWidth - messageWidth) / 2;
                int messageY = GamePanel.screenHeight / 4;

                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRect(messageX - 10, messageY - 30, messageWidth + 20, 40);

                g2.setColor(Color.RED);
                g2.drawString(timeoutText, messageX, messageY);

                g2.setFont(originalFont);
            } else {
                showingTimeoutMessage = false;
            }
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

    public void updateScore(Entity enemy) {
        int scoreToAdd = 0;
        switch (enemy) {
            case Spider _ -> scoreToAdd = 100;
            case Eagle _ -> scoreToAdd = 50;
            case Bee _ -> scoreToAdd = 20;
            default -> {
            }
        }
        this.score += scoreToAdd;
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

    public void startWaveTimer() {
        this.waveStartTime = System.nanoTime();
    }

    public void stopWaveTimer() {
        this.waveStartTime = 0;
    }

    public void showTimeoutMessage() {
        this.showingTimeoutMessage = true;
        this.timeoutMessageStartTime = System.currentTimeMillis();
    }

}
