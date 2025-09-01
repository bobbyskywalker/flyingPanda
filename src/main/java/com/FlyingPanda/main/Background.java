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

    private int mainX = 0, layer1X = 0, layer2X = 0;
    private static final double mainSpeed = 0.5, layer1Speed = 1.5, layer2Speed = 2;

    Background(String mainPath, String layer1Path, String layer2Path) {
        try {
            mainLayer = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(mainPath)));
            layer1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(layer1Path)));
            layer2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(layer2Path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(GamePanel gp) {
        mainX -= mainSpeed;
        layer1X -= layer1Speed;
        layer2X -= layer2Speed;

        if (mainX <= -gp.screenWidth) mainX = 0;
        if (layer1X <= -gp.screenWidth) layer1X = 0;
        if (layer2X <= -gp.screenWidth) layer2X = 0;
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        g2.drawImage(mainLayer, mainX, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(mainLayer, mainX + gp.screenWidth, 0, gp.screenWidth, gp.screenHeight, null);

        g2.drawImage(layer1, layer1X, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(layer1, layer1X + gp.screenWidth, 0, gp.screenWidth, gp.screenHeight, null);

        g2.drawImage(layer2, layer2X, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(layer2, layer2X + gp.screenWidth, 0, gp.screenWidth, gp.screenHeight, null);
    }
}

