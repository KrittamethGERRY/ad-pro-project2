package se233.notcontra;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.CheatManager;
import se233.notcontra.view.MainMenu;
import se233.notcontra.view.GameStages.FirstStage;
import se233.notcontra.view.GameStages.GameStage;
import se233.notcontra.view.GameStages.SecondStage;
import se233.notcontra.view.GameStages.ThirdStage;

public class Launcher extends Application {
	public static Stage primaryStage;
	private static MainMenu menu = null;
	private static Scene menuScene = null;
	private static Scene currentScene = null;
	private static GameStage currentStage = null;
	public static Integer currentStageIndex = null;
	private static Thread currentDrawingThread = null;
	private static Thread currentGameThread = null;
	private static GameLoop currentGameLoop = null;
	private static DrawingLoop currentDrawingLoop = null;
	
    @Override
    public void start(Stage stage) {
    	menu = new MainMenu();
    	menuScene = new Scene(menu, GameStage.WIDTH, GameStage.HEIGHT);
    	currentScene = menuScene;
    	stage.setScene(menuScene);
    	stage.setTitle("Not Contra");
    	stage.show();
    	primaryStage = stage;
    }
    
    public static void changeStage(int index) {
    	javafx.application.Platform.runLater(() -> {
    		if (GameLoop.isPaused) GameLoop.pause(); // unpause the game
    		if (currentGameLoop != null) {
    			currentGameLoop.stop();
    			currentGameThread = null;
    		}
    		if (currentDrawingLoop != null) {
    			currentDrawingLoop.stop();
    			currentDrawingThread = null;
    		}
    		
    		currentStageIndex = index;
    		GameStage gameStage = switch (index) {
	    		case 0 -> new FirstStage();
	    		case 1 -> new SecondStage();
	    		case 2 -> new ThirdStage();
				default -> throw new IllegalArgumentException("Unexpected value: " + index);
    		};
    		currentScene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);
    		currentScene.setOnKeyPressed(e -> {
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
				if (e.getCode() == gameStage.getPlayer().getCheatKey()) {
					CheatManager.getInstance().toggleCheats();
				}
    		});
    		currentScene.setOnKeyReleased(e -> {
    			gameStage.getKeys().remove(e.getCode());

    		});
    		currentGameLoop = new GameLoop(gameStage);
    		currentDrawingLoop = new DrawingLoop(gameStage);
    		
    		currentGameThread = new Thread(currentGameLoop, "GameLoopThread");
    		currentDrawingThread = new Thread(currentDrawingLoop, "DrawingLoopThread");


    		primaryStage.setScene(currentScene);
    		currentStage = gameStage;
    		
    		currentGameThread.start();
    		currentDrawingThread.start();
    		
    	});
    }
    
    public static void exitToMenu() {
    	javafx.application.Platform.runLater(() -> {
        	primaryStage.setScene(menuScene);
        	if (GameLoop.isPaused) GameLoop.pause(); // Unpause the game
    		if (currentGameLoop != null) {
    			currentGameLoop.stop();
    			currentGameThread = null;
    		}
    		if (currentDrawingLoop != null) {
    			currentDrawingLoop.stop();
    			currentDrawingThread = null;
    		}
    	});

    }
    
    public static GameStage getCurrentStage() {
    	return currentStage;
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
