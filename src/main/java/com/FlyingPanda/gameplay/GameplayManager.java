package com.FlyingPanda.gameplay;

import com.FlyingPanda.collectible.Collectible;
import com.FlyingPanda.collectible.FireBullet;
import com.FlyingPanda.collectible.HealthPickup;
import com.FlyingPanda.entity.Bee;
import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import java.io.IOException;
import java.util.ArrayList;

public class GameplayManager {
    private int waveNum = 1;
    private int numEagles = 0;
    private int numBees = 0;

    private int numEliminatedEnemies = 0;
    private int numEnemiesToEliminate = 5;

    private static final long WAVE_DELAY_MS = 5000;
    private long waveEndTime = 0;
    private boolean waitingForNextWave = false;
    private boolean waveEnd = false;

    private ArrayList<Eagle> eagles = new ArrayList<>();
    private ArrayList<Bee> bees = new ArrayList<>();
    private ArrayList<Collectible> collectibles = new ArrayList<>();

    private HUD hud;
    private GamePanel gp;

    private void spawnEagles() {
        for (int i = 0; i < numEagles; i++)
            eagles.add(new Eagle(gp, hud));
    }

    private void spawnBees() {
        for (int i = 0; i < numBees; i++)
            bees.add(new Bee(gp, hud));
    }

    private void updateEnemies() {
        if (!waveEnd) {
            if (eagles.isEmpty() && bees.isEmpty()) {
                if (numEagles == 6)
                    numEagles = 0;
                numEagles++;
                spawnEagles();
                if (numBees == 6)
                    numBees = 0;
                if (waveNum % 2 == 0)
                    numBees++;
                spawnBees();
            }
        }
        for (Eagle e : eagles)
            e.update();
        for (Bee b : bees)
            b.update();
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
        numEagles = 0;
        numBees = 0;
        numEnemiesToEliminate += 2;

        waitingForNextWave = true;
        waveEndTime = System.currentTimeMillis();
        hud.setWaveCompletionInfo(waveNum - 1, waveNum, WAVE_DELAY_MS);
        waveEnd = false;

    }

    public void updateCurrentWave() {
        if (numEliminatedEnemies >= numEnemiesToEliminate && bees.isEmpty() && eagles.isEmpty() && !waveEnd) {
            waveEnd = true;
            setupNewWave();
        } else if (waitingForNextWave) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - waveEndTime >= WAVE_DELAY_MS) {
                waitingForNextWave = false;
                hud.clearWaveCompletionInfo();
                updateEnemies();
                updateCollectibles();
            }
        } else {
            updateEnemies();
            updateCollectibles();
        }
    }

    public GameplayManager(GamePanel gp, HUD hud) throws IOException {
        this.gp = gp;
        this.hud = hud;
        collectibles.add(new FireBullet( 20_000_000_000L, hud, "fire"));
        collectibles.add(new HealthPickup(30_000_000_000L, hud, "health"));
    }

    public void dispose() {
        eagles.clear();
        bees.clear();
        collectibles.clear();
        eagles = null;
        bees = null;
        collectibles = null;
        hud = null;
        gp = null;
    }

    public ArrayList<Eagle> getEagles() {
        return this.eagles;
    }

    public ArrayList<Bee> getBees() {
        return this.bees;
    }

    public ArrayList<Collectible> getCollectibles() {
        return this.collectibles;
    }

    public int getNumEliminatedEnemies() {
        return numEliminatedEnemies;
    }

    public void setNumEliminatedEnemies(int numEliminatedEnemies) {
        this.numEliminatedEnemies = numEliminatedEnemies;
    }
}
