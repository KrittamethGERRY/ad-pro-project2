package se233.notcontra.view.GameStages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Items.SpecialMagazine;
import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Items.TankBuster;
import se233.notcontra.view.Platform;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Keys;
import se233.notcontra.model.PatrolEnemy;
import se233.notcontra.model.Player;
import se233.notcontra.model.Boss.Boss;
import se233.notcontra.model.Boss.WallBoss;
import se233.notcontra.model.Enums.EnemyType;

public class FirstStage extends GameStage {
	
	public FirstStage() {
		ImageView scoreBackground = drawScore();
		ImageView livesBackground = drawLives();
		platforms = new ArrayList<Platform>();
		this.backgroundIMG = new Image(Launcher.class.getResource("assets/Backgrounds/firstStage.png").toString());
		ImageView background = new ImageView(backgroundIMG);
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		player = new Player(30, 0 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		player.respawn();

		boss = new WallBoss(660, 350, 500, 750, this);
		Platform platform1 = new Platform(165, 0, 300, false);
		Platform platform2 = new Platform(160, 175, 390, false);
		Platform platform3 = new Platform(150, 335, 475, false);
		Platform platform4 = new Platform(165, 0, 450, false);
		Platform groundPlatform = new Platform(1280, 0, 585, true);
		spawnItem();
		platforms.add(platform1);
		platforms.add(platform2);
		platforms.add(platform3);
		platforms.add(platform4);
		platforms.add(groundPlatform);
		
		// spawn minions before boss
		bossPhase = false;
		totalMinions = 4;
		PatrolEnemy patrolEnemy1 = new PatrolEnemy(695, 175, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		PatrolEnemy patrolEnemy2 = new PatrolEnemy(700, 200, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		PatrolEnemy patrolEnemy3 = new PatrolEnemy(645, 215, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		PatrolEnemy patrolEnemy4 = new PatrolEnemy(1000, 210, 1, 64, 64, 2, 2, 1, "assets/Enemy/Patrol_E.png", 500, EnemyType.PATROL);
		
		GameLoop.enemies.addAll(List.of(patrolEnemy1, patrolEnemy2, patrolEnemy3, patrolEnemy4));
		getChildren().addAll(background, boss, scoreBackground, patrolEnemy1, patrolEnemy2, patrolEnemy3, patrolEnemy4, livesBackground
				, livesLabel, scoreLabel, platform1, platform2, platform3, platform4, groundPlatform, item, player);
		player.respawn();
		logging();
	}
	
	@Override
	public Item getItem() {
		return this.item;
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
	public void spawnItem() {
		boolean itemType = new Random().nextBoolean();
		item = itemType ? new SpecialMagazine(64,64,150,280) : new TankBuster(64,64,150,280);
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
		logger.info("Item spawned at X:{} Y:{}", item.getXPos(), item.getYPos());
	}
	@Override
	public Boss getBoss() { return this.boss; }
	@Override
	public List<Enemy> getEnemies() { return GameLoop.enemies; }
	
	@Override
	public List<Bullet> getBullets() { return GameLoop.bullets; }
}