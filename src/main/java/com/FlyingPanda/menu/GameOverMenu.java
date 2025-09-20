package com.FlyingPanda.menu;

import com.FlyingPanda.main.GamePanel;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

public class GameOverMenu extends JPanel {
    private int score;
    private String playerName = "";

    StringBuilder sb = new StringBuilder();

    GamePanel gp;

    private String buildScorePayload() {
        String jsonPayload = String.format(
                "{\"name\":\"%s\",\"score\":%d}",
                playerName,
                score
        );
        sb.setLength(0);
        sb.append(jsonPayload);
        return jsonPayload;
    }


    private void sendScorePayload() throws MalformedURLException {
        try {
            System.out.println("Attempting to send score payload...");

            String payload = String.format("{\n    \"name\": \"%s\",\n    \"score\": %d\n}",
                    playerName, score);
            System.out.println("Payload being sent:");
            System.out.println(payload);

            URL leaderboardURL = new URL("https://just-viper-agme-13490751.koyeb.app/scores");
            HttpsURLConnection conn = (HttpsURLConnection) leaderboardURL.openConnection();

            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            byte[] payloadBytes = payload.getBytes("UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(payloadBytes.length));

            try (java.io.OutputStream os = conn.getOutputStream()) {
                os.write(payloadBytes);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("Score successfully submitted!");
            } else {
                System.err.println("Failed to submit score. Response code: " + responseCode);
            }

            conn.disconnect();

        } catch (Exception e) {
            System.err.println("Error sending score payload:");
            e.printStackTrace();
        }
    }

    public GameOverMenu(int score, GamePanel gp) {
        this.score = score;
        this.gp = gp;
    }

    public void showGameOverScreen(MainMenu mainMenu) {
        SwingUtilities.invokeLater(() -> {
            try {
                String gameOverText = "GAME OVER!\nFinal Score: " + score +
                        "\n\nWould you like to submit your score?";

                int choice = JOptionPane.showConfirmDialog(
                        null,
                        gameOverText,
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    String name = JOptionPane.showInputDialog(
                            null,
                            "Enter your name:",
                            "Score Submission",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    if (name != null && !name.trim().isEmpty()) {
                        this.playerName = name.trim();
                        sendScorePayload();
                        System.out.println("Score submitted for player: " + playerName);
                    }
                }
            } catch (Exception e) {
                System.err.println("Dialog error (continuing anyway): " + e.getMessage());
            }

            mainMenu.returnToMenu();
            if (gp != null) {
                gp.dispose();
                gp = null;
            }
        });
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
