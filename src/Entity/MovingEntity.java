package Entity;

import math2d.Force;

public abstract class MovingEntity extends Entity {
    protected final Force vector;

    protected final double maxSpeed;

    public MovingEntity(final double width, final double height, final double maxSpeed) {
        super(width, height);

        this.maxSpeed = maxSpeed;
        this.vector = new Force();
    }


    public void applyForce(final Force f) {
        this.vector.add(f);
    }


    public void applyForce(final double x, final double y) {
        Force f = new Force(x, y);
        applyForce(f);
    }


    public double getDirection() {
        return this.vector.getDirection();
    }


    public void move(final long delta) {
        // Maximum speed
        if (this.vector.getValue() > this.maxSpeed) {
            this.vector.setValue(this.maxSpeed);
        }

        // Update position
        setLocation(getLocationX() + this.vector.getX() * delta, getLocationY() + this.vector.getY() * delta);
    }

}
