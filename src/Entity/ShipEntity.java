package Entity;


import Game.MainGame;
import gfx.Animation;
import gfx.DisplayObject;
import gfx.Fixed;
import gfx.Manager;
import math2d.Force;
import sfx.Sound;
import sfx.SoundManager;

import java.awt.*;
import java.io.IOException;

public class ShipEntity extends MovingEntity {
    public static final double SPEED = 0.01;
    public static final double MAX_SPEED = 0.5;
    public static final double ROTATE_SPEED = Math.PI / 1000;

    public static final int WIDTH = 40;

    public static final int HEIGHT = 40;

    private double direction;
    //gfx
    private DisplayObject currentFrame;

    private Fixed defaultFrame;

    private Animation animationFrame;

    private Animation leftFrame;

    private Animation rightFrame;

    private Animation explosionFrame;
    //sfx
    private Sound shotSound;

    private Sound engineSound;

    private Sound explosionSound;

    private void initGfx() throws Exception {
        defaultFrame = Manager.getInstance().getObject("space_ship.png");
        animationFrame = Manager.getInstance().getObjectAnimation("space_ship_1.png", "space_ship_2.png");
        rightFrame = Manager.getInstance().getObjectAnimation("space_ship_right_1.png", "space_ship_right_2.png");
        leftFrame = Manager.getInstance().getObjectAnimation("space_ship_left_1.png", "space_ship_left_2.png");
        explosionFrame = Manager.getInstance().getObjectAnimation("explosion-1.png",
                "explosion-2.png",
                "explosion-3.png",
                "explosion-4.png",
                "explosion-5.png",
                "explosion-6.png",
                "explosion-7.png",
                "explosion-8.png",
                "explosion-9.png",
                "explosion-10.png",
                "explosion-11.png",
                "explosion-12.png"
        );
        explosionFrame.setLoop(false);

        currentFrame = defaultFrame;
    }

    private void initSfx() throws Exception {
        engineSound = SoundManager.getInstance().getSound("engine.wav");
        explosionSound = SoundManager.getInstance().getSound("explosion.wav");
        shotSound = SoundManager.getInstance().getSound("laser.wav");
    }

    public ShipEntity() {
        super(WIDTH, HEIGHT, MAX_SPEED);
        try {
            initGfx();
            initSfx();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderGfx(Graphics g, int dx, int dy, long delta) {
        if (currentFrame instanceof Animation) {
            ((Animation) currentFrame).frame(delta);
        }
        currentFrame.draw(g, getLocationX() - dx, getLocationY() - dy, direction);
    }

    long deathTime = -1;

    @Override
    public void renderSfx(long delta) {
        shotSound.render(delta);
        engineSound.render(delta);
        explosionSound.render(delta);

        if (explosionSound.isRunning() && deathTime == -1) {
            deathTime = System.currentTimeMillis();
        }
        if (deathTime != -1 && !explosionSound.isRunning()) {
            destroy();
        }
    }

    public void move(long delta, boolean up, boolean down, boolean right, boolean left) {
        currentFrame = defaultFrame;
        if (right) {
            direction += ROTATE_SPEED * delta;
            currentFrame = rightFrame;
        } else if (left) {
            direction -= ROTATE_SPEED * delta;
            currentFrame = leftFrame;
        }
        if (up) {
            currentFrame = animationFrame;
            Force push = new Force();
            push.setX(SPEED * Math.cos(this.direction));
            push.setY(SPEED * Math.sin(this.direction));
            applyForce(push);
        }
        if (up || right || left) {
            engineSound.play(false);
        } else {
            engineSound.stop();
        }

        double x = getLocationX();
        double y = getLocationY();
        if (x <= getWidth()) {
            applyForce(Math.abs(vector.getX() * 1.5), 0);
        }
        if (y <= getHeight()) {
            applyForce(0, Math.abs(vector.getY() * 1.5));
        }

        if (x >= MainGame.Game_width - getWidth()) {
            applyForce(-Math.abs(vector.getX() * 1.5), 0);
        }
        if ((y > MainGame.Game_Height - getHeight())) {
            applyForce(0, -Math.abs(vector.getY() * 1.5));
        }
    }

    @Override
    public void impactWith(Entity orther) {
        if (orther instanceof ShotEntity) {
            getPlayer().hit();
            orther.destroy();
        }
    }

    public void impactWithEntity(Entity e) {
        if (e instanceof ShipEntity) {

            getPlayer().impact();
            e.destroy();
        }
    }

    public double getDirection() {
        return direction;
    }

    public void explosion() {
        currentFrame = explosionFrame;
        explosionSound.play();
    }

    public void shot() {
        shotSound.play(true);
    }


}
