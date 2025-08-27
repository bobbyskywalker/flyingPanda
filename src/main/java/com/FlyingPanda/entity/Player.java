package com.FlyingPanda.entity;

import com.FlyingPanda.main.Controller;
import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gp;
    Controller keyHandler;

    public Player(Controller keyH, GamePanel gp) {
        this.gp = gp;
        this.keyHandler = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        this.x = 100;
        this.y = 100;
        speed = 4;
        direction = "down";
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

    public void update() {
        if (keyHandler.upPressed) {
            this.direction = "up";
            this.y -= this.speed;
        } else if (keyHandler.downPressed) {
            this.direction = "down";
            this.y += this.speed;
        } else if (keyHandler.leftPressed) {
            this.direction = "left";
            this.x -= this.speed;
        } else if (keyHandler.rightPressed) {
            this.direction = "right";
            this.x += this.speed;
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1)
                spriteNum = 2;
            else if (spriteNum == 2)
                spriteNum = 1;
            spriteCounter = 0;
        }
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
        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}
