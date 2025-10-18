package se233.notcontra.model;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.controller.SpriteAnimation;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.PlayerState;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.model.Items.SpecialMagazine;
import se233.notcontra.view.Platform;
import se233.notcontra.view.GameStages.GameStage;

public class Player extends Pane {
	private static final Logger logger = LogManager.getLogger(Player.class);
	
	private KeyCode leftKey;
	private KeyCode rightKey;
	private KeyCode upKey;
	private KeyCode downKey;
	private KeyCode shootKey;
	private KeyCode jumpKey;
	private KeyCode CheatKey;
	
	private int xPos;
	private int yPos;
	private int startX;
	private int startY;
	private double xVelocity = 0;
	private double xAcceleration = 1;
	private double yVelocity = 0;
	private double yAcceleration = 0.40d;
	private double xMaxVelocity = 5;
	private double yMaxVelocity = 10;
	
	private Rectangle hitBox;
	
	private int lives = 3;
	
	// Movement booleans
	private boolean isMoveRight = true;
	private boolean isMoveLeft = false;
	private boolean isJumping = false;
	private boolean isFalling = true;
	private boolean canJump = false;
	private boolean isProning = false;
	private boolean isOnPlatform = false;
	private boolean isDropping = false;
	private boolean canDropDown = false;
	private boolean isBuffed = false;
	private boolean isSpecialMag = false;
	private boolean isTankBuster = false;
	private boolean isDying = false;
	private boolean CheatActive = true;
	
	public static int height;
	public static int width;
	private SpriteAnimation sprite;
	
	private long lastShotTime = 0;
	private int fireDelay = 30;
	private int bulletPerClip = 3;
	private int dropDownTimer = 0;
	private int buffTimer = 0;
	private int reloadTimer = 0;
	public static int respawnTimer = 0;
	public static int spawnProtectionTimer = 200;
	
	private PlayerState playerState;
	
	public Player(int xPos, int yPos, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode downKey) {
		this.playerState = PlayerState.IDLE;
		this.bulletPerClip = 3;
		this.dropDownTimer = 0;
		this.buffTimer = 0;
		this.reloadTimer = 0;
		respawnTimer = 0;
		this.startX = xPos;
		this.startY = yPos;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.upKey = upKey;
		this.downKey = downKey;
		this.shootKey = KeyCode.L;
		this.jumpKey = KeyCode.K;
		this.CheatKey = KeyCode.F1;
		this.xPos = xPos;
		this.yPos = yPos;
		Player.height = 64;
		Player.width = 64;
		this.hitBox = new Rectangle(width - 25, height);
		this.setTranslateX(xPos);
		this.setTranslateY(yPos);
		Image image = new Image(Launcher.class.getResourceAsStream("assets/Player/player_idle.png"));
		this.sprite = new SpriteAnimation(image, 2, 2, 1, 0, 0, 64, 64);
		this.sprite.setFitWidth(80);
		this.sprite.setFitHeight(80);
		this.sprite.setTranslateY(-16);
		double offset = (Player.width - 80) / 2.0; 
		this.sprite.setTranslateX(offset);
		this.sprite.setTranslateY(offset);
		this.hitBox.setFill(Color.TRANSPARENT);
		this.getChildren().addAll(sprite, hitBox);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	//					Starting of Movement Behaviors
	public void moveLeft() {
		if (isTankBuster) {
			isMoveLeft = false;
			isMoveRight = true;
			return;
		}
		isMoveRight = false;
		isMoveLeft = true;
		this.setScaleX(-1);
		
	}
	
	public void moveRight() {
		isMoveRight = true;
		isMoveLeft = false;
		this.setScaleX(1);
	}
	
	public void jump() {
		if (canJump) {
			SoundController.getInstance().playJumpSound();
			yVelocity = yMaxVelocity;
			canJump = false;
			isJumping = true;
			isFalling = false;
		}
	}
	
	public void respawn() {
		enableKeys();
		this.yPos = this.startY;
		this.xPos = this.startX;
		this.isMoveLeft = false;
		this.isMoveRight = false;
		this.isFalling = true;
		this.canJump = false;
		this.isJumping = false;
		this.isDying = false;
		this.setState(PlayerState.IDLE);
	}
	
	
	public void die() {
		respawnTimer = 100;
		spawnProtectionTimer = 200;
		lives--;
		isDying = true;
		this.setState(PlayerState.DIE);
		SoundController.getInstance().playPlayerDieSound();
		if (lives <= 0) {
			javafx.application.Platform.runLater(() -> {
				GameLoop.pause();
				ButtonType retry = new ButtonType("Retry", ButtonBar.ButtonData.OK_DONE);
				ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
				Alert alert = new Alert(AlertType.CONFIRMATION, "Game Over!");
				alert.setTitle("Game Over!");
				alert.setHeaderText("Retry?");
				alert.getButtonTypes().setAll(retry, quit);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == retry) {
					Launcher.changeStage(Launcher.currentStageIndex);
				} else {
					Launcher.exitToMenu();
				}
			});
		}
	}
	
	public void setProning(boolean isProning) {
		this.isProning = isProning;
	}

	public void prone() {
		isProning = true;
		hitBox.setHeight(height/2);
		hitBox.setY(height/2);
		stop();
	}
	
	public void resetHitBoxHeight() {
		if (isProning == false) {
			hitBox.setHeight(height);
			hitBox.setY(0);
		}
	}
	
	public void dropDown() {
		if (isOnPlatform && canDropDown) {
			isDropping = true;
			yVelocity = 4;
			dropDownTimer = 12;
			isOnPlatform = false;
			canJump = false;
			isFalling = true;
			isJumping = false;
			canDropDown = false;
			SoundController.getInstance().playProneSound();
		}
	}
	
	public void shoot(GameStage gameStage, ShootingDirection direction) {
		if (reloadTimer > 0) {
			return;
		}

		int xBulletPos = switch (direction) {
			case UP -> xPos + (width/2);
			case RIGHT, UP_RIGHT, DOWN_RIGHT -> xPos + width;
			case LEFT, UP_LEFT, DOWN_LEFT -> xPos;
		};
		
		int yBulletPos = switch(direction) {
			case RIGHT, LEFT -> yPos;
			case UP, UP_RIGHT, UP_LEFT -> yPos - height;
			case DOWN_RIGHT, DOWN_LEFT -> yPos + height;
		};
		
    	long now = System.currentTimeMillis();
    	if (now - lastShotTime >= fireDelay) {
    		lastShotTime = now;
    		if (bulletPerClip > 0) {
    			reloadTimer = 0;
    			bulletPerClip--;
    		} else {
    			reloadTimer = 30;
    			bulletPerClip = 3;
    		}

            Bullet bullet = isProning ? new Bullet(xBulletPos, (yPos + height/2), 10, 10, direction , BulletOwner.PLAYER)
            		: new Bullet(xBulletPos, yBulletPos, 10, 10, direction , BulletOwner.PLAYER);
            if (isBuffed && isSpecialMag) {
            	bullet.setBulletSprite(new Image(Launcher.class.getResourceAsStream("assets/Item/Entities/Bullet_Sp.png")));
            }
            SoundController.getInstance().playShootSound();
            GameLoop.bullets.add(bullet);

            javafx.application.Platform.runLater(() -> {
                gameStage.getChildren().add(bullet);
            });
    	}
	}
	
	public void updateTimer() {
		if (reloadTimer > 0) {
			reloadTimer--;
		}
		if (spawnProtectionTimer > 0) {
			spawnProtectionTimer--;
		}
	}
	
	public void stop() {
		if (isTankBuster) {
			isMoveLeft = false;
			isMoveRight = true;
			return;
		}
		
		isMoveLeft = false;
		isMoveRight = false;
		xVelocity = 0;
	}
	
	public void repaint() {
		moveY();
		if (respawnTimer > 0) {
			respawnTimer--;
			disableKeys();
			if (respawnTimer == 0) {
				respawn();
			}
			return;
		}
		isDying = false;
		enableKeys();
		moveX();
	}
	
	public void moveX() {
		setTranslateX(xPos);
		if (isMoveRight) {
			xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
			xPos += xVelocity;
		} 
		if (isMoveLeft)	{
			xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
			xPos -= xVelocity;
		}
	}
	
	public void moveY() {
		setTranslateY(yPos);
		if (isFalling) {
			yVelocity = yVelocity >= yMaxVelocity ? yMaxVelocity : yVelocity + yAcceleration;
			yPos += yVelocity;
		} else if (isJumping) {
			yVelocity = yVelocity <= 0 ? 0 : yVelocity - yAcceleration;
			yPos -= yVelocity;
		}
	}
	
	public void checkHighestJump() {
		if (isJumping && yVelocity <= 0) {
			isJumping = false;
			isFalling = true;
			yVelocity = 0;
		}
	}
	
	public void checkStageBoundaryCollision() {
		if ((xPos + width) >= GameStage.WIDTH) {
			xPos = GameStage.WIDTH - width;
		}
		if (xPos <= 0) {
			xPos = 0;
		}
	}
	
	public void checkItemCollision(GameStage gameStage) {
		if (buffTimer > 0) {
			buffTimer--;
			return;
		} else if (isBuffed && this.isSpecialMag) {
			isBuffed = false;
			isSpecialMag = false;
			fireDelay = 30;
			bulletPerClip = 3;
		} else if (isBuffed && this.isTankBuster) {
			isTankBuster = false;
			xMaxVelocity = 5;
		}
		if (gameStage.getItem() != null) {
			if (gameStage.getItem().getBoundsInParent().intersects(this.getBoundsInParent())) {
				boolean isSpecialMag = gameStage.getItem() instanceof SpecialMagazine;
				isBuffed = true;
				if (isSpecialMag) {
					this.isSpecialMag = true;
					this.isTankBuster = false;
					buffTimer = 200;
					fireDelay = 0;
					bulletPerClip = Integer.MAX_VALUE;
				} else {
					SoundController.getInstance().playScreamingSound();
					this.isTankBuster = true;
					this.isSpecialMag = false;
					bulletPerClip = 3;
					buffTimer = 100;
					xMaxVelocity = 13;
					this.setState(PlayerState.CHARGING);
					stop();
				}

				SoundController.getInstance().playPickItemSound();
				javafx.application.Platform.runLater(() -> {
					gameStage.removeItem();
				});
			}
		}

	}
	public void checkPlatformCollision(List<Platform> platforms) {
		if (dropDownTimer > 0) {
			dropDownTimer--;
			return;
		}
		
		boolean onAPlatformThisFrame = false;
		
			for (Platform platform : platforms) {
				boolean isCollidedXAxis = (xPos + width) > platform.getxPos() && xPos < platform.getxPos() + platform.getPaneWidth();
				boolean isLanding = isFalling && (yPos + height) <= platform.getyPos() && (yPos + height + yVelocity) >= platform.getyPos();
				boolean isStanding = Math.abs((yPos + height) - platform.getyPos()) < 1;				
				
				if (isCollidedXAxis && (isLanding || (isOnPlatform && isStanding))) {
					
					if (isLanding) {
						yPos = platform.getyPos() - height;
						yVelocity = 0;
						isFalling = false;
					}
					
					canJump = true;
					isOnPlatform = true;
					if (platform.getIsGround()) {
						canDropDown = false;
					} else {
						canDropDown = true;
					}
					onAPlatformThisFrame = true;
					
					break;
			}
		}
		
		if (!isFalling && !isJumping && !onAPlatformThisFrame) {
			isFalling = true;
			isOnPlatform = false;
			canJump = false;
		}
	}
	
	public void isCollided(GameStage gameStage) {
		int bossX = gameStage.getBoss().getXPos();
		int bossY = gameStage.getBoss().getYPos();
		
		boolean xAxisCollision = this.xPos + this.width >= bossX;
		boolean yAxisCollision = this.yPos + this.height >= bossY;
		
		if (xAxisCollision && yAxisCollision && isTankBuster && respawnTimer <= 0) {
			die();
			Effect explosion = new Effect(ImageAssets.EXPLOSION_IMG, 8, 8, 1, bossX, 300, 128, 128);
			DrawingLoop.effects.add(explosion);
			javafx.application.Platform.runLater(() -> gameStage.getChildren().add(explosion));
			for (Enemy enemy: GameLoop.enemies) {
				enemy.takeDamage(5000, null);
			}
			SoundController.getInstance().playExplosionSound();
			if (lives > 0) gameStage.getBoss().getWeakPoints().clear();
			return;
		} else if (xAxisCollision) {
			this.xPos = bossX - width;
		}

	}
	
	public void enableKeys() {
		this.upKey = KeyCode.W;
		this.downKey = KeyCode.S;
		this.leftKey = KeyCode.A;
		this.rightKey = KeyCode.D;
		this.jumpKey = KeyCode.K;
		this.shootKey = KeyCode.L;
	}
	
	public void disableKeys() {
		this.upKey = null;
		this.downKey = null;
		this.leftKey = null;
		this.rightKey = null;
		this.jumpKey = null;
		this.shootKey = null;
	}
	
	
	// 				End of Movement Behaviors
	
	// GETTERS SETTERS
	public int getLives() {
		return this.lives;
	}
	
	public KeyCode getLeftKey() {
		return this.leftKey;
	}

	public KeyCode getRightKey() {
		return rightKey;
	}

	public KeyCode getUpKey() {
		return upKey;
	}

	public KeyCode getDownKey() {
		return downKey;
	}
	
	public KeyCode getShootKey() {
		return shootKey;
	}

	public KeyCode getJumpKey() {
		return jumpKey;
	}

	public KeyCode getCheatKey(){
		return CheatKey;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}
	
	public boolean isProning() {
		return isProning;
	}

	public boolean getTankBuster() { return isTankBuster; }
	public boolean isDying() { return isDying; }

	public SpriteAnimation getImageView() { return this.sprite;}
	
	public Rectangle getHitBox() { return this.hitBox; }
	
	public PlayerState getState() { return this.playerState; }
	public void setState(PlayerState playerState) {
		this.playerState = playerState;
	}

	public boolean isJumping() { return this.isJumping; }

	public boolean CheatOnandOff(KeyCode cheatKey) {
		return CheatActive = !CheatActive;}

	public void logPos() {
		logger.info("Player - X: {}, Y: {}", xPos, yPos);
	}
	
}