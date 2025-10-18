package se233.notcontra.model.Items;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.notcontra.view.Platform;

public abstract class Item extends Pane {
    private int width;
    private int height;
    private int xPos, yPos;
    private ImageView sprite;
    
    // Gravity and falling physics
    private boolean isFalling = true;
    private boolean isOnPlatform = false;
    private int yVelocity = 0;
    private int yAcceleration = 1;
    private int yMaxVelocity = 10;

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

    public void paint() {
        setTranslateX(xPos);
        setTranslateY(yPos);
    }


    public void update(List<Platform> platforms) {
        addForceDown();
        checkPlatformCollision(platforms);
        paint();
    }


    private void addForceDown() {
        if (isFalling) {
            yVelocity = Math.min(yVelocity + yAcceleration, yMaxVelocity);
            yPos += yVelocity;
        }
    }


    public void checkPlatformCollision(List<Platform> platforms) {
        boolean onAPlatformThisFrame = false;

        for (Platform platform : platforms) {
            boolean isCollidedXAxis = (xPos + width) > platform.getxPos() && 
                                      xPos < platform.getxPos() + platform.getPaneWidth();
            boolean isLanding = isFalling && 
                               (yPos + height) <= platform.getyPos() && 
                               (yPos + height + yVelocity) >= platform.getyPos();
            boolean isStanding = Math.abs((yPos + height) - platform.getyPos()) < 1;

            if (isCollidedXAxis && (isLanding || (isOnPlatform && isStanding))) {
                if (isLanding) {
                    yPos = platform.getyPos() - height;
                    yVelocity = 0;
                    isFalling = false;
                }
                isOnPlatform = true;
                onAPlatformThisFrame = true;
                break;
            }
        }

        if (!onAPlatformThisFrame) {
            isFalling = true;
            isOnPlatform = false;
        }
    }

    public boolean isOutOfBounds(int screenWidth, int screenHeight) {
        return yPos > screenHeight || xPos < -width || xPos > screenWidth;
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
    
    public boolean isFalling() {
        return isFalling;
    }
    
    public boolean isOnPlatform() {
        return isOnPlatform;
    }
}