package se233.notcontra.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.model.Item;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public class FirstStage extends GameStage {
	
	public FirstStage() {
		platforms = new ArrayList<Platform>();
		this.backgroundIMG = new Image(Launcher.class.getResource("assets/Background.png").toString());
		ImageView background = new ImageView(backgroundIMG);
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		player = new Player(30, 250 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		Platform platform1 = new Platform(100, 300, 300);
		Platform platform2 = new Platform(250, 300, 200);
		item = new Item(64, 64, 100, 280);
		platforms.add(platform1);
		platforms.add(platform2);
		getChildren().addAll(background, platform1, platform2, item, player);
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
	public void removeItem() {
		getChildren().remove(item);
		item = null;
	}
}