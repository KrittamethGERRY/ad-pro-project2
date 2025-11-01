package se233.notcontra.view.GameStages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.model.Boss.RDBoss;
import se233.notcontra.model.Enemy.Enemy;
import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Items.SpecialMagazine;
import se233.notcontra.model.Items.TankBuster;
import se233.notcontra.view.Platform;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.controller.SoundController;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.ImageAssets;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;
import se233.notcontra.model.Boss.Boss;

public class ThirdStage extends GameStage {

	public ThirdStage() {
		GameLoop.enemies.clear();
		SoundController.getInstance().stopAllSounds();
		SoundController.getInstance().playThirdStageMusic();
		ImageView scoreBackground = drawScore();
		ImageView livesBackground = drawLives();
		ImageView background = new ImageView(ImageAssets.THIRD_STAGE);
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		platforms = new ArrayList<Platform>();
		player = new Player(30, 300 , KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		scoreBackground.setLayoutX(990);
		scoreLabel.setLayoutX(1100);
		GameLoop.enemies.clear();

		player.respawn();

		Platform groundPlatform = new Platform(1280, 0, 665, true);
		Platform platform1 = new Platform(200, 100, 550, false);
		Platform platform2 = new Platform(250, 880, 550, false);
		Platform platform3 = new Platform(350, 0, 370, false);
		Platform platform4 = new Platform(435, GameStage.WIDTH - 435, 370, false);

		platforms.addAll(List.of(groundPlatform, platform1, platform2, platform3, platform4));

		boss = new RDBoss(510, 55, 128, 128 , this);
		getChildren().addAll(background, scoreBackground, livesBackground, livesLabel, scoreLabel, groundPlatform, platform1, platform2, platform3, platform4, player, boss);
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
		if (item == null && !GameLoop.isPaused) {
			int randX = new Random().nextInt(0, 1280);
			item = new SpecialMagazine(64,64,randX,100);
			this.getChildren().add(item);
		}
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