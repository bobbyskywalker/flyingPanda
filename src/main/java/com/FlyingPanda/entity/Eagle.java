package com.FlyingPanda.entity;

import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Eagle extends Entity {
    GamePanel gp;

    public Eagle(GamePanel gp) {
        this.gp = gp;
        setDefaultValues();
        getEagleImage();
    }

    public void setDefaultValues() {
        Random rand = new Random();

        this.x = gp.screenWidth;
        this.y = rand.nextInt(gp.screenHeight - gp.tileSize);

        speed = 1 + rand.nextInt(3);
        shootingRatio = 50;
        direction = "left";
        bullets = new ArrayList<>();
    }

    public void getEagleImage() {
        try {
            front1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/eagle/eagle-1.png")));;
            front2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/eagle/eagle-2.png")));;
            front3 = front4 = front5 = left1 = left2 = left3 =
                    right1 = right2 = right3 = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateEagleBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();
            if (b.x < 0 || b.x > gp.screenWidth ||
                    b.y < 0 || b.y > gp.screenHeight) {
                bullets.remove(i);
                i--;
            }
        }
    }

    public void update() {
        int offset = 10;

        if (Objects.equals(direction, "left")) {
            if (x > offset)
                this.x -= this.speed;
            else
                direction = "right";

        } else if (Objects.equals(direction, "right")) {
            if (x < gp.getWidth() - offset)
                this.x += this.speed;
            else
                direction = "left";
        }
        spriteCounter++;

        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        shootCounter++;
        if (shootCounter > shootingRatio) {
            bullets.add(new Bullet(gp, this, "left"));
            shootCounter = 0;
        }
        updateEagleBullets();
    }


    public void draw(Graphics2D g2) {
        BufferedImage img = (this.spriteNum == 1) ? front1 : front2;
        if (bullets != null) {
            for (Bullet b: bullets) {
                b.draw(g2);
            }
        }
        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}
