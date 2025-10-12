package se233.notcontra.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import se233.notcontra.Launcher;
import se233.notcontra.model.Items.HellfireMagazine;
import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public class FirstStage extends GameStage {
	
	public FirstStage() {
		platforms = new ArrayList<Platform>();
		this.backgroundIMG = new Image(Launcher.class.getResource("assets/firstStage.png").toString());
		ImageView background = new ImageView(backgroundIMG);
		background.setFitWidth(WIDTH);
		background.setFitHeight(HEIGHT);
		player = new Player(30, 600 ,KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
		Platform platform1 = new Platform(165, 0, 300, false);
		Platform platform2 = new Platform(160, 175, 390, false);
		Platform platform3 = new Platform(150, 335, 475, false);
		Platform platform4 = new Platform(165, 0, 450, false);
		Platform groundPlatform = new Platform(1280, 0, 585, true);
		item = new HellfireMagazine(64, 64, 100, 280);
		platforms.add(platform1);
		platforms.add(platform2);
		platforms.add(platform3);
		platforms.add(platform4);
		platforms.add(groundPlatform);
		Button respawnBtn = new Button("Respawn");
		respawnBtn.setOnAction(e -> {
			player.respawn();
		});
		getChildren().addAll(background, platform1, platform2, platform3, platform4, groundPlatform, item, player, respawnBtn);
		player.respawn();
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