package se233.notcontra.model;

import javafx.scene.image.Image;
import se233.notcontra.Launcher;

public class ImageAssets {
	public static final Image PLAYER_CHARGING_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_charging.png"));
	public static final Image PLAYER_DIE_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_die.png"));
	public static final Image PLAYER_WALK_SHOOT_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_straight.png"));
	public static final Image PLAYER_PRONE_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_prone.png"));
	public static final Image PLAYER_JUMP_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_jump.png"));
	public static final Image PLAYER_SHOOT_DOWN_SIDE_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_down_side.png"));
	public static final Image PLAYER_SHOOT_UP_SIDE_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_up_side.png"));
	public static final Image PLAYER_SHOOT_UP = new Image(Launcher.class.getResourceAsStream("assets/Player/player_walk_shoot_up.png"));
	public static final Image PLAYER_IDLE_IMG = new Image(Launcher.class.getResourceAsStream("assets/Player/player_idle.png"));

	public static final Image EXPLOSION_IMG = new Image(Launcher.class.getResourceAsStream("assets/Effects/explosionSprite.png"));
}
