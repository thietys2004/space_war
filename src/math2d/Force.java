package math2d;

public class Force {
    private double x;

    private double y;

    public Force(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Force() {
        this(0, 0);
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

    public void add(final Force f) {
        this.x += f.x;
        this.y += f.y;
    }

    public double getDirection() {
        return Math.atan2(y, x);
    }

    public void setDirection(double angle) {
        x = getValue() * Math.cos(angle);
        y = getValue() * Math.sin(angle);
    }

    public double getValue() {
        return Math.sqrt(x * x + y * y);
    }

    public void setValue(double coefficient) {
        double Value = getValue();
        x = coefficient * (x / Value);
        y = coefficient * (y / Value);
    }
}
