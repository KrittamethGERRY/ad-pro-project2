package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
		sprite = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Item/Entities/bullet.png")));
		sprite.setFitHeight(64);
		sprite.setFitWidth(64);
		this.getChildren().add(sprite);
		this.setWidth(16);
		this.setHeight(8);
	}

	private Vector2D calculateVelocity(int speedX, int speedY, ShootingDirection direction) {
		switch (direction) {
			case LEFT: this.setRotate(180);
				return new Vector2D(-speedX, 0);
			case RIGHT:
				return new Vector2D(speedX, 0);
			case UP: this.setRotate(90);
				return new Vector2D(0, -speedY);
			case UP_LEFT: this.setRotate(45);
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
	
	public void setBulletSprite(Image image) {
		sprite.setImage(image);
		sprite.setFitHeight(8);
		sprite.setFitWidth(16);
	}
}

