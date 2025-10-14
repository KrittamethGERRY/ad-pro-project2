package se233.notcontra.model;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStage;

public class Enemy extends Rectangle {
    double xPos, yPos, width, height;
    double speed = 2;
    boolean alive = true;
    private EnemyType  type;
    private int shootTimer = 75;


    public Enemy(double xPos, double yPos, double speed, double width, double height, EnemyType type) {
    	this.setTranslateX(xPos);
    	this.setTranslateY(yPos);
    	this.setFill(Color.GREEN);
    	this.setWidth(width);
    	this.setHeight(height);
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
        // Calculate center positions
        // This type enemy didn't work yet
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
        // Only wall shooters can shoot
        if (type == EnemyType.FLYING) {
            return null;
        }

        //long currentTime = System.currentTimeMillis();

        if (shootTimer > 0) {
            shootTimer--;
            return null;
        }

        double enemyCenterX = xPos + width / 2;
        double enemyCenterY = yPos + height / 2;
        double playerCenterX = player.getXPosition() + Player.width / 2;
        double playerCenterY = player.getYPosition() + Player.height / 2;

        double deltaX = playerCenterX - enemyCenterX;
        double deltaY = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        double bulletSpeed = 5.0;
        int speedX = (int) ((deltaX / distance) * bulletSpeed);
        int speedY = (int) ((deltaY / distance) * bulletSpeed);

        ShootingDirection direction = determineDirection(deltaX, deltaY);

        //lastShotTime = currentTime;
        shootTimer = 35;
        return new Bullet((int)enemyCenterX, (int)enemyCenterY,
                Math.abs(speedX), Math.abs(speedY),
                direction, BulletOwner.ENEMY);
    }

    private ShootingDirection determineDirection(double deltaX, double deltaY) {
        double angle = Math.atan2(deltaY, deltaX);
        double degrees = Math.toDegrees(angle);

        if (degrees < 0) degrees += 360;

        if (degrees >= 337.5 || degrees < 22.5) return ShootingDirection.RIGHT;
        else if (degrees >= 22.5 && degrees < 67.5) return ShootingDirection.DOWN_RIGHT;
        else if (degrees >= 67.5 && degrees < 112.5) return ShootingDirection.DOWN_LEFT;
        else if (degrees >= 112.5 && degrees < 157.5) return ShootingDirection.DOWN_LEFT;
        else if (degrees >= 157.5 && degrees < 202.5) return ShootingDirection.LEFT;
        else if (degrees >= 202.5 && degrees < 247.5) return ShootingDirection.UP_LEFT;
        else if (degrees >= 247.5 && degrees < 292.5) return ShootingDirection.UP_LEFT;
        else return ShootingDirection.UP_RIGHT;
    }

    public double getXPos() { return xPos; }
    public double getYPos() { return yPos; }
    public double getW() { return width; }
    public double getH() { return height; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }
    public EnemyType getType() { return type; }

}