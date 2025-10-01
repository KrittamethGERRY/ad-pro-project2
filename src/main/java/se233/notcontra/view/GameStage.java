package se233.notcontra.view;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public abstract class GameStage extends Pane {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final int GROUND = 360;
	
	protected Image backgroundIMG;
	protected Player player;
	protected Keys keys;
	
	public GameStage() {
		this.keys = new Keys();
	}
	
	public abstract Keys getKeys();
	public abstract Player getPlayer();
}