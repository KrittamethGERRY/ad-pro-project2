package se233.notcontra.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.ShootingDirection;

public class Bullet extends Rectangle {
    private Vector2D velocity;
	private Vector2D position;
    private ShootingDirection direction;
	private BulletOwner owner;
	private boolean Alive = true;

    public Bullet(int xPosition, int yPosition, int speedX, int speedY, ShootingDirection direction , BulletOwner owner) {
    	this.direction = direction;
        this.position = new Vector2D(xPosition, yPosition);
		this.velocity =  calculateVelocity(speedX, speedY, direction);
		this.owner = owner;

		setupBullet();
    }

	public Bullet(Vector2D startPos, Vector2D directionVector, double speed, BulletOwner owner) {
		this.position = new Vector2D(startPos.x, startPos.y);
		this.owner = owner;
		this.direction = ShootingDirection.RIGHT;
		Vector2D normalizedDir = directionVector.normalize();
		this.velocity = directionVector.multiply(speed);

		setupBullet();
	}

	private void setupBullet() {
		setTranslateX((int)position.x);
		setTranslateY((int)position.y);
		this.setFill(Color.BLACK);
		this.setWidth(10);
		this.setHeight(10);
	}

	private Vector2D calculateVelocity(int speedX, int speedY, ShootingDirection direction) {
		switch (direction) {
			case LEFT:
				return new Vector2D(-speedX, 0);
			case RIGHT:
				return new Vector2D(speedX, 0);
			case UP:
				return new Vector2D(0, -speedY);
			case UP_LEFT:
				return new Vector2D(-speedX + 2, -speedY + 2);
			case UP_RIGHT:
				return new Vector2D(speedX - 2, -speedY - 2);
			case DOWN_LEFT:
				return new Vector2D(-speedX - 2, speedY - 2);
			case DOWN_RIGHT:
				return new Vector2D(speedX + 2, speedY - 2);
			default:
				return new Vector2D(0, 0);
		}
	}

	public void move() {
		position = position.add(velocity);
		setTranslateX((int)position.x);
		setTranslateY((int)position.y);
	}

	public void move(float deltaTime) {
		Vector2D movement = velocity.multiply(deltaTime);
		position = position.add(movement);
		setTranslateX((int)position.x);
		setTranslateY((int)position.y);
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

	public int getXPosition() {
		return (int)position.x;
	}

	public int getYPosition() {
		return (int)position.y;
	}

	public boolean isAlive() { return Alive; }
	public void destroy() { Alive = false; }

	public boolean isOutOfBounds(int screenWidth, int screenHeight) {
		return position.x < -5 || position.x > screenWidth + 5 ||
				position.y < -5 || position.y > screenHeight + 5;
	}

	public BulletOwner getOwner() { return owner; }
	public boolean isEnemyBullet() { return owner == BulletOwner.ENEMY;}
}

