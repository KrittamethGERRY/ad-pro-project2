package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;

public class Bullet extends Pane {
    private int xPosition, yPosition, speedX, speedY;
    private ShootingDirection direction;
    private Image bulletImg;
    public Bullet(int xPosition, int yPosition, int speedX, int speedY, ShootingDirection direction) {
    	setTranslateX(xPosition);
    	setTranslateY(yPosition);
    	bulletImg = new Image(Launcher.class.getResourceAsStream("assets/FD.png"));
    	ImageView imageView = new ImageView(bulletImg);
    	imageView.setFitHeight(8);
    	imageView.setFitWidth(8);
    	getChildren().add(imageView);
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
    	yPosition -= speedY;
    	xPosition += speedX;
    }
    
    public void moveUpLeft() {
    	yPosition -= speedY;
    	xPosition -= speedX;
    }
    
    public void moveDownRight() {
    	yPosition += speedY;
    	xPosition += speedX;
    }
    
    public void moveDownLeft() {
    	yPosition += speedY;
    	xPosition -= speedX;
    }

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}
}