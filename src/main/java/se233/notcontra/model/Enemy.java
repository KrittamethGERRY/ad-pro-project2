package se233.notcontra.model;

public class Enemy {
    double x, y, w = 40, h = 60;
    double speed = 2;
    boolean alive = true;

    Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void update() {
        x += speed;
        if (x < 100 || x > 700) speed *= -1; // patrol
    }
}