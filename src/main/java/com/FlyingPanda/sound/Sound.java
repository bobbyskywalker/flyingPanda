package com.FlyingPanda.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/Sounds/SFX/toggle.wav");
        soundURL[1] = getClass().getResource("/Sounds/SFX/Player/shoot.wav");
        soundURL[2] = getClass().getResource("/Sounds/SFX/Player/hit.wav");
        soundURL[3] = getClass().getResource("/Sounds/SFX/Enemy/enemy_shoot.wav");
        soundURL[4] = getClass().getResource("/Sounds/SFX/Enemy/hitX.wav");
        soundURL[5] = getClass().getResource("/Sounds/Music/gameplay_music.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        clip.stop();
    }
}
