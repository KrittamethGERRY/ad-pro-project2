package se233.notcontra.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStages.GameStage;

public class PauseMenu extends VBox {
	private String buttonStyle = "";	
	private Label text;
	private Button continueButton;
	private Button restartButton;
	private Button exitButton;
	
	public PauseMenu() {
		super(10);
		this.setPrefWidth(200);
		this.setPrefHeight(250);
		text = new Label("Paused");
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
		this.setViewOrder(-1);
		this.setAlignment(Pos.CENTER);
		continueButton.setStyle(buttonStyle);
		exitButton.setStyle(buttonStyle);
		restartButton.setStyle(buttonStyle);
		continueButton.setPrefSize(200, 50);
		restartButton.setPrefSize(200, 50);
		exitButton.setPrefSize(200, 50);
		this.getStylesheets().add(Launcher.class.getResource("styles/pausemenu.css").toString());

		getChildren().addAll(text, continueButton, restartButton, exitButton);
	}
}
