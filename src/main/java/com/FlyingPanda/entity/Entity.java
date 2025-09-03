package com.FlyingPanda.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Entity {
    public int x, y;
    public int speed;

    public ArrayList<Bullet> bullets;
    int shootingRatio;
    int shootCounter;

    public BufferedImage front1, front2, front3, front4, front5, left1, left2, left3, right1, right2, right3;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public ArrayList<Bullet> getBullets() {
        return this.bullets;
    }
}
