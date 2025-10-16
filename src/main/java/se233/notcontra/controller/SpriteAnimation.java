package se233.notcontra.controller;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;


public class SpriteAnimation extends AnimationTimer {

    private final ImageView imageView;
    private final int totalFrames;
    private final int columns;

    private final int frameWidth;
    private final int frameHeight;

    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private final long frameDuration;


    public SpriteAnimation(ImageView imageView, int durationMs, int totalFrames, int columns, int frameWidth, int frameHeight) {
        this.imageView = imageView;
        this.totalFrames = totalFrames;
        this.columns = columns;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        // Calculate how long each frame should be displayed
        this.frameDuration = (long) (durationMs * 1_000_000.0 / totalFrames);
    }

    @Override
    public void handle(long now) {
        // Check if enough time has passed to switch to the next frame
        if (now - lastFrameTime >= frameDuration) {
            // Move to the next frame, looping back to 0 if at the end
            currentFrame = (currentFrame + 1) % totalFrames;

            // Calculate the row and column of the current frame in the spritesheet
            int col = currentFrame % columns;
            int row = currentFrame / columns;

            // Calculate the x and y coordinates of the frame's top-left corner
            int frameX = col * frameWidth;
            int frameY = row * frameHeight;

            // Update the ImageView's viewport to display the new frame
            imageView.setViewport(new Rectangle2D(frameX, frameY, frameWidth, frameHeight));

            // Update the time of the last frame change
            lastFrameTime = now;
        }
    }
}