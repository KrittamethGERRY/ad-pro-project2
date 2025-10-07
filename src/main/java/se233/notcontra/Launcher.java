package se233.notcontra;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.FirstStage;
import se233.notcontra.view.GameStage;
import se233.notcontra.view.MainMenu;
import se233.notcontra.view.SecondStage;
import se233.notcontra.view.ThirdStage;

public class Launcher extends Application {
	public static Stage primaryStage;
	public static List<GameStage> stages = List.of(
			new FirstStage(), new SecondStage(), new ThirdStage()
			);
	private static GameStage currentStage = null;
	
    @Override
    public void start(Stage stage) {
    	
    	primaryStage = stage;
    	Scene scene = new Scene(new MainMenu(), GameStage.WIDTH, GameStage.HEIGHT);
    	stage.setScene(scene);
    	stage.setTitle("Not Contra");
    	stage.show();

    }
    
    public static void changeStage(int index) {
    	javafx.application.Platform.runLater(() -> {
    		GameStage gameStage = stages.get(index);
    		Scene newScene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);
    		newScene.setOnKeyPressed(e -> {
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
    		GameLoop gameLoop = new GameLoop(gameStage);
    		DrawingLoop drawingLoop = new DrawingLoop(gameStage);
    		primaryStage.setScene(newScene);
    		(new Thread(drawingLoop)).start();
    		(new Thread(gameLoop)).start();    		
    	});
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
