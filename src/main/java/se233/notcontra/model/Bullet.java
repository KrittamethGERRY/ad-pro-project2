package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.ShootingDirection;

public class Bullet extends Pane {
    private Vector2D velocity;
	private Vector2D position;
    private ShootingDirection direction;
	private BulletOwner owner;
	private boolean Alive = true;
	private ImageView sprite;
	private boolean gravityEnabled = false;
	private double velocityX;
	private double velocityY;
	private static final double GRAVITY = 0.3;

    public Bullet(int xPos, int yPos, int speedX, int speedY, ShootingDirection direction , BulletOwner owner) {
    	this.direction = direction;
        this.position = new Vector2D(xPos, yPos);
		this.velocity =  calculateVelocity(speedX, speedY, direction);
		this.owner = owner;

		this.velocityX = speedX;
		this.velocityY = speedY;
		setupBullet();
    }

	public void setGravityEnabled(boolean enabled) {
		this.gravityEnabled = enabled;
	}

	public Bullet(Vector2D startPos, Vector2D directionVector, double speed, BulletOwner owner) {
		this.position = new Vector2D(startPos.getX(), startPos.getY());
		this.owner = owner;
		this.direction = ShootingDirection.RIGHT;
		Vector2D normalizedDir = directionVector.normalize();
		this.velocity = directionVector.multiply(speed);

		setupBullet();
	}

	private void setupBullet() {
		setTranslateX((int)position.getX());
		setTranslateY((int)position.getY());
		sprite = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Item/Entities/Bullet.png")));
		if (this.owner != BulletOwner.PLAYER) {
			sprite = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Item/Entities/Cannonball.png")));
		}
		sprite.setFitHeight(32);
		sprite.setFitWidth(32);
		this.getChildren().add(sprite);
		this.setWidth(32);
		this.setHeight(32);
	}

	private Vector2D calculateVelocity(int speedX, int speedY, ShootingDirection direction) {
		switch (direction) {
			case LEFT: this.setRotate(180);
				return new Vector2D(-speedX, 0);
			case RIGHT:
				return new Vector2D(speedX, 0);
			case UP: this.setRotate(270);
				return new Vector2D(0, -speedY);
			case UP_LEFT: this.setRotate(225);
				return new Vector2D(-speedX - 2, -speedY + 2);
			case UP_RIGHT: this.setRotate(-45);
				return new Vector2D(speedX + 2, -speedY - 2);
			case DOWN_LEFT: this.setRotate(135);
				return new Vector2D(-speedX - 2, speedY - 2);
			case DOWN_RIGHT: this.setRotate(-315);
				return new Vector2D(speedX + 2, speedY - 2);
			default:
				return new Vector2D(0, 0);
		}
	}

	public void move() {
		if (gravityEnabled) {
			velocityY += GRAVITY; // Apply gravity
			setTranslateX(getTranslateX() + velocityX);
			setTranslateY(getTranslateY() + velocityY);
		} else {
			position = position.add(velocity);
			setTranslateX((int) position.getX());
			setTranslateY((int) position.getY());
		}
	}

	public void move(float deltaTime) {
		Vector2D movement = velocity.multiply(deltaTime);
		position = position.add(movement);
		setTranslateX((int)position.getX());
		setTranslateY((int)position.getY());
	}


	public Vector2D getPosition() {
		return position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D newVelocity) {
		this.velocity = newVelocity;
	}

	public int getxPos() {
		return (int)position.getX();
	}

	public int getyPos() {
		return (int)position.getY();
	}

	public boolean isAlive() { return Alive; }
	public void destroy() { Alive = false; }

	public boolean isOutOfBounds(int screenWidth, int screenHeight) {
		return position.getX() < -5 || position.getX() > screenWidth + 5 ||
				position.getY() < -5 || position.getY() > screenHeight + 5;
	}

	public BulletOwner getOwner() { return owner; }
	public boolean isEnemyBullet() { return owner == BulletOwner.ENEMY;}
	
	public void setBulletSprite(Image image) {
		sprite.setImage(image);
		sprite.setFitHeight(64);
		sprite.setFitWidth(64);
	}
}

