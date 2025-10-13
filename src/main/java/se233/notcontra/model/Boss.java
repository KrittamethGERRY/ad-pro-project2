package se233.notcontra.model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Boss extends Pane {

    private double x, y;
    private int health;
    private final int maxHealth;
    private boolean isAlive;
    private BossState currentState;
    private long stateTimer;

    private int width, height;
    private ArrayList<Rectangle> weakPoints;
    private ImageView sprite;

    public Boss(double x, double y, int width, int height, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.isAlive = true;
        this.currentState = BossState.ENTERING;
        this.stateTimer = System.currentTimeMillis();

        // Position the Pane itself
        setLayoutX(x);
        setLayoutY(y);

        this.weakPoints = new ArrayList<>();
        this.sprite = new ImageView();
        // Add the sprite to the Pane's children so it's visible
        getChildren().add(sprite);
    }

    public void update() {
        if (!isAlive) {
            return;
        }
        switch (currentState) {
            case ENTERING:   handleEnteringState();   break;
            case IDLE:       handleIdleState();       break;
            case ATTACKING:  handleAttackingState();  break;
            case VULNERABLE: handleVulnerableState(); break;
            case DEFEATED:   handleDefeatedState();   break;
        }
        updateWeakPointsPosition();
    }

    // These methods are protected so subclasses can override them
    protected void handleEnteringState() {
        if (System.currentTimeMillis() - stateTimer > 2000) {
            setState(BossState.IDLE);
        }
    }

    protected void handleIdleState() {
        if (System.currentTimeMillis() - stateTimer > 3000) {
            setState(BossState.ATTACKING);
        }
    }

    protected void handleAttackingState() {
        if (System.currentTimeMillis() - stateTimer > 5000) {
            setState(BossState.IDLE);
        }
    }

    protected void handleVulnerableState() {
        if (System.currentTimeMillis() - stateTimer > 4000) {
            setState(BossState.ATTACKING);
        }
    }

    protected void handleDefeatedState() {
        if (System.currentTimeMillis() - stateTimer > 3000) {
            this.isAlive = false;
        }
    }

    public void takeDamage(int amount) {
        if (currentState == BossState.VULNERABLE || currentState == BossState.ATTACKING) {
            this.health -= amount;
            if (this.health <= 0) {
                this.health = 0;
                setState(BossState.DEFEATED);
            }
        }
    }

    protected void updateWeakPointsPosition() {
        // This method can be overridden by stationary bosses
        if (!weakPoints.isEmpty()) {
            // Update positions relative to the Pane's origin (0,0)
            weakPoints.get(0).setX(0);
            weakPoints.get(0).setY(0);
        }
    }

    protected void setState(BossState newState) {
        this.currentState = newState;
        this.stateTimer = System.currentTimeMillis();
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int gethight() { return height; }
    public int getwidth() { return width; }
    public BossState getCurrentState() { return currentState; }
    public boolean isDefeated() { return isDefeated(); }
    public boolean isAlive() { return isAlive; }
    public ArrayList<Rectangle> getWeakPoints() { return weakPoints; }
    public long getStateTimer() { return stateTimer; } // Added for subclasses
}