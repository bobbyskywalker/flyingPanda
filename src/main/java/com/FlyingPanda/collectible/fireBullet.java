package com.FlyingPanda.collectible;

import com.FlyingPanda.hud.HUD;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class fireBullet extends Collectible {

    public fireBullet(HUD hud, String type) throws IOException {
        super(hud, type);
        this.setImg(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Sprites/bullets/fire.png"))));
    }

}
