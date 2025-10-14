package se233.notcontra.model.Items;

import javafx.scene.paint.Color;

public class TankBuster extends Item {
	
	public TankBuster(int width, int height, int xPos, int yPos) {
		super(width, height, xPos, yPos);
		this.setFill(Color.YELLOW);
	}
}