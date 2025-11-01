package se233.notcontra.model.Boss;

import javafx.scene.image.Image;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.model.ImageAssets;
import se233.notcontra.model.Enemy.Enemy;
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
    private static Enemy RdleftEye;
    private static Enemy RdrightEye;
    public static int totalEYES = 2;

    private Enemy lastHandspawn = null;
    private Enemy lasteyesShoot = null;

    private int spawnAnimationTimer = 0;


    public RDBoss(int xPos, int yPos, int Height, int Width, GameStage gameStage) {
        super(xPos, yPos, Width, Height, 25000);
        getWeakPoints().clear();
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
        Rdhead = new Enemy(xPos, yPos, 0, Width, Height, 128, 128, 1, 1, 1, ImageAssets.RD_HEAD, 25000, EnemyType.RDHEAD);
        Rdlefthand = new Enemy( -300, -38, 0, 164, 128, 164, 128, 1, 1, 1, ImageAssets.RD_LEFTHAND, 7000, EnemyType.RDHAND);
        Rdrighthand = new Enemy( +330, -38, 0, 164, 128, 164, 128, 1, 1, 1, ImageAssets.RD_RIGHTHAND, 7000, EnemyType.RDHAND);
        RdleftEye = new Enemy(40, 50, 0, 32,32, 32, 32, 1, 1 ,1 ,  ImageAssets.RD_LEFTEYE, 2500,EnemyType.RDEYES);
        RdrightEye = new Enemy(85, 50, 0, 32,32, 32, 32, 1, 1 ,1 ,  ImageAssets.RD_RIGHTEYE, 2500,EnemyType.RDEYES);
        this.gameStage = gameStage;

        Rdhead.getSprite().setFitHeight(192);
        Rdhead.getSprite().setFitWidth(192);

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
            SoundController.getInstance().playJavaAttackSound();
            if (spawnAnimationTimer > 0){
                updateSpawnAnimation();
                Rdlefthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_leftHand_Spawn.png")),1,1,1);
            }else {
                Rdlefthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_leftHand_IDEL.png")),1,1,1);
            }
        }
        else if (!Rdlefthand.isAlive() && Rdrighthand.isAlive()) {
            spawnEnemy(Rdrighthand);
            SoundController.getInstance().playJavaAttackSound();
            if (spawnAnimationTimer > 0) {
                updateSpawnAnimation();
                Rdrighthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_rightHand_Spawn.png")),1,1,1);
            }else {
                Rdrighthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_rightHand_IDEL.png")),1,1,1);
            }
        }
        else if (Rdlefthand.isAlive() && Rdrighthand.isAlive()) {
            if (lastHandspawn == null || lastHandspawn == Rdrighthand) {
                spawnEnemy(Rdlefthand);
                lastHandspawn = Rdlefthand;
                if (spawnAnimationTimer > 0) {
                    updateSpawnAnimation();
                    Rdlefthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_leftHand_Spawn.png")),1,1,1);
                }else {
                    Rdlefthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_leftHand_IDEL.png")),1,1,1);
                }
            } else {
                spawnEnemy(Rdrighthand);
                lastHandspawn = Rdrighthand;
                if (spawnAnimationTimer > 0) {
                    updateSpawnAnimation();
                    Rdrighthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_rightHand_Spawn.png")),1,1,1);
                }else {
                    Rdrighthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_rightHand_IDEL.png")),1,1,1);
                }
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

        if(!Rdlefthand.isAlive()) {
            Rdlefthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_leftHand_DEAD.png")), 2,2,1);
        }

        if(!Rdrighthand.isAlive()) {
            Rdrighthand.getSprite().changeSpriteSheet(new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_rightHand_DEAD.png")), 2,2,1);
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
                spawnX = ((int) Hand.getXPos());
                spawnY = ((int) Hand.getYPos())+50;
            }else {
                spawnX = ((int) Hand.getXPos()) + 50;
                spawnY = ((int) Hand.getYPos()) + 50;
            }

            Enemy enemy = new Enemy(spawnX, spawnY, 2, 64, 82, 64, 82, 4, 4, 1, ImageAssets.FLYING_ENEMY, 50, EnemyType.FLYING);

            // NOTE: Get children's global position do not touch!!!!
            //System.out.print("Enemy Bound: " + getLocalToParentTransform());
            GameLoop.enemies.add(enemy);
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

    public Enemy getRdrightEye() {
        return RdrightEye;
    }

    public Enemy getRdleftEye() {
        return RdleftEye;
    }

    public Enemy getRdlefthand() {
        return Rdlefthand;
    }

    public  Enemy getRdrighthand() {
        return Rdrighthand;
    }

    public int getSpawnAnimationTimer() {
        return spawnAnimationTimer;
    }

    public void updateSpawnAnimation() {
        if (spawnAnimationTimer > 0) {
            spawnAnimationTimer--;
        }
    }

    public static List<Enemy> getEYES() { return List.of(RdleftEye, RdrightEye); }
}
