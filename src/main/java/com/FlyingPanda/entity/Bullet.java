package com.FlyingPanda.entity;

import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Bullet {
    GamePanel gp;
    Entity owner;

    public int x, y;
    public int speed = 5;

    public BufferedImage left, right;
    public String direction;

    public Bullet(GamePanel gp, Entity owner, String direction) {
        this.gp = gp;
        this.owner = owner;
        this.direction = direction;
        this.x = owner.x;
        this.y = owner.y;
    }

    public void getBulletImage() {
        try {
            left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/bamboo.png")));
            right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/bamboo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (Objects.equals(direction, "left")) {
            this.x -= this.speed;
        } else if (Objects.equals(direction, "right")) {
            this.x += this.speed;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage img = switch (direction) {
            case "left" -> left;
            case "right" -> right;
            default -> null;
        };
        g2.drawImage(img, this.x, this.y, gp.tileSize, gp.tileSize, null);
    }
}
