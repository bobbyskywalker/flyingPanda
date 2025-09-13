package com.FlyingPanda.collision;

import com.FlyingPanda.bullet.Bullet;
import com.FlyingPanda.collectible.Collectible;
import com.FlyingPanda.entity.*;
import com.FlyingPanda.hud.HUD;
import com.FlyingPanda.main.GamePanel;
import com.FlyingPanda.gameplay.GameplayManager;

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
        return isColliding(bullet.getX(), bullet.getY(), GamePanel.tileSize, GamePanel.tileSize,
                entityX, entityY, GamePanel.tileSize, GamePanel.tileSize);
    }

    /* enemies hitting player */
    public static void checkEnemyBulletsHitPlayer(List<Entity> enemies, Player player, GamePanel gp) {
        for (Entity enemy : enemies) {
            var enemyBullets = enemy.getBullets();
            if (enemyBullets == null) continue;

            for (int i = 0; i < enemyBullets.size(); i++) {
                Bullet enemyBullet = enemyBullets.get(i);
                if (checkBulletEntityCollision(enemyBullet, player.getX(), player.getY())) {
                    enemyBullets.remove(i);
                    enemy.setBullets(enemyBullets);
                    i--;
                    player.setHealth(player.getHealth() - enemy.getShotDamage());
                    if (player.getHealth() <= 0) {
                        player.setLives(player.getLives() - 1);
                        if (player.getLives() > 0) {
                            player.setHealth(100);
                        } else {
                            gp.gameOver();
                        }
                    }
                }
            }
        }
    }

    /* player bullets hitting enemies */
    public static void checkPlayerBulletsHitEnemies(Player player, List<Entity> enemies, HUD hud, GameplayManager wm) {
        var playerBullets = player.getBullets();

        for (int bulletIndex = 0; bulletIndex < playerBullets.size(); bulletIndex++) {
            Bullet playerBullet = playerBullets.get(bulletIndex);

            for (int enemyIndex = 0; enemyIndex < enemies.size(); enemyIndex++) {
                Entity enemy = enemies.get(enemyIndex);
                if (checkBulletEntityCollision(playerBullet, enemy.getX(), enemy.getY())) {
                    playerBullets.remove(bulletIndex);
                    player.setBullets(playerBullets);
                    bulletIndex--;

                    enemy.setHealth(enemy.getHealth() - player.getShotDamage());

                    if (enemy.getHealth() <= 0) {
                        wm.setNumEliminatedEnemies(wm.getNumEliminatedEnemies() + 1);
                        enemies.remove(enemyIndex);
                        hud.updateScore(enemy);
                    }
                    break;
                }
            }
        }
    }

    /* player collecting collectibles */
    private static void checkCollectibleCollection(Player p, List<Collectible> collectibles) {
        for (Collectible c : collectibles) {
            if (isColliding(c.getX(), c.getY(), GamePanel.tileSize, GamePanel.tileSize,
                        p.getX(), p.getY(), GamePanel.tileSize, GamePanel.tileSize)) {

                c.beCollected();
                if (Objects.equals(c.getCollectibleType(), "fire")) {
                    p.equipFireBullet();
                } else if (Objects.equals(c.getCollectibleType(), "health")) {
                    int health = p.getHealth();
                    p.setHealth(health + 15);
                }
            }
        }
    }

    /* the ONLY method callable from outside */
    public static void checkAllCollisions(Player player, List<Entity> enemies, HUD hud, GameplayManager wm, GamePanel gp) {
        checkEnemyBulletsHitPlayer(enemies, player, gp);
        checkPlayerBulletsHitEnemies(player, enemies, hud, wm);
        checkCollectibleCollection(player, wm.getCollectibles());
    }
}