package se233.notcontra.model.Items;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import se233.notcontra.Launcher;

public class TankBuster extends Item {
	
	public TankBuster(int width, int height, int xPos, int yPos) {
		super(width, height, xPos, yPos, new Image(Launcher.class.getResourceAsStream("assets/Item/Collectibles/tankbuster.png")));
	}
}