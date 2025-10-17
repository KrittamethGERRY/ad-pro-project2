package se233.notcontra.controller;

import java.util.List;

import se233.notcontra.model.Boss.WallBoss;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.view.GameStages.FirstStage;
import se233.notcontra.view.GameStages.GameStage;
import se233.notcontra.view.GameStages.SecondStage;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import se233.notcontra.model.*;

public class DrawingLoop implements Runnable {	
	public static List<Effect> effects = new ArrayList<>();
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
	
	public void checkAllCollisions(Player player) {
		player.checkHighestJump();
		player.checkStageBoundaryCollision();
		player.checkPlatformCollision(gameStage.getPlatforms());
		player.checkItemCollision(gameStage);
		player.isCollided(gameStage);

	}

	public void paint(Player player) {
		player.repaint();
	}

	private void paintBullet(List<Bullet> bullets, ShootingDirection direction) {
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();
			
			boolean shouldRemove = false;

			if (bullet.isOutOfBounds(GameStage.WIDTH, GameStage.HEIGHT )) {
				shouldRemove = true;
			}
			
			// Enemies collision with bullet
			for (Enemy enemy : gameStage.getEnemies()) {
				if (gameStage.getBoss().localToParent(enemy.getBoundsInParent()).intersects(bullet.getBoundsInParent())
						&& bullet.getOwner() == BulletOwner.PLAYER) {
					// Add effect after the bullet hit the enemies
					Effect explosion = new Effect(ImageAssets.EXPLOSION_IMG, 8, 8, 1, bullet.getXPosition() - 16, bullet.getYPosition() - 16, 64, 64);
					effects.add(explosion);
					javafx.application.Platform.runLater(() -> {
						gameStage.getChildren().add(explosion);
					});
					enemy.takeDamage(500);
					
					Platform.runLater(this::updateScore);
					
					if (enemy.getType() == EnemyType.WALL_SHOOTER) {
						
					} else if (enemy.getType() == EnemyType.TURRET) {
						if (WallBoss.totalTurret <= 0 && !gameStage.getBoss().getWeakPoints().isEmpty()) {
							Enemy core = gameStage.getBoss().getWeakPoints().getFirst();
                            Platform.runLater(() -> {
                                GameLoop.enemies.add(core);
                                    gameStage.getBoss().getChildren().add(core);
                                    gameStage.getBoss().getWeakPoints().remove(core);
                            });
						}
					}
					shouldRemove = true;
				}
			}
			
			// Player collisions with bullet
			
			Bounds playerBounds = gameStage.getPlayer().localToParent(gameStage.getPlayer().getHitBox().getBoundsInParent());
			if (playerBounds.intersects(bullet.getBoundsInParent())
					&& bullet.getOwner() != BulletOwner.PLAYER
					&& !gameStage.getPlayer().getTankBuster()
					&& !gameStage.getPlayer().isDying()) {
				gameStage.getPlayer().die();

				shouldRemove = true;
			}
			Platform.runLater(this::updateLives);
			
			// Remove bullet
			if (shouldRemove) {
				iterator.remove();
				Platform.runLater(() -> gameStage.getChildren().remove(bullet));
			}
		}
	}
	
	private void paintEffects(List<Effect> effects) {
        Iterator<Effect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.tick();
            if (effect.isFinished()) {
                iterator.remove();
                Platform.runLater(() -> gameStage.getChildren().remove(effect));
            }
        }
    }

	private void updateScore() {
		gameStage.getScoreLabel().setText("Score: " + String.format("%06d", GameLoop.getScore()));
	}
	
	private void updateLives() {
		gameStage.getLivesLabel().setText("Lives: " + gameStage.getPlayer().getLives());
	}

	private void updateBoss() {
		if (gameStage instanceof FirstStage) {
			if (gameStage.getBoss().getChildren().isEmpty()) {

			}
		} else if (gameStage instanceof SecondStage) {
			
		}
	}
	
	private void updateEnemies() {
		Iterator<Enemy> iterator = GameLoop.enemies.iterator();
		while (iterator.hasNext()) {
			Enemy enemy = iterator.next();
			if (enemy.isAlive()) {
				enemy.updateWithPlayer(gameStage.getPlayer(), gameStage);
			} else {
				// Kill remove enemy from the stage
				iterator.remove();
				javafx.application.Platform.runLater(() -> {
					gameStage.getBoss().getChildren().remove(enemy);
					GameLoop.enemies.remove(enemy);
				});
			} 
		}
	}
	
	public void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		while (running) {
			if (!GameLoop.isPaused) {
				float time = System.currentTimeMillis();
				checkAllCollisions(gameStage.getPlayer());
				paint(gameStage.getPlayer());
				paintEffects(effects);
				paintBullet(GameLoop.bullets, GameLoop.shootingDir);
				updateEnemies();
				updateBoss();
				if (gameStage.getBoss() != null && gameStage.getBoss().isAlive()) {
					gameStage.getBoss().update();
				}
			}
			
			float time = System.currentTimeMillis();
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