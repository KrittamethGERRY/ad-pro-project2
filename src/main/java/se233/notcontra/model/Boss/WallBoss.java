package se233.notcontra.model.Boss;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.EnemyState;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.model.Turret;
import se233.notcontra.view.GameStages.GameStage;

public class WallBoss extends Boss {
    private final int phaseChangeHealth;
    private Enemy turretLeft;
    private Enemy turretRight;
    private Enemy core;
    private GameStage gameStage;
    private Enemy lastTurretFired = null;
    private boolean coreRevealed = false;

    private int enemyTimer = 0;
    public static int totalTurret = 2;
    private final int maxEnemies = 1;

    public WallBoss(int xPos, int yPos, int width, int height, GameStage gameStage) {
        super(xPos, yPos, width, height, 30000);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.gameStage = gameStage;
        this.phaseChangeHealth = this.getMaxHealth() / 2;
        turretLeft = new Enemy(0, 0, 0, 32, 32, 1, 1, 1,"assets/Boss/Boss1/Turret.png", this.getMaxHealth()/4 + 100, EnemyType.TURRET);
        turretRight = new Enemy(100, 0, 0, 32, 32, 1, 1, 1,"assets/Boss/Boss1/Turret.png", this.getMaxHealth()/4 + 100, EnemyType.TURRET);
        int coreX = (int) ((turretLeft.getXPos() + turretRight.getXPos()) / 2 + 16);
        core = new Enemy(coreX, 0, 100, 64, 64, 2, 1, 1, "assets/Boss/Boss1/core.png", this.getMaxHealth()/2, EnemyType.WALL);
        core.getSprite().setLayoutY(60);
        core.getSprite().setLayoutX(-23);
        core.getSprite().setFitHeight(192);
        core.getSprite().setFitWidth(192);

        GameLoop.enemies.addAll(List.of(turretLeft, turretRight));
        System.out.println(this.localToParent(core.getBoundsInParent()));
        getWeakPoints().add(core);
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

        if (turretLeft.isAlive() && !turretRight.isAlive()) {
            shootFromTurret(turretLeft);
            turretLeft.setState(EnemyState.ATTACKING);
            turretLeft.setState(EnemyState.IDLE);
            shootTimer = 100;
        }
        else if (!turretLeft.isAlive() && turretRight.isAlive()) {
            shootFromTurret(turretRight);
            turretRight.setState(EnemyState.ATTACKING);
            turretRight.setState(EnemyState.IDLE);
            shootTimer = 100;
        }
        else if (turretLeft.isAlive() && turretRight.isAlive()) {
            if (lastTurretFired == null || lastTurretFired == turretRight) {
                shootFromTurret(turretLeft);
                lastTurretFired = turretLeft;
                turretLeft.setState(EnemyState.ATTACKING);
            } else {
                shootFromTurret(turretRight);
                lastTurretFired = turretRight;
                turretRight.setState(EnemyState.ATTACKING);
            }
            turretRight.setState(EnemyState.IDLE);
            turretLeft.setState(EnemyState.IDLE);
            shootTimer = 100;
        }

        spawnEnemy();

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
            Enemy enemy = new Enemy(spawnX, spawnY, 0, 32, 32, 2, 5, 1,"assets/Enemy/enemy_wall_shooter.png", 1, EnemyType.WALL_SHOOTER);
            // NOTE: Get children's global position do not touch!!!!
            //System.out.print("Enemy Bound: " + getLocalToParentTransform());
            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                this.getChildren().add(enemy);
            });
            enemyTimer = 500;
        }
    }
}