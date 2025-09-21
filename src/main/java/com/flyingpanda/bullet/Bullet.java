package com.flyingpanda.bullet;

import com.flyingpanda.entity.Entity;
import com.flyingpanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Bullet {
    GamePanel gp;
    Entity owner;

    private int x;
    private int y;
    private final int speed;

    private String type;

    private BufferedImage left;
    private BufferedImage right;
    private final String direction;

    public Bullet(GamePanel gp, Entity owner, String direction, String type, int speed) {
        this.gp = gp;
        this.owner = owner;
        this.direction = direction;
        this.type = type;
        this.speed = speed;
        this.x = owner.getX();
        this.y = owner.getY();
        getBulletImage();
    }

    /* load bullet image according to type */
    public void getBulletImage() {
        try {
            switch (type) {
                case "bamboo":
                    left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/bamboo-1.png")));
                    right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/bamboo-1.png")));
                    break;
                case "fire":
                    left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/fire.png")));
                    right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/fire.png")));
                    break;
                case "spiderweb":
                    left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/spiderweb.png")));
                    right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/spiderweb.png")));
                    break;
                case "energyBall":
                    left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/energy-ball.png")));
                    right = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/energy-ball.png")));
                    break;
                default:
                    break;
            }
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
        g2.drawImage(img, this.x, this.y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
