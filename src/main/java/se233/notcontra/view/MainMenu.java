package se233.notcontra.view;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import se233.notcontra.Launcher;

public class MainMenu extends AnchorPane {
	
	private boolean isSelectingLevel = false;
	
	public MainMenu() {
		Label title = new Label("NotContra");
		VBox buttonBox = new VBox(20);
		ImageView background = new ImageView();
		title.setId("title");
		title.setLayoutX(50);
		title.setLayoutY(35);
		background.setFitHeight(GameStage.HEIGHT);
		background.setFitWidth(GameStage.WIDTH);
		Button startButton = drawStartButton();
		Button selectStageButton = drawStageButton();
		Button exitButton = drawExitButton();
		VBox list = drawLevelListPane(selectStageButton);
		String style = Launcher.class.getResource("styles/style.css").toString();
		this.getStylesheets().add(style);
		buttonBox.setLayoutX(50);
		buttonBox.setLayoutY(100);
		list.setAlignment(Pos.CENTER);
		list.setPrefSize(100, 150);
		list.setLayoutX(buttonBox.getWidth() + buttonBox.getLayoutX() + list.getPrefWidth() + 100);
		list.setLayoutY((buttonBox.getHeight() + buttonBox.getLayoutY())/5 + list.getPrefHeight());
		list.setVisible(false);
		buttonBox.getChildren().addAll(startButton, selectStageButton, exitButton);
		startButton.setOnAction(e -> {
			Launcher.changeStage(0);
		});
		selectStageButton.setOnAction(e -> {
			isSelectingLevel = !isSelectingLevel;
			if (!isSelectingLevel) {
				list.setVisible(true);
			} else {
				list.setVisible(false);
			}
		});
		exitButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Are you sure?");
			alert.setHeaderText("Exit?");
			alert.setContentText("Are you sure you want to exit?");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				Launcher.primaryStage.close();
			}
		});
		PauseMenu pauseMenu = new PauseMenu();
		pauseMenu.setLayoutX(1000);
		pauseMenu.setLayoutY(400);
		getChildren().addAll(background, title, buttonBox, list, pauseMenu);
	}
	
	public Button drawStartButton() {
		Button startButton = new Button("Start");
		startButton.setPrefSize(150, 50);
		return startButton;
	}
	
	public Button drawStageButton() {
		Button selectStageButton = new Button("Select Level");
		selectStageButton.setPrefSize(200, 50);
		return selectStageButton;
	}
	
	public Button drawExitButton() {
		Button exitButton = new Button("Quit");
		exitButton.setPrefSize(150, 50);
		return exitButton;
	}
	
	public VBox drawLevelListPane(Button selectStageButton) {
		isSelectingLevel = !isSelectingLevel;
		Button stage1 = new Button("Level 1");
		Button stage2 = new Button("Level 2");
		Button stage3 = new Button("Level 3");
		VBox stageList = new VBox(5);
		stage1.setId("stageButton");
		stage2.setId("stageButton");
		stage3.setId("stageButton");
		stage1.setOnAction(event -> {
			Launcher.changeStage(0);
		});
		stage2.setOnAction(event -> {
			Launcher.changeStage(1);
		});
		stage3.setOnAction(event -> {
			Launcher.changeStage(2);
		});
		
		stageList.getChildren().addAll(stage1, stage2, stage3);
		return stageList;
	}
}