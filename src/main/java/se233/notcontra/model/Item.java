package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;

public class Item extends Pane {
	private int width;
	private int height;
	private int xPos, yPos;
	private ImageView sprite;
	
	public Item(int width, int height, int xPos, int yPos) {
		setTranslateX(xPos);
		setTranslateY(yPos);
		this.width = width;
		this.height = height;
		this.xPos = xPos;
		this.yPos = yPos;
		sprite = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/FD.png")));
		sprite.setFitHeight(height);
		sprite.setFitWidth(width);
		getChildren().add(sprite);
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