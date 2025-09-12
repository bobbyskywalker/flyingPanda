package com.FlyingPanda.utils;

import com.FlyingPanda.collectible.Collectible;
import com.FlyingPanda.entity.Bullet;
import com.FlyingPanda.entity.Entity;
import com.FlyingPanda.entity.Eagle;
import com.FlyingPanda.entity.Bee;
import com.FlyingPanda.entity.Player;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;
import com.FlyingPanda.gameplay.GameplayManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollissionChecker {

    private CollissionChecker() {}

    public static boolean isColliding(int x1, int y1, int width1, int height1,
                                      int x2, int y2, int width2, int height2) {
        return x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 < y2 + height2 &&
                y1 + height1 > y2;
    }

    public static boolean checkBulletEntityCollision(Bullet bullet, int entityX, int entityY) {
        return isColliding(bullet.x, bullet.y, GamePanel.tileSize, GamePanel.tileSize,
                entityX, entityY, GamePanel.tileSize, GamePanel.tileSize);
    }

    /* enemy list merger for polymorphic checks */
    private static List<Entity> mergeEnemyLists(List<Eagle> eagles, List<Bee> bees) {
        List<Entity> allEnemies = new ArrayList<>();
        allEnemies.addAll(eagles);
        allEnemies.addAll(bees);
        return allEnemies;
    }

    /* enemies hitting player */
    public static void checkEnemyBulletsHitPlayer(List<Eagle> eagles, List<Bee> bees, Player player, GamePanel gp) {
        List<Entity> allEnemies = mergeEnemyLists(eagles, bees);

        for (Entity enemy : allEnemies) {
            var enemyBullets = enemy.getBullets();
            if (enemyBullets == null) continue;

            for (int i = 0; i < enemyBullets.size(); i++) {
                Bullet enemyBullet = enemyBullets.get(i);
                if (checkBulletEntityCollision(enemyBullet, player.getX(), player.getY())) {
                    enemyBullets.remove(i);
                    enemy.setBullets((ArrayList<Bullet>) enemyBullets);
                    i--;
                    player.setHealth(player.getHealth() - enemy.getShotDamage());
                    if (player.getHealth() <= 0) {
                        if (player.getLives() > 0) {
                            player.setHealth(100);
                            player.setLives(player.getLives() - 1);
                        } else {
                            gp.gameOver();
                        }
                    }
                }
            }
        }
    }

    /* player bullets hitting enemies */
    public static void checkPlayerBulletsHitEnemies(Player player, List<Eagle> eagles, List<Bee> bees, HUD hud, GameplayManager wm) {
        var playerBullets = player.getBullets();

        for (int bulletIndex = 0; bulletIndex < playerBullets.size(); bulletIndex++) {
            Bullet playerBullet = playerBullets.get(bulletIndex);
            boolean hitFound = false;

            // collision with eagles
            for (int eagleIndex = 0; eagleIndex < eagles.size(); eagleIndex++) {
                Eagle eagle = eagles.get(eagleIndex);
                if (checkBulletEntityCollision(playerBullet, eagle.getX(), eagle.getY())) {
                    playerBullets.remove(bulletIndex);
                    player.setBullets((ArrayList<Bullet>) playerBullets);
                    bulletIndex--;

                    eagle.setHealth(eagle.getHealth() - player.getShotDamage());

                    if (eagle.getHealth() <= 0) {
                        wm.setNumEliminatedEnemies(wm.getNumEliminatedEnemies() + 1);
                        eagles.remove(eagleIndex);
                        hud.setScore(hud.getScore() + 30);
                    }
                    hitFound = true;
                    break;
                }
            }

            // colission with bees
            if (!hitFound) {
                for (int beeIndex = 0; beeIndex < bees.size(); beeIndex++) {
                    Bee bee = bees.get(beeIndex);
                    if (checkBulletEntityCollision(playerBullet, bee.getX(), bee.getY())) {
                        playerBullets.remove(bulletIndex);
                        player.setBullets((ArrayList<Bullet>) playerBullets);
                        bulletIndex--;

                        bee.setHealth(bee.getHealth() - player.getShotDamage());

                        if (bee.getHealth() <= 0) {
                            wm.setNumEliminatedEnemies(wm.getNumEliminatedEnemies() + 1);
                            bees.remove(beeIndex);
                            hud.setScore(hud.getScore() + 50);
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void checkCollectibleCollection(Player p, List<Collectible> collectibles) {
        for (Collectible c : collectibles) {
            if (isColliding(c.getX(), c.getY(), GamePanel.tileSize, GamePanel.tileSize,
                        p.getX(), p.getY(), GamePanel.tileSize, GamePanel.tileSize)) {

                c.beCollected();
                if (Objects.equals(c.getCollectibleType(), "fire")) {
                    p.equipFireBullet();
                }
            }
        }
    }

    public static void checkAllCollisions(Player player, List<Eagle> eagles, List<Bee> bees, HUD hud, GameplayManager wm, GamePanel gp) {
        checkEnemyBulletsHitPlayer(eagles, bees, player, gp);
        checkPlayerBulletsHitEnemies(player, eagles, bees, hud, wm);
        checkCollectibleCollection(player, wm.getCollectibles());
    }
}