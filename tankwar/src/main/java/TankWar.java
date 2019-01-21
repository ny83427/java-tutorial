import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class TankWar extends JComponent {
    private static final long serialVersionUID = -6766726706227546163L;

    static final int WIDTH = 800, HEIGHT = 600;

    private static final int INIT_ENEMY_TANK_ROWS = 3;

    private static final int INIT_ENEMY_TANK_COUNT = 12;

    private static final int REPAINT_INTERVAL = 50;

    private Tank tank;
    private Blood blood;
    private List<Tank> enemyTanks;
    private List<Missile> missiles;
    private List<Explode> explodes;
    private final Wall[] walls;
    private final AtomicInteger enemiesKilled = new AtomicInteger(0);

    private TankWar() {
        this.walls = new Wall[] {
            new Wall(250, 100, 300, 20),
            new Wall(100, 200, 20, 150),
            new Wall(680, 200, 20, 150)
        };
        this.init();
    }

    private void init() {
        this.tank = new Tank(WIDTH / 2, 50);
        this.tank.initDirection(Direction.Down);
        // there would be read and write concurrently
        this.enemyTanks = new CopyOnWriteArrayList<>();
        this.initEnemyTanks();
        this.missiles = new CopyOnWriteArrayList<>();
        this.explodes = new CopyOnWriteArrayList<>();
        this.blood = new Blood();
        // Attention! As game can restart again and gain, key listener would be accumulated again and again
        // As a result, previous listeners must be removed before add current tank
        KeyListener[] listeners = this.getKeyListeners();
        for (KeyListener keyListener : listeners) {
            this.removeKeyListener(keyListener);
        }
        this.addKeyListener(this.tank);
    }

    Blood getBlood() {
        return this.blood;
    }

    void removeEnemyTank(Tank tank) {
        this.enemiesKilled.addAndGet(1);
        this.enemyTanks.remove(tank);
    }

    void addMissile(Missile missile) {
        this.missiles.add(missile);
    }

    void removeMissile(Missile missile) {
        this.missiles.remove(missile);
    }

    void removeExplode(Explode explode) {
        this.explodes.remove(explode);
    }

    void addExplode(Explode explode) {
        this.explodes.add(explode);
    }

    private void initEnemyTanks() {
        for (int i = 0; i < INIT_ENEMY_TANK_ROWS; i++) {
            for (int j = 0; j < INIT_ENEMY_TANK_COUNT / INIT_ENEMY_TANK_ROWS; j++) {
                Tank tank = new Tank(100 + 100 * (j + 1), 300 + i * 50, true);
                tank.initDirection(Direction.Up);
                this.enemyTanks.add(tank);
            }
        }
    }

    void restart() {
        this.enemiesKilled.set(0);
        this.init();
        this.start();
    }

    void start() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                while (tank.isLive()) {
                    try {
                        repaint();
                        Tools.sleepSilently(REPAINT_INTERVAL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // once for "Game Over" rendering
                repaint();
                return null;
            }
        }.execute();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!tank.isLive()) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Default", Font.BOLD, 100));
            g.drawString("GAME OVER", 80, HEIGHT / 2 - 40);
            g.setColor(Color.WHITE);

            g.setFont(new Font("Default", Font.BOLD, 50));
            g.drawString("Press F2 to Start", 180, HEIGHT / 2 + 60);
            return;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Default", Font.BOLD, 14));
        g.drawString("Missiles: " + missiles.size(), 10, 50);
        g.drawString("Explodes: " + explodes.size(), 10, 70);
        g.drawString("Our Tank HP: " + tank.getHp(), 10, 90);
        g.drawString("Enemies Left: " + enemyTanks.size(), 10, 110);
        g.drawString("Enemies Killed: " + enemiesKilled.get(), 10, 130);

        if (enemyTanks.isEmpty()) {
            this.initEnemyTanks();
        }

        for (Missile m : missiles) {
            m.hitTanks(enemyTanks);
            m.hitTank(tank);
            m.hitWalls(walls);
            m.draw(g);
        }

        for (Explode explode : explodes) explode.draw(g);

        for (Tank tank : enemyTanks) {
            tank.collidesWithWalls(walls);
            tank.collidesWithTanks(enemyTanks);
            tank.draw(g);
        }

        tank.draw(g);
        // allow our tank to ignore collides rules currently
        // tank.collidesWithWalls(walls);
        // tank.collidesWithTanks(enemyTanks);
        tank.eat(blood);
        for (Wall wall : walls) wall.draw(g);
        blood.draw(g);
    }

    private static TankWar INSTANCE;

    static TankWar getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TankWar();
        return INSTANCE;
    }

    public static void main(String[] args) {
        Platform.startup(() -> {});
        Tools.setTheme();
        JFrame frame = new JFrame("The Most Boring Tank War Game");
        frame.setIconImage(new ImageIcon(TankWar.class.getResource("/icon.png")).getImage());
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(400, 100);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        TankWar tankWar = TankWar.getInstance();
        frame.add(tankWar);
        // KeyListeners need to be on the focused component to work
        tankWar.setFocusable(true);
        frame.setVisible(true);
    }

}
