package com.flyingpanda.entity;

import com.flyingpanda.bullet.Bullet;
import com.flyingpanda.hud.HUD;
import com.flyingpanda.main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Bee extends Entity {
    GamePanel gp;

    public Bee(GamePanel gp, HUD hud) {
        this.gp = gp;
        setDefaultValues(hud);
        getBeeImage();
    }

    @Override
    public void setDefaultValues() { /* unused */ }

    @Override
    public void setDefaultValues(HUD hud) {
        Random rand = getRand();
        this.setX(GamePanel.SCREEN_WIDTH);
        int availableHeight = GamePanel.SCREEN_HEIGHT - hud.getHudHeight() - GamePanel.TILE_SIZE;
        this.setY(hud.getHudHeight() + rand.nextInt(availableHeight));

        setSpeed(1 + rand.nextInt(3));
        shootingRatio = 90;
        setShotDamage(15);
        setDirection("left");
        setBullets(new ArrayList<>());
        getSoundShoot().setFile(3);
    }

    public void getBeeImage() {
        try {
            front1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bee/bee-1.png")));
            front2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bee/bee-2.png")));
            front3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bee/bee-3.png")));
            front4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bee/bee-4.png")));
            front5 = left1 = left2 = left3 = right1 = right2 = right3 = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(HUD hud) { /* unused by the bee class */ }

    @Override
    public void update() {
        int offset = 10;

        var x = getX();
        if (Objects.equals(getDirection(), FACING_LEFT)) {
            if (x > offset)
                setX(x - getSpeed());
            else
                setDirection(FACING_RIGHT);

        } else if (Objects.equals(getDirection(), FACING_RIGHT)) {
            if (x < GamePanel.SCREEN_WIDTH - offset)
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
            bullets.add(new Bullet(gp, this, FACING_LEFT, "energyBall", 5));
            bullets.add(new Bullet(gp, this, FACING_RIGHT, "energyBall", 5));
            getSoundShoot().play();
            setBullets(bullets);
            shootCounter = 0;
        }
        updateEntityBullets();
    }
}
