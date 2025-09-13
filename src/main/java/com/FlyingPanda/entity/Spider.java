package com.FlyingPanda.entity;

import com.FlyingPanda.bullet.Bullet;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Spider extends Entity {
    GamePanel gp;

    public Spider(GamePanel gp, HUD hud) {
        this.gp = gp;
        setDefaultValues(hud);
        getSpiderImage();
    }

    @Override
    public void setDefaultValues() { /* unused */ }

    @Override
    public void setDefaultValues(HUD hud) {
        Random rand = getRand();
        this.setX(GamePanel.screenWidth);
        int availableHeight = GamePanel.screenHeight - hud.getHudHeight() - GamePanel.tileSize;
        this.setY(hud.getHudHeight() + rand.nextInt(availableHeight));

        setSpeed(5);
        shootingRatio = 70;
        setShotDamage(30);
        setDirection("left");
        setBullets(new ArrayList<>());
    }

    public void getSpiderImage() {
        try {
            front1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/spider/spider-01.png")));
            front2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/spider/spider-02.png")));
            front3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/spider/spider-03.png")));
            front4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/spider/spider-04.png")));
            front5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/spider/spider-05.png")));
            left1 = left2 = left3 = right1 = right2 = right3 = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(HUD hud) { /* unused by the spider class */ }

    @Override
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
            Bullet newBullet = new Bullet(gp, this, getDirection(), "spiderweb", 10);
            bullets.add(newBullet);
            setBullets(bullets);
            shootCounter = 0;
        }
        updateEntityBullets();
    }
}
