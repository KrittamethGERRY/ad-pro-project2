package se233.notcontra.view.GameStages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import se233.notcontra.model.Items.SpecialMagazine;
import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Items.TankBuster;
import se233.notcontra.view.Platform;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Keys;
import se233.notcontra.model.PatrolEnemy;
import se233.notcontra.model.Player;
import se233.notcontra.model.Boss.Boss;
import se233.notcontra.model.Boss.JavaBoss;
import se233.notcontra.model.Enums.EnemyType;

public class SecondStage extends GameStage {

	public SecondStage() {
		SoundController.getInstance().stopAllSounds();
		SoundController.getInstance().playSecondStageMusic();
		ImageView scoreBackground = drawScore();
		ImageView livesBackground = drawLives();
		ImageView background = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/secondStage.png")));
		ImageView platformIm = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/platforms.png")));
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		platformIm.setFitWidth(640);
		platformIm.setFitHeight(50);
		platformIm.setLayoutX(0);
		platformIm.setLayoutY(360);
		platforms = new ArrayList<Platform>();
		player = new Player(30, 300 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		player.respawn();
		Platform platform1 = new Platform(640, 0, 375, false);
		Platform groundPlatform = new Platform(1280, 0, 490, true);
		platforms.addAll(List.of(groundPlatform, platform1));
		GameLoop.enemies.clear();

		boss = new JavaBoss(1010, 20,128,256,this);


		bossPhase = false;
		totalMinions = 4;
		PatrolEnemy patrolEnemy1 = new PatrolEnemy(695, 175, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		PatrolEnemy patrolEnemy2 = new PatrolEnemy(700, 200, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		PatrolEnemy patrolEnemy3 = new PatrolEnemy(645, 215, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		PatrolEnemy patrolEnemy4 = new PatrolEnemy(1000, 210, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		
		GameLoop.enemies.addAll(List.of(patrolEnemy1, patrolEnemy2, patrolEnemy3, patrolEnemy4));

		getChildren().addAll(background, patrolEnemy1, patrolEnemy2, patrolEnemy3,patrolEnemy4
				, scoreBackground, livesBackground, livesLabel, scoreLabel
				, platformIm, groundPlatform, platform1, player, boss);

	}
	
	@Override
	public Keys getKeys() {
		return this.keys;
	}
	@Override
	public Player getPlayer() {
		return this.player;
	}
	@Override
	public List<Platform> getPlatforms() {
		return this.platforms;
	}
	
	@Override
	public Item getItem() {
		return this.item;
	}
	
	@Override
	public void spawnItem() {
		boolean itemType = new Random().nextBoolean();
		int randX = new Random().nextInt(0, 900);
		item = itemType ? new SpecialMagazine(64,64,randX,100) : new TankBuster(64,64,randX,100);
		this.getChildren().add(item);
	}
	
	@Override
	public void removeItem() {
		getChildren().remove(item);
		item = null;
	}

	@Override
	public void logging() {
		logger.info("Player spawned at X:{} Y:{}", player.getxPos(), player.getyPos());
		for (Platform platform : platforms) {
			logger.info("Platform spawned at X:{} Y:{} Width:{}", platform.getxPos(), platform.getyPos(), platform.getPaneWidth());
		}
	}
	
	@Override
	public Boss getBoss() { return this.boss; }
	@Override
	public List<Enemy> getEnemies() { return GameLoop.enemies; }
	
	@Override
	public List<Bullet> getBullets() { return GameLoop.bullets; }
}