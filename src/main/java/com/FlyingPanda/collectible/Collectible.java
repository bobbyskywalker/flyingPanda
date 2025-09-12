package com.FlyingPanda.collectible;

import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class Collectible {
    private int x;
    private int y;

    private long lastSpawnTime;
    private final long spawnIntervalTime;

    private boolean isOnMap;

    private final HUD hud;

    private String collectibleType;

    private BufferedImage img;

    protected Collectible(long spawnIntervalTime, HUD hud, String type) {
        this.hud = hud;
        this.collectibleType = type;
        this.isOnMap = false;
        this.lastSpawnTime = System.nanoTime();
        this.spawnIntervalTime = spawnIntervalTime;
    }

    private void spawnCollectibleAt() {
        Random rand = new Random();

        this.setX(rand.nextInt(GamePanel.screenWidth));
        int availableHeight = GamePanel.screenHeight - hud.getHudHeight() - GamePanel.tileSize;
        this.setY(hud.getHudHeight() + rand.nextInt(availableHeight));
        lastSpawnTime = System.nanoTime();
    }

    private boolean isTimeForSpawn() {
        long currentTime = System.nanoTime();
        return currentTime - lastSpawnTime >= spawnIntervalTime;

    }

    public void beCollected() {
        this.isOnMap = false;
        this.lastSpawnTime = System.nanoTime();
    }

    public void update() {
        if (!isOnMap && isTimeForSpawn()) {
            spawnCollectibleAt();
            isOnMap = true;
        }
    }

    public void draw(Graphics2D g2) {
        if (isOnMap)
            g2.drawImage(img, x, y, GamePanel.tileSize, GamePanel.tileSize, null);
    }

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

    public long getLastSpawnTime() {
        return lastSpawnTime;
    }

    public void setLastSpawnTime(long lastSpawnTime) {
        this.lastSpawnTime = lastSpawnTime;
    }

    public boolean isOnMap() {
        return isOnMap;
    }

    public void setOnMap(boolean onMap) {
        isOnMap = onMap;
    }

    public String getCollectibleType() {
        return collectibleType;
    }

    public void setCollectibleType(String collectibleType) {
        this.collectibleType = collectibleType;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
