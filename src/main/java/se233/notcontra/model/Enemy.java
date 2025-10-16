package se233.notcontra.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SpriteAnimation;
import se233.notcontra.model.Boss.Wallboss;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.view.GameStages.GameStage;

public class Enemy extends Pane {
    private int xPos, yPos, width, height;
    private int health;
    double speed = 2;
    boolean alive = true;
    private EnemyType  type;
    private int shootTimer = 75;
    private SpriteAnimation sprite;

    public Enemy(int xPos, int yPos, int speed, int width, int height, String imgName, int health, EnemyType type) {
    	setTranslateX(xPos);
    	setTranslateY(yPos);
    	Image image = new Image(Launcher.class.getResourceAsStream(imgName));
    	sprite = new SpriteAnimation(image, 5, 5, 1, 0, 0, 32, 32);
    	this.getChildren().add(sprite);
    	this.setWidth(width);
    	this.setHeight(height);
    	this.health = health;
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.type = type;
        
    }

    public void updateWithPlayer(Player player, GameStage gameStage) {
        if (!alive) return;

        if (type == EnemyType.FLYING) {
            flyTowardsPlayer(player);

            if (checkCollisionWithPlayer(player)) {
                handleFlyingAttack(player);
            }
        } else if (type == EnemyType.WALL_SHOOTER) {
            Bullet bullet = shootAtPlayer(player);
            if (bullet != null) {
                GameLoop.bullets.add(bullet);
                Platform.runLater(() -> {
                    gameStage.getChildren().add(bullet);
                });
            }
        }
    }

    private void flyTowardsPlayer(Player player) {
        double enemyCenterX = xPos + width / 2;
        double enemyCenterY = yPos + height / 2;
        double playerCenterX = player.getXPosition() + Player.width / 2;
        double playerCenterY = player.getYPosition() + Player.height / 2;

        double deltaX = playerCenterX - enemyCenterX;
        double deltaY = playerCenterY - enemyCenterY;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 5) {
            xPos += (deltaX / distance) * speed;
            yPos += (deltaY / distance) * speed;
        }
    }

    public boolean checkCollisionWithPlayer(Player player) {
        double playerX = player.getXPosition();
        double playerY = player.getYPosition();
        double playerW = Player.width;
        double playerH = Player.height;

        return xPos < playerX + playerW &&
        		xPos + width > playerX &&
        		yPos < playerY + playerH &&
        		yPos + height > playerY;
    }

    private void handleFlyingAttack(Player player) {

    }

    public Bullet shootAtPlayer(Player player) {

        if (type == EnemyType.FLYING) {
            return null;
        }

        if (shootTimer > 0) {
            shootTimer--;
            return null;
        }

        Vector2D enemyCenter =  new Vector2D((double) (xPos + width / 2), (double) (yPos + height / 2));

        Vector2D playerCenter = new Vector2D((double) (player.getXPosition() + Player.width / 2.0), (double) (player.getYPosition() + Player.height / 2.0));

        Vector2D directionToPlayer = playerCenter.subtract(enemyCenter);

        if (directionToPlayer.getLength() == 0) {
            return null;
        }

        double bulletSpeed = 0.007525;
        ShootingDirection direction = determineDirection(directionToPlayer);

        shootTimer = 75;
        return new Bullet(enemyCenter,
                directionToPlayer,
                (double) bulletSpeed,
                BulletOwner.ENEMY
        );
    }

    private ShootingDirection determineDirection(Vector2D direction) {
        // Calculate angle in degrees
        double angle = Math.atan2(direction.y, direction.x);
        double degrees = Math.toDegrees(angle);

        // Normalize to 0-360 range
        if (degrees < 0) degrees += 360;


        if (degrees >= 337.5 || degrees < 22.5) {
            return ShootingDirection.RIGHT;
        }
        else if (degrees >= 22.5 && degrees < 67.5) {
            return ShootingDirection.DOWN_RIGHT;
        }
        else if (degrees >= 67.5 && degrees < 112.5) {
            return ShootingDirection.DOWN_LEFT;
        }
        else if (degrees >= 112.5 && degrees < 157.5) {
            return ShootingDirection.LEFT;
        }
        else if (degrees >= 157.5 && degrees < 202.5) {
            return ShootingDirection.LEFT;
        }
        else if (degrees >= 202.5 && degrees < 247.5) {
            return ShootingDirection.UP_LEFT;
        }
        else if (degrees >= 247.5 && degrees < 292.5) {
            return ShootingDirection.UP_LEFT;
        }
        else {
            return ShootingDirection.UP_RIGHT;
        }
    }
    
    // Reduce Enemy HP, and Add Game score
    public void takeDamage(int damage) {
    	health -= damage;
    	if (health <= 0) {
    		switch (type) {
    		case WALL_SHOOTER: GameLoop.addScore(100); break;
    		case TURRET: GameLoop.addScore(500); Wallboss.totalTurret--;  break;
    		case FLYING: GameLoop.addScore(150); break;
    		case WALL: GameLoop.addScore(1000);  break;
    		}
    		kill();
    	}
    }

    public double getXPos() { return xPos; }
    public double getYPos() { return yPos; }
    public double getW() { return width; }
    public double getH() { return height; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }
    public EnemyType getType() { return type; }

}