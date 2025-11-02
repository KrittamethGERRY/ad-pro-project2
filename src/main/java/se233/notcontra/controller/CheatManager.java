package se233.notcontra.controller;

import javafx.scene.control.Label;
import se233.notcontra.Launcher;

public class CheatManager {

    private static final CheatManager instance = new CheatManager();
    private static final Label INVINCIBLE_LABEL = new Label("INVINCIBLE MODE ON");
    private static final Label ONE_SHOT_LABEL = new Label("ONE SHOT MODE ON");
    private boolean isInvincible = false;
    private boolean isOneShot = false;

    private CheatManager() {
    }


    public static CheatManager getInstance() {
        return instance;
    }

    public void toggleInvincibility() {
        this.isInvincible = !this.isInvincible;
        if (Launcher.getCurrentStage() != null) {
            if (isInvincible) {
            	INVINCIBLE_LABEL.setStyle("-fx-font-size: 2em;"
            			+ "-fx-text-fill: RED");
            	Launcher.getCurrentStage().getChildren().add(INVINCIBLE_LABEL);
            	SoundController.getInstance().playCheatOnSound();
            } else {
            	Launcher.getCurrentStage().getChildren().remove(INVINCIBLE_LABEL);
            }
            System.out.println("Invincible is now: " + (isInvincible ? "ON" : "OFF"));
        }
    }
    
    public void toggleOneShot() {
    	this.isOneShot = !this.isOneShot;
    	if (Launcher.getCurrentStage() != null) {
            if (isOneShot) {
            	ONE_SHOT_LABEL.setStyle("-fx-font-size: 2em;"
            			+ "-fx-text-fill: RED");
            	ONE_SHOT_LABEL.setLayoutY(24 + ONE_SHOT_LABEL.getHeight());
            	Launcher.getCurrentStage().getChildren().add(ONE_SHOT_LABEL);
            	SoundController.getInstance().playCheatOnSound();
            } else {
            	Launcher.getCurrentStage().getChildren().remove(ONE_SHOT_LABEL);
            }
            System.out.println("One hit kill is now: " + (isInvincible ? "ON" : "OFF"));
    	}
    }
    
    public boolean isInvincible() {
        return this.isInvincible;
    }
    
    public boolean isOneShot() {
    	return this.isOneShot;
    }
}
