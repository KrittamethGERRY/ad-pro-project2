package se233.notcontra.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;

public class Platform extends Rectangle {
	private static final Logger logger = LogManager.getLogger(Platform.class);
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
		this.platformImg = new Image(Launcher.class.getResourceAsStream("assets/FD.png"));
		this.isGround = isGround;
		setFill(new ImagePattern(platformImg, 0, 0, width, PLATFORM_HEIGHT, false));
		logger.info("{} spawned at {} {}", Platform.class.getName(), xPos, yPos);
	}
	
	public int getPaneWidth() { return this.width; }
	public int getXPosition() { return this.xPos; }
	public int getYPosition() { return this.yPos; }
	public boolean getIsGround() { return this.isGround; }
}