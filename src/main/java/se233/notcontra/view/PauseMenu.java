package se233.notcontra.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se233.notcontra.Launcher;

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
		restartButton = new Button("Restart");
		exitButton = new Button("Exit");
		this.setLayoutX(GameStage.WIDTH/2 - (this.getWidth()/2));
		this.setLayoutY(GameStage.HEIGHT/2 - (this.getHeight()/2));
		this.setStyle("-fx-background-color: gray");
		getChildren().addAll(text, continueButton, restartButton, exitButton);
		
	}
}
