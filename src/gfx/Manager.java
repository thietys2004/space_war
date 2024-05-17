package gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class Manager {
    private final String RESOURCES_PATH = "resources/ship/";

    private static Manager single = new Manager();

    public static Manager getInstance() {
        return single;
    }

    private HashMap<String, BufferedImage> object = new HashMap<String, BufferedImage>();

    private BufferedImage getSourceImage(String ref) throws IOException {

        BufferedImage sourceImage = null;

        URL url = this.getClass().getClassLoader().getResource(RESOURCES_PATH + ref);

//        if (url == null) {
//            throw new Exception("Image not found: " + RESOURCES_PATH + ref);
//        }
        sourceImage = ImageIO.read(url);
        object.put(ref, sourceImage);
        return sourceImage;
    }

    public Fixed getObject(String ref) throws IOException {

        if (object.get(ref) != null) {

            return new Fixed(object.get(ref));

        }

        getSourceImage(ref);
        return getObject(ref);

    }

    public Animation getObjectAnimation(String... ref) throws IOException {

        Fixed object[] = new Fixed[ref.length];

        for (int i = 0; i < ref.length; i++) {

            object[i] = getObject(ref[i]);

        }
        return new Animation(object);
    }
}
