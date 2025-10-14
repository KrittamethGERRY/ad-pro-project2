package se233.notcontra.model;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStage;

public class Wallboss extends Boss {
    private final int phaseChangeHealth;
    private Enemy turretLeft;
    private Enemy turretRight;
    private Rectangle core;
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
        turretLeft = new Enemy(0, 0, 0, 32, 32, this.getMaxHealth()/2, EnemyType.TURRET);
        turretRight = new Enemy(100, 0, 0, 32, 32, this.getMaxHealth()/2, EnemyType.TURRET);
        GameLoop.enemies.addAll(List.of(turretLeft, turretRight));
        System.out.println(this.localToParent(turretLeft.getBoundsInParent()));
        getWeakPoints().add(turretLeft);
        getWeakPoints().add(turretRight);
        getWeakPoints().add(core);
        core.setFill(Color.BLUE);
        javafx.application.Platform.runLater(() -> {
            this.getChildren().addAll(turretLeft, turretRight, core);        	
        });
    }

    @Override
    protected void handleAttackingState() {
        if (shootTimer > 0) {
            shootTimer--;
            return;
        }

        if (Math.random() < 0.6) {
            Enemy firingTurret = (Math.random() < 0.5) ? turretLeft : turretRight;
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
            setState(BossState.VULNERABLE);
        }
    }

    private void shootFromTurret(Enemy turret) {

        ShootingDirection[] leftDirections = {
                ShootingDirection.LEFT,
                ShootingDirection.DOWN_LEFT
        };

        ShootingDirection randomDirection = leftDirections[(int)(Math.random() * leftDirections.length)];

        int turretXPos = (int) (this.getXPos() + turret.getXPos());
        int turretYPos = (int) (this.getYPos() + turret.getYPos());
        
        int speedX = 5;
        int speedY = (randomDirection == ShootingDirection.UP_LEFT ||
                randomDirection == ShootingDirection.DOWN_LEFT) ? 3 : 0;

        Bullet bullet = new Bullet(turretXPos, turretYPos,speedX, speedY, randomDirection , BulletOwner.ENEMY);

        GameLoop.bullets.add(bullet);
        Platform.runLater(() -> {
            gameStage.getChildren().add(bullet);
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

            int spawnX = 100;
            int spawnY = -200;

            // Create wall shooter (stands still and shoots)
            Enemy enemy = new Enemy(spawnX, spawnY, 0, 64, 64, 1, EnemyType.WALL_SHOOTER);
            // NOTE: Get children's position do not touch!!!!
            System.out.print("Enemy Bound: " + enemy.getBoundsInParent());
            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                this.getChildren().add(enemy);
            });
            enemyTimer = 500;
        }
    }
    @Override
    protected void updateWeakPointsPosition() {
    }

}