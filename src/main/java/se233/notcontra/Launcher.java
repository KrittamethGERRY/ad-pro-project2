package se233.notcontra;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.MainMenu;
import se233.notcontra.view.GameStages.FirstStage;
import se233.notcontra.view.GameStages.GameStage;
import se233.notcontra.view.GameStages.SecondStage;
import se233.notcontra.view.GameStages.ThirdStage;

public class Launcher extends Application {
	public static Stage primaryStage;
	private static GameStage currentStage = null;
	private static Thread currentDrawingThread = null;
	private static Thread currentGameThread = null;
	private static GameLoop currentGameLoop = null;
	private static DrawingLoop currentDrawingLoop = null;
	
    @Override
    public void start(Stage stage) {
    	Scene scene = new Scene(new MainMenu(), GameStage.WIDTH, GameStage.HEIGHT);
    	stage.setScene(scene);
    	stage.setTitle("Not Contra");
    	stage.show();
    	primaryStage = stage;
    }
    
    public static void changeStage(int index) {
    	javafx.application.Platform.runLater(() -> {
    		if (currentGameLoop != null) {
    			currentGameLoop.stop();
    		}
    		if (currentDrawingLoop != null) {
    			currentDrawingLoop.stop();
    		}
    		GameStage gameStage = switch (index) {
	    		case 0 -> new FirstStage();
	    		case 1 -> new SecondStage();
	    		case 2 -> new ThirdStage();
				default -> throw new IllegalArgumentException("Unexpected value: " + index);
    		};
    		Scene newScene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);
    		newScene.setOnKeyPressed(e -> {
    			if (e.getCode() == KeyCode.ESCAPE) {
    				GameLoop.pause();
    			}
    			if (e.getCode() != gameStage.getPlayer().getShootKey()) {
        			gameStage.getKeys().add(e.getCode());
    			} else {
    				if (!gameStage.getKeys().isPressed(e.getCode())) {
    					gameStage.getKeys().add(e.getCode());
    					gameStage.getKeys().addPressed(e.getCode());
    				}
    			}
    		});
    		newScene.setOnKeyReleased(e -> {
    			gameStage.getKeys().remove(e.getCode());

    		});
    		currentGameLoop = new GameLoop(gameStage);
    		currentDrawingLoop = new DrawingLoop(gameStage);
    		
    		currentGameThread = new Thread(currentGameLoop, "GameLoopThread");
    		currentDrawingThread = new Thread(currentDrawingLoop, "DrawingLoopThread");


    		primaryStage.setScene(newScene);
    		currentStage = gameStage;
    		
    		currentGameThread.start();
    		currentDrawingThread.start();
    		
    	});
    }
    
    public static GameStage getCurrentStage() {
    	return currentStage;
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
