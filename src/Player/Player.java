package Player;

import java.awt.*;

import Entity.ShipEntity;
import Entity.ShotEntity;
import Game.GameMap;
import Game.GamePad;

public abstract class Player {
    private String name;

    private int health;

    private final ShipEntity entity;


    private final GamePad gamepad;

    private long lastShot;

    public final long DELAY_BETWEEN_SHOT = 500;

    private boolean dead;


    public Player() {
        this.entity = new ShipEntity();
        this.entity.setPlayer(this);

        this.gamepad = new GamePad();
        this.health = 100;
        this.dead = true;
    }


    public void spawn(double x, double y) {
        this.entity.setLocation(x, y);
        this.dead = false;
    }


    public void drawDetails(Graphics g, int dx, int dy) {
        // Tính toán vị trí của đối tượng trên màn hình
        int x = new Double(getLocationX() - dx - getWidth() / 2).intValue();
        int y = new Double(getLocationY() - dy - getHeight() / 2).intValue();

        //  tên của đối tượng
        g.setColor(Color.WHITE);
        g.drawString(name, x, y - 15);

        // thanh mau
        g.setColor(Color.RED);
        g.fillRect(x, y - 11, ShipEntity.WIDTH, 3);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 11, health * ShipEntity.WIDTH / 100, 3);
    }

    public void move(long delta) {
        entity.move(delta, gamepad.isUp(), gamepad.isDown(), gamepad.isRight(), gamepad.isLeft());
    }

    public ShotEntity shot(long delta) {
        if (gamepad.isShot() && System.currentTimeMillis() - lastShot > DELAY_BETWEEN_SHOT) {
            lastShot = System.currentTimeMillis();

            double x1 = getLocationX();
            double y1 = getLocationY();

            double x2 = getGamePad().getMouseLocationX();
            double y2 = getGamePad().getMouseLocationY();

            double direction = Math.atan((y2 - y1) / (x2 - x1));

            if (x2 < x1) {
                direction += Math.PI;
            }

            double x = getLocationX() + Math.cos(direction) * getWidth();
            double y = getLocationY() + Math.sin(direction) * getHeight();

            ShotEntity shot = new ShotEntity((int) x, (int) y, direction);

            entity.shot();

            return shot;
        }
        return null;
    }


    public GamePad getGamePad() {
        return this.gamepad;

    }

    public ShipEntity getEntity() {
        return this.entity;
    }

    public String getName() {
        return name;
    }

    public double getLocationX() {
        return this.entity.getLocationX();
    }

    public double getLocationY() {
        return this.entity.getLocationY();
    }

    public double getWidth() {
        return this.entity.getWidth();
    }

    public double getHeight() {
        return this.entity.getHeight();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void hit() {
        this.health -= 15;

        if (this.health <= 0) {
            this.health = 0;

            this.entity.explosion();
            this.dead = true;
        }
    }

    public void impact() {
        this.health -= 100;
        if (this.health <= 0) {
            this.health = 0;

            this.entity.explosion();
            this.dead = true;
        }
    }

    public boolean isDead() {
        return this.dead;
    }
}
