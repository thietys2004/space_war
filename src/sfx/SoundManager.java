package sfx;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {
    private String RESOURCES_PATH = "resources/sound/";
    private static SoundManager single = new SoundManager();

    public static SoundManager getInstance() {
        return single;
    }

    HashMap<String, SoundData> soundData = new HashMap<String, SoundData>();

    private SoundData getSoundData(String ref) {
        try {
            URL url = this.getClass().getClassLoader().getResource(RESOURCES_PATH + ref);
            AudioInputStream in = AudioSystem.getAudioInputStream(url);

            AudioFormat format = in.getFormat();


            byte[] data = new byte[format.getFrameSize()];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int length;

            while ((length = in.read(data)) >= 0)
                out.write(data, 0, length);

            in.close();

            data = out.toByteArray();

            SoundData sd = new SoundData(data, format);
            soundData.put(ref, sd);

            return sd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Sound getSound(String ref) throws Exception, UnsupportedAudioFileException, LineUnavailableException {
        if (soundData.get(ref) != null) {
            return new Sound(soundData.get(ref));
        }

        getSoundData(ref);

        return getSound(ref);
    }
}
