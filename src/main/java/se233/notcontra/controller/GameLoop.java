package se233.notcontra.controller;

import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.image.Image;
import se233.notcontra.Launcher;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.model.ImageAssets;
import se233.notcontra.model.Player;
import se233.notcontra.model.Enums.PlayerState;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.view.PauseMenu;
import se233.notcontra.view.GameStages.GameStage;

public class GameLoop implements Runnable{
	public static ShootingDirection shootingDir;
	public static final Logger logger = LogManager.getLogger(GameLoop.class);

	private GameStage gameStage;
	private static PauseMenu pauseMenu;
	private int frameRate;
	private float interval;
	private boolean running;
	private static int score;
	
	public static boolean isPaused = false;

	public static List<Bullet> bullets = new ArrayList<>();
	public static List<Enemy> enemies = new ArrayList<>();

	public GameLoop(GameStage gameStage) {
		score = 0;
		pauseMenu = new PauseMenu();
		pauseMenu.setVisible(false);
		gameStage.getChildren().add(pauseMenu);
		
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
		boolean jumpPressed = gameStage.getKeys().isPressed(player.getJumpKey());
		
		
		if (isPaused || player.getState() == PlayerState.CHARGING || player.getState() == PlayerState.DIE) {
			return;
		}

		if (leftPressed && rightPressed) {
			player.stop();
			player.setProning(false);
			player.setState(PlayerState.IDLE);
		} else if (leftPressed) {
			player.moveLeft();
			shootingDir = ShootingDirection.LEFT;
			player.setProning(false);
			player.setState(PlayerState.WALKSHOOT);
		} else if (rightPressed) {
			player.moveRight();
			shootingDir = ShootingDirection.RIGHT;
			player.setProning(false);
			player.setState(PlayerState.WALKSHOOT);
		} else if (downPressed) {
			player.prone();
			tracePlayerAction("Prone");
			player.setState(PlayerState.PRONE);
		} else {
			player.setProning(false);
			player.stop();
			player.setState(PlayerState.IDLE);
		}

		// Facing Direction
		if (upPressed && !rightPressed && !leftPressed) {
			shootingDir = ShootingDirection.UP;
			tracePlayerAction("Facing up");
			player.setState(PlayerState.FACE_UP);
		} else if (upPressed && rightPressed) {
			shootingDir = ShootingDirection.UP_RIGHT;
			tracePlayerAction("Facing up right");
			player.setState(PlayerState.FACE_UP_SIDE);
		} else if (downPressed && rightPressed) {
			shootingDir = ShootingDirection.DOWN_RIGHT;
			tracePlayerAction("Facing down right");			
			player.setState(PlayerState.FACE_DOWN_SIDE);
		} else if (upPressed && leftPressed) {
			shootingDir = ShootingDirection.UP_LEFT;
			tracePlayerAction("Facing up left");
			player.setState(PlayerState.FACE_UP_SIDE);
		} else if (downPressed && leftPressed) {
			shootingDir = ShootingDirection.DOWN_LEFT;
			tracePlayerAction("Facing down left");			
			player.setState(PlayerState.FACE_DOWN_SIDE);
		} else {
			// Set default direction while not pressing any key
			shootingDir = shootingDir.toString().matches(".*RIGHT") ? ShootingDirection.RIGHT : ShootingDirection.LEFT;
			PlayerState currentState = player.getState();
		    if (currentState != PlayerState.PRONE && currentState != PlayerState.WALKSHOOT) {
		        player.setState(PlayerState.IDLE);
		    }
		}
		
		if (gameStage.getKeys().isJustPressed(player.getShootKey())) {
			player.shoot(gameStage, shootingDir);
			if (player.isProning()) {
				player.setState(PlayerState.PRONE);
			}

			tracePlayerAction("Shoot");
		}
		
		if (jumpPressed && downPressed) {
			player.dropDown();
			tracePlayerAction("Drop from platform");
		} else if (jumpPressed) {
			player.jump();
			tracePlayerAction("Jump");
		} 

		gameStage.getKeys().clear();
	}
	
	public void updateAnimation(Player player) {
		PlayerState currentState = player.getState();
		
		if (currentState == PlayerState.CHARGING) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_CHARGING_IMG, 3, 3, 1);
			player.getImageView().tick();
			return;
		}
		
		if (player.isJumping()) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_JUMP_IMG, 3, 3, 1);
			player.getImageView().tick();
			return;
		}
		
		if (currentState == PlayerState.SHOOTING) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_WALK_SHOOT_IMG, 3, 3, 1);
		} else if (currentState == PlayerState.PRONE) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_PRONE_IMG, 1, 1, 1);
		} else if (currentState == PlayerState.DIE) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_DIE_IMG, 5, 2, 3);
		} else if (currentState == PlayerState.JUMP) {
		} else if (currentState == PlayerState.FACE_DOWN_SIDE) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_SHOOT_DOWN_SIDE_IMG, 3, 3, 1);
		} else if (currentState == PlayerState.FACE_UP_SIDE) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_SHOOT_UP_SIDE_IMG, 3, 3, 1);
		} else if (currentState == PlayerState.FACE_UP) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_SHOOT_UP, 1, 3, 1);
		} else if (currentState == PlayerState.WALKSHOOT) {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_WALK_SHOOT_IMG, 2, 2, 1);
		} else {
			player.getImageView().changeSpriteSheet(ImageAssets.PLAYER_IDLE_IMG, 3, 3, 1);
		} 
		player.getImageView().tick();
	}

	public void updateEnemyAnimation(Enemy enemy){
		if (enemy.getType() == EnemyType.TURRET) {
			if (!enemy.isAlive()) {
				enemy.getSprite().changeSpriteSheet(getimage("assets/Boss/Boss1/Turret_dead.png"), 2, 2, 1);
			} else if (enemy.isAlive()){
				enemy.updateShootingAnimation();
				if(enemy.getShootingAnimationTimer() > 0) {
					enemy.getSprite().changeSpriteSheet(getimage("assets/Boss/Boss1/Turret_fire.png"), 1, 1, 1);
				} else {
					enemy.getSprite().changeSpriteSheet(getimage("assets/Boss/Boss1/Turret_IDEL.png"), 1, 1, 1);
				}
			}
		}

		if (enemy.getType() == EnemyType.WALL){
			if(!enemy.isAlive()){
				enemy.getSprite().changeSpriteSheet(getimage("assets/Boss/Boss1/Core.png"), 1, 3, 1);
			}
		}
		enemy.getSprite().tick();
	}

	public Image getimage(String path){
		return new Image(Launcher.class.getResourceAsStream(path));
	}

	public static void addScore(int addition) {
		score += addition;
		traceScore(addition);
	}
	public static int getScore() { return score; }
	
	public void stop() {
		running = false;
	}
	
	public static void pause() {
		isPaused = !isPaused;
		pauseMenu.setVisible(isPaused);
	}
	
	public static void traceScore(int addition) {
		logger.info("Score: {}+. Current score = {}", addition, score);
	}
	
	public static void tracePlayerAction(String action) {
		logger.debug("Player {}", action);
	}


	@Override
	public void run() {
		while (running) {
			float startTime = System.currentTimeMillis();
			update(gameStage.getPlayer());
			updateAnimation(gameStage.getPlayer());
			for (Enemy e : new ArrayList<>(gameStage.getEnemies())) {
			    updateEnemyAnimation(e);
			}
			float elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime < interval) {
				try {
					Thread.sleep((long) (interval- elapsedTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep((long) (interval - (interval % elapsedTime)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}