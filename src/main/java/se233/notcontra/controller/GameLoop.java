package se233.notcontra.controller;

import java.util.ArrayList;
import java.util.Iterator;

import se233.notcontra.model.Bullet;
import se233.notcontra.model.Player;
import se233.notcontra.model.ShootingDirection;
import se233.notcontra.view.GameStage;

public class GameLoop implements Runnable{

	private GameStage gameStage;
	private int frameRate;
	private float interval;
	private boolean running;
	private ShootingDirection shootingDir;

	public static ArrayList<Bullet> bullets = new ArrayList<>();

	public GameLoop(GameStage gameStage) {
		this.gameStage = gameStage;
		this.frameRate = 10;
		this.interval = 1000 / frameRate;
		this.shootingDir = ShootingDirection.RIGHT;
		this.running = true;
	}

	private void update(Player player) {
		boolean leftPressed = gameStage.getKeys().isPressed(player.getLeftKey());
		boolean rightPressed = gameStage.getKeys().isPressed(player.getRightKey());
		boolean upPressed = gameStage.getKeys().isPressed(player.getUpKey());
		boolean downPressed = gameStage.getKeys().isPressed(player.getDownKey());
		boolean shootPressed = gameStage.getKeys().isPressed(player.getShootKey());
		boolean jumpPressed = gameStage.getKeys().isPressed(player.getJumpKey());

		if (leftPressed && rightPressed) {
			player.stop();
			player.setProning(false);
		} else if (leftPressed) {
			player.moveLeft();
			shootingDir = ShootingDirection.LEFT;
			player.setProning(false);
		} else if (rightPressed) {
			player.moveRight();
			shootingDir = ShootingDirection.RIGHT;
			player.setProning(false);
		} else if (downPressed) {
			player.prone();
		} else {
			player.setProning(false);
			player.stop();
		}

		
		if (upPressed && !rightPressed && !leftPressed) {
			shootingDir = ShootingDirection.UP;
		} else if (upPressed && rightPressed) {
			shootingDir = ShootingDirection.UP_RIGHT;
		} else if (downPressed && rightPressed) {
			shootingDir = ShootingDirection.DOWN_RIGHT;
		} else if (upPressed && leftPressed) {
			shootingDir = ShootingDirection.UP_LEFT;
		} else if (downPressed && leftPressed) {
			shootingDir = ShootingDirection.DOWN_LEFT;
		}
		
		
		if (jumpPressed && !downPressed) {
			player.jump();
		}

		if (shootPressed) {
			player.shoot(gameStage, shootingDir);
		}
	}

	@Override
	public void run() {
		while (running) {
			float time = System.currentTimeMillis();
			update(gameStage.getPlayer());
			time = System.currentTimeMillis() - time;
			if (time < interval) {
				try {
					Thread.sleep((long) (interval-time));
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