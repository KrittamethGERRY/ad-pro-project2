package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;
import se233.notcontra.view.GameStage;

public class Player extends Pane {
	
	private KeyCode leftKey;
	private KeyCode rightKey;
	private KeyCode upKey;
	private KeyCode downKey;
	private KeyCode shootKey;
	
	private int xPosition;
	private int yPosition;
	private double xVelocity = 0;
	private double xAcceleration = 1;
	private double yVelocity = 0;
	private double yAcceleration = 0.40d;
	private double xMaxVelocity = 5;
	private double yMaxVelocity = 10;
	
	// Movement booleans
	private boolean isMoveRight = true;
	private boolean isMoveLeft = false;
	private boolean isJumping = false;
	private boolean isFalling = true;
	private boolean canJump = false;
	
	public static int height;
	public static int width;
	private ImageView sprite;
	
	public Player(int xPosition, int yPosition, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode downKey) {
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.upKey = upKey;
		this.downKey = downKey;
		this.shootKey = KeyCode.SPACE;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.height = 32;
		this.width = 32;
		this.setTranslateX(xPosition);
		this.setTranslateY(yPosition);
		this.sprite = new ImageView();
		
		this.sprite.setImage(new Image(Launcher.class.getResource("assets/FD.png").toString()));
		this.sprite.setFitWidth(width);
		this.sprite.setFitHeight(height);
		getChildren().add(sprite);		
		System.out.println("The player is created");
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
	
	public void prone() {
		if (!isJumping && !isFalling && canJump) stop();
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
	
	public void checkGroundCollision() {
		if (isFalling & yPosition >= GameStage.GROUND - height) {
			isFalling = false;
			canJump = true;
			yVelocity = 0;
			System.out.println("Floor reached");
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
	
	// 				End of Movement Behaviors
	
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


	public int getXPosition() {
		return xPosition;
	}


	public int getYPosition() {
		return yPosition;
	}

}