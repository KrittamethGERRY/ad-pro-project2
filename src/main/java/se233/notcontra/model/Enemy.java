package se233.notcontra.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.controller.SpriteAnimation;
import se233.notcontra.model.Boss.Boss;
import se233.notcontra.model.Boss.RDBoss;
import se233.notcontra.model.Boss.WallBoss;
import se233.notcontra.model.Enums.*;
import se233.notcontra.view.GameStages.GameStage;

public class Enemy extends Pane {
    protected int xPos;
	protected int yPos;
	protected int width;
	protected int height;
    protected int health;
    private double speed;
    boolean alive = true;
    private EnemyType  type;
    private int shootTimer = 100;
    private SpriteAnimation sprite;
    private EnemyState enemyState;
    private int shootingAnimationTimer = 0;


    public Enemy(int xPos, int yPos, double speed, int width, int height, int count, int column, int row, String imgName, int health, EnemyType type) {
    	setTranslateX(xPos);
    	setTranslateY(yPos);
    	Image image = new Image(Launcher.class.getResourceAsStream(imgName));
    	sprite = new SpriteAnimation(image, count, column, row, 0, 0, width, height);
    	this.getChildren().add(sprite);
    	this.setWidth(width);
    	this.setHeight(height);
    	sprite.setFitHeight(48);
    	sprite.setFitWidth(48);
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
            setState(EnemyState.ATTACKING);

        } else if (type == EnemyType.WALL_SHOOTER) {
            Bullet bullet = shootAtPlayer(player);
            if (bullet != null) {
                GameLoop.bullets.add(bullet);
                Platform.runLater(() -> {
                    gameStage.getChildren().add(bullet);
                });
            }
            setState(EnemyState.ATTACKING);
        }
        if (type == EnemyType.RDEYES){
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
        double worldX = getTranslateX();
        double worldY = getTranslateY();

        // Account for parent offsets
        if (getParent() != null && getParent().getParent() != null) {
            worldX += getParent().getTranslateX();
            worldY += getParent().getTranslateY();
        }

        // Calculate centers
        Vector2D enemyCenter = new Vector2D(worldX + width / 2.0, worldY + height / 2.0);
        Vector2D playerCenter = new Vector2D(player.getxPos() + Player.width / 2.0,
                player.getyPos() + Player.height / 2.0);

        // Direction vector from enemy to player
        Vector2D direction = playerCenter.subtract(enemyCenter);
        double distance = direction.getLength();

        // Ignore invalid distances
        if (distance < 1 || distance > 1000) return;

        // Normalize and move
        direction = direction.normalize();

        speed = 3.0; // tweak this for faster/slower movement

        // Apply movement in that direction
        double moveX = direction.x * speed;
        double moveY = direction.y * speed;

        // Update enemy position
        setTranslateX(getTranslateX() + moveX);
        setTranslateY(getTranslateY() + moveY);
    }



    public Bullet shootAtPlayer(Player player) {

        if (type == EnemyType.FLYING) {
            return null;
        }

        if (shootTimer > 0) {
            shootTimer--;
            return null;
        }

        double worldX = getTranslateX();
        double worldY = getTranslateY();

        if (getParent() != null && getParent().getParent() != null) {
            worldX += getParent().getTranslateX();
            worldY += getParent().getTranslateY();
        }

        Vector2D enemyCenter = new Vector2D(worldX + width / 2.0, worldY + height / 2.0);
        Vector2D playerCenter = new Vector2D((double) (player.getxPos() + Player.width / 2.0), (double) (player.getyPos() + Player.height / 2.0));
        Vector2D directionToPlayer = playerCenter.subtract(enemyCenter);

        if (directionToPlayer.getLength() < 1 || directionToPlayer.getLength() > 1000) {
            System.out.println("Enemy too far from player or same position. Distance: " + directionToPlayer);
            return null;
        }

        if (directionToPlayer.getLength() == 0) {
            return null;
        }

        double bulletSpeed = 0.007;

        shootTimer = 100;
        shootingAnimationTimer = 20;

        return new Bullet(
                enemyCenter,
                directionToPlayer,
                (double) bulletSpeed,
                BulletOwner.ENEMY
        );
    }


    private ShootingDirection determineDirection(Vector2D direction) {
        // Calculate angle in degrees
        double angle = Math.toDegrees(Math.atan2(direction.y, direction.x));
        double degrees = Math.toDegrees(angle);
        if (degrees < 0) degrees += 360;

        // Map to 8 directions
        if (degrees >= 337.5 || degrees < 22.5) {
            return ShootingDirection.RIGHT;
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return ShootingDirection.DOWN_RIGHT;
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return ShootingDirection.DOWN_LEFT;
        } else if (degrees >= 112.5 && degrees < 202.5) {
            return ShootingDirection.LEFT;
        } else if (degrees >= 202.5 && degrees < 292.5) {
            return ShootingDirection.UP_LEFT;
        } else {
            return ShootingDirection.UP_RIGHT;
        }
    }
    
    // Reduce Enemy HP, and Add Game score
    public void takeDamage(int damage, Boss boss) {
    	health -= damage;
    	switch (type) {
    	case PATROL: break;
    	case WALL_SHOOTER: break;
    	case TURRET: SoundController.getInstance().playMetalHitSound(); break;
    	case FLYING: break;
    	case WALL: SoundController.getInstance().playMetalHitSound2(); break;
        case RDEYES: SoundController.getInstance().playMetalHitSound(); break;
        case RDHAND: SoundController.getInstance().playMetalHitSound2(); break;
        case RDHEAD: SoundController.getInstance().playMetalHitSound(); break;
    	}
    	System.out.println("Health: " + this.health);
    	if (health <= 0) {
    		switch (type) {
    		case PATROL: GameLoop.addScore(300); SoundController.getInstance().playDieSound(); GameStage.totalMinions--; break;
    		case WALL_SHOOTER: GameLoop.addScore(100); SoundController.getInstance().playDieSound(); break;
    		case TURRET: GameLoop.addScore(500); WallBoss.totalTurret--;  break;
    		case FLYING: GameLoop.addScore(150); break;
    		case WALL: GameLoop.addScore(1000); SoundController.getInstance().playExplosionSound(); boss.getWeakPoints().remove(0); break;
            case JAVAHEAD: GameLoop.addScore(2500); SoundController.getInstance().playExplosionSound(); boss.getWeakPoints().remove(0); break;
            case RDEYES: GameLoop.addScore(500); RDBoss.totalEYES--; break;
            case RDHAND: GameLoop.addScore(500); break;
            case RDHEAD: GameLoop.addScore(3000); SoundController.getInstance().playExplosionSound(); boss.getWeakPoints().remove(0); break;
			default: 
				break;
    		}
    		kill();
            setState(EnemyState.DEAD);
    	}
    }

    public int getShootingAnimationTimer() {
        return shootingAnimationTimer;
    }



    public void updateShootingAnimation() {
        if (shootingAnimationTimer > 0) {
            shootingAnimationTimer--;
        }
    }



    public void setShootingAnimationTimer(int timer) {
        this.shootingAnimationTimer = timer;
    }


    public double getXPos() { return xPos; }
    public double getYPos() { return yPos; }
    public double getW() { return width; }
    public double getH() { return height; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }
    public EnemyState  getState() { return enemyState;}
    public void setState(EnemyState enemyState) { this.enemyState = enemyState;}
    public EnemyType getType() { return type; }
    public SpriteAnimation getSprite() { return sprite; }

}