package se233.notcontra.model.Boss;

import javafx.scene.image.Image;
import se233.notcontra.Launcher;
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
    private Enemy lasteyesShoot = null;


    public RDBoss(int xPos, int yPos, int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 25000);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        Rdhead = new Enemy(-130, -50, 0, Width, Height, 1, 1, 1,"assets/Boss/Boss3/boss3_head.png", this.getMaxHealth(), EnemyType.WALL);
        Rdlefthand = new Enemy( -210, -45, 0, Width, Height, 2, 1, 1,"assets/Boss/Boss3/boss3_left_hand.png", 7000, EnemyType.RDHAND);
        Rdrighthand = new Enemy( +100, -45, 0, Width, Height, 2, 1, 1,"assets/Boss/Boss3/boss3_right_hand.png", 7000, EnemyType.RDHAND);
        RdleftEye = new Enemy(-45, -2, 0, 32,32, 1, 1 ,1 ,  "assets/Boss/Boss3/boss3_left_eye.png", 2500,EnemyType.RDEYES);
        RdrightEye = new Enemy(-22, -2, 0, 32,32, 1, 1 ,1 ,  "assets/Boss/Boss3/boss3_right_eye.png", 2500,EnemyType.RDEYES);
        this.gameStage = gameStage;

        Rdhead.getSprite().setFitHeight(192);
        Rdhead.getSprite().setFitWidth(192);
        Rdhead.getSprite().setLayoutX(Rdhead.getXPos());
        Rdhead.getSprite().setLayoutY(Rdhead.getYPos());
        Rdlefthand.getSprite().setFitHeight(128);
        Rdlefthand.getSprite().setFitWidth(128);
        Rdlefthand.getSprite().setLayoutX(Rdlefthand.getXPos());
        Rdlefthand.getSprite().setLayoutY(Rdlefthand.getYPos());
        Rdrighthand.getSprite().setFitHeight(128);
        Rdrighthand.getSprite().setFitWidth(128);
        Rdrighthand.getSprite().setLayoutX(Rdrighthand.getXPos());
        Rdrighthand.getSprite().setLayoutY(Rdrighthand.getYPos());
        RdleftEye.getSprite().setLayoutX(RdleftEye.getXPos());
        RdleftEye.getSprite().setLayoutY(RdleftEye.getYPos());
        RdrightEye.getSprite().setLayoutX(RdrightEye.getXPos());
        RdrightEye.getSprite().setLayoutY(RdrightEye.getYPos());

        GameLoop.enemies.addAll(List.of(Rdlefthand, Rdrighthand, RdleftEye, RdrightEye));

        getWeakPoints().add(Rdhead);
        javafx.application.Platform.runLater(() -> {
            this.getChildren().addAll(Rdhead ,Rdlefthand, Rdrighthand, RdleftEye, RdrightEye);
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

        if (RdleftEye.isAlive() && !RdrightEye.isAlive()) {
            RdleftEye.shootAtPlayer(gameStage.getPlayer());
        }
        else if (!RdleftEye.isAlive() && RdrightEye.isAlive()) {
            RdrightEye.shootAtPlayer(gameStage.getPlayer());
        }
        else if (RdleftEye.isAlive() && RdrightEye.isAlive()) {
            if (lasteyesShoot == null || lasteyesShoot == Rdrighthand) {
                RdleftEye.shootAtPlayer(gameStage.getPlayer());
                lasteyesShoot = RdleftEye;
            } else {
                RdrightEye.shootAtPlayer(gameStage.getPlayer());
                lasteyesShoot = RdrightEye;
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
                spawnX = ((int) Hand.getXPos())-180;
                spawnY = ((int) Hand.getYPos())+50;
            }else {
                spawnX = ((int) Hand.getXPos()) + 180;
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
