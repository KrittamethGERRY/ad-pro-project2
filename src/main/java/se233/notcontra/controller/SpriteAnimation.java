package se233.notcontra.controller;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SpriteAnimation extends ImageView {

    int count, columns, rows, offsetX, offsetY, width, height, curIndex, curColumnIndex =0 , curRowIndex = 0;
    Image currentImage;
    private long lastFrameTime = 0;
    private long frameDuration = 200_000_000;
    public SpriteAnimation(Image image, int count, int columns, int rows, int offsetX, int offsetY, int width, int height) {
        this.setImage(image);
        this.currentImage = image;
        this.count = count;
        this.columns = columns;
        this.rows = rows;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        this.lastFrameTime = System.nanoTime();
    }
    public void tick() {
        long now = System.nanoTime();
        if (now - lastFrameTime >= frameDuration) {
            curIndex = (curIndex + 1) % count;
            curColumnIndex = curIndex % columns;
            curRowIndex = curIndex / columns;
            interpolate();
            lastFrameTime = now;
        }
    }
    
    public void changeSpriteSheet(Image image, int count, int columns, int rows) {
        if (this.currentImage != image || this.count != count || this.columns != columns || this.rows != rows) {
            this.setImage(image);
            this.currentImage = image;
            this.count = count;
            this.columns = columns;
            this.rows = rows;
            
            this.width = (int) image.getWidth() / columns;
            this.height = (int) image.getHeight() / rows;
            
            this.curIndex = 0;
            this.curColumnIndex = 0;
            this.curRowIndex = 0;
            this.lastFrameTime = System.nanoTime();
            
            interpolate();
        }
    }
    
    protected void interpolate() {
        final int x = curColumnIndex * width + offsetX;
        final int y = curRowIndex * height + offsetY;
        this.setViewport(new Rectangle2D(x, y, width, height));
    }
    
}