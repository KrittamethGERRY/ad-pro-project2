package se233.notcontra.view.GameStages;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.model.Boss.RDBoss;
import se233.notcontra.model.Items.Item;
import se233.notcontra.view.Platform;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;
import se233.notcontra.model.Boss.Boss;

public class ThirdStage extends GameStage {

	public ThirdStage() {
		ImageView scoreBackground = drawScore();
		ImageView livesBackground = drawLives();
		ImageView background = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/thirdStage.png")));
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		platforms = new ArrayList<Platform>();
		player = new Player(30, 300 , KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);

		player.respawn();

		Platform groundPlatform = new Platform(1280, 0, 490, false);
		platforms.addAll(List.of(groundPlatform));

		boss = new RDBoss(1280/2, 100, 128, 128 , this);
		getChildren().addAll(background, scoreBackground, livesBackground, livesLabel, scoreLabel, groundPlatform, player, boss);
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