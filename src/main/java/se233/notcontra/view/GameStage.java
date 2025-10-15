package se233.notcontra.view;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import se233.notcontra.model.Items.Item;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Boss;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Enemy;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public abstract class GameStage extends Pane {
	protected Logger logger = LogManager.getLogger(GameStage.class);
	protected List<Platform> platforms;
	protected Label scoreLabel;
	protected Label livesLabel;
	protected Item item;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	protected Image backgroundIMG;
	protected Player player;
	protected Keys keys;
	protected Boss boss;
	
	public GameStage() {
		this.keys = new Keys();
	}
	
	public ImageView drawScore() {
		scoreLabel = new Label(String.format("%06d", 0));
		ImageView scoreBackground = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Paper.png")));
		scoreBackground.setFitWidth(150);
		scoreBackground.setFitHeight(60);
		scoreBackground.setLayoutX(GameStage.WIDTH - 175);
		scoreBackground.setLayoutY(25);
		scoreLabel.setLayoutX(GameStage.WIDTH - 135);
		scoreLabel.setLayoutY(50);
		
		return scoreBackground;
	}
	
	public void updateScore(int score) {
		scoreLabel.setText(String.format("%06d", score));
	}
	
	public ImageView drawLives() {
		livesLabel = new Label("Lives: 3");
		ImageView livesBackground = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Paper.png")));
		livesBackground.setFitWidth(150);
		livesBackground.setFitHeight(60);
		livesBackground.setLayoutX(25);
		livesBackground.setLayoutY(25);
		livesLabel.setLayoutX(65);
		livesLabel.setLayoutY(50);
		
		return livesBackground;
	}
	
	public abstract Keys getKeys();
	public abstract Player getPlayer();
	public abstract List<Platform> getPlatforms();
	public abstract Item getItem();
	
	public abstract void logging();
	public abstract void spawnItem();
	public abstract void removeItem();
	public abstract Boss getBoss();
	public Label getScoreLabel() { return scoreLabel;}
	public Label getLivesLabel() { return livesLabel;}
	public abstract List<Enemy> getEnemies();
	public abstract List<Bullet> getBullets();
}