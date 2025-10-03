package se233.notcontra.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;

public class Platform extends Pane {
	private static final Logger logger = LogManager.getLogger(Platform.class);
	public static final String tag = "";
	
	private int width;
	private int xPos;
	private int yPos;
	private Image platformImg;
	
	public Platform(int width, int xPos, int yPos) {
		setTranslateX(xPos);
		setTranslateY(yPos);
		this.width = width;
		this.xPos = xPos;
		this.yPos = yPos;
		this.platformImg = new Image(Launcher.class.getResourceAsStream("assets/FD.png"));
		ImageView imageView = new ImageView(platformImg);
		imageView.setFitHeight(5);
		imageView.setFitWidth(width);
		this.getChildren().add(imageView);
		logger.info("{} spawned at {} {}", Platform.class.getName(), xPos, yPos);		
	}
	
	public int getPaneWidth() { return this.width; }
	public int getXPosition() { return this.xPos; }
	public int getYPosition() { return this.yPos; }
}