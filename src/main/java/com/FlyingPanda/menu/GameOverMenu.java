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
        String gameOverText = "GAME OVER!\n Submit your name to post the score to the leaderboard!";
        int choice = JOptionPane.showConfirmDialog(this, gameOverText, "Game Over", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            playerName = JOptionPane.showInputDialog(this, gameOverText, "Game Over", JOptionPane.PLAIN_MESSAGE);
            /* PLACEHOLDER, POST THE SCORE TO SERVER */
        }
        mainMenu.returnToMenu();
        gp.dispose();
        this.gp = null;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
