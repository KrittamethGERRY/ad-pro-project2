package se233.notcontra.model;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStage;

public class Wallboss extends Boss {
    private final int phaseChangeHealth;
    private Turret turretLeft;
    private Turret turretRight;
    private final Rectangle core;
    private GameStage gameStage;

    private int enemyTimer = 0;
    private final int maxEnemies = 1;

    public Wallboss(int xPos, int yPos, int width, int height, GameStage gameStage) {
        super(xPos, yPos, width, height, 5000);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.gameStage = gameStage;
        this.phaseChangeHealth = this.getMaxHealth() / 2;
        this.core = new Rectangle(0, 80, 128, 128);
        turretLeft = new Turret(0, 0, 32, 32);
        turretRight = new Turret(100, 0, 32, 32);

        getWeakPoints().add(turretLeft);
        getWeakPoints().add(turretRight);
        core.setFill(Color.BLUE);
        
        getChildren().addAll(turretLeft, turretRight, core);
    }

    @Override
    protected void handleAttackingState() {
        if (shootTimer > 0) {
            shootTimer--;
            return;
        }

        if (Math.random() < 0.6) {
            Turret firingTurret = (Math.random() < 0.5) ? turretLeft : turretRight;
            shootFromTurret(firingTurret);
            shootTimer = 100;
        }

        spawnEnemy();
        // Check if health has dropped below the threshold to trigger Phase 2
        if (getHealth() <= this.phaseChangeHealth) {
            System.out.println("Turrets destroyed! Core is exposed!");
            getWeakPoints().remove(turretLeft);
            getWeakPoints().remove(turretRight);
            getChildren().remove(turretLeft);
            getChildren().remove(turretRight);

            // TODO: Trigger explosion animations at turret locations.

            // Switch to the VULNERABLE state (Phase 2)
            getWeakPoints().add(core);
        }
    }

    private void shootFromTurret(Turret turret) {

        ShootingDirection[] leftDirections = {
                ShootingDirection.LEFT,
                ShootingDirection.DOWN_LEFT
        };

        ShootingDirection randomDirection = leftDirections[(int)(Math.random() * leftDirections.length)];

        int turretXPos = this.getXPos() + turret.xPos;
        int turretYPos = this.getYPos() + turret.yPos;
        
        int speedX = 5;
        int speedY = (randomDirection == ShootingDirection.UP_LEFT ||
                randomDirection == ShootingDirection.DOWN_LEFT) ? 3 : 0;

        Bullet bullet = new Bullet(turretXPos, turretYPos,speedX, speedY, randomDirection , BulletOwner.ENEMY);

        GameLoop.bullets.add(bullet);
        Platform.runLater(() -> {
            gameStage.getChildren().add(bullet);
            System.out.println("Boss shot bullet in direction: " + randomDirection);
        });
    }

    private void spawnEnemy() {
    	if (enemyTimer > 0) {
    		enemyTimer--;
    		return;
    	}
        int aliveCount = 0;
        for (Enemy enemy : GameLoop.enemies) {
            if (enemy.isAlive() && enemy.getType() == EnemyType.WALL_SHOOTER) {
                aliveCount++;
            }
        }

        if (aliveCount < maxEnemies) {

            double spawnX = this.getXPos() + 100;
            double spawnY = this.getYPos() - 200;

            // Create wall shooter (stands still and shoots)
            Enemy enemy = new Enemy(spawnX, spawnY, 0, 32, 32, EnemyType.WALL_SHOOTER);

            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                getChildren().add(enemy);
            });
            System.out.println("Enemy spawned at: " + spawnX + ", " + spawnY);
            enemyTimer = 500;
        }
    }
    @Override
    protected void updateWeakPointsPosition() {
    }

    @Override
    protected void handleDefeatedState(){

    }

}