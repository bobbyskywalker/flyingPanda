package com.FlyingPanda.collectible;

import com.FlyingPanda.hud.HUD;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class HealthPickup extends Collectible {
    public HealthPickup(long spawnIntervalTime, HUD hud, String type) throws IOException {
        super(spawnIntervalTime, hud, type);
        this.setImg(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/collectibles/health.png"))));
    }
}
