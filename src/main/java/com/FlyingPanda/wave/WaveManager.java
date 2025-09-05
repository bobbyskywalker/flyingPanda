package com.FlyingPanda.wave;

import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WaveManager {
    private int waveNum = 1;
    private int numEagles = 0;

    private int numEliminatedEnemies = 0;
    private int numEnemiesToEliminate = 5;
    private boolean waveEnd = false;

    private ArrayList<Eagle> eagles = new ArrayList<>();

    private HUD hud;
    private GamePanel gp;

    private void spawnEagles() {
        for (int i = 0; i < numEagles; i++)
            eagles.add(new Eagle(gp, hud));
    }

    private void updateEnemies() {
        if (eagles.isEmpty() && !waveEnd) {
            numEagles++;
            spawnEagles();
        }
        for (Eagle e : eagles)
            e.update();
    }

    private void setupNewWave() {
        waveNum++;
        numEliminatedEnemies = 0;
        hud.setWaveNumber(waveNum);
        numEagles = 0;
        numEnemiesToEliminate += 2;
        // TODO:
        // display new wave info popup
        // wait 5s for a new wave
        waveEnd = false;

    }

    public void updateCurrentWave() {
        if (numEliminatedEnemies >= numEnemiesToEliminate && eagles.isEmpty()) {
            waveEnd = true;
            setupNewWave();
        } else {
            updateEnemies();
        }
    }

    public WaveManager(GamePanel gp, HUD hud) {
        this.gp = gp;
        this.hud = hud;
    }

    public ArrayList<Eagle> getEagles() {
        return this.eagles;
    }

    public int getNumEliminatedEnemies() {
        return numEliminatedEnemies;
    }

    public void setNumEliminatedEnemies(int numEliminatedEnemies) {
        this.numEliminatedEnemies = numEliminatedEnemies;
    }
}
