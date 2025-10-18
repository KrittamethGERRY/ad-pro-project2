package se233.notcontra.model;

import java.util.List;

import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.view.Platform;
import se233.notcontra.view.GameStages.GameStage;

public class PatrolEnemy extends Enemy {
	
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
    boolean isAlive = true;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;
	private boolean isOnPlatform;
	
	public PatrolEnemy(int xPos, int yPos, double speed, int width, int height, int count, int column, int row, String imgName, int health, EnemyType type) {
		super(xPos, yPos, speed, width, height, count, column, row, imgName, health, type);
	}
	
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
	
	public void respawn() {
		this.yPosition = this.startY;
		this.xPosition = this.startX;
		this.isMoveLeft = false;
		this.isMoveRight = false;
		this.isFalling = true;
		this.canJump = false;
		this.isJumping = false;
	}
	
	public void checkStageBoundaryCollision() {
		if ((xPosition + width) >= GameStage.WIDTH) {
			xPosition = GameStage.WIDTH - width;
		}
		if (xPosition <= 0) {
			xPosition = 0;
		}
	}
	
	public void checkPlatformCollision(List<Platform> platforms) {		
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
	public void repaint() {
		moveY();
		moveX();
	}
	
    public void stop() {
        isMoveLeft = false;
        isMoveRight = false;
    }

	public void jump() {
		if (canJump) {
			yVelocity = yMaxVelocity;
			canJump = false;
			isJumping = true;
			isFalling = false;
		}
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
	
	
    public void updateAI(Player player) {
        if (xPos > player.getXPosition() + (player.getWidth()/2)) {
            this.getSprite().tick();
            this.moveLeft();
        } else if (xPosition < player.getXPosition() - (player.getWidth()/2) ) {
            this.getSprite().tick();
            this.moveRight();
        } else {
            this.stop();
        }
        if (yPosition < player.getYPosition() - player.getHeight() - 5) {
            this.jump();
        }
    }

    // Check
    public void checkReachGameWall() {
        if(xPos <= 0) {
            xPos = 0;
        } else if (xPos+getWidth() >= GameStage.WIDTH) {
        	xPos = GameStage.WIDTH-(int)getWidth();
        }
    }
    public void checkReachHighest () {
        if(isJumping && yVelocity <= 0) {
            isJumping = false;
            isFalling = true;
            yVelocity = 0;
        }
    }
    // Damage
    public void takeDamage(int dmg) {
        health -= dmg;
        if(health <= 0) isAlive = false;
    }

}
