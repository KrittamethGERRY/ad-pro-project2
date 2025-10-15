package se233.notcontra.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Player;
import se233.notcontra.model.ShootingDirection;
import se233.notcontra.view.GameStage;

public class GameLoop implements Runnable{
	public static ShootingDirection shootingDir;
	private static int score;
	
	private GameStage gameStage;
	private int frameRate;
	private float interval;
	private boolean running;
	

	public static List<Bullet> bullets = new ArrayList<>();
	public static ArrayList<Enemy> enemies = new ArrayList<>();

	public GameLoop(GameStage gameStage) {
		this.score = 0;
		this.gameStage = gameStage;
		this.frameRate = 10;
		this.interval = 1000 / frameRate;
		GameLoop.shootingDir = ShootingDirection.RIGHT;
		this.running = true;
	}

	private void update(Player player) {
		boolean leftPressed = gameStage.getKeys().isPressed(player.getLeftKey());
		boolean rightPressed = gameStage.getKeys().isPressed(player.getRightKey());
		boolean upPressed = gameStage.getKeys().isPressed(player.getUpKey());
		boolean downPressed = gameStage.getKeys().isPressed(player.getDownKey());
		boolean shootPressed = gameStage.getKeys().isPressed(player.getShootKey());
		boolean jumpPressed = gameStage.getKeys().isPressed(player.getJumpKey());
		boolean pausePressed = gameStage.getKeys().isPressed(KeyCode.ESCAPE);

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
		} else {
			// Set default direction while not pressing any key
			shootingDir = shootingDir.toString().matches(".*RIGHT") ? ShootingDirection.RIGHT : ShootingDirection.LEFT;
		}
		if (gameStage.getKeys().isJustPressed(player.getShootKey())) {
			player.shoot(gameStage, shootingDir);
		}
		
		if (jumpPressed && downPressed) {
			player.dropDown();
		} else if (jumpPressed) {
			player.jump();
		}

		gameStage.getKeys().clear();
	}
	
	public static void addScore(int addition) {
		score += addition;
	}
	
	public static int getScore() { return score; }
	
	public void stop() {
		running = false;
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