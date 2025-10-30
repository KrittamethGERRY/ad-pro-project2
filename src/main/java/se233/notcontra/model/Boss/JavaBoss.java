package se233.notcontra.model.Boss;

import javafx.scene.image.Image;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.ImageAssets;
import se233.notcontra.model.Enums.EnemyState;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.view.GameStages.GameStage;


public class JavaBoss extends Boss{

    private final int maxEnemies = 3;
    private int enemyTimer = 0;
    private GameStage gameStage;
    private final Enemy Head;
    private int spawnAnimationTimer = 0;

    public JavaBoss(int xPos, int yPos , int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 20000);
    	getWeakPoints().clear();
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        Head = new Enemy(xPos, yPos, 0, Width, Height, Width, Height, 1, 1, 1, ImageAssets.JAVA_IDLE, this.getMaxHealth(), EnemyType.JAVAHEAD);
        Head.getSprite().setFitHeight(Height);
        Head.getSprite().setFitWidth(Width);
        this.gameStage = gameStage;

        getWeakPoints().add(Head);
        GameLoop.enemies.add(Head);
        javafx.application.Platform.runLater(() -> {
            this.getChildren().addAll(Head);
        });
    }

    @Override
    protected void handleAttackingState() {
        if (Head.isAlive()) {
            spawnEnemy();
            if (getSpawnAnimationTimer() > 0) {
                updateSpawnAnimation();
                Head.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss2/JAVA_SpawnF.png")), 1, 1, 1);
            }  else {
                Head.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss2/JAVA_IDEL.png")), 1, 1, 1);
            }
        }

    }

    private void spawnEnemy() {

        if (enemyTimer > 0) {
            enemyTimer--;
            return;
        }
        int aliveCount = 0;
        for (Enemy enemy : GameLoop.enemies) {
            if (enemy.isAlive() && enemy.getType() == EnemyType.FLYING) {
                aliveCount++;
            }
        }

        if (aliveCount < maxEnemies) {
            int spawnX = -50;
            int spawnY = +100;


            Enemy enemy = new Enemy(spawnX, spawnY, 3, 64, 64, 64, 64, 4, 4, 1, ImageAssets.FLYING_ENEMY, 50, EnemyType.FLYING);
            // NOTE: Get children's global position do not touch!!!!
            //System.out.print("Enemy Bound: " + getLocalToParentTransform());
            GameLoop.enemies.add(enemy);
            SoundController.getInstance().playJavaAttackSound();
            javafx.application.Platform.runLater(() -> {
                this.getChildren().add(enemy);
            });

            enemyTimer = 80;
            spawnAnimationTimer = 20;

            if (aliveCount == maxEnemies) {
                enemyTimer = 500;
            }
        }
    }

    public int getSpawnAnimationTimer() {
        return spawnAnimationTimer;
    }

    public void updateSpawnAnimation() {
        if (spawnAnimationTimer > 0) {
            spawnAnimationTimer--;
        }
    }
}
