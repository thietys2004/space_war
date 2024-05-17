package Entity;

import Player.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    private final Rectangle2D rect;

    private boolean destroyed;

    private Player player;

    public Entity(final double width, final double height) {
        this.destroyed = false;
        this.rect = new Rectangle2D.Double(0, 0, width, height);
    }


    public boolean isDestroyed() {
        return destroyed;
    }


    public void destroy() {
        this.destroyed = true;
    }


    public Player getPlayer() {
        return player;
    }


    public void setPlayer(final Player player) {
        this.player = player;
    }


    public double getLocationX() {
        return this.rect.getCenterX();
    }

    public double getLocationY() {
        return this.rect.getCenterY();
    }


    public Rectangle2D getRectangle() {
        return this.rect;
    }

    public void setLocation(double x, double y) {
        this.rect.setRect(x - rect.getWidth() / 2, y - rect.getHeight() / 2, rect.getWidth(), rect.getHeight());
    }

    public double getWidth() {
        return this.rect.getWidth();
    }


    public double getHeight() {
        return this.rect.getHeight();
    }


    public abstract void renderGfx(final Graphics g, int dx, int dy, long delta);


    public void renderSfx(long delta) {

    }


    public Point2D getLocation() {
        return new Point2D.Double(this.rect.getCenterX(), this.rect.getCenterY());
    }


    public double distanceTo(final Entity entity) {
        return this.getLocation().distance(entity.getLocation());
    }


    public void impactWith(final Entity entity) {
        //
    }

    public void impactWithEntity(final Entity entity) {

    }

    public boolean impactesWithEntity(final Entity entity) {
        return rect.intersects(entity.getRectangle());
    }

    public boolean impactesWith(final Entity other) {
        return rect.intersects(other.rect);
    }


    public void interactWith(final Entity entity) {

    }
}
