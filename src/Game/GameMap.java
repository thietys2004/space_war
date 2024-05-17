package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class GameMap {
    private BufferedImage backgroundImage;

    public GameMap() {
        try {
            URL url = this.getClass().getClassLoader().getResource("resources/background/space_map1.png");
            this.backgroundImage = ImageIO.read(url);
        } catch (Exception e) {
        }
    }

    public void draw(final Graphics g, final int localPlayerX, final int localPlayerY, final int dx, final int dy) {
        g.drawImage(backgroundImage, 0, 0, MainGame.MiniMap_Width, MainGame.MiniMap_Height, dx, dy, dx + MainGame.MiniMap_Width, dy + MainGame.MiniMap_Height, null);
    }
}
