package se233.notcontra.model.Boss;

import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Enums.EnemyType;
import se233.notcontra.view.GameStages.GameStage;

import java.util.List;

public class RDBoss extends Boss{
    private final int maxEnemies = 2;
    private int enemyTimer = 0;
    private GameStage gameStage;
    private Enemy Rdhead;
    private Enemy Rdlefthand;
    private Enemy Rdrighthand;
    private Enemy RdleftEye;
    private Enemy RdrightEye;

    private Enemy lastHandspawn = null;


    public RDBoss(int xPos, int yPos, int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 10000);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        Rdhead = new Enemy(0, 0, 0, Width, Height, 1, 1, 1,"assets/Boss/Boss3/boss3_head.png", this.getMaxHealth() / 2, EnemyType.Head);
        Rdlefthand = new Enemy( -250, 0, 0, Width, Height, 2, 1, 1,"assets/Boss/Boss3/boss3_left_hand.png", this.getMaxHealth() / 4, EnemyType.Head);
        Rdrighthand = new Enemy( +200, 0, 0, Width, Height, 2, 1, 1,"assets/Boss/Boss3/boss3_right_hand.png", this.getMaxHealth() / 4, EnemyType.Head);
        RdleftEye = new Enemy(+28, 30, 0, 32,32, 1, 1 ,1 ,  "assets/Boss/Boss3/boss3_left_eye.png", this.getMaxHealth() / 5,EnemyType.WALL_SHOOTER);
        RdrightEye = new Enemy(+57, 30, 0, 32,32, 1, 1 ,1 ,  "assets/Boss/Boss3/boss3_right_eye.png", this.getMaxHealth() / 5,EnemyType.WALL_SHOOTER);
        this.gameStage = gameStage;

        GameLoop.enemies.addAll(List.of(Rdlefthand, Rdrighthand, RdleftEye, RdrightEye));

        getWeakPoints().add(Rdhead);
        javafx.application.Platform.runLater(() -> {
            this.getChildren().addAll( Rdlefthand, Rdrighthand, RdleftEye, RdrightEye);
        });

    }

    @Override
    protected void handleAttackingState() {
        if (Rdlefthand.isAlive() && !Rdrighthand.isAlive()) {
            spawnEnemy(Rdlefthand);
        }
        else if (!Rdlefthand.isAlive() && Rdrighthand.isAlive()) {
            spawnEnemy(Rdrighthand);
        }
        else if (Rdlefthand.isAlive() && Rdrighthand.isAlive()) {
            if (lastHandspawn == null || lastHandspawn == Rdrighthand) {
                spawnEnemy(Rdlefthand);
                lastHandspawn = Rdlefthand;
            } else {
                spawnEnemy(Rdrighthand);
                lastHandspawn = Rdrighthand;
            }
        }

        if (!RdrightEye.isAlive() && !RdleftEye.isAlive()) {
            GameLoop.enemies.add(Rdhead);
        }
    }

    private void spawnEnemy(Enemy Hand) {
        int spawnX;
        int spawnY;

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
            if(Hand ==  Rdlefthand) {
                spawnX = ((int) Hand.getXPos()) + 30;
                spawnY = ((int) Hand.getYPos()) + 50;
            }else {
                spawnX = ((int) Hand.getXPos()) + 47;
                spawnY = ((int) Hand.getYPos()) + 50;
            }

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
