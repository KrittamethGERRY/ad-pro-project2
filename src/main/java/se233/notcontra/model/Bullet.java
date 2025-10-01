package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.Launcher;

public class Bullet extends Pane {
    private int xPosition, yPosition, speedX, speedY;
    private Image bulletImg;
    public Bullet(int xPosition, int yPosition, int speedX, int speedY) {
    	setTranslateX(xPosition);
    	setTranslateY(yPosition);
    	bulletImg = new Image(Launcher.class.getResourceAsStream("assets/FD.png"));
    	ImageView imageView = new ImageView(bulletImg);
    	imageView.setFitHeight(50);
    	imageView.setFitWidth(50);
    	getChildren().add(imageView);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.speedX = speedX;
        this.speedY = speedY;
    }
    
    public void move() {
    	setTranslateX(xPosition);
    	xPosition += speedX;
    }

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}
}