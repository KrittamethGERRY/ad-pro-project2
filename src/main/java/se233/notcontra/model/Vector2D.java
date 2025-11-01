package se233.notcontra.model;

public class Vector2D {
    private double x;
	private double y;
    public Vector2D(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.getX() + other.getX(), this.getY() + other.getY());
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.getX() * scalar, this.getY() * scalar);
    }

    public Vector2D normalize() {
        double length = (double) Math.sqrt(getX() * getX() + getY() * getY());
        if (length > 0) {
            return new Vector2D(getX() / length, getY() / length);
        }
        return new Vector2D(0, 0);
    }

    public double distance(Vector2D other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float getLength() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
