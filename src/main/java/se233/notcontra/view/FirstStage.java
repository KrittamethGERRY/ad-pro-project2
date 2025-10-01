package se233.notcontra.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public class FirstStage extends GameStage {
	
	public FirstStage() {
		this.backgroundIMG = new Image(Launcher.class.getResource("assets/Background.png").toString());
		ImageView background = new ImageView(backgroundIMG);
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		player = new Player(30, 250 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		getChildren().addAll(background, player);
	}

	public Keys getKeys() {
		return this.keys;
	}
	public Player getPlayer() {
		return this.player;
	}
}