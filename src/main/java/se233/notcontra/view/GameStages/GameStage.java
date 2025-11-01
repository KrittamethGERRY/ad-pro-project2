package se233.notcontra.view.GameStages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.model.Items.Item;
import se233.notcontra.view.PauseMenu;
import se233.notcontra.view.Platform;
import se233.notcontra.Launcher;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;
import se233.notcontra.model.Boss.Boss;
import se233.notcontra.model.Enemy.Enemy;

public abstract class GameStage extends Pane {
	protected Logger logger = LogManager.getLogger(GameStage.class);
	protected List<Platform> platforms;
	protected Label scoreLabel;
	protected Label livesLabel;
	protected Item item;
	protected PauseMenu pauseMenu;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static int totalMinions;
	public static boolean bossPhase = false;
	
	protected Image backgroundIMG;
	protected Player player;
	protected Keys keys;
	protected Boss boss;
	
	public GameStage() {
		this.keys = new Keys();
	}
	
	public ImageView drawScore() {
		scoreLabel = new Label("Score: 000000");
		scoreLabel.setStyle("-fx-font-weight: bold;"
				+ "-fx-font-size: 2em;");
		ImageView scoreBackground = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Paper.png")));
		scoreBackground.setFitWidth(300);
		scoreBackground.setFitHeight(60);
		scoreBackground.setLayoutX(25 + 300);
		scoreBackground.setLayoutY(25);
		scoreLabel.setLayoutX(385);
		scoreLabel.setLayoutY(40);
		
		return scoreBackground;
	}
	
	public ImageView drawLives() {
		livesLabel = new Label("Lives: 3");
		livesLabel.setStyle("-fx-font-weight: bold;"
				+ "-fx-font-size: 2em;"
				+ "-fx-fill: red;");
		ImageView livesBackground = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/Paper.png")));
		livesBackground.setFitWidth(150);
		livesBackground.setFitHeight(60);
		livesBackground.setLayoutX(25);
		livesBackground.setLayoutY(25);
		livesLabel.setLayoutX(livesBackground.getFitWidth() - 95);
		livesLabel.setLayoutY(livesBackground.getFitHeight()  - 20);
		
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