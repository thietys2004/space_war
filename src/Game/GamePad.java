package Game;

public class GamePad {
    private boolean up;

    private boolean down;

    private boolean right;

    private boolean left;

    private boolean shot;

    private double mouseX;

    private double mouseY;

    public boolean isUp() {
        return this.up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return this.down;
    }

    public void setDown(final boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return this.right;
    }

    public void setRight(final boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return this.left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isShot() {
        return this.shot;
    }

    public void setShot(final boolean shot) {
        this.shot = shot;
    }

    public void setMouseLocation(final double x, final double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public double getMouseLocationX() {
        return this.mouseX;
    }

    public double getMouseLocationY() {
        return this.mouseY;
    }

}
