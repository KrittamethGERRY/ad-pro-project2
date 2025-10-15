package se233.notcontra.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PauseMenu extends Pane {
	private Label text;
	private Button continueButton;
	private Button restartButton;
	private Button exitButton;
	
	public PauseMenu() {
		text = new Label("Paused");
		continueButton = new Button("Continue");
		restartButton = new Button("Restart");
		exitButton = new Button("Exit");
		
		getChildren().addAll(text, continueButton, restartButton, exitButton);
		
	}
}
