package se233.notcontra.model.Items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public abstract class Item extends Pane {
	private int width;
	private int height;
	private int xPos, yPos;
	private ImageView sprite;
	
	public Item(int width, int height, int xPos, int yPos, Image img) {
		this.setTranslateX(xPos);
		this.setTranslateY(yPos);
		this.width = width;
		this.height = height;
		this.xPos = xPos;
		this.yPos = yPos;
		this.setWidth(width);
		this.setHeight(height);
		sprite = new ImageView(img);
		this.getChildren().add(sprite);
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