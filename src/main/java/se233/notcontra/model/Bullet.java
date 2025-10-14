package se233.notcontra.model;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    private int xPosition, yPosition, speedX, speedY;
    private ShootingDirection direction;
	private BulletOwner owner;
	private boolean Alive = true;

    public Bullet(int xPosition, int yPosition, int speedX, int speedY, ShootingDirection direction , BulletOwner owner) {
    	setTranslateX(xPosition);
    	setTranslateY(yPosition);
    	this.setFill(Color.BLACK);
    	this.setWidth(10);
    	this.setHeight(10);
    	this.direction = direction;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.speedX = speedX;
        this.speedY = speedY;
		this.owner = owner;
    }
    
    public void move() {
    	switch (direction) {
    	case LEFT: moveLeft(); break;
    	case RIGHT: moveRight(); break;
    	case UP: moveUp(); break;
    	case UP_LEFT: moveUpLeft(); break;
    	case UP_RIGHT: moveUpRight(); break;
    	case DOWN_LEFT: moveDownLeft(); break;
    	case DOWN_RIGHT: moveDownRight(); break;
    	}

		setTranslateX(xPosition);
		setTranslateY(yPosition);
    }
    
    public void moveLeft() {
    	xPosition -= speedX;
    }
    
    public void moveRight() {
    	xPosition += speedX;
    }
    
    public void moveUp() {
    	yPosition -= speedY;
    }
    
    public void moveUpRight() {
    	yPosition -= speedY + 2;
    	xPosition += speedX - 2;
    }
    
    public void moveUpLeft() {
    	yPosition -= speedY - 2;
    	xPosition -= speedX - 2;
    }
    
    public void moveDownRight() {
    	yPosition += speedY - 2;
    	xPosition += speedX + 2;
    }
    
    public void moveDownLeft() {
    	yPosition += speedY - 2;
    	xPosition -= speedX + 2;
    }

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}
	
	public void enemyCollision(List<Enemy> enemies, Boss boss) {
		for (Enemy enemy : enemies) {
			if (boss.localToParent(enemy.getBoundsInParent()).intersects(this.getBoundsInParent()) && this.owner == BulletOwner.PLAYER) {
				enemy.setFill(Color.BLACK);
				System.out.println("Collided");
			}
		}
	}
	

	public boolean isAlive() { return Alive; }
	public void destroy() { Alive = false; }

	public boolean isEnemyBullet() { return owner == BulletOwner.ENEMY;}
}