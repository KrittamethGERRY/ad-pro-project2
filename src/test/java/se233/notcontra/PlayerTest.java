package se233.notcontra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.input.KeyCode;
import se233.notcontra.model.Player;
import se233.notcontra.view.FirstStage;
import se233.notcontra.view.GameStage;

public class PlayerTest {
	private Player player;
	private GameStage gameStage;
	
	@BeforeEach
	public void setup() {
		player = new Player(0, 0, KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S);
	}
	@Test
	public void respawn_moveRight_thenXPosIncrease() throws Exception {
		player.respawn();
		player.moveRight();
		player.moveX();
		assertTrue(player.getXPosition() > 0);
	}
	
	@Test
	public void player_moveLeft_thenYPosDecrease() throws Exception {
		player.respawn();
		player.moveLeft();
		player.moveX();
		assertTrue(player.getXPosition() < 0);
	}
	
	@Test
	public void player_jump_thenYPosDecrease() {
		player.respawn();
		player.jump();
		player.moveY();
		assertTrue(player.getYPosition() < 0);
	}
	
	@Test
	public void player_move_passLeftBorder_XPosShouldStaySame() {
		
	}
	@Test
	public void player_move_passRightBorder_XPosShouldStaySame() {
		
	}
}