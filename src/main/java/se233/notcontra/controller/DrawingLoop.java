package se233.notcontra.controller;

import java.util.ArrayList;

import javafx.application.Platform;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Player;
import se233.notcontra.model.ShootingDirection;
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
	
	public void checkAllCollisions(Player player) {
		player.checkGroundCollision();
		player.checkHighestJump();
		player.checkStageBoundaryCollision();
	}
	
	public void paint(Player player) {
		player.repaint();
	}
	
	public void paintBullet(ArrayList<Bullet> bullets, ShootingDirection direction) {
		bullets.forEach(bullet -> {
			bullet.move();
			if (bullet.getXPosition() >= GameStage.WIDTH || bullet.getXPosition() <= 0) {
				javafx.application.Platform.runLater(() -> {
					gameStage.getChildren().remove(bullet);
					bullets.remove(bullet);
				});
			}
		});
	}
	
	@Override
	public void run() {
		while (running) {
			float time = System.currentTimeMillis();
			checkAllCollisions(gameStage.getPlayer());
			paint(gameStage.getPlayer());
			paintBullet(GameLoop.bullets, GameLoop.shootingDir);
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