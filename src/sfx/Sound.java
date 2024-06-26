package sfx;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound {
    private SoundData data;
    public int index;
    private boolean running;

    private SourceDataLine sourceData;

    public Sound(SoundData data) {
        this.data = data;
        this.index = 0;
        this.running = false;

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, data.getFormat());
            sourceData = (SourceDataLine) AudioSystem.getLine(info);
            sourceData.open(data.getFormat());
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void render(long delta) {

        if (!running) {
            if (sourceData.isRunning()) {
                sourceData.stop();
            }
            return;
        }


        if (!sourceData.isRunning()) {
            sourceData.start();
        }

        int sampleRate = (int) ((delta * (data.getFormat().getChannels() * data.getFormat().getSampleRate() * data.getFormat().getSampleSizeInBits() / 8)) / 1000);

        int b = Math.min(sampleRate, data.getData().length - index);


        b -= (b % data.getFormat().getFrameSize());

        if (b > 0) {
            sourceData.write(data.getData(), index, b);
            index += b;
        }

        if (index >= data.getData().length) {
            sourceData.stop();
            running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void play() {
        play(false);
    }

    public void play(boolean override) {
        if (running && !override) return;

        running = true;
        index = 0;
    }

    public void stop() {
        running = false;
    }
}
