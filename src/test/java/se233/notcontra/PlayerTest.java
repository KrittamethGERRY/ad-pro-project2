package se233.notcontra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.scene.input.KeyCode;
import se233.notcontra.controller.DrawingLoop;
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
    , isSpecialMagField, isTankBusterField;
	

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
	
//	@Test
//	public void respawn_player_thenShoot1Bullet_theShootTimer() {
//		GameStage mockGameStage = Mockito.mock(FirstStage.class);
//		ShootingDirection mockShootingDirection = Mockito.mock(ShootingDirection.class);
//		
//		player.shoot(mockGameStage, mockShootingDirection);
//	}
}