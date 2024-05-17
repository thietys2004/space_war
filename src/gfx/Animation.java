package gfx;

import java.awt.*;

public class Animation extends DisplayObject {
    public static final long DEFAULT_FRAME_DURATION = 75;

    private long lastFrameChange;

    private int FrameNumber;

    private long FrameDuration;

    private final Fixed[] frames;

    private boolean loop;

    //true if object is animation in a loop
    public boolean isLoop() {
        return loop;
    }

    //mark object to animation in loop
    //
    public void setLoop(final boolean loop) {
        this.loop = loop;
    }

    public Animation(final Fixed... frames) {
        super(frames[0].getWidth(), frames[0].getHeight());

        this.frames = frames;
        this.FrameDuration = DEFAULT_FRAME_DURATION;
        this.FrameNumber = 0;
        this.lastFrameChange = 0;
        this.loop = true;
    }

    @Override
    public void draw(final Graphics g, final double x, final double y, final double direction) {
        if (FrameNumber < frames.length) {
            frames[FrameNumber].draw(g, x, y, direction);
        }
    }

    public void frame(final long delta) {
        lastFrameChange += delta;

        if (lastFrameChange > FrameDuration) {
            lastFrameChange = 0;
            FrameNumber++;

            if (FrameNumber >= frames.length && loop) {
                FrameNumber = 0;
            }
        }
    }

    public long getFrameDuration() {
        return FrameDuration;
    }


    public void setFrameDuration(final long FrameDuration) {
        this.FrameDuration = FrameDuration;
    }
}
