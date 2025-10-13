package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;

public class Bullet extends Pane {
    private int xPosition, yPosition, speedX, speedY;
    private ShootingDirection direction;
    private Image bulletImg;
	private BulletOwner owner;
	private boolean Alive = true;

    public Bullet(int xPosition, int yPosition, int speedX, int speedY, ShootingDirection direction , BulletOwner owner) {
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

	public boolean isAlive() { return Alive; }
	public void destroy() { Alive = false; }

	public boolean isEnemyBullet() { return owner == BulletOwner.ENEMY;}
}