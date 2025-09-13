package com.FlyingPanda.entity;

import com.FlyingPanda.bullet.Bullet;
import com.FlyingPanda.hud.HUD;
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
    private int lives = 3;
    private String equippedBulletType;

    private long fireBulletStartTime = 0;
    private final long FIRE_BULLET_DURATION = 10_000_000_000L;
    private boolean hasFireBulletActive = false;


    public Player(Controller keyH, GamePanel gp) {
        this.gp = gp;
        this.keyHandler = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void dispose() {
        this.gp = null;
        this.keyHandler = null;
        this.getBullets().clear();
        front1 = front2 = front3 = front4 = front5 = left1 = left2 = left3 = right1 = right2 = right3 = null;
    }

    @Override
    public void setDefaultValues(HUD hud) { /* unused abstract method, override below without hud arg */ }

    @Override
    public void setDefaultValues() {
        setX(100);
        setY(100);

        setSpeed(4);
        shootingRatio = 15;
        setDirection("down");
        setEquippedBulletType("bamboo");
        setShotDamage(15);
        setBullets(new ArrayList<>());
    }

    public void update() {}

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
        var playerBullets = getBullets();

        if (playerBullets != null) {
            for (int i = 0; i < playerBullets.size(); i++) {
                Bullet b = playerBullets.get(i);
                b.update();

                if (b.getX() < 0 || b.getX() > GamePanel.screenWidth) {
                    playerBullets.remove(b);
                    setBullets((ArrayList<Bullet>) playerBullets);
                    i--;
                }
            }
        }
    }

    private void resetBulletType() {
        setEquippedBulletType("bamboo");
        setShotDamage(15);
        hasFireBulletActive = false;
    }

    public void equipFireBullet() {
        setEquippedBulletType("fire");
        setShotDamage(30);
        fireBulletStartTime = System.nanoTime();
        hasFireBulletActive = true;
    }

    public void update(HUD hud) {
        if (hasFireBulletActive && System.nanoTime() - fireBulletStartTime >= FIRE_BULLET_DURATION) {
            resetBulletType();
        }

        if (keyHandler.isUpPressed()) {
            setDirection("up");
            if (this.getY() > hud.getHudHeight()) {
                this.setY(getY() - getSpeed());
            }
        }
        if (keyHandler.isDownPressed()) {
            setDirection("down");
            if (this.getY() + GamePanel.tileSize < GamePanel.screenHeight) {
                this.setY(getY() + getSpeed());
            }
        }
        if (keyHandler.isLeftPressed()) {
            setDirection("left");
            if (this.getX() > 0) {
                this.setX(getX() - getSpeed());
            }
        }
        if (keyHandler.isRightPressed()) {
            setDirection("right");
            if (this.getX() + GamePanel.tileSize < GamePanel.screenWidth) {
                this.setX(getX() + getSpeed());
            }
        }
        if (keyHandler.isSpacePressed()) {
            shootCounter++;
            if (shootCounter > shootingRatio) {
                getBullets().add(new Bullet(gp, this, getDirection(), equippedBulletType, 5));
                shootCounter = 0;
            }
        }

        int spriteCounter = getSpriteCounter();
        int spriteNum = getSpriteNum();
        setSpriteCounter( getSpriteCounter() + 1);
        if (spriteCounter > 10) {
            if (spriteNum == 1)
                setSpriteNum(2);
            else if (spriteNum == 2)
                setSpriteNum(1);
            setSpriteCounter(0);
        }
        updatePlayerBullets();
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage img = null;

        drawHealthBar(g2, GamePanel.tileSize);

        int spriteNum = getSpriteNum();
        switch (getDirection()) {
            case "up", "down":
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
            default:
                break;
        }

        var bullets = getBullets();
        if (bullets != null) {
            for (Bullet b: bullets) {
                b.draw(g2);
            }
        }
        g2.drawImage(img, getX(), getY(), GamePanel.tileSize, GamePanel.tileSize, null);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public String getEquippedBulletType() {
        return equippedBulletType;
    }

    public void setEquippedBulletType(String equippedBulletType) {
        this.equippedBulletType = equippedBulletType;
    }
}
