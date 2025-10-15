package se233.notcontra.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PauseMenu extends VBox {
	private Label text;
	private Button continueButton;
	private Button restartButton;
	private Button exitButton;
	
	public PauseMenu() {
		super(25);
		this.setWidth(200);
		this.setHeight(250);
		text = new Label("Paused");
		continueButton = new Button("Continue");
		restartButton = new Button("Restart");
		exitButton = new Button("Exit");
		
		
		getChildren().addAll(text, continueButton, restartButton, exitButton);
		
	}
}
