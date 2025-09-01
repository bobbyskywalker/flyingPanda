package com.FlyingPanda.entity;

import com.FlyingPanda.main.Controller;
import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gp;
    Controller keyHandler;

    public Player(Controller keyH, GamePanel gp) {
        this.gp = gp;
        this.keyHandler = keyH;

        this.solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        this.x = 100;
        this.y = 100;
        speed = 4;
        shootingRatio = 15;
        direction = "down";
        bullets = new ArrayList<>();
    }

    public void getPlayerImage() {
        try {
            front1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-front-1.png")));
            front2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-front-2.png")));
            front3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-front-3.png")));
            front4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-front-4.png")));
            front5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-front-5.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-left-1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-left-2.png")));
            left3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-left-3.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-right-1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-right-2.png")));
            right3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/player/panda-right-3.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayerBullets() {
        if (bullets != null) {
            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                b.update();

                if (b.x < 0 || b.x > gp.screenWidth) {
                    bullets.remove(b);
                    i--;
                }
            }
        }
    }

    public void update() {
        if (keyHandler.upPressed) {
            this.direction = "up";
            if (this.y > 0) {
                this.y -= this.speed;
            }
        }
        if (keyHandler.downPressed) {
            this.direction = "down";
            if (this.y + gp.tileSize < gp.screenHeight) {
                this.y += this.speed;
            }
        }
        if (keyHandler.leftPressed) {
            this.direction = "left";
            if (this.x > 0) {
                this.x -= this.speed;
            }
        }
        if (keyHandler.rightPressed) {
            this.direction = "right";
            if (this.x + gp.tileSize < gp.screenWidth) {
                this.x += this.speed;
            }
        }
        if (keyHandler.spacePressed) {
            shootCounter++;
            if (shootCounter > shootingRatio) {
                bullets.add(new Bullet(gp, this, "right"));
                shootCounter = 0;
            }
        }

        collisionOn = false;
        gp.colissionChecker.checkTile(this);

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1)
                spriteNum = 2;
            else if (spriteNum == 2)
                spriteNum = 1;
            spriteCounter = 0;
        }

        updatePlayerBullets();
    }

    public void draw(Graphics2D g2) {
        BufferedImage img = null;

        switch (direction) {
            case "up":
            case "down":
                if (spriteNum == 1)
                    img = front1;
                else if (spriteNum == 2)
                    img = front2;
                else if (spriteNum == 3)
                    img = front3;
                else if (spriteNum == 4)
                    img = front4;
                else if (spriteNum == 5)
                    img = front5;
                break;
            case "left":
                if (spriteNum == 1)
                    img = left1;
                else if (spriteNum == 2)
                    img = left2;
                else if (spriteNum == 3)
                    img = left3;
                break;
            case "right":
                if (spriteNum == 1)
                    img = right1;
                else if (spriteNum == 2)
                    img = right2;
                else if (spriteNum == 3)
                    img = right3;
                break;
        }
        if (bullets != null) {
            for (Bullet b: bullets) {
                b.draw(g2);
            }
        }

        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}
