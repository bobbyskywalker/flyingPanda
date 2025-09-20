package com.FlyingPanda.menu;

import com.FlyingPanda.main.GamePanel;
import com.FlyingPanda.sound.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainMenu extends JPanel implements KeyListener {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_SPACING = 20;

    private final String[] menuOptions = {"Start Game", "How to Play", "Leaderboard", "Exit Game"};
    private int selectedOption = 0;

    private static final String fontName = "Arial";
    private final Font titleFont;
    private final Font buttonFont;

    private final JFrame parentFrame;
    private GamePanel gamePanel;

    private final transient Sound toggleSound = new Sound();

    public MainMenu(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setPreferredSize(new Dimension(GamePanel.screenWidth, GamePanel.screenHeight));
        setBackground(new Color(20, 20, 40));
        setFocusable(true);
        addKeyListener(this);

        titleFont = new Font(fontName, Font.ITALIC, 48);
        buttonFont = new Font(fontName, Font.BOLD, 24);
        toggleSound.setFile(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        drawTitle(g2);
        drawMenu(g2);
        drawInstructions(g2);
    }

    private void drawBackground(Graphics2D g2) {
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(30, 30, 60),
                0, getHeight(), new Color(60, 30, 90)
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawTitle(Graphics2D g2) {
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);

        String title = "Flying Panda";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (getWidth() - titleWidth) / 2;
        int titleY = 150;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(title, titleX + 3, titleY + 3);

        g2.setColor(Color.WHITE);
        g2.drawString(title, titleX, titleY);
    }

    private void drawMenu(Graphics2D g2) {
        g2.setFont(buttonFont);
        FontMetrics fm = g2.getFontMetrics();

        int startY = 250;
        int buttonY = startY;

        for (int i = 0; i < menuOptions.length; i++) {
            String option = menuOptions[i];
            int buttonWidth = Math.max(BUTTON_WIDTH, fm.stringWidth(option) + 40);
            int buttonX = (getWidth() - buttonWidth) / 2;

            if (i == selectedOption) {
                g2.setColor(new Color(100, 150, 255, 150));
                g2.fillRoundRect(buttonX - 10, buttonY - 35, buttonWidth + 20, BUTTON_HEIGHT, 15, 15);
                g2.setColor(new Color(100, 150, 255));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(buttonX - 10, buttonY - 35, buttonWidth + 20, BUTTON_HEIGHT, 15, 15);
            } else {
                g2.setColor(new Color(70, 70, 100, 100));
                g2.fillRoundRect(buttonX - 10, buttonY - 35, buttonWidth + 20, BUTTON_HEIGHT, 15, 15);
                g2.setColor(new Color(150, 150, 150));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(buttonX - 10, buttonY - 35, buttonWidth + 20, BUTTON_HEIGHT, 15, 15);
            }

            if (i == selectedOption) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(new Color(200, 200, 200));
            }

            int textX = (getWidth() - fm.stringWidth(option)) / 2;
            g2.drawString(option, textX, buttonY);

            buttonY += BUTTON_HEIGHT + BUTTON_SPACING;
        }
    }

    private void drawInstructions(Graphics2D g2) {
        g2.setFont(new Font(fontName, Font.PLAIN, 16));
        g2.setColor(new Color(180, 180, 180));

        String instructions = "Use UP/DOWN arrows to navigate, ENTER to select";
        String signature   = "Created by agarbacz";
        String version     = "Flying Panda v1.0 (2025)";
        FontMetrics fm = g2.getFontMetrics();
        int instructionsWidth = fm.stringWidth(instructions);
        int signatureWidth = fm.stringWidth(signature);
        int versionWidth = fm.stringWidth(version);
        int genWidth = getWidth();

        int instructionsTextX = (genWidth - instructionsWidth) / 2;
        int sigTextX = (genWidth - signatureWidth) / 2;
        int versionX = (genWidth - versionWidth) / 2;
        int textY = getHeight() - 60;

        g2.drawString(instructions, instructionsTextX, textY - 80);
        g2.setColor(Color.WHITE);
        g2.drawString(signature, sigTextX, textY - 20);
        g2.drawString(version, versionX, textY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                toggleSound.play();
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
                repaint();
                break;

            case KeyEvent.VK_DOWN:
                toggleSound.play();
                selectedOption = (selectedOption + 1) % menuOptions.length;
                repaint();
                break;

            case KeyEvent.VK_ENTER:
                try {
                    handleMenuSelection();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;

            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            default:
                break;
        }
    }

    private void handleMenuSelection() throws IOException {
        switch (selectedOption) {
            case 0:
                startGame();
                break;
            case 1:
                showHowToPlay();
                break;
            case 2:
                showLeaderboard();
                break;
            case 3:
                exitGame();
                break;
            default:
                break;
        }
    }

    private void startGame() throws IOException {
        gamePanel = new GamePanel(this);
        parentFrame.remove(this);
        parentFrame.add(gamePanel);
        parentFrame.revalidate();
        parentFrame.repaint();
        gamePanel.requestFocus();
        gamePanel.startGameThread();
        gamePanel.startBackgroundMusic();
    }

    private void showHowToPlay() {
        String howToPlayText =
                "HOW TO PLAY:\n\n" +
                        "• Use ARROW KEYS to move your panda\n" +
                        "• Press SPACEBAR to shoot bullets\n" +
                        "• Eliminate enemies to complete waves\n" +
                        "• Survive as many waves as possible!\n\n" +
                        "Good luck, Flying Panda!";

        JOptionPane.showMessageDialog(
                this,
                howToPlayText,
                "How to Play",
                JOptionPane.INFORMATION_MESSAGE
        );
        requestFocus();
    }

    private List<String> fetchTopScores() {
        List<String> scores = new ArrayList<>();
        try {
            URL leaderboardURL = new URL("http://localhost:8000/scores");
            HttpURLConnection conn = (HttpURLConnection) leaderboardURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonResponse = response.toString();
                scores = parseScoresFromJson(jsonResponse);

            } else {
                System.err.println("Failed to fetch leaderboard. Response code: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Error fetching leaderboard: " + e.getMessage());
        }

        return scores;
    }

    private List<String> parseScoresFromJson(String jsonResponse) {
        List<String> scores = new ArrayList<>();

        try {
            System.out.println("Raw JSON response: " + jsonResponse);

            jsonResponse = jsonResponse.trim();

            int scoresStart = jsonResponse.indexOf("\"scores\":");
            if (scoresStart == -1) {
                System.err.println("No 'scores' field found in JSON response");
                return scores;
            }

            int arrayStart = jsonResponse.indexOf("[", scoresStart);
            int arrayEnd = jsonResponse.indexOf("]", arrayStart);

            if (arrayStart == -1 || arrayEnd == -1) {
                System.err.println("Invalid scores array format");
                return scores;
            }

            String scoresArray = jsonResponse.substring(arrayStart + 1, arrayEnd);
            System.out.println("Extracted scores array: " + scoresArray); // Debug line

            if (scoresArray.trim().isEmpty()) {
                System.out.println("No scores in the array");
                return scores;
            }

            String[] entries = scoresArray.split("\\},\\s*\\{");
            int rank = 1;

            for (String entry : entries) {
                if (rank > 5) break;

                entry = entry.replace("{", "").replace("}", "").trim();

                String name = "";
                int score = 0;

                String[] fields = entry.split(",");
                for (String field : fields) {
                    String[] keyValue = field.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replace("\"", "");
                        String value = keyValue[1].trim().replace("\"", "");

                        if ("name".equals(key)) {
                            name = value;
                        } else if ("score".equals(key)) {
                            try {
                                score = Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing score: " + value);
                            }
                        }
                    }
                }

                if (!name.isEmpty()) {
                    String scoreEntry = String.format("%d. %s - %,d points", rank, name, score);
                    scores.add(scoreEntry);
                    System.out.println("Added score: " + scoreEntry);
                    rank++;
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return scores;
    }

    private void showLeaderboard() {
        List<String> topScores = fetchTopScores();

        StringBuilder leaderboardText = new StringBuilder("LEADERBOARD:\n\n");

        if (topScores.isEmpty()) {
            leaderboardText.append("No scores available yet!\n\n")
                    .append("Be the first to submit a score!");
        } else {
            for (String score : topScores) {
                leaderboardText.append(score).append("\n");
            }

            for (int i = topScores.size(); i < 5; i++) {
                leaderboardText.append(String.format("%d. --- - --- points\n", i + 1));
            }
        }

        JOptionPane.showMessageDialog(
                this,
                leaderboardText.toString(),
                "Leaderboard",
                JOptionPane.INFORMATION_MESSAGE
        );
        requestFocus();
    }


    private void exitGame() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            requestFocus();
        }
    }

    public void returnToMenu() {
        if (gamePanel != null) {
            parentFrame.remove(gamePanel);
            gamePanel = null;
        }
        parentFrame.add(this);
        parentFrame.revalidate();
        parentFrame.repaint();
        requestFocus();
        selectedOption = 0;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { /* Unused override */ }

    @Override
    public void keyReleased(KeyEvent e) { /* Unused too lol */ }
}