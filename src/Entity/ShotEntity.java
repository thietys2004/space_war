package Entity;

import gfx.Animation;
import gfx.DisplayObject;
import gfx.Manager;

import java.awt.*;

public class ShotEntity extends MovingEntity {

    private static final double speed = 0.8;

    private DisplayObject currentFrame;

    private transient Animation exploderFrame;

    public ShotEntity(final int x, final int y, final double direction) {
        super(10, 10, speed);
        setLocation(x, y);
        try {
            this.currentFrame = Manager.getInstance().getObject("greenLaserRay.png");
            this.exploderFrame = Manager.getInstance().getObjectAnimation("greenLaserRay.png");
        } catch (Exception e) {

        }
        vector.setX(speed * Math.cos(direction));

        vector.setY(speed * Math.sin(direction));
    }

    @Override
    public void renderGfx(final Graphics g, final int dx, final int dy, final long delta) {
        this.currentFrame.draw(g, getLocationX() - dx, getLocationY() - dy, this.vector.getDirection());
    }

    @Override
    //quan ly va cham
    public void impactWith(final Entity other) {
        this.currentFrame = this.exploderFrame;
    }
}
