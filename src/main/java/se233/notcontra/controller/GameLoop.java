package se233.notcontra.controller;

import java.util.ArrayList;
import java.util.Iterator;

import se233.notcontra.model.Bullet;
import se233.notcontra.model.Player;
import se233.notcontra.view.GameStage;

public class GameLoop implements Runnable{
	
	private GameStage gameStage;
	private int frameRate;
	private float interval;
	private boolean running;
	
	private long lastShotTime = 0;
	private int fireDelay = 500;
	
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	
	public GameLoop(GameStage gameStage) {
		this.gameStage = gameStage;
		this.frameRate = 10;
		this.interval = 1000 / frameRate;
		this.running = true;
	}
	
    private void update(Player player) {
        boolean leftPressed = gameStage.getKeys().isPressed(player.getLeftKey());
        boolean rightPressed = gameStage.getKeys().isPressed(player.getRightKey());
        boolean upPressed = gameStage.getKeys().isPressed(player.getUpKey());
        boolean downPressed = gameStage.getKeys().isPressed(player.getDownKey());
        boolean shootPressed = gameStage.getKeys().isPressed(player.getShootKey());

        if (!downPressed) {
            if (leftPressed && rightPressed) {
                player.stop();
            } else if (leftPressed) {
            	player.moveLeft();
            } else if (rightPressed) {
            	player.moveRight();
            } else {
                player.stop();
            }
        } else {
        	player.prone();
        }

        if (upPressed && !downPressed) {
        	player.jump();
        }
        
        if (shootPressed) {
        	long now = System.currentTimeMillis();
        	if (now - lastShotTime >= fireDelay) {
        		lastShotTime = now;
        		
                Bullet bullet = new Bullet(
                        player.getXPosition() + Player.width,
                        player.getYPosition() - Player.height,
                        15, 0
                    );
                    bullets.add(bullet);

                    javafx.application.Platform.runLater(() -> {
                        gameStage.getChildren().add(bullet);
                    });
        	}
        }
    }
    
    @Override
    public void run() {
    	while (running) {
    		float time = System.currentTimeMillis();
    		update(gameStage.getPlayer());
    		time = System.currentTimeMillis() - time;
    		if (time < interval) {
    			try {
    				Thread.sleep((long) (interval-time));
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		} else {
    			try {
    				Thread.sleep((long) (interval - (interval % time)));
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
}