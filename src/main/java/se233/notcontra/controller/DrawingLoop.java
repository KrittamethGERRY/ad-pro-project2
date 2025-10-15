package se233.notcontra.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.model.*;
import se233.notcontra.view.FirstStage;
import se233.notcontra.view.GameStage;
import se233.notcontra.view.SecondStage;

public class DrawingLoop implements Runnable {	
	private GameStage gameStage;
	private int frameRate;
	private float interval;
	private static boolean running;
	
	public DrawingLoop(GameStage gameStage) {
		this.gameStage = gameStage;
		frameRate = 60;
		interval = 1000f / frameRate;
		running = true;
	}
	
	public void checkPlayerCollisions(Player player) {
		player.checkHighestJump();
		player.checkStageBoundaryCollision();
		player.checkPlatformCollision(gameStage.getPlatforms());
		player.checkItemCollision(gameStage);
	}

	public void paint(Player player) {
		player.repaint();
	}

	private void paintBullet(GameStage gameStage, ShootingDirection direction) {
		Iterator<Bullet> iterator = gameStage.getBullets().iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();
			
			boolean shouldRemove = false;

			if (bullet.getXPosition() >= GameStage.WIDTH || bullet.getXPosition() <= 0 ||
				bullet.getYPosition() >= GameStage.HEIGHT || bullet.getYPosition() <= 0) {
				shouldRemove = true;
			}
			
			// Enemies collision with bullet
			for (Enemy enemy : gameStage.getEnemies()) {
				if (gameStage.getBoss().localToParent(enemy.getBoundsInParent()).intersects(bullet.getBoundsInParent())
						&& bullet.getOwner() == BulletOwner.PLAYER) {
					enemy.takeDamage(500);
					
					Platform.runLater(this::updateScore);
					
					if (enemy.getType() == EnemyType.WALL_SHOOTER) {
						
					} else if (enemy.getType() == EnemyType.TURRET) {
						if (Wallboss.totalTurret <= 0 && !gameStage.getBoss().getWeakPoints().isEmpty()) {
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
			if (gameStage.getPlayer().getBoundsInParent().intersects(bullet.getBoundsInParent()) && !gameStage.getPlayer().getTankBuster()) {
				gameStage.getPlayer().die();
				shouldRemove = true;
			}
			
			// Remove bullet
			if (shouldRemove) {
				iterator.remove();
				Platform.runLater(() -> gameStage.getChildren().remove(bullet));
				
			}
		}
	}
	
	private void updateScore() {
		gameStage.getScoreLabel().setText(String.format("%06d", GameLoop.getScore()));
	}
	
	private void updateBoss() {
		if (gameStage instanceof FirstStage) {
			
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
			float time = System.currentTimeMillis();
			checkPlayerCollisions(gameStage.getPlayer());
			paint(gameStage.getPlayer());
			paintBullet(gameStage, GameLoop.shootingDir);
			updateEnemies();
			updateBoss();

			if (gameStage.getBoss() != null && gameStage.getBoss().isAlive()) {
				gameStage.getBoss().update();
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
	public static void setRunning(boolean running) { running = running; }
	public static boolean getRunning() { return running; }
}