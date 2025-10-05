package se233.notcontra.model;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import se233.notcontra.Launcher;

import java.util.ArrayList;

public class Boss extends Pane {

    // Define the missing enum for BossState
    private double x, y;
    private int health;
    private final int maxHealth;
    private boolean isAlive;
    private BossState currentState;
    private long stateTimer;
    private long attackCooldown;
    public static int width, height;
    private ArrayList<Rectangle> weakPoints;

    private ImageView sprite;


    // Animation parameters

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
        this.attackCooldown = 0;
        this.weakPoints = new ArrayList<>();
        this.weakPoints.add(new Rectangle(x, y, width, height));
        this.sprite = new ImageView();

        this.sprite.setImage(new Image(Launcher.class.getResource("assets/FD.png").toString()));
        this.sprite.setFitWidth(width);
        this.sprite.setFitHeight(height);
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

    private void handleEnteringState() {
        if (System.currentTimeMillis() - stateTimer > 2000) {
            setState(BossState.IDLE);
        }
    }

    private void handleIdleState() {
        if (System.currentTimeMillis() - stateTimer > 3000) {
            setState(BossState.ATTACKING);
        }
    }

    private void handleAttackingState() {
        if (System.currentTimeMillis() - stateTimer > 5000) {
            setState(BossState.IDLE);
        }
    }

    private void handleVulnerableState() {
        if (System.currentTimeMillis() - stateTimer > 4000) {
            setState(BossState.ATTACKING);
        }
    }

    private void handleDefeatedState() {
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

    private void updateWeakPointsPosition() {
        if (!weakPoints.isEmpty()) {
            weakPoints.get(0).setX(this.x);
            weakPoints.get(0).setY(this.y);
        }
    }

    private void setState(BossState newState) {
        this.currentState = newState;
        this.stateTimer = System.currentTimeMillis();
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getwidth() { return width; }
    public int getheight() { return height; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public BossState getCurrentState() { return currentState; }
    public boolean isDefeated() { return currentState == BossState.DEFEATED; }
    public boolean isAlive() { return isAlive; }
    public ArrayList<Rectangle> getWeakPoints() { return weakPoints; }
}