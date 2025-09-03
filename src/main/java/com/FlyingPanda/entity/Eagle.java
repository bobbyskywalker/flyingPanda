package com.FlyingPanda.entity;

import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
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

        this.setX(GamePanel.screenWidth);
        this.setY(rand.nextInt(GamePanel.screenHeight - GamePanel.tileSize));

        setSpeed(1 + rand.nextInt(3));
        shootingRatio = 50;
        setShotDamage(10);
        setDirection("left");
        setBullets(new ArrayList<>());
    }

    public void getEagleImage() {
        try {
            front1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/eagle/eagle-1.png")));
            front2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/eagle/eagle-2.png")));
            front3 = front4 = front5 = left1 = left2 = left3 =
                    right1 = right2 = right3 = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateEagleBullets() {
        var bullets = getBullets();

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();
            if (b.x < 0 || b.x > GamePanel.screenWidth ||
                    b.y < 0 || b.y > GamePanel.screenHeight) {
                bullets.remove(i);
                i--;
            }
        }
    }

    public void update() {
        int offset = 10;

        var x = getX();
        if (Objects.equals(getDirection(), "left")) {
            if (x > offset)
                setX(x - getSpeed());
            else
                setDirection("right");

        } else if (Objects.equals(getDirection(), "right")) {
            if (x < GamePanel.screenWidth - offset)
                this.setX(x + this.getSpeed());
            else
                setDirection("left");
        }

        int spriteCounter = getSpriteCounter();
        int spriteNum = getSpriteNum();
        setSpriteCounter(spriteCounter + 1);

        if (spriteCounter > 10) {
            setSpriteNum((spriteNum == 1) ? 2 : 1);
            setSpriteCounter(0);
        }

        shootCounter++;
        if (shootCounter > shootingRatio) {
            ArrayList<Bullet> bullets = (ArrayList<Bullet>) getBullets();
            bullets.add(new Bullet(gp, this, "left"));
            setBullets(bullets);
            shootCounter = 0;
        }
        updateEagleBullets();
    }


    public void draw(Graphics2D g2) {
        BufferedImage img = (this.getSpriteNum() == 1) ? front1 : front2;

        drawHealthBar(g2, GamePanel.tileSize);

        var bullets = getBullets();
        if (bullets != null) {
            for (Bullet b: bullets) {
                b.draw(g2);
            }
        }
        g2.drawImage(img, getX(), getY(), GamePanel.tileSize, GamePanel.tileSize, null);
    }
}
