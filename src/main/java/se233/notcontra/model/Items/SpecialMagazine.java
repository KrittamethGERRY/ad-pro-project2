 package se233.notcontra.model.Items;

import javafx.scene.image.Image;
import se233.notcontra.Launcher;

public class SpecialMagazine extends Item {
	
	public SpecialMagazine(int width, int height, int xPos, int yPos) {
		super(width, height, xPos, yPos, new Image(Launcher.class.getResourceAsStream("assets/Item/Collectibles/specialMagazine.png")));
	}
}