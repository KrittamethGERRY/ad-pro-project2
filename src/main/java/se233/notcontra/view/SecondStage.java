package se233.notcontra.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import se233.notcontra.model.Items.HellfireMagazine;
import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Items.TankBuster;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Boss;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public class SecondStage extends GameStage {

	public SecondStage() {
		ImageView scoreBackground = drawScore();
		ImageView livesBackground = drawLives();
		ImageView background = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/secondStage.png")));
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		platforms = new ArrayList<Platform>();
		player = new Player(30, 300 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		player.respawn();
		Platform groundPlatform = new Platform(1280, 0, 490, true);
		spawnItem();
		platforms.add(groundPlatform);

		getChildren().addAll(background, scoreBackground, livesBackground, livesLabel, scoreLabel, groundPlatform, item, player);

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
		item = itemType ? new HellfireMagazine(64,64,150,280) : new TankBuster(64,64,150,280);
	}
	
	@Override
	public void removeItem() {
		getChildren().remove(item);
		item = null;
	}
	
	@Override
	public Boss getBoss() { return boss; }
	
	@Override
	public List<Enemy> getEnemies() { return GameLoop.enemies; }
	
	@Override
	public List<Bullet> getBullets() { return GameLoop.bullets; }
	
	@Override
	public void logging() {
		logger.info("Player spawned at X:{} Y:{}", player.getXPosition(), player.getYPosition());
		for (Platform platform : platforms) {
			logger.info("Platform spawned at X:{} Y:{} Width:{}", platform.getXPosition(), platform.getYPosition(), platform.getPaneWidth());
		}
		logger.info("Item spawned at X:{} Y:{}", item.getXPos(), item.getYPos());
	}
}