package se233.notcontra.model;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SpriteAnimation;
import se233.notcontra.model.Boss.Boss;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.PlayerState;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.model.Items.HellfireMagazine;
import se233.notcontra.view.Platform;
import se233.notcontra.view.GameStages.GameStage;

public class Player extends Pane {
	
	private KeyCode leftKey;
	private KeyCode rightKey;
	private KeyCode upKey;
	private KeyCode downKey;
	private KeyCode shootKey;
	private KeyCode jumpKey;
	
	private int xPosition;
	private int yPosition;
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
	private boolean isHellfireMag = false;
	private boolean isTankBuster = false;
	private boolean isDying = false;
	
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
	
	private PlayerState playerState;
	
	public Player(int xPosition, int yPosition, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode downKey) {
		this.playerState = PlayerState.IDLE;
		this.bulletPerClip = 3;
		this.dropDownTimer = 0;
		this.buffTimer = 0;
		this.reloadTimer = 0;
		respawnTimer = 0;
		this.startX = xPosition;
		this.startY = yPosition;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.upKey = upKey;
		this.downKey = downKey;
		this.shootKey = KeyCode.L;
		this.jumpKey = KeyCode.K;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		Player.height = 64;
		Player.width = 64;
		this.hitBox = new Rectangle(width, height);
		this.setTranslateX(xPosition);
		this.setTranslateY(yPosition);
		Image image = new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk.png"));
		this.sprite = new SpriteAnimation(image, 2, 2, 1, 0, 0, 64, 64);
		this.sprite.setFitWidth(80);
		this.sprite.setFitHeight(80);
		this.sprite.setTranslateY(-16);
		this.getChildren().add(sprite);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	//					Starting of Movement Behaviors
	public void moveLeft() {
		if (isTankBuster) {
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
			yVelocity = yMaxVelocity;
			canJump = false;
			isJumping = true;
			isFalling = false;
		}
	}
	
	public void respawn() {
		enableKeys();
		this.yPosition = this.startY;
		this.xPosition = this.startX;
		this.isMoveLeft = false;
		this.isMoveRight = false;
		this.isFalling = true;
		this.canJump = false;
		this.isJumping = false;
		isDying = false;
	}
	
	
	public void die() {
		respawnTimer = 100;
		lives--;
		isDying = true;
		if (lives <= 0) {
			Launcher.exitToMenu();
		}
	}
	
	public void setProning(boolean isProning) {
		this.isProning = isProning;
	}

	public void prone() {
		isProning = true;
		stop();
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
			System.out.println("Drop down platform");
		}
	}
	
	public void shoot(GameStage gameStage, ShootingDirection direction) {
		if (reloadTimer > 0) {
			reloadTimer--;
			return;
		}

		int xBulletPos = switch (direction) {
			case UP -> xPosition + (width/2);
			case RIGHT, UP_RIGHT, DOWN_RIGHT -> xPosition + width;
			case LEFT, UP_LEFT, DOWN_LEFT -> xPosition;
		};
		
		int yBulletPos = switch(direction) {
			case RIGHT, LEFT -> yPosition;
			case UP, UP_RIGHT, UP_LEFT -> yPosition - height;
			case DOWN_RIGHT, DOWN_LEFT -> yPosition + height;
		};
		
    	long now = System.currentTimeMillis();
    	if (now - lastShotTime >= fireDelay) {
    		lastShotTime = now;
    		if (bulletPerClip > 0) {
    			reloadTimer = 0;
    			bulletPerClip--;
    		} else {
    			reloadTimer = 2;
    			bulletPerClip = 3;
    		}

            Bullet bullet = isProning ? new Bullet(xBulletPos, (yPosition + height/2), 10, 10, direction , BulletOwner.PLAYER)
            		: new Bullet(xBulletPos, yBulletPos, 10, 10, direction , BulletOwner.PLAYER);
            if (isBuffed && isHellfireMag) {
            	bullet.setBulletSprite(new Image(Launcher.class.getResourceAsStream("assets/Item/Entities/bullet.png")));
            }
            GameLoop.bullets.add(bullet);

            javafx.application.Platform.runLater(() -> {
                gameStage.getChildren().add(bullet);
            });
    	}
	}
	
	public void stop() {
		if (isTankBuster) {
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
			if (respawnTimer == 0) respawn();
			return;
		}
		hitBox.setX(xPosition);
		hitBox.setY(yPosition);
		isDying = false;
		enableKeys();
		moveX();
	}
	
	public void moveX() {
		setTranslateX(xPosition);
		if (isMoveRight) {
			xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
			xPosition += xVelocity;
		} 
		if (isMoveLeft)	{
			xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
			xPosition -= xVelocity;
		}
	}
	
	public void moveY() {
		setTranslateY(yPosition);
		if (isFalling) {
			yVelocity = yVelocity >= yMaxVelocity ? yMaxVelocity : yVelocity + yAcceleration;
			yPosition += yVelocity;
		} else if (isJumping) {
			yVelocity = yVelocity <= 0 ? 0 : yVelocity - yAcceleration;
			yPosition -= yVelocity;
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
		if ((xPosition + width) >= GameStage.WIDTH) {
			xPosition = GameStage.WIDTH - width;
		}
		if (xPosition <= 0) {
			xPosition = 0;
		}
	}
	
	public void checkItemCollision(GameStage gameStage) {
		if (buffTimer > 0) {
			buffTimer--;
			return;
		} else if (isBuffed && this.isHellfireMag) {
			isBuffed = false;
			isHellfireMag = false;
			fireDelay = 30;
			bulletPerClip = 3;
		} else if (isBuffed && this.isTankBuster) {
			isTankBuster = false;
			xMaxVelocity = 5;
		}
		if (gameStage.getItem() != null) {
			if (gameStage.getItem().getBoundsInParent().intersects(this.getBoundsInParent())) {
				boolean isHellfireMag = gameStage.getItem() instanceof HellfireMagazine;
				isBuffed = true;
				if (isHellfireMag) {
					this.isHellfireMag = true;
					this.isTankBuster = false;
					buffTimer = 200;
					bulletPerClip = Integer.MAX_VALUE;
				} else {
					this.isTankBuster = true;
					this.isHellfireMag = false;
					bulletPerClip = 3;
					buffTimer = 100;
					xMaxVelocity = 13;
					stop();
				}

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
				boolean isCollidedXAxis = (xPosition + width) > platform.getXPosition() && xPosition < platform.getXPosition() + platform.getPaneWidth();
				boolean isLanding = isFalling && (yPosition + height) <= platform.getYPosition() && (yPosition + height + yVelocity) >= platform.getYPosition();
				boolean isStanding = Math.abs((yPosition + height) - platform.getYPosition()) < 1;				
				
				if (isCollidedXAxis && (isLanding || (isOnPlatform && isStanding))) {
					
					if (isLanding) {
						yPosition = platform.getYPosition() - height;
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
	
	public void isCollided(Boss boss) {
		int bossX = boss.getXPos();
		int bossY = boss.getYPos();
		
		boolean xAxisCollision = this.xPosition + this.width >= bossX;
		boolean yAxisCollision = this.yPosition + this.height >= bossY;
		
		if (xAxisCollision && yAxisCollision && isTankBuster && respawnTimer <= 0) {
			die();
			return;
		} else if (xAxisCollision) {
			this.xPosition = boss.getXPos() - width;
		}
		//////////////////// 			PLAYER DIES WHEN CHARGING TO THE BOSS WITH TANK BUSTER AND THE BOSS TAKE DAMAGE

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

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
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
	
}