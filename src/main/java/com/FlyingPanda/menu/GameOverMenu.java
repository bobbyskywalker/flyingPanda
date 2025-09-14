package com.FlyingPanda.menu;

import com.FlyingPanda.main.GamePanel;

import javax.swing.*;

public class GameOverMenu extends JPanel {
    private String playerName = "";
    private int score;
    StringBuilder sb = new StringBuilder();

    GamePanel gp;

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
                        // TODO: Submit score to server
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
