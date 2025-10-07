package se233.notcontra.controller;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Player;
import se233.notcontra.model.ShootingDirection;
import se233.notcontra.model.Wallboss;
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
	
	public void checkAllCollisions(Player player , Wallboss wallboss) {
		player.checkGroundCollision();
		player.checkHighestJump();
		player.checkStageBoundaryCollision();

		if (wallboss != null && wallboss.isAlive()) {
			checkBulletBossCollisions(GameLoop.bullets, wallboss);
			// TODO: Add player vs. boss collision logic here
		}
	}


	private void checkBulletBossCollisions(ArrayList<Bullet> bullets, Wallboss wallboss) {
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
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

	public void paint(Player player) {
		player.repaint();
	}
	
	public void paintBullet(ArrayList<Bullet> bullets, ShootingDirection direction) {
		bullets.forEach(bullet -> {
			bullet.move();
			if ((bullet.getXPosition() >= GameStage.WIDTH || bullet.getXPosition() <= 0) /* or collided with enemy/boss*/) {
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
			checkAllCollisions(gameStage.getPlayer() , gameStage.getWallboss());
			paint(gameStage.getPlayer());
			paintBullet(GameLoop.bullets, GameLoop.shootingDir);

			if (gameStage.getWallboss() != null && gameStage.getWallboss().isAlive()) {
				gameStage.getWallboss().update();
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