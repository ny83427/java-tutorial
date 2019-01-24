import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final List<Wall> walls;
    private int enemiesKilled;

    private TankWar() {
        this.walls = Arrays.asList(
            new Wall(250, 100, 300, 20),
            new Wall(100, 200, 20, 150),
            new Wall(680, 200, 20, 150)
        );
        this.init();
    }

    private void init() {
        this.tank = new Tank(WIDTH / 2, 50);
        this.tank.initDirection(Direction.Down);
        this.enemyTanks = new ArrayList<>();
        this.initEnemyTanks();
        this.missiles = new ArrayList<>();
        this.explodes = new ArrayList<>();
        this.blood = new Blood();
        // Attention! As game can restart again and gain, key listener would be accumulated again and again
        // As a result, previous listeners must be removed before add current tank
        KeyListener[] listeners = this.getKeyListeners();
        for (KeyListener keyListener : listeners) {
            this.removeKeyListener(keyListener);
        }
        this.addKeyListener(this.tank);
    }

    void addMissile(Missile missile) {
        this.missiles.add(missile);
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
        this.enemiesKilled = 0;
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
                        triggerEvent();
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
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Default", Font.BOLD, 14));
            g.drawString("Missiles: " + missiles.size(), 10, 50);
            g.drawString("Explodes: " + explodes.size(), 10, 70);
            g.drawString("Our Tank HP: " + tank.getHp(), 10, 90);
            g.drawString("Enemies Left: " + enemyTanks.size(), 10, 110);
            g.drawString("Enemies Killed: " + enemiesKilled, 10, 130);

            this.drawGameObjects(missiles, g);
            this.drawGameObjects(explodes, g);
            this.drawGameObjects(explodes, g);
            this.drawGameObjects(enemyTanks, g);
            this.drawGameObjects(walls, g);

            this.drawGameObject(tank, g);
            this.drawGameObject(blood, g);
        }
    }

    private <T extends GameObject> void drawGameObjects(List<T> objects, Graphics g) {
        // Don't use foreach or iterator here to avoid java.util.ConcurrentModificationException
        // Or use CopyOnWriteArrayList instead of ArrayList so that foreach can be safely used
        for (int i = 0; i < objects.size(); i++) {
            this.drawGameObject(objects.get(i), g);
        }
    }

    private <T extends GameObject> void drawGameObject(T obj, Graphics g) {
        if (obj != null && obj.isLive()) {
            obj.draw(g);
        }
    }

    private void triggerEvent() {
        if (enemyTanks.isEmpty()) {
            this.initEnemyTanks();
        }

        missiles.removeIf(m -> !m.isLive());
        // Use classic loop to prevent CME
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(enemyTanks);
            m.hitTank(tank);
            m.hitWalls(walls);
        }

        int prev = enemyTanks.size();
        enemyTanks.removeIf(e -> !e.isLive());
        enemiesKilled += (prev - enemyTanks.size());
        enemyTanks.forEach(e -> {
            e.actRandomly();
            e.collidesWithWalls(walls);
            e.collidesWithTanks(enemyTanks);
        });

        if (tank.isDying()) {
            this.blood.reAppearRandomly();
        }
        tank.collidesWithWalls(walls);
        tank.collidesWithTanks(enemyTanks);
        tank.collidesWithBlood(blood);

        explodes.removeIf(e -> !e.isLive());
    }

    private static TankWar INSTANCE;

    /**
     * Singleton: lazy initial
     */
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
