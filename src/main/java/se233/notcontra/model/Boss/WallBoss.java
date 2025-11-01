package se233.notcontra.model.Boss;

import java.util.List;

import javafx.application.Platform;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.ImageAssets;
import se233.notcontra.model.Enemy.Enemy;
import se233.notcontra.model.Enums.BulletOwner;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.view.GameStages.GameStage;

public class WallBoss extends Boss {
    private static Enemy turretLeft;
    private static Enemy turretRight;
    private static Enemy core;
    private GameStage gameStage;
    private Enemy lastTurretFired = null;

    private int enemyTimer = 0;
    public static int totalTurret = 2;
    private final int maxEnemies = 1;

    public WallBoss(int xPos, int yPos, int width, int height, GameStage gameStage) {
        super(xPos, yPos, width, height, 30000);
    	getWeakPoints().clear();
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.gameStage = gameStage;
        turretLeft = new Enemy(-35, -10, 0, 64, 32, 64, 32, 1, 1, 1, ImageAssets.IDLE_TURRET, this.getMaxHealth()/4 + 100, EnemyType.TURRET);
        turretRight = new Enemy(90, -10, 0, 64, 32, 64, 32, 1, 1, 1, ImageAssets.IDLE_TURRET, this.getMaxHealth()/4 + 100, EnemyType.TURRET);
        core = new Enemy(0, 0, 0, 192, 192, 64, 64, 2, 3, 1, ImageAssets.IDLE_CORE, this.getMaxHealth()/2, EnemyType.WALL);
        core.setLayoutY(60);
        core.setLayoutX(-23);
        getWeakPoints().add(core);
        Platform.runLater(() -> {
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
            shootTimer = 100;
        }
        else if (!turretLeft.isAlive() && turretRight.isAlive()) {
            shootFromTurret(turretRight);
            shootTimer = 100;
        }
        else if (turretLeft.isAlive() && turretRight.isAlive()) {
            if (lastTurretFired == null || lastTurretFired == turretRight) {
                shootFromTurret(turretLeft);
                lastTurretFired = turretLeft;
            } else {
                shootFromTurret(turretRight);
                lastTurretFired = turretRight;
            }
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


        int speedX = -8;

        int speedY;

        if (randomDirection == ShootingDirection.DOWN_LEFT) {
            speedY = -3;
        } else {
            speedY = -6;
        }

        Bullet bullet = new Bullet(turretXPos, turretYPos,speedX, speedY, randomDirection , BulletOwner.ENEMY);
        SoundController.getInstance().playCannonSound();
        bullet.setGravityEnabled(true);

        turret.setShootingAnimationTimer(20);

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
            int spawnX = 0;
            int spawnY = -200;

            // Create wall shooter (stands still and shoots)
            Enemy enemy = new Enemy(spawnX, spawnY, 0, 64, 64, 64, 64, 2, 5, 1, ImageAssets.WALL_ENEMY, 1, EnemyType.WALL_SHOOTER);
            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                this.getChildren().add(enemy);
            });
            enemyTimer = 500;
        }
    }
    
    public static Enemy getCore() { return core; }
    public static List<Enemy> getTurrets() { return List.of(turretLeft, turretRight); }
}