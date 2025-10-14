package se233.notcontra.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.*;

public class FirstStage extends GameStage {
	
	public FirstStage() {
		this.backgroundIMG = new Image(Launcher.class.getResource("assets/Background.png").toString());
		ImageView background = new ImageView(backgroundIMG);
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		player = new Player(30, 250 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		this.wallboss = new Wallboss(500, 100, this);
		getChildren().addAll(background, player, this.wallboss);

		Enemy Wall_shooter = new Enemy(500, 50, 2, 30, 30, EnemyType.WALL_SHOOTER);
		GameLoop.enemies.add(Wall_shooter);
		EnemyView Wall_shooterView = new EnemyView(Wall_shooter);
		getChildren().add(Wall_shooterView);
	}

	public Keys getKeys() {
		return this.keys;
	}
	public Player getPlayer() {
		return this.player;
	}
	public Wallboss getBoss() {return this.wallboss; }
}