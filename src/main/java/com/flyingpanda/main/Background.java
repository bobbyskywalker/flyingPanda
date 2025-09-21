package com.flyingpanda.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Background {
    private BufferedImage mainLayer;
    private BufferedImage layer1;
    private BufferedImage layer2;

    private int mainX = 0;
    private int layer1X = 0;
    private int layer2X = 0;
    double layer1Speed = 1.5;
    double layer2Speed = 2;

    Background(String mainPath, String layer1Path, String layer2Path) {
        try {
            mainLayer = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(mainPath)));
            layer1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(layer1Path)));
            layer2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(layer2Path)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        layer1X -= (int) layer1Speed;
        layer2X -= (int) layer2Speed;

        if (mainX <= -GamePanel.SCREEN_WIDTH) mainX = 0;
        if (layer1X <= -GamePanel.SCREEN_WIDTH) layer1X = 0;
        if (layer2X <= -GamePanel.SCREEN_WIDTH) layer2X = 0;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(mainLayer, mainX, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        g2.drawImage(mainLayer, mainX + GamePanel.SCREEN_WIDTH, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);

        g2.drawImage(layer1, layer1X, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        g2.drawImage(layer1, layer1X + GamePanel.SCREEN_WIDTH, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);

        g2.drawImage(layer2, layer2X, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        g2.drawImage(layer2, layer2X + GamePanel.SCREEN_WIDTH, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
    }
}

