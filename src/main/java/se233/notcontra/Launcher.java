package se233.notcontra;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.FirstStage;
import se233.notcontra.view.GameStage;
import se233.notcontra.view.MainMenu;

public class Launcher extends Application {
	public static Stage primaryStage;
	
    @Override
    public void start(Stage stage) {
    	GameStage currentStage = drawFirstStage();
    	GameLoop gameLoop = new GameLoop(currentStage);
    	DrawingLoop drawingLoop = new DrawingLoop(currentStage);
    	Scene scene = new Scene(new MainMenu(), GameStage.WIDTH, GameStage.HEIGHT);
    	scene.setOnKeyPressed(event -> {
    		currentStage.getKeys().add(event.getCode());
    	});
    	scene.setOnKeyReleased(event -> {
    		currentStage.getKeys().remove(event.getCode());
    	});
    	stage.setScene(scene);
    	stage.setTitle("Not Contra");
    	stage.show();
    	(new Thread(gameLoop)).start();
    	(new Thread(drawingLoop)).start();
    }
    
    public GameStage drawFirstStage() {
    	GameStage firstStage = new FirstStage();
    	return firstStage;
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
