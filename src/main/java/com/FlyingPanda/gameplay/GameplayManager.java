package com.FlyingPanda.gameplay;

import com.FlyingPanda.collectible.Collectible;
import com.FlyingPanda.collectible.FireBullet;
import com.FlyingPanda.collectible.HealthPickup;
import com.FlyingPanda.entity.*;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameplayManager {
    private int waveNum = 1;

    private int numEliminatedEnemies = 0;
    private int numEnemiesToEliminate = 4;

    private static final long WAVE_DELAY_MS = 5000;
    private static final long TIME_TO_FINISH_WAVE = 60_000_000_000L;
    private long waveStartTime;
    private long waveEndTime = 0;
    private boolean waitingForNextWave = false;
    private boolean waveEnd = false;

    private List<Entity> enemies = new ArrayList<>();
    private ArrayList<Collectible> collectibles = new ArrayList<>();

    private Player player;
    private HUD hud;
    private GamePanel gp;

    private void spawnEnemiesForWave(int waveNumber) {
        int baseEagles = 1;
        int baseBees = 0;
        int baseSpiders = 0;

        int numEagles = baseEagles + waveNumber;
        int numBees = baseBees + (waveNumber / 2);
        int numSpiders = baseSpiders + (waveNumber / 3);

        for (int i = 0; i < numEagles; i++)
            enemies.add(new Eagle(gp, hud));

        for (int i = 0; i < numBees; i++)
            enemies.add(new Bee(gp, hud));

        for (int i = 0; i < numSpiders; i++)
            enemies.add(new Spider(gp, hud));
    }

    private void updateEnemies() {
        if (!waveEnd) {
            if (enemies.isEmpty())
                spawnEnemiesForWave(waveNum);
            for (Entity e : enemies)
                e.update();
        }
    }

    private void updateCollectibles() {
        for (Collectible c: collectibles) {
            c.update();
        }
    }

    private void setupNewWave() {
        waveNum++;
        numEliminatedEnemies = 0;
        hud.setWaveNumber(waveNum);
        numEnemiesToEliminate += 1;

        waitingForNextWave = true;
        waveEndTime = System.currentTimeMillis();
        hud.setWaveCompletionInfo(waveNum - 1, waveNum, WAVE_DELAY_MS);
        waveEnd = false;

    }

    public void updateCurrentWave() {
        if (numEliminatedEnemies >= numEnemiesToEliminate && enemies.isEmpty() && !waveEnd) {
            waveEnd = true;
            hud.stopWaveTimer();
            setupNewWave();
        } else if (waitingForNextWave) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - waveEndTime >= WAVE_DELAY_MS) {
                waitingForNextWave = false;
                hud.clearWaveCompletionInfo();
                waveStartTime = System.nanoTime();
                hud.startWaveTimer();
                updateEnemies();
                updateCollectibles();
            }
        } else {
            if (waveStartTime > 0) {
                long currentTime = System.nanoTime();
                long waveTimePassed = currentTime - waveStartTime;

                if (waveTimePassed >= TIME_TO_FINISH_WAVE) {
                    player.setLives(player.getLives() - 1);
                    hud.showTimeoutMessage();

                    if (player.getLives() <= 0)
                        gp.gameOver();
                    waveStartTime = System.nanoTime();
                    hud.startWaveTimer();
                }
            }

            updateEnemies();
            updateCollectibles();
        }
    }

    public GameplayManager(GamePanel gp, HUD hud, Player player) throws IOException {
        this.gp = gp;
        this.hud = hud;
        this.player = player;

        this.waveStartTime = System.nanoTime();
        this.hud.startWaveTimer();

        collectibles.add(new FireBullet( 20_000_000_000L, hud, "fire"));
        collectibles.add(new HealthPickup(60_000_000_000L, hud, "health"));
    }

    public void dispose() {
        enemies.clear();
        collectibles.clear();
        enemies = null;
        collectibles = null;
        hud = null;
        gp = null;
    }

    public List<Entity> getEnemies() {
        return this.enemies;
    }

    public List<Collectible> getCollectibles() {
        return this.collectibles;
    }

    public int getNumEliminatedEnemies() {
        return numEliminatedEnemies;
    }

    public void setNumEliminatedEnemies(int numEliminatedEnemies) {
        this.numEliminatedEnemies = numEliminatedEnemies;
    }
}
