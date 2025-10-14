package se233.notcontra.model;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStage;

import java.util.ArrayList;
import java.util.List;

public class Wallboss extends Boss {
    private final int phaseChangeHealth;
    private final Rectangle turretLeft;
    private final Rectangle turretRight;
    private final Rectangle core;
    private GameStage gameStage;
    private ImageView turretleftSprite;
    private ImageView turretrightSprite;
    private ImageView coreSprite;
    private ImageView wallbossSprite;

    private List<EnemyView> spawnedEnemyViews;
    private long lastEnemySpawnTime;
    private final long enemySpawnCooldown = 5000; // Spawn enemy every 5 seconds
    private final int maxEnemies = 1;

    public Wallboss(double x, double y ,GameStage gameStage) {
        super(x, y, 300, 200, 5000);
        this.gameStage = gameStage;
        this.phaseChangeHealth = this.getMaxHealth() / 2;

        this.turretLeft = new Rectangle(30, 50, 40, 40);
        this.turretRight = new Rectangle(115, 50, 40, 40);
        this.core = new Rectangle(65, 80, 60, 60);
        this.turretleftSprite = new ImageView(Launcher.class.getResource("assets/FD.png").toString());
        this.turretrightSprite = new ImageView(Launcher.class.getResource("assets/FD.png").toString());
        this.coreSprite = new ImageView(Launcher.class.getResource("assets/FD.png").toString());
        this.wallbossSprite = new ImageView(Launcher.class.getResource("assets/wall.png").toString());
        this.wallbossSprite.setFitWidth(300);
        this.wallbossSprite.setFitHeight(200);
        getChildren().add(wallbossSprite);

        getWeakPoints().add(turretLeft);
        getWeakPoints().add(turretRight);
        getWeakPoints().add(core);


        turretLeft.setFill(Color.RED);
        turretRight.setFill(Color.RED);
        core.setFill(Color.BLUE);
        getChildren().addAll(turretLeft, turretRight, core);

        this.spawnedEnemyViews = new ArrayList<>();
        this.lastEnemySpawnTime = System.currentTimeMillis();
    }

    @Override
    protected void handleAttackingState() {
        if (shootTimer > 0) {
            shootTimer--;
            return;
        }

        if (Math.random() < 0.6) {
            Rectangle firingTurret = (Math.random() < 0.5) ? turretLeft : turretRight;
            shootFromTurret(firingTurret);
            System.out.println("Shoot");
            shootTimer = 100;
        }

        spawnEnemy();
        updateEnemyViews();
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

    private void shootFromTurret(Rectangle turret) {
        int startX = (int) (this.getLayoutX() + turret.getX() + turret.getWidth() / 2);
        int startY = (int) (this.getLayoutY() + turret.getY() + turret.getHeight() / 2);

        ShootingDirection[] leftDirections = {
                ShootingDirection.LEFT,
                ShootingDirection.DOWN_LEFT
        };

        ShootingDirection randomDirection = leftDirections[(int)(Math.random() * leftDirections.length)];

        int speedX = 5;
        int speedY = (randomDirection == ShootingDirection.UP_LEFT ||
                randomDirection == ShootingDirection.DOWN_LEFT) ? 3 : 0;

        Bullet bullet = new Bullet(startX, startY, speedX, speedY, randomDirection , BulletOwner.ENEMY);

        GameLoop.bullets.add(bullet);
        Platform.runLater(() -> {
            gameStage.getChildren().add(bullet);
            System.out.println("Boss shot bullet in direction: " + randomDirection);
        });
    }

    private void spawnEnemy() {
        long currentTime = System.currentTimeMillis();

        int aliveCount = 0;
        for (Enemy enemy : GameLoop.enemies) {
            if (enemy.isAlive() && enemy.getType() == EnemyType.WALL_SHOOTER) {
                aliveCount++;
            }
        }

        if (currentTime - lastEnemySpawnTime > enemySpawnCooldown && aliveCount < maxEnemies) {

            double spawnX = this.getLayoutX() + Math.random() * (300 - 40);
            double spawnY = this.getLayoutY() + 20;

            // Create wall shooter (stands still and shoots)
            Enemy enemy = new Enemy(
                    spawnX,
                    spawnY,
                    0,    // speed = 0 (stands still)
                    40,   // width
                    60,   // height
                    EnemyType.WALL_SHOOTER
            );

            GameLoop.enemies.add(enemy);

            // Create visual
            EnemyView enemyView = new EnemyView(enemy);
            spawnedEnemyViews.add(enemyView);

            Platform.runLater(() -> {
                gameStage.getChildren().add(enemyView);
            });

            lastEnemySpawnTime = currentTime;
            System.out.println("Wall shooter spawned at: " + spawnX + ", " + spawnY);
        }
    }

    private void updateEnemyViews() {
        // Update positions and remove dead enemy views
        spawnedEnemyViews.removeIf(view -> {
            if (!view.getEnemy().isAlive()) {
                Platform.runLater(() -> gameStage.getChildren().remove(view));
                return true;
            }
            view.updatePosition();
            return false;
        });
    }
    @Override
    protected void updateWeakPointsPosition() {
    }

}