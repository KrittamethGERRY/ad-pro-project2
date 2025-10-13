package se233.notcontra.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyView extends Pane {
    private Enemy enemy;
    private Rectangle sprite;

    public EnemyView(Enemy enemy) {
        this.enemy = enemy;

        // Create visual representation
        sprite = new Rectangle(enemy.getW(), enemy.getH());

        // Different colors for different types
        if (enemy.getType() == EnemyType.WALL_SHOOTER) {
            sprite.setFill(Color.GREEN);
        } else if (enemy.getType() == EnemyType.FLYING) {
            sprite.setFill(Color.RED);
        }

        getChildren().add(sprite);
        updatePosition();
    }

    public void updatePosition() {
        setTranslateX(enemy.getX());
        setTranslateY(enemy.getY());
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
