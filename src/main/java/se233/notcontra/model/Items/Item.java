package se233.notcontra.model.Items;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.model.Player;

public abstract class Item extends Rectangle {
	private int width;
	private int height;
	private int xPos, yPos;
	
	public Item(int width, int height, int xPos, int yPos) {
		this.setTranslateX(xPos);
		this.setTranslateY(yPos);
		this.width = width;
		this.height = height;
		this.xPos = xPos;
		this.yPos = yPos;
		this.setWidth(width);
		this.setHeight(height);
	}

	public int getPaneWidth() {
		return width;
	}

	public int getPaneHeight() {
		return height;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}	
}