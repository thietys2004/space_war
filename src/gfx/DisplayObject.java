package gfx;

import java.awt.*;


// Represents a sprite to be displayed on the screen.

public abstract class DisplayObject {
    //width of object
    protected int width;
    //height of object
    protected int height;

    //defines size of object
    public DisplayObject(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract void draw(Graphics g, double x, double y, double direction);
}
