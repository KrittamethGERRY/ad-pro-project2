package se233.notcontra.controller;

import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Player;
import se233.notcontra.model.Enums.PlayerState;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.view.PauseMenu;
import se233.notcontra.view.GameStages.GameStage;

public class GameLoop implements Runnable{
	public static ShootingDirection shootingDir;

	private GameStage gameStage;
	private static PauseMenu pauseMenu;
	private int frameRate;
	private float interval;
	private boolean running;
	private static int score;
	
	public static boolean isPaused = false;

	public static List<Bullet> bullets = new ArrayList<>();
	public static ArrayList<Enemy> enemies = new ArrayList<>();

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
			player.setState(PlayerState.PRONE);
		} else {
			player.setProning(false);
			player.stop();
			player.setState(PlayerState.IDLE);
		}

		// Facing Direction
		if (upPressed && !rightPressed && !leftPressed) {
			shootingDir = ShootingDirection.UP;
			
			player.setState(PlayerState.FACE_UP);
		} else if (upPressed && rightPressed) {
			shootingDir = ShootingDirection.UP_RIGHT;
			
			player.setState(PlayerState.FACE_UP_SIDE);
		} else if (downPressed && rightPressed) {
			shootingDir = ShootingDirection.DOWN_RIGHT;
			
			player.setState(PlayerState.FACE_DOWN_SIDE);
		} else if (upPressed && leftPressed) {
			shootingDir = ShootingDirection.UP_LEFT;
			
			player.setState(PlayerState.FACE_UP_SIDE);
		} else if (downPressed && leftPressed) {
			shootingDir = ShootingDirection.DOWN_LEFT;
			
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
		}
		
		if (jumpPressed && downPressed) {
			player.dropDown();
		} else if (jumpPressed) {
			player.jump();
		} 

		gameStage.getKeys().clear();
	}
	
	public void updateAnimation(Player player) {
		PlayerState currentState = player.getState();
		
		if (currentState == PlayerState.CHARGING) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_charging.png")), 3, 3, 1);
			return;
		}
		
		if (player.isJumping()) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_jump.png")), 3, 3, 1);

			return;
		}
		
		if (currentState == PlayerState.SHOOTING) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_straight.png")), 3, 3, 1);
		} else if (currentState == PlayerState.PRONE) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_prone.png")), 1, 1, 1);
		} else if (currentState == PlayerState.DIE) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_die.png")), 5, 2, 3);
		} else if (currentState == PlayerState.JUMP) {
		} else if (currentState == PlayerState.FACE_DOWN_SIDE) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_down_side.png")), 3, 3, 1);
		} else if (currentState == PlayerState.FACE_UP_SIDE) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_up_side.png")), 3, 3, 1);
		} else if (currentState == PlayerState.FACE_UP) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_up.png")), 1, 3, 1);
		} else if (currentState == PlayerState.WALKSHOOT) {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_straight.png")), 2, 2, 1);
		} else {
			player.getImageView().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Player/player_idle.png")), 3, 3, 1);
		} 
		player.getImageView().tick();
		System.out.println(currentState);
	}
	
	public static void addScore(int addition) {
		score += addition;
	}
	public static int getScore() { return score; }
	
	public void stop() {
		running = false;
	}
	
	public static void pause() {
		isPaused = !isPaused;
		pauseMenu.setVisible(isPaused);
	}


	@Override
	public void run() {
		while (running) {
			float time = System.currentTimeMillis();
			update(gameStage.getPlayer());
			updateAnimation(gameStage.getPlayer());
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