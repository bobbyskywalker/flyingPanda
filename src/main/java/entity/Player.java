package entity;

import main.Controller;
import main.GamePanel;

import java.awt.*;

public class Player extends Entity {
    GamePanel gp;
    Controller keyHandler;

    public Player(Controller keyH, GamePanel gp) {
        this.gp = gp;
        this.keyHandler = keyH;

        setDefaultValues();
    }

    public void setDefaultValues() {
        this.x = 100;
        this.y = 100;
        speed = 4;
    }

    public void update() {
        if (keyHandler.upPressed) {
            this.y -= this.speed;
        } else if (keyHandler.downPressed) {
            this.y += this.speed;
        } else if (keyHandler.leftPressed) {
            this.x -= this.speed;
        } else if (keyHandler.rightPressed) {
            this.x += this.speed;
        }
    }


    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(this.x, this.y, gp.tileSize, gp.tileSize);
        g2.dispose();
    }
}
