package se233.notcontra.model.Boss;

import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Enums.EnemyState;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.view.GameStages.GameStage;


public class JavaBoss extends Boss{

    private final int maxEnemies = 3;
    private int enemyTimer = 0;
    private GameStage gameStage;
    private final Enemy Head;

    public JavaBoss(int xPos, int yPos , int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 7000);
        this.Head = new Enemy(xPos, yPos, 0, Width, Height, 1, 1, 1,"assets/Boss/Boss2/JAVA.png", 7000, EnemyType.WALL);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
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
            Head.setState(EnemyState.ATTACKING);
        }
        Head.setState(EnemyState.IDLE);
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

            // Create wall shooter (stands still and shoots)
            Enemy enemy = new Enemy(spawnX, spawnY, 2, 64, 82, 5, 5, 1,"assets/Enemy/FlyingEnemy.png", 50, EnemyType.FLYING);
            // NOTE: Get children's global position do not touch!!!!
            //System.out.print("Enemy Bound: " + getLocalToParentTransform());
            GameLoop.enemies.add(enemy);
            javafx.application.Platform.runLater(() -> {
                this.getChildren().add(enemy);
            });

            enemyTimer = 80;

            if (aliveCount == maxEnemies) {
                enemyTimer = 500;
            }
        }
    }
}
