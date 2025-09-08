package com.FlyingPanda.entity;

import com.FlyingPanda.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    private int x;
    private int y;
    private int speed;

    private int health = 100;
    private int maxHealth = 100;
    private static final int HEALTH_BAR_HEIGHT = 10;
    private static final int HEALTH_BAR_WIDTH = 80;
    private static final int HEALTH_BAR_OFFSET_Y = 10;

    private ArrayList<Bullet> bullets;
    int shootingRatio;
    int shootCounter;
    private int shotDamage = 10;

    public BufferedImage front1, front2, front3, front4, front5, left1, left2, left3, right1, right2, right3;
    private String direction;

    private int spriteCounter = 0;
    private int spriteNum = 1;

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

    /* Accessors */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void drawHealthBar(Graphics2D g2, int entityWidth) {
        if (health <= 0) return;

        int healthBarX = x + (entityWidth - HEALTH_BAR_WIDTH) / 2;
        int healthBarY = y - HEALTH_BAR_OFFSET_Y - HEALTH_BAR_HEIGHT;

        float healthPercentage = (float) health / maxHealth;
        int currentHealthWidth = (int) (HEALTH_BAR_WIDTH * healthPercentage);

        g2.setColor(Color.RED);
        g2.fillRect(healthBarX, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        g2.setColor(Color.GREEN);
        g2.fillRect(healthBarX, healthBarY, currentHealthWidth, HEALTH_BAR_HEIGHT);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(healthBarX, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    public int getShotDamage() {
        return shotDamage;
    }

    public void setShotDamage(int shotDamage) {
        this.shotDamage = shotDamage;
    }

    public List<Bullet> getBullets() {
        return this.bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }
}