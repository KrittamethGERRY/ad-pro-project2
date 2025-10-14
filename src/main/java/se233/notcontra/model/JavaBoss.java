package se233.notcontra.model;

import se233.notcontra.view.GameStage;

import java.util.List;

public class JavaBoss extends Boss{
    private List<EnemyView> spawnedEnemyViews;
    private long lastEnemySpawnTime;
    private final long enemySpawnCooldown = 5000; // Spawn enemy every 5 seconds
    private final int maxEnemies = 1;

    public JavaBoss(double x, double y , GameStage gameStage) {
        super(x, y, 300, 200, 5000);
    }



    @Override
    protected void handleAttackingState() {

    }

}
