package se233.notcontra.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    private int xPosition, yPosition, speedX, speedY;
    private ShootingDirection direction;
    public Bullet(int xPosition, int yPosition, int speedX, int speedY, ShootingDirection direction) {
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
    }
    
    public void move() {
    	setTranslateX(xPosition);
    	setTranslateY(yPosition);
    	switch (direction) {
    	case LEFT: moveLeft(); break;
    	case RIGHT: moveRight(); break;
    	case UP: moveUp(); break;
    	case UP_LEFT: moveUpLeft(); break;
    	case UP_RIGHT: moveUpRight(); break;
    	case DOWN_LEFT: moveDownLeft(); break;
    	case DOWN_RIGHT: moveDownRight(); break;
    	}
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
}