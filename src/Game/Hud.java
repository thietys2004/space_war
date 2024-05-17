package Game;

import Player.Player;

import java.awt.*;
import java.util.Set;

public class Hud {
    private static final int MINI_WIDTH = 100;

    private static final int MINI_HEIGHT = 100;

    private static final int MINI_X = MainGame.MiniMap_Height - MINI_WIDTH - 5;

    private static final int MINI_Y = 5;


    public void draw(final Graphics g, final Player player, final Set<Player> players) {
        g.setColor(Color.RED);
        g.drawRect(MINI_X, MINI_Y, MINI_WIDTH, MINI_HEIGHT);

        int x = (int) player.getLocationX() * MINI_WIDTH / MainGame.Game_width;
        int y = (int) player.getLocationY() * MINI_HEIGHT / MainGame.Game_Height;

        g.setColor(Color.YELLOW);
        g.fillOval(MINI_X + x, MINI_Y + y, 4, 4);

        g.setColor(Color.BLUE);
        for (Player p : players) {
            if (p == player || p.isDead()) {
                continue;
            }

            x = (int) p.getLocationX() * MINI_WIDTH / MainGame.Game_width;
            y = (int) p.getLocationY() * MINI_HEIGHT / MainGame.Game_Height;

            g.fillOval(MINI_X + x, MINI_Y + y, 4, 4);
        }
    }
}
