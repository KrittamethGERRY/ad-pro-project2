package se233.notcontra.model;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;
import se233.notcontra.controller.GameLoop;
import se233.notcontra.view.GameStage;

public class Wallboss extends Boss {


    private final int phaseChangeHealth;
    private final Rectangle turretLeft;
    private final Rectangle turretRight;
    private final Rectangle core;
    private GameStage gameStage;
    private long lastShotTime;
    private final long shootCooldown = 1000;
    private ImageView turretleftSprite;
    private ImageView turretrightSprite;
    private ImageView coreSprite;
    private ImageView wallbossSprite;

    public Wallboss(double x, double y ,GameStage gameStage) {
        super(x, y, 300, 200, 5000);


        this.phaseChangeHealth = this.getMaxHealth() / 2;

        // 3. Define the wall's components (turrets and core)
        // Positions are relative to the Pane's top-left corner (0,0)
        this.turretLeft = new Rectangle(30, 50, 40, 40);
        this.turretRight = new Rectangle(115, 50, 40, 40);
        this.core = new Rectangle(65, 80, 60, 60);
        this.turretleftSprite = new ImageView(Launcher.class.getResource("assets/FD.png").toString());
        this.turretrightSprite = new ImageView(Launcher.class.getResource("assets/FD.png").toString());
        this.coreSprite = new ImageView(Launcher.class.getResource("assets/FD.png").toString());
        this.wallbossSprite = new ImageView(Launcher.class.getResource("assets/wall.png").toString());
        this.wallbossSprite.setFitWidth(300);
        this.wallbossSprite.setFitHeight(200);
        getChildren().add(wallbossSprite);

        getWeakPoints().add(turretLeft);
        getWeakPoints().add(turretRight);
        getWeakPoints().add(core);

        // --- Add visual components to the Pane for rendering ---
        // For now, we'll just show the hitboxes with colors for clarity
        turretLeft.setFill(Color.RED);
        turretRight.setFill(Color.RED);
        core.setFill(Color.BLUE);
        getChildren().addAll(turretLeft, turretRight, core);
        this.lastShotTime = System.currentTimeMillis();
    }

    @Override
    protected void handleAttackingState() {
        // This is PHASE 1. The turrets are active.
        long currentTime = System.currentTimeMillis();
        // Check if the cooldown has passed
        if (currentTime - lastShotTime > shootCooldown) {
            // Decide randomly whether to shoot or not (e.g., 60% chance)
            if (Math.random() < 0.6) {
                // Decide randomly which turret shoots
                Rectangle firingTurret = (Math.random() < 0.5) ? turretLeft : turretRight;
                shootFromTurret(firingTurret);
            }
            // Reset the timer regardless of whether a shot was fired
            lastShotTime = currentTime;
        }

        // Check if health has dropped below the threshold to trigger Phase 2
        if (getHealth() <= this.phaseChangeHealth) {
            System.out.println("Turrets destroyed! Core is exposed!");
            getWeakPoints().remove(turretLeft);
            getWeakPoints().remove(turretRight);
            getChildren().remove(turretLeft);
            getChildren().remove(turretRight);

            // TODO: Trigger explosion animations at turret locations.

            // Switch to the VULNERABLE state (Phase 2)
            setState(BossState.VULNERABLE);
        }
    }

    private void shootFromTurret(Rectangle turret) {
        // Calculate the bullet's starting position (center of the turret)
        // We add the boss's position to get the absolute coordinates on the screen
        int startX = (int) (this.getLayoutX() + turret.getX() + turret.getWidth() / 2);
        int startY = (int) (this.getLayoutY() + turret.getY() + turret.getHeight() / 2);

        // Create a new bullet (shoots left with speed 5)
        Bullet bullet = new Bullet(startX, startY, 5, 0, ShootingDirection.LEFT);

        // Add the bullet to the central bullet list in GameLoop
        GameLoop.bullets.add(bullet);
        javafx.application.Platform.runLater(() -> {
            gameStage.getChildren().add(bullet);
        });
    }

    @Override
    protected void handleVulnerableState() {
        // This is PHASE 2. Only the core remains.
        // The takeDamage() logic in the parent class handles the rest.
        // Once health reaches 0, it will automatically switch to DEFEATED.
    }

    @Override
    protected void updateWeakPointsPosition() {
        // A wall is stationary, so we override this to do nothing.
    }
}