package se233.notcontra.model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import se233.notcontra.controller.SpriteAnimation;

public class Effect extends Pane {
    private SpriteAnimation sprite;
    private int currentFrame = 0;
    private int totalFrames;

    public Effect(Image spriteSheet, int count, int columns, int rows, double x, double y) {
        this.totalFrames = count;

        int width = (int) spriteSheet.getWidth() / columns;
        int height = (int) spriteSheet.getHeight() / rows;

        this.sprite = new SpriteAnimation(spriteSheet, count, columns, rows, 0, 0, 32, 32);

        setTranslateX(x);
        setTranslateY(y);
        sprite.setFitWidth(64);
        sprite.setFitHeight(64);
        getChildren().add(this.sprite);
    }

    public void tick() {
        this.sprite.tick();
        this.currentFrame++;
    }

    public boolean isFinished() {
        return this.currentFrame >= this.totalFrames;
    }
}