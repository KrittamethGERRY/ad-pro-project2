package se233.notcontra.model;

import java.util.List;

import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.view.Platform;
import se233.notcontra.view.GameStages.GameStage;

public class PatrolEnemy extends Enemy {

	private int startX;
	private int startY;
	private double xVelocity = 0;
	private double xAcceleration = 1;
	private double yVelocity = 0;
	private double yAcceleration = 0.40d;
	private double xMaxVelocity = 2;
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
		this.setScaleX(1);
		
	}
	
	public void moveRight() {
		isMoveRight = true;
		isMoveLeft = false;
		this.setScaleX(-1);
	}
	
	public void respawn() {
		this.yPos = this.startY;
		this.xPos = this.startX;
		this.isMoveLeft = false;
		this.isMoveRight = false;
		this.isFalling = true;
		this.canJump = false;
		this.isJumping = false;
	}
	
	public void checkStageBoundaryCollision() {
		if ((xPos + width) >= GameStage.WIDTH) {
			xPos = GameStage.WIDTH - width;
		}
		if (xPos <= 0) {
			xPos = 0;
		}
	}
	
	public void checkPlatformCollision(List<Platform> platforms) {		
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
	
    public void updateAI(Player player) {
        if (xPos > player.getxPos() - player.getWidth()/2) {
            this.getSprite().tick();
            this.moveLeft();
        } else if (xPos < player.getxPos() + player.width/2) {
            this.getSprite().tick();
            this.moveRight();
        } else {
            this.stop();
        }
        if (yPos > player.getyPos() + player.height) {
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
    
    public void takeDamage(int dmg) {
        health -= dmg;
        if(health <= 0) isAlive = false;
    }

}
