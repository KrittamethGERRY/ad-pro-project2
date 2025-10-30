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
	
	public static final Image PATROL_ENEMY = new Image(Launcher.class.getResourceAsStream("assets/Enemy/Patrol_E.png"));
	public static final Image FLYING_ENEMY = new Image(Launcher.class.getResourceAsStream("assets/Enemy/FlyingEnemy.png"));
	public static final Image WALL_ENEMY = new Image( Launcher.class.getResourceAsStream("assets/Enemy/Wall_shooter.png"));
	
	public static final Image DESTROYED_TURRET = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss1/Turret_dead.png"));
	public static final Image FIRING_TURRET = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss1/Turret_fire.png"));
	public static final Image IDLE_TURRET = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss1/Turret_IDEL.png"));
	public static final Image IDLE_CORE = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss1/core.png"));
	public static final Image DESTROYED_CORE = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss1/Core_dead.png"));
	
	public static final Image JAVA_IDLE = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss2/JAVA_IDEL.png"));
	public static final Image DESTROYED_JAVA = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss2/JAVA_DEAD.png"));
	
	public static final Image DESTROYED_RDHEAD = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_head_DEAD.png"));
	public static final Image DESTROYED_RD_LEFTEYE = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_lefteyes_DEAD.png"));
	public static final Image DESTROYED_RD_RIGHTEYE = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_righteyes_DEAD.png"));
	
	public static final Image RD_HEAD = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/boss3_head.png"));
	public static final Image RD_LEFTHAND = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_leftHand_IDEL.png"));
	public static final Image RD_RIGHTHAND = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/RD_rightHand_IDEL.png"));
	public static final Image RD_LEFTEYE = new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/boss3_left_eye.png"));
	public static final Image RD_RIGHTEYE= new Image(Launcher.class.getResourceAsStream("assets/Boss/Boss3/boss3_right_eye.png"));

	public static final Image EXPLOSION_IMG = new Image(Launcher.class.getResourceAsStream("assets/Effects/explosionSprite.png"));
	
	public static final Image MAIN_MENU = new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/MainMenu.png"));
	public static final Image FIRST_STAGE = new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/firstStage.png"));
	public static final Image SECOND_STAGE = new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/secondStage.png"));
	public static final Image THIRD_STAGE = new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/thirdStage.png"));
	public static final Image PLATFORM = new Image(Launcher.class.getResourceAsStream("assets/Backgrounds/platforms.png"));
}
