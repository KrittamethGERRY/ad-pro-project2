package se233.notcontra.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenu extends BorderPane {
	
	public static String buttonStyle = "-fx-background-color: Blue;" + "-fx-text-fill: white;"
			+ "-fx-font-size: 2em;"
			+ "-fx-margin: 20px;"
			+ "-fx-background-radious: 0;";
	
	public MainMenu() {
		VBox buttonBox = new VBox();
		ImageView background = new ImageView();
		background.setFitHeight(GameStage.HEIGHT);
		background.setFitWidth(GameStage.WIDTH);
		Button startButton = drawStartButton();
		Button selectStageButton = drawStageButton();
		Button exitButton = drawExitButton();
		Insets margin = new Insets(0,25,25,25);
		VBox.setMargin(startButton, margin);
		VBox.setMargin(selectStageButton, margin);
		VBox.setMargin(exitButton, margin);
		buttonBox.getChildren().addAll(startButton, selectStageButton, exitButton);
		getChildren().addAll(background, buttonBox);
	}
	
	public Button drawStartButton() {
		Button startButton = new Button("Start");
		startButton.setPrefSize(150, 50);
		startButton.setStyle(buttonStyle);
		return startButton;
	}
	
	public Button drawStageButton() {
		Button selectStageButton = new Button("Select Stage");
		selectStageButton.setPrefSize(150, 50);
		selectStageButton.setStyle(buttonStyle);
		return selectStageButton;
	}
	
	public Button drawExitButton() {
		Button exitButton = new Button("Quit");
		exitButton.setPrefSize(150, 50);
		exitButton.setStyle(buttonStyle);		
		return exitButton;
	}
}