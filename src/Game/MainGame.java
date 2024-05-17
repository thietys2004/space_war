package Game;

import Entity.*;

import Player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class MainGame extends JFrame {

    public final static String GAME_NAME = "War Space";

    public final static int MiniMap_Width = 1024;

    public final static int MiniMap_Height = 768;

    public final static int Game_width = 1920;

    public final static int Game_Height = 1200;

    public final static double Player_X = MiniMap_Width * 0.5;

    public final static double Player_Y = MiniMap_Height * 0.5;

    private final BufferStrategy strategy;

    private Graphics2D g2;

    private final GameMap map;

    private final Hud hud;

    private final Player local_player;


    private final Set<Player> players = new HashSet<Player>();

    private Set<Entity> entities = new HashSet<Entity>();

    private Set<Entity> entitiesToRemove = new HashSet<Entity>();

    private Rectangle2D visible = new Rectangle2D.Double();

    public MainGame(String characterName) {
        super(GAME_NAME);
        setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(GAME_NAME);

        //tao con tro chuot
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        getContentPane().setCursor(blankCursor);

        // Tạo Canvas để vẽ đồ họa
        Canvas can = new Canvas();
        can.setPreferredSize(new Dimension(MiniMap_Width, MiniMap_Height));
        add(can, BorderLayout.CENTER);

        pack();
        setVisible(true);


        can.setIgnoreRepaint(true);
        can.createBufferStrategy(2);


        strategy = can.getBufferStrategy();
        g2 = (Graphics2D) strategy.getDrawGraphics();

        map = new GameMap();
        hud = new Hud();
        local_player = new LocalPlayer();


        can.addKeyListener(new GameKeyListener());
        can.addMouseListener(new GameMouseListener());


        for (int i = 0; i < 4; i++) {
            final BotPlayer bot = new BotPlayer();
            bot.setName("bot " + i);
            addPlayer(bot);
            new Thread() {
                public void run() {
                    while (true) {
                        int i = (int) (Math.random() * 100) % 4;

                        switch (i) {
                            case 0:
                                bot.getGamePad().setLeft(!bot.getGamePad().isLeft());
                                break;
                            case 1:
                                bot.getGamePad().setRight(!bot.getGamePad().isRight());
                                break;
                            case 2:
                                bot.getGamePad().setUp(!bot.getGamePad().isUp());
                                break;
                            case 3:
                                bot.getGamePad().setShot(!bot.getGamePad().isShot());
                                break;
                            case 4:
                                bot.getGamePad().setDown(!bot.getGamePad().isDown());
                                break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        characterName = JOptionPane.showInputDialog(this, "Nhập tên nhân vật:", "Nhập tên", JOptionPane.PLAIN_MESSAGE);
        if (characterName != null && !characterName.isEmpty()) {
            local_player.setName(characterName);
        } else {
            local_player.setName("Người chơi");
        }
        addPlayer(local_player);

        long lastLoopTime = System.currentTimeMillis();

        while (true) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            frame(delta);

            try {
                long loopDuration = System.currentTimeMillis() - lastLoopTime;
                long l = 1000 / 60 - loopDuration;
                Thread.sleep(1 < 0 ? 0 : 1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private int dx, dy;

    private void frame(long delta) {
        if (local_player.getEntity().getLocationX() < MainGame.Player_X) {

            dx = 0;

        } else if (local_player.getEntity().getLocationX() > MainGame.Game_width - MainGame.Player_X) {

            dx = MainGame.Game_width - MainGame.MiniMap_Width;

        } else {

            dx = (int) (local_player.getEntity().getLocationX() - MainGame.Player_X);

        }


        if (local_player.getEntity().getLocationY() < MainGame.Player_Y) {

            dy = 0;

        } else if (local_player.getEntity().getLocationY() > MainGame.Game_Height - MainGame.Player_Y) {
            dy = MainGame.Game_Height - MainGame.MiniMap_Height;
        } else {
            dy = (int) (local_player.getEntity().getLocationY() - MainGame.Player_Y);
        }
        visible.setRect(dx, dy, MiniMap_Width, MiniMap_Height);

        map.draw(g2, (int) local_player.getEntity().getLocationX(), (int) local_player.getEntity().getLocationY(), dx, dy);

        hud.draw(g2, local_player, players);

        for (Entity entity : entities) {
            // Phá hủy thực thể nếu nó được đánh dấu là bị hủy
            if (entity.isDestroyed() || isOutOfArea(entity)) {
                entitiesToRemove.add(entity);
                continue;
            }
            if (visible.intersects(entity.getRectangle())) {
                entity.renderGfx(g2, dx, dy, delta);
            }

            entity.renderSfx(delta);

            if (entity instanceof MovingEntity) {
                ((MovingEntity) entity).move(delta);
            }

            // va chạm và tương tác
            for (Entity other : entities) {
                if (other == entity) continue;
                other.interactWith(entity);
                if (entity.impactesWith(other)) {
                    other.impactWith(entity);

                }
            }
            for (Entity other : entities) {
                if (other == entity) continue;
                other.interactWith(entity);
                if (entity.impactesWithEntity(other)) {
                    other.impactWithEntity(entity);
                }

            }


        }

        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();

        for (Player player : players) {
            if (player.isDead()) {
                continue;
            }

            player.move(delta);

            player.drawDetails(g2, dx, dy);

            ShotEntity shot = player.shot(delta);
            if (shot != null) {
                entities.add(shot);
            }
        }
        //con tro
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        local_player.getGamePad().setMouseLocation(mouseLocation.getX() + dx - this.getX(), mouseLocation.getY() + dy - this.getY());
        g2.setColor(Color.CYAN);
        g2.fillRect((int) local_player.getGamePad().getMouseLocationX() - dx - 5, (int) local_player.getGamePad().getMouseLocationY() - dy - 5, 10, 10);


        if (this.local_player.isDead()) {
            endGame(false);
        } else {
            boolean allDead = true;
            for (Player p : players) {
                if (p == local_player) {
                    continue;
                }
                allDead &= p.isDead();
                if (!allDead) {
                    break;
                }
            }
            if (allDead) {
                endGame(true);
            }
        }
        strategy.show();
    }

    private void endGame(boolean victory) {
        String[] options = {"Chơi Lại", "Quit"};
        int choice = JOptionPane.showOptionDialog(this, victory ? "Bạn đã thắng !" : "Bạn thua rồi!", "Kết thúc trò chơi",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == JOptionPane.YES_OPTION) {

//            MainGame mainGame = new MainGame(local_player.getName());
//            mainGame.setVisible(true); // Hiển thị MainGame mới
//            dispose(); // Đóng cửa sổ hiện tại
//            setVisible(true);
//            entities.clear();
//            players.clear();
//            players.add(local_player);
//            frame(0);
        } else {
            // Thực hiện hành động khi chọn Quit
            System.exit(0);
        }
    }

    private void addPlayer(Player player) {
        double x = Math.random() * Game_width;
        double y = Math.random() * Game_Height;

        player.spawn(x, y);

        this.players.add(player);
        this.entities.add(player.getEntity());
    }

    private boolean isOutOfArea(final Entity entity) {
        Rectangle area = new Rectangle(0, 0, MainGame.Game_width, MainGame.Game_Height);
        return !area.intersects(entity.getRectangle());
    }

    private class GameKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                local_player.getGamePad().setUp(true);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                local_player.getGamePad().setDown(true);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                local_player.getGamePad().setLeft(true);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                local_player.getGamePad().setRight(true);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                local_player.getGamePad().setShot(true);
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                local_player.getGamePad().setUp(false);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                local_player.getGamePad().setDown(false);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                local_player.getGamePad().setLeft(false);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                local_player.getGamePad().setRight(false);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                local_player.getGamePad().setShot(false);
            }
        }
    }

    private class GameMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            local_player.getGamePad().setShot(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            local_player.getGamePad().setShot(false);
        }
    }


    public static void main(String arg[]) {
        new MainGame(null);
    }

}
