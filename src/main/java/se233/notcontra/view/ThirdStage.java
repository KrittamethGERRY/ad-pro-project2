package se233.notcontra.view;

import java.util.List;

import se233.notcontra.model.Item;
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
	public void removeItem() {
		getChildren().remove(item);
		item = null;
	}
}