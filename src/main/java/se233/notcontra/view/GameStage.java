package se233.notcontra.view;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import se233.notcontra.model.Item;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public abstract class GameStage extends Pane {
	protected List<Platform> platforms;
	protected Label scoreLabel;
	protected Item item;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int GROUND = HEIGHT - 200;
	
	protected Image backgroundIMG;
	protected Player player;
	protected Keys keys;
	
	public GameStage() {
		this.keys = new Keys();
	}
	
	public abstract Keys getKeys();
	public abstract Player getPlayer();
	public abstract List<Platform> getPlatforms();
	public abstract Item getItem();
	
	public abstract void removeItem();
}