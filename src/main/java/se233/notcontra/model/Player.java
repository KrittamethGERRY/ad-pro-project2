package se233.notcontra.model;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Items.HellfireMagazine;
import se233.notcontra.view.GameStage;
import se233.notcontra.view.Platform;

public class Player extends Rectangle {
	
	private KeyCode leftKey;
	private KeyCode rightKey;
	private KeyCode upKey;
	private KeyCode downKey;
	private KeyCode shootKey;
	private KeyCode jumpKey;
	
	private int xPosition;
	private int yPosition;
	private double xVelocity = 0;
	private double xAcceleration = 1;
	private double yVelocity = 0;
	private double yAcceleration = 0.40d;
	private double xMaxVelocity = 5;
	private double yMaxVelocity = 10;
	
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
	
	public static int height;
	public static int width;
	private ImageView sprite;
	
	private long lastShotTime = 0;
	private int fireDelay = 30;
	private int bulletPerClip = 3;
	private int dropDownTimer = 0;
	private int buffTimer = 0;
	private int reloadTimer = 0;
	
	public Player(int xPosition, int yPosition, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode downKey) {
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
		this.setTranslateX(xPosition);
		this.setTranslateY(yPosition);
		this.sprite = new ImageView();
		
		this.sprite.setImage(new Image(Launcher.class.getResource("assets/FD.png").toString()));
		this.sprite.setFitWidth(width);
		this.sprite.setFitHeight(height);
		this.setWidth(width);
		this.setHeight(height);
		this.setFill(Color.AZURE);
	}
	
	//					Starting of Movement Behaviors
	public void moveLeft() {
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
		yPosition -= 100;
		isFalling = true;
		canJump = false;
		isJumping = false;
		this.setTranslateX(xPosition);
		this.setTranslateY(yPosition);

	}
	
	public void die() {
		lives--;
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
    		
            Bullet bullet = isProning ? new Bullet(xBulletPos, (yPosition + height/2), 10, 10, direction)
            		: new Bullet(xBulletPos, yBulletPos, 10, 10, direction);
                GameLoop.bullets.add(bullet);

                javafx.application.Platform.runLater(() -> {
                    gameStage.getChildren().add(bullet);
                });
    	}
	}
	
	public void stop() {
		isMoveLeft = false;
		isMoveRight = false;
		xVelocity = 0;
	}
	
	public void repaint() {
		moveX();
		moveY();
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
	
//	public void checkItemCollision(GameStage gameStage) {
//		if (buffTimer > 0) {
//			buffTimer--;
//			return;
//		} else if (isBuffed) {
//			isBuffed = false;
//			fireDelay = 30;
//			bulletPerClip = 3;
//		}
//		
//		if (gameStage.getItem() != null) {
//			double itemXPos = gameStage.getItem().getXPos();
//			double itemYPos = gameStage.getItem().getYPos();
//			double itemWidth = gameStage.getItem().getPaneWidth();
//			double itemHeight = gameStage.getItem().getPaneHeight();
//			
//			boolean isHellfireMag = gameStage.getItem() instanceof HellfireMagazine;
//			
//			boolean collidedXAxis = ((xPosition + width) >= itemXPos && xPosition <= itemXPos) ||
//					(xPosition <= (itemXPos + itemWidth) && (xPosition + width) >= itemXPos +itemWidth);
//			boolean collidedYAxis = (yPosition + height >= itemYPos) && (yPosition <= itemYPos+itemHeight);
//			if (collidedXAxis && collidedYAxis) {
//				isBuffed = true;
//				buffTimer = 200;
//				
//				bulletPerClip = Integer.MAX_VALUE;
//				System.out.print(isHellfireMag);
//				javafx.application.Platform.runLater(() -> {
//					gameStage.removeItem();
//					return;
//				});
//			}
//		}
//
//	}
	
	public void checkItemCollision(GameStage gameStage) {
		if (buffTimer > 0) {
			buffTimer--;
			return;
		} else if (isBuffed) {
			isBuffed = false;
			fireDelay = 30;
			bulletPerClip = 3;
		}
		if (gameStage.getItem() != null) {
			if (gameStage.getItem().getBoundsInParent().intersects(this.getBoundsInParent())) {
				boolean isHellfireMag = gameStage.getItem() instanceof HellfireMagazine;
				isBuffed = true;
				if (isHellfireMag) {
					buffTimer = 200;
					bulletPerClip = Integer.MAX_VALUE;
				} else {
					System.out.println("Another Item");
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
	
	// 				End of Movement Behaviors
	
	// GETTERS SETTERS
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

}