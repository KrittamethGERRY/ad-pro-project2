package se233.notcontra;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.FirstStage;
import se233.notcontra.view.GameStage;

public class Launcher extends Application {
	public static Stage primaryStage;
	
    @Override
    public void start(Stage stage) {
    	GameStage gameStage = new FirstStage();
    	GameLoop gameLoop = new GameLoop(gameStage);
    	DrawingLoop drawingLoop = new DrawingLoop(gameStage);
    	Scene scene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);
    	scene.setOnKeyPressed(event -> {
    		gameStage.getKeys().add(event.getCode());
    	});
    	scene.setOnKeyReleased(event -> {
    		gameStage.getKeys().remove(event.getCode());
    	});
    	stage.setScene(scene);
    	stage.setTitle("Not Contra");
    	stage.show();
    	(new Thread(gameLoop)).start();
    	(new Thread(drawingLoop)).start();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
