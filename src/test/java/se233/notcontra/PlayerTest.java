package se233.notcontra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import se233.notcontra.controller.DrawingLoop;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.model.Bullet;
import se233.notcontra.model.Player;
import se233.notcontra.model.Enums.ShootingDirection;
import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Items.SpecialMagazine;
import se233.notcontra.model.Items.TankBuster;
import se233.notcontra.view.GameStages.FirstStage;
import se233.notcontra.view.GameStages.GameStage;

public class PlayerTest  {
	Player player;
	// Player fields
    Field xVelocityField, yVelocityField, yAccelerationField, xPositionField
    , yPositionField, isFallingField, canJumpField, isJumpingField
    , isMoveRightField, isMoveLeftField, livesField, isBuffedField
    , isSpecialMagField, isTankBusterField, bulletPerClipField, reloadTimerField
    , lastShotTimeField, widthField, heightField, isProningField
    , canDropDownField, isOnPlatformField, dropDownTimerField;
	
	@BeforeAll
	public static void initJfxRuntime() {
	    javafx.application.Platform.startup(() -> {});
	}
	
	@BeforeEach
	public void setUp() throws NoSuchFieldException{
		player = new Player(0, 0, KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
        xVelocityField = player.getClass().getDeclaredField("xVelocity");
        yVelocityField = player.getClass().getDeclaredField("yVelocity");
        yAccelerationField = player.getClass().getDeclaredField("yAcceleration");
        xPositionField = player.getClass().getDeclaredField("xPosition");
        yPositionField = player.getClass().getDeclaredField("yPosition");
        isFallingField = player.getClass().getDeclaredField("isFalling");
        canJumpField = player.getClass().getDeclaredField("canJump");
        isJumpingField = player.getClass().getDeclaredField("isJumping");
        isMoveLeftField = player.getClass().getDeclaredField("isMoveLeft");
        isMoveRightField = player.getClass().getDeclaredField("isMoveRight");
        livesField = player.getClass().getDeclaredField("lives");
        isBuffedField = player.getClass().getDeclaredField("isBuffed");
        isSpecialMagField = player.getClass().getDeclaredField("isSpecialMag");
        isTankBusterField = player.getClass().getDeclaredField("isTankBuster");
        bulletPerClipField = player.getClass().getDeclaredField("bulletPerClip");
        reloadTimerField = player.getClass().getDeclaredField("reloadTimer");
        lastShotTimeField = player.getClass().getDeclaredField("lastShotTime");
        widthField = player.getClass().getDeclaredField("width");
        heightField = player.getClass().getDeclaredField("height");
        isProningField = player.getClass().getDeclaredField("isProning");
        canDropDownField = player.getClass().getDeclaredField("canDropDown");
        isOnPlatformField = player.getClass().getDeclaredField("isOnPlatform");
        dropDownTimerField = player.getClass().getDeclaredField("dropDownTimer");
        
        xVelocityField.setAccessible(true);
        yVelocityField.setAccessible(true);
        yAccelerationField.setAccessible(true);
        xPositionField.setAccessible(true);
        yPositionField.setAccessible(true);
        isFallingField.setAccessible(true);
        canJumpField.setAccessible(true);
        isJumpingField.setAccessible(true);
        isMoveRightField.setAccessible(true);
        isMoveLeftField.setAccessible(true);
        livesField.setAccessible(true);
        isBuffedField.setAccessible(true);
        isSpecialMagField.setAccessible(true);
        isTankBusterField.setAccessible(true);
        bulletPerClipField.setAccessible(true);
        reloadTimerField.setAccessible(true);
        lastShotTimeField.setAccessible(true);
        heightField.setAccessible(true);
        widthField.setAccessible(true);
        isProningField.setAccessible(true);
        canDropDownField.setAccessible(true);
        isOnPlatformField.setAccessible(true);
        dropDownTimerField.setAccessible(true);

	}
	
	@Test
	public void respawn_playerPositionIs0and0() {
		player.respawn();
		assertEquals(0, player.getXPosition(), "Start X should be 0");
		assertEquals(0, player.getYPosition(), "Start Y shoud be 0");
	}
	
	@Test
	public void moveRightOnce_xPositionIncreaseBy1() throws IllegalAccessException{
		xPositionField.setInt(player, 0);
		player.moveRight();
		player.moveX();
		assertEquals(1, player.getXPosition(), "xPosition should be 1");
	}
	@Test
	public void moveLeft_xPositionDecreaseByXVelocity() throws IllegalArgumentException, IllegalAccessException {
		xPositionField.setInt(player, 0);
		player.moveLeft();
		player.moveX();
		assertEquals(-1, player.getXPosition(), "xPosition should be -1");
	}
	@Test
	public void respawn_xPositionShouldBe0_moveToLeftBorder_thenCheckWallCollision() throws IllegalArgumentException, IllegalAccessException {
		xPositionField.setInt(player, 0);
		player.respawn();
		player.moveLeft();
		player.moveX();
		player.checkStageBoundaryCollision();
		assertEquals(0, player.getXPosition(), "xPosition should be 0");
	}
	@Test
	public void respawn_xPositionShouldBe1280_moveToRightBorder_thenCheckWallCollision() throws IllegalArgumentException, IllegalAccessException {
		player.respawn();
		xPositionField.setInt(player, GameStage.WIDTH + player.width); // Player width = 64, gameStage width = 1280
		player.moveRight();
		player.moveX();
		player.checkStageBoundaryCollision();
		assertEquals(GameStage.WIDTH-player.width, player.getXPosition(), "xPosition should be 1216");
	}
	
	@Test
	public void givePlayer2Lives_playerDieOnce_thenLivesWillDecreaseBy1() throws IllegalArgumentException, IllegalAccessException {
		livesField.setInt(player, 2);
		player.die();
		assertEquals(1, livesField.getInt(player), "Player lives should decrease by 1");
	}
	
	@Test
	public void playerAtCoordinate0_0_moveRight_theCoordinateIncreaseByXVelocity() throws IllegalArgumentException, IllegalAccessException {
		xPositionField.setInt(player, 0);
		xVelocityField.setInt(player, 10);
		int previousXPosition = player.getXPosition();
		player.moveRight();
		player.moveX();
		int currentXPosition = player.getXPosition();
		assertEquals(xPositionField.getInt(player), currentXPosition, "xPosition moved based on xVelocity.");
	}
	
	@Test
	public void playerAtCoordinate0_0_moveLeft_theCoordinateDecreaseByXVelocity() throws IllegalArgumentException, IllegalAccessException {
		xPositionField.setInt(player, 0);
		xVelocityField.setInt(player, 10);
		player.moveLeft();
		player.moveX();
		int currentXPosition = player.getXPosition();
		assertEquals(xPositionField.getInt(player), currentXPosition, "xPosition moved based on yVelocity.");
	}
	
	@Test
	public void playerAtCoordinate0_10_jump_theCoordinateYDecreaseByYVelocity() throws IllegalArgumentException, IllegalAccessException {
		yPositionField.setInt(player, 10);
		yVelocityField.setInt(player, 10);
		player.jump();
		player.moveY();
		int currentYPosition = player.getYPosition();
		assertEquals(yPositionField.getInt(player), currentYPosition, "Player position is decrease by yVelocity of 10");
	}
	
	@Test
	public void disableKey_thenCheckTheKey_isNull() {
		player.disableKeys();
		assertNull(player.getDownKey(), "Down key is null.");
		assertNull(player.getUpKey(), "Up key is null.");
		assertNull(player.getLeftKey(), "Left key is null.");
		assertNull(player.getRightKey(), "Right key is null.");
		assertNull(player.getShootKey(), "Shoot key is null.");
		assertNull(player.getJumpKey(), "Jump key is null.");
	}
	
	@Test
	public void disableKeyOnce_andEnableKeyAgain_thenCheckTheKey_isDefined() {
		player.disableKeys();
		player.enableKeys();
		assertNotNull(player.getDownKey(), "Down key is not null.");
		assertNotNull(player.getUpKey(), "Up key is not null.");
		assertNotNull(player.getLeftKey(), "Left key is not null.");
		assertNotNull(player.getRightKey(), "Right key is not null.");
		assertNotNull(player.getShootKey(), "Shoot key is not null.");
		assertNotNull(player.getJumpKey(), "Jump key is not null.");
	}
	
	
	@Test
	public void spawnSpecialMagazineAtRightSide_ofPlayer_movePlayerToRight_checkItemCollision() throws Exception {
		GameStage mockGameStage = Mockito.mock(FirstStage.class);
		SpecialMagazine specialMagazine = new SpecialMagazine(player.getXPosition() + player.width + 1,0,1,1);
		when(mockGameStage.getItem()).thenReturn(specialMagazine);
		
		player.moveRight();
		player.moveX();
		player.checkItemCollision(mockGameStage);
		assertTrue(isBuffedField.getBoolean(player), "Player got buffed after pick up the item.");
		assertTrue(isSpecialMagField.getBoolean(player), "Player pick up the special magazine.");
	}
	
	@Test
	public void spawnTankBusterAtRightSide_ofPlayer_movePlayerToRight_checkItemCollision() throws Exception {
		GameStage mockGameStage = Mockito.mock(FirstStage.class);
		TankBuster tankBuster = new TankBuster(player.getXPosition() + player.width + 1,0,1,1);
		when(mockGameStage.getItem()).thenReturn(tankBuster);
		
		player.moveRight();
		player.moveX();
		player.checkItemCollision(mockGameStage);
		assertTrue(isTankBusterField.getBoolean(player), "Player pick up the tankbuster.");
	}
	
	@Test
	public void spawnTankBusterAtRightSide_ofPlayer_movePlayerToRight_checkItemCollision_thenCheckisMoveRight_alwaysTrue() throws Exception {
		GameStage mockGameStage = Mockito.mock(FirstStage.class);
		TankBuster tankBuster = new TankBuster(player.getXPosition() + player.width + 1,0,1,1);
		when(mockGameStage.getItem()).thenReturn(tankBuster);
		
		player.moveRight();
		player.moveX();
		player.checkItemCollision(mockGameStage);
		player.moveLeft();
		assertFalse(isMoveLeftField.getBoolean(player), "Player always move right when picked up the tankbuster");
	}
	
	@Test
	public void player_onPlatform_startDropdown_thenDropDownTimer_shouldBeSet() throws IllegalArgumentException, IllegalAccessException {
		canDropDownField.setBoolean(player, true);
		dropDownTimerField.setInt(player, 0);
		isOnPlatformField.setBoolean(player, true);
		player.dropDown();
		assertEquals(12, dropDownTimerField.getInt(player), "Dropdown Timer should be set to 12");
	}
	
	@Test
	public void playerWith1Bullet_shoots2Times_thenReloadTimerIsSet() throws IllegalArgumentException, IllegalAccessException {
	    player.respawn();
	    GameLoop.bullets = new ArrayList<>(); 
	    GameStage mockGameStage = Mockito.mock(FirstStage.class);
	    ObservableList<Node> nodeList = FXCollections.observableArrayList();
	    when(mockGameStage.getChildren()).thenReturn(nodeList);

	    bulletPerClipField.setInt(player, 1); 
	    lastShotTimeField.setLong(player, 0); 

	    player.shoot(mockGameStage, ShootingDirection.RIGHT);

	    assertEquals(0, bulletPerClipField.getInt(player), "Clip should be empty");

	    // Bypass the fireDelay of each bullet
	    lastShotTimeField.setLong(player, 0L);

	    player.shoot(mockGameStage, ShootingDirection.RIGHT);

	    assertEquals(30, reloadTimerField.getInt(player), "The reload timer should start at 30");
	    assertEquals(3, bulletPerClipField.getInt(player), "The clip should be refilled to 3");
	}
	
	@Test
	public void playerProne_isProningShouldBe_true() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		isProningField.setBoolean(player, false);
		player.prone();
		assertTrue(isProningField.getBoolean(player), "isProning should be true.");
	}
	
}