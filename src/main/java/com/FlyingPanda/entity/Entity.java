package com.FlyingPanda.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Entity {
    public int x, y;
    public int speed;

    private int health = 100;
    private int maxHealth = 100;
    private static final int HEALTH_BAR_HEIGHT = 10;
    private static final int HEALTH_BAR_WIDTH = 80;
    private static final int HEALTH_BAR_OFFSET_Y = 10;

    public ArrayList<Bullet> bullets;
    int shootingRatio;
    int shootCounter;
    private int shotDamage = 10;

    public BufferedImage front1, front2, front3, front4, front5, left1, left2, left3, right1, right2, right3;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public ArrayList<Bullet> getBullets() {
        return this.bullets;
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
}
