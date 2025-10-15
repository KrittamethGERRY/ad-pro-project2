package se233.notcontra.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStage;

import java.util.List;

public class JavaBoss extends Boss{

    private final int maxEnemies = 3;
    private int enemyTimer = 0;
    private GameStage gameStage;
    private final Enemy Head;

    public JavaBoss(int xPos, int yPos , int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 7000);
        this.Head = new Enemy(xPos, yPos, 0, Width, Height, 10000, EnemyType.WALL);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        this.gameStage = gameStage;
        getWeakPoints().add(Head);
        Head.setFill(Color.RED);

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

            Enemy enemy = new Enemy(spawnX, spawnY, 0, 32, 32, 500, EnemyType.FLYING);

            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                getChildren().add(enemy);
            });
            System.out.println("Enemy spawned at: " + spawnX + ", " + spawnY);
            enemyTimer = 500;
        }
    }
}
