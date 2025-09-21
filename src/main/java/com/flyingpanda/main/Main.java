package com.flyingpanda.main;

import com.flyingpanda.menu.MainMenu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Flying Panda");

        MainMenu mainMenu = new MainMenu(window);
        window.add(mainMenu);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        mainMenu.requestFocus();
    }
}
