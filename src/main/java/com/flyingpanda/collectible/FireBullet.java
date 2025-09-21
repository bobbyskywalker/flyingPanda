package com.flyingpanda.collectible;

import com.flyingpanda.hud.HUD;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class FireBullet extends Collectible {

    public FireBullet(long spawnIntervalTime, HUD hud, String type) throws IOException {
        super(spawnIntervalTime, hud, type);
        this.setImg(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/fire.png"))));
    }

}
