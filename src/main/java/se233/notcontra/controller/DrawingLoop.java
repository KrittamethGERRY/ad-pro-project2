package se233.notcontra.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import se233.notcontra.model.*;
import se233.notcontra.view.GameStage;

public class DrawingLoop implements Runnable {
	private GameStage gameStage;
	private int frameRate;
	private float interval;
	private boolean running;
	
	public DrawingLoop(GameStage gameStage) {
		this.gameStage = gameStage;
		frameRate = 60;
		interval = 1000f / frameRate;
		running = true;
	}

	public void checkAllCollisions(Player player ) {
		player.checkGroundCollision();
		player.checkHighestJump();
		player.checkStageBoundaryCollision();

	}

	public void checkPlayerBoss(Player player , Wallboss wallboss) {
		if (wallboss != null && wallboss.isAlive()) {
				checkBulletBossCollisions(GameLoop.bullets, wallboss);
			// TODO: Add player vs. boss collision logic here
		}
	}

	private void checkBulletBossCollisions(ArrayList<Bullet> bullets, Wallboss wallboss) {
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
			if (bullet.isEnemyBullet()) continue;

			boolean hit = false;
			for (Rectangle hitbox : wallboss.getWeakPoints()) {
				if (bullet.getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
					wallboss.takeDamage(50);

					Platform.runLater(() -> gameStage.getChildren().remove(bullet));
					bulletIterator.remove();

					hit = true;
					break;
				}
			}
			if (hit) continue;
		}
	}

	private void checkBulletEnemyCollisions(ArrayList<Bullet> bullets, ArrayList<Enemy> enemies) {
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
			if (bullet.isEnemyBullet()) continue; // Skip enemy bullets

			Iterator<Enemy> enemyIterator = enemies.iterator();
			boolean bulletHit = false;

			while (enemyIterator.hasNext()) {
				Enemy enemy = enemyIterator.next();
				if (!enemy.isAlive()) continue;

				// Create a rectangle for enemy bounds
				double enemyLeft = enemy.getX();
				double enemyRight = enemy.getX() + enemy.getW();
				double enemyTop = enemy.getY();
				double enemyBottom = enemy.getY() + enemy.getH();

				double bulletX = bullet.getXPosition();
				double bulletY = bullet.getYPosition();

				// Simple point-in-rectangle collision
				if (bulletX >= enemyLeft && bulletX <= enemyRight &&
						bulletY >= enemyTop && bulletY <= enemyBottom) {

					enemy.kill();
					Platform.runLater(() -> gameStage.getChildren().remove(bullet));
					bulletIterator.remove();
					bulletHit = true;
					System.out.println("Enemy destroyed!");
					break;
				}
			}
			if (bulletHit) break;
		}
	}

	private void checkEnemyPlayerCollisions(ArrayList<Enemy> enemies, Player player) {
		for (Enemy enemy : enemies) {
			if (!enemy.isAlive()) continue;
			if (enemy.getType() == EnemyType.FLYING) {
				if (enemy.checkCollisionWithPlayer(player)) {
					enemy.kill();
					System.out.println("Player hit by flying enemy! Game Over!");
					// TODO: Handle player death/damage
				}
			}
		}
	}

	public void paint(Player player) {
		player.repaint();
	}

	private void paintBullet(ArrayList<Bullet> bullets, ShootingDirection direction) {
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();

			if (bullet.getXPosition() >= GameStage.WIDTH || bullet.getXPosition() <= 0 ||
					bullet.getYPosition() >= GameStage.HEIGHT || bullet.getYPosition() <= 0) {
				Platform.runLater(() -> gameStage.getChildren().remove(bullet));
				iterator.remove();
			}
		}
	}

	private void updateEnemies(ArrayList<Enemy> enemies, Player player) {
		Iterator<Enemy> iterator = enemies.iterator();
		while (iterator.hasNext()) {
			Enemy enemy = iterator.next();
			if (enemy.isAlive()) {
				enemy.updateWithPlayer(player, gameStage);
			} else {
				iterator.remove();
			}
		}
	}
	
	@Override
	public void run() {
		while (running) {
			float time = System.currentTimeMillis();
			checkAllCollisions(gameStage.getPlayer());
			paint(gameStage.getPlayer());
			paintBullet(GameLoop.bullets, GameLoop.shootingDir);
			updateEnemies(GameLoop.enemies, gameStage.getPlayer());

			if (gameStage.getWallboss() != null && gameStage.getWallboss().isAlive()) {
				Platform.runLater(() -> gameStage.getWallboss().update());
			}

			time = System.currentTimeMillis() - time;
			if (time < interval) {
				try {
					Thread.sleep((long) (interval - time));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep((long) (interval - (interval % time)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}