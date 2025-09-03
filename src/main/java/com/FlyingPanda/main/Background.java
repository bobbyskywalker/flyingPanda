package com.FlyingPanda.main;

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
    private static final double mainSpeed = 0.5;
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
        mainX -= (int) mainSpeed;
        layer1X -= (int) layer1Speed;
        layer2X -= (int) layer2Speed;

        if (mainX <= -GamePanel.screenWidth) mainX = 0;
        if (layer1X <= -GamePanel.screenWidth) layer1X = 0;
        if (layer2X <= -GamePanel.screenWidth) layer2X = 0;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(mainLayer, mainX, 0, GamePanel.screenWidth, GamePanel.screenHeight, null);
        g2.drawImage(mainLayer, mainX + GamePanel.screenWidth, 0, GamePanel.screenWidth, GamePanel.screenHeight, null);

        g2.drawImage(layer1, layer1X, 0, GamePanel.screenWidth, GamePanel.screenHeight, null);
        g2.drawImage(layer1, layer1X + GamePanel.screenWidth, 0, GamePanel.screenWidth, GamePanel.screenHeight, null);

        g2.drawImage(layer2, layer2X, 0, GamePanel.screenWidth, GamePanel.screenHeight, null);
        g2.drawImage(layer2, layer2X + GamePanel.screenWidth, 0, GamePanel.screenWidth, GamePanel.screenHeight, null);
    }
}

