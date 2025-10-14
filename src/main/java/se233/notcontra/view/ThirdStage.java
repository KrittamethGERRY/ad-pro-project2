package se233.notcontra.view;

import java.util.List;

import se233.notcontra.model.Items.Item;
import se233.notcontra.model.Boss;
import se233.notcontra.model.Keys;
import se233.notcontra.model.Player;

public class ThirdStage extends GameStage {

	public ThirdStage() {
		
	}
	
	@Override
	public Keys getKeys() {
		return this.keys;
	}
	
	@Override
	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public List<Platform> getPlatforms() {
		return this.platforms;
	}
	
	@Override
	public Item getItem() {
		return this.item;
	}
	
	@Override
	public void spawnItem() {
		
	}
	
	@Override
	public void removeItem() {
		getChildren().remove(item);
		item = null;
	}
	
	@Override
	public Boss getBoss() {
		return this.boss;
	}
	
	@Override
	public void logging() {
		logger.info("Player spawned at X:{} Y:{}", player.getXPosition(), player.getYPosition());
		for (Platform platform : platforms) {
			logger.info("Platform spawned at X:{} Y:{} Width:{}", platform.getXPosition(), platform.getYPosition(), platform.getPaneWidth());
		}
		logger.info("Item spawned at X:{} Y:{}", item.getXPos(), item.getYPos());
	}
}