package se233.notcontra.model.Boss;

import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.view.GameStages.GameStage;


public class JavaBoss extends Boss{

    private final int maxEnemies = 3;
    private int enemyTimer = 0;
    private GameStage gameStage;
    private final Enemy Head;

    public JavaBoss(int xPos, int yPos , int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 7000);
        this.Head = new Enemy(xPos, yPos, 0, Width, Height, 3, 3, 1,"assets/Boss/Boss2/JAVA.png", 10000, EnemyType.WALL);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.gameStage = gameStage;
        getWeakPoints().add(Head);

        getChildren().addAll(Head);
    }



    @Override
    protected void handleAttackingState() {
        spawnEnemy();
    }

    private void spawnEnemy() {

        int aliveCount = 0;
        for (Enemy enemy : GameLoop.enemies) {
            if (enemy.isAlive() && enemy.getType() == EnemyType.FLYING) {
                aliveCount++;
            }
        }

        if (aliveCount < maxEnemies) {

            int spawnX = this.getXPos() - 200;
            int spawnY = this.getYPos() + 30;

            Enemy enemy = new Enemy(spawnX, spawnY, 0, 32, 32, 5, 5, 1, "assets/Enemy/FlyingEnemy.png", 500, EnemyType.FLYING);

            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                getChildren().add(enemy);
            });
            System.out.println("Enemy spawned at: " + spawnX + ", " + spawnY);
            enemyTimer = 500;
        }
    }
}
