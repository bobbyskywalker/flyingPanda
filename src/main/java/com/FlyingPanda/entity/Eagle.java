package com.FlyingPanda.entity;

import com.FlyingPanda.bullet.Bullet;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Eagle extends Entity {
    GamePanel gp;

    public Eagle(GamePanel gp, HUD hud) {
        this.gp = gp;
        setDefaultValues(hud);
        getEagleImage();
    }

    @Override
    public void setDefaultValues() { /* unused */ }

    @Override
    public void setDefaultValues(HUD hud) {
        Random rand = getRand();
        this.setX(GamePanel.screenWidth);
        int availableHeight = GamePanel.screenHeight - hud.getHudHeight() - GamePanel.tileSize;
        this.setY(hud.getHudHeight() + rand.nextInt(availableHeight));

        setSpeed(1 + rand.nextInt(3));
        shootingRatio = 70;
        setShotDamage(10);
        setDirection("left");
        setBullets(new ArrayList<>());
        getSoundShoot().setFile(3);
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

    @Override
    public void update(HUD hud) { /* unused by the eagle class */ }

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
            bullets.add(new Bullet(gp, this, "left", "bamboo", 5));
            setBullets(bullets);
            getSoundShoot().play();
            shootCounter = 0;
        }
        updateEntityBullets();
    }
}
