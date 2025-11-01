package se233.notcontra.view;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;

public class Platform extends Rectangle {
	private static final int PLATFORM_HEIGHT = 5;
	
	private int width;
	private int xPos;
	private int yPos;
	private Image platformImg;
	private boolean isGround;
	
	public Platform(int width, int xPos, int yPos, boolean isGround) {
		setTranslateX(xPos);
		setTranslateY(yPos);
		this.width = width;
		this.xPos = xPos;
		this.yPos = yPos;
		this.isGround = isGround;
	}
	
	public int getPaneWidth() { return this.width; }
	public int getxPos() { return this.xPos; }
	public int getyPos() { return this.yPos; }
	public boolean getIsGround() { return this.isGround; }
}