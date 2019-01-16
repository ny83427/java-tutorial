import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class TankWar extends JFrame {
	private static final long serialVersionUID = -6766726706227546163L;

	static final int WIDTH = 800, HEIGHT = 600;

    private static final int INIT_ENEMY_TANK_COUNT = 10;

    private static final int DELAY_TIME = 50;

    private Tank tank;
    private Blood blood;
    List<Tank> enemyTanks;
    List<Missile> missiles;
    List<Explode> explodes;
    private Wall wall1, wall2;

    private TankWar() {
        this.tank = new Tank(WIDTH / 2 - Tank.WIDTH / 2, 50);
        this.wall1 = new Wall(100, 200, 20, 150);
        this.wall2 = new Wall(300, 100, 300, 20);
        this.enemyTanks = new ArrayList<>(INIT_ENEMY_TANK_COUNT);
        this.initEnemyTanks();
        this.missiles = new ArrayList<>();
        this.explodes = new ArrayList<>();
        this.blood = new Blood();

        this.setTitle("The Most Boring Tank War Game");
        this.setIconImage(new ImageIcon(this.getClass().getResource("/icon.png")).getImage());
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(400, 100);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBackground(Color.GREEN);

        this.addKeyListener(tank);
        this.setVisible(true);
    }

    private void initEnemyTanks() {
        for (int i = 0; i < INIT_ENEMY_TANK_COUNT; i++) {
            Tank tank = new Tank(50 + 40 * (i + 1), 500, true);
            tank.pointDirection = Direction.Up;
            this.enemyTanks.add(tank);
        }
    }

    private void start() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                while (tank.isLive()) {
                    try {
                        repaint();
                        Tools.sleepSilently(DELAY_TIME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    private Image offScreenImage;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(WIDTH, HEIGHT);
        }

        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
        gOffScreen.setColor(c);

        this.paint(gOffScreen);

        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Default", Font.BOLD, 14));
        g.drawString("Missiles: " + missiles.size(), 10, 50);
        g.drawString("Explodes: " + explodes.size(), 10, 70);
        g.drawString("Enemies: " + enemyTanks.size(), 10, 90);
        g.drawString("HP: " + tank.getHp(), 10, 110);
        g.setColor(c);

        if (enemyTanks.isEmpty()) {
            this.initEnemyTanks();
        }

        for (Missile m : missiles) {
            m.hitTanks(enemyTanks);
            m.hitTank(tank);
            m.hitWall(wall1);
            m.hitWall(wall2);
            m.draw(g);
        }

        for (Explode explode : explodes) {
            explode.draw(g);
        }

        for (Tank tank : enemyTanks) {
            tank.collidesWithWall(wall1);
            tank.collidesWithWall(wall2);
            tank.collidesWithTanks(enemyTanks);
            tank.draw(g);
        }

        tank.draw(g);
        tank.eat(blood);
        wall1.draw(g);
        wall2.draw(g);
        blood.draw(g);
    }

    private static TankWar INSTANCE;

    static TankWar getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TankWar();
        return INSTANCE;
    }

    public static void main(String[] args) {
        Tools.setTheme();
        TankWar.getInstance().start();
    }

}
