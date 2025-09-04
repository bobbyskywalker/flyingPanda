
package com.FlyingPanda.utils;

import com.FlyingPanda.entity.Bullet;
import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.entity.Player;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class CollissionChecker {

    private CollissionChecker() {}

    public static boolean isColliding(int x1, int y1, int width1, int height1,
                                      int x2, int y2, int width2, int height2) {
        return x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 < y2 + height2 &&
                y1 + height1 > y2;
    }

    public static boolean checkEntityCollision(Player player, Eagle eagle) {
        return isColliding(player.getX(), player.getY(), GamePanel.tileSize, GamePanel.tileSize,
                eagle.getX(), eagle.getY(), GamePanel.tileSize, GamePanel.tileSize);
    }

    public static boolean checkBulletEntityCollision(Bullet bullet, int entityX, int entityY) {
        return isColliding(bullet.x, bullet.y, GamePanel.tileSize, GamePanel.tileSize,
                entityX, entityY, GamePanel.tileSize, GamePanel.tileSize);
    }

    /* enemies hitting player */
    public static void checkEnemyBulletsHitPlayer(List<Eagle> eagles, Player player) {
        for (Eagle eagle : eagles) {
            var eagleBullets = eagle.getBullets();
            for (int i = 0; i < eagleBullets.size(); i++) {
                Bullet enemyBullet = eagleBullets.get(i);
                if (checkBulletEntityCollision(enemyBullet, player.getX(), player.getY())) {
                    eagleBullets.remove(i);
                    eagle.setBullets((ArrayList<Bullet>) eagleBullets);
                    i--;
                    player.setHealth(player.getHealth() - eagle.getShotDamage());
                }
            }
        }
    }

    /* player bullets hitting enemies */
    public static void checkPlayerBulletsHitEnemies(Player player, List<Eagle> eagles, HUD hud) {
        var playerBullets = player.getBullets();
        for (int bulletIndex = 0; bulletIndex < playerBullets.size(); bulletIndex++) {
            Bullet playerBullet = playerBullets.get(bulletIndex);

            for (int eagleIndex = 0; eagleIndex < eagles.size(); eagleIndex++) {
                Eagle eagle = eagles.get(eagleIndex);
                if (checkBulletEntityCollision(playerBullet, eagle.getX(), eagle.getY())) {
                    playerBullets.remove(bulletIndex);
                    player.setBullets((ArrayList<Bullet>) playerBullets);
                    bulletIndex--;

                    var processedEagle = eagles.get(eagleIndex);
                    int processedEagleHealth = processedEagle.getHealth();
                    processedEagle.setHealth(processedEagleHealth - player.getShotDamage());

                    if (processedEagle.getHealth() <= 0) {
                        eagles.remove(eagleIndex);
                        hud.setScore(hud.getScore() + 10);
                    }
                    break;
                }
            }
        }
    }

    public static void checkAllCollisions(Player player, List<Eagle> eagles, HUD hud) {
        checkEnemyBulletsHitPlayer(eagles, player);
        checkPlayerBulletsHitEnemies(player, eagles, hud);
    }
}