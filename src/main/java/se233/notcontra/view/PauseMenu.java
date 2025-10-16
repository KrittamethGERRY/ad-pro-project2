package se233.notcontra.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStages.GameStage;

public class PauseMenu extends VBox {
	private Label text;
	private Button continueButton;
	private Button restartButton;
	private Button exitButton;
	
	public PauseMenu() {
		super(25);
		this.setPrefWidth(200);
		this.setPrefHeight(250);
		text = new Label("Paused");
		text.setStyle("-fx-font-size: 2em; -fx-font-weight: bold;");
		continueButton = new Button("Continue");
		continueButton.setOnAction(e -> {
			GameLoop.pause();
		});
		restartButton = new Button("Restart");
		restartButton.setOnAction(e -> {
			Launcher.changeStage(Launcher.currentStageIndex);
		});
		exitButton = new Button("Exit");
		exitButton.setOnAction(e -> {
			Launcher.exitToMenu();
		});
		this.setLayoutX(GameStage.WIDTH/2 - (this.getPrefWidth()/2));
		this.setLayoutY(GameStage.HEIGHT/2 - (this.getPrefHeight()/2));
		this.setStyle("-fx-background-color: gray");
		getChildren().addAll(text, continueButton, restartButton, exitButton);
		
	}
}
