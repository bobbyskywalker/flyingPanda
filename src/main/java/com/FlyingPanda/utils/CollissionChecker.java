
package com.FlyingPanda.utils;

import com.FlyingPanda.entity.Bullet;
import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.entity.Player;
import com.FlyingPanda.main.GamePanel;
import java.util.ArrayList;

public class CollissionChecker {

    private CollissionChecker() {}

    public static boolean isColliding(int x1, int y1, int width1, int height1,
                                      int x2, int y2, int width2, int height2) {
        return x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 < y2 + height2 &&
                y1 + height1 > y2;
    }

    /* player - enemy collision */
    public static boolean checkEntityCollision(Player player, Eagle eagle, GamePanel gp) {
        return isColliding(player.x, player.y, gp.tileSize, gp.tileSize,
                eagle.x, eagle.y, gp.tileSize, gp.tileSize);
    }

    /* entity bullet collision */
    public static boolean checkBulletEntityCollision(Bullet bullet, int entityX, int entityY, GamePanel gp) {
        return isColliding(bullet.x, bullet.y, gp.tileSize, gp.tileSize,
                entityX, entityY, gp.tileSize, gp.tileSize);
    }

    /* enemies hitting player */
    public static void checkEnemyBulletsHitPlayer(ArrayList<Eagle> eagles, Player player, GamePanel gp) {
        for (Eagle eagle : eagles) {
            for (int i = 0; i < eagle.bullets.size(); i++) {
                Bullet enemyBullet = eagle.bullets.get(i);
                if (checkBulletEntityCollision(enemyBullet, player.x, player.y, gp)) {
                    eagle.bullets.remove(i);
                    i--;
                    // TODO: Reduce player health, play sound effect, etc.
                    System.out.println("Player hit by eagle bullet!");
                }
            }
        }
    }

    /* player bullets hitting enemies */
    public static void checkPlayerBulletsHitEnemies(Player player, ArrayList<Eagle> eagles, GamePanel gp) {
        for (int bulletIndex = 0; bulletIndex < player.bullets.size(); bulletIndex++) {
            Bullet playerBullet = player.bullets.get(bulletIndex);

            for (int eagleIndex = 0; eagleIndex < eagles.size(); eagleIndex++) {
                Eagle eagle = eagles.get(eagleIndex);
                if (checkBulletEntityCollision(playerBullet, eagle.x, eagle.y, gp)) {
                    // Handle enemy hit by player bullet
                    player.bullets.remove(bulletIndex);
                    bulletIndex--;
                    // TODO: Reduce enemy health, add score, play sound effect, etc.
                    // For now, let's just remove the eagle
                    eagles.remove(eagleIndex);
                    eagleIndex--;
                    System.out.println("Eagle hit by player bullet!");
                    break;
                }
            }
        }
    }

    public static void checkAllCollisions(Player player, ArrayList<Eagle> eagles, GamePanel gp) {
        checkEnemyBulletsHitPlayer(eagles, player, gp);
        checkPlayerBulletsHitEnemies(player, eagles, gp);
    }
}