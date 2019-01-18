import java.awt.*;
import java.util.List;

class Missile {
    private static final int SPEED = 10;

    static final int WIDTH = 10, HEIGHT = 10;

    static final String OBJECT_TYPE = Missile.class.getName().toLowerCase();

    private int x, y;
    private final Direction dir;

    private boolean enemy;
    private boolean live = true;

    private Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    Missile(int x, int y, boolean enemy, Direction dir) {
        this(x, y, dir);
        this.enemy = enemy;
    }

    void draw(Graphics g) {
        if (!live) {
            TankWar.getInstance().removeMissile(this);
        } else {
            g.drawImage(dir.get(OBJECT_TYPE), x, y, null);
            x += dir.xFactor * SPEED;
            y += dir.yFactor * SPEED;
            if (x < 0 || x > TankWar.WIDTH || y < 0 || y > TankWar.HEIGHT) {
                live = false;
            }
        }
    }

    private Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    private static final int LETHALITY = 20;

    boolean hitTank(Tank tank) {
        if (this.live && this.getRectangle().intersects(tank.getRectangle()) &&
            tank.isLive() && this.enemy != tank.isEnemy()) {
            if (!tank.isEnemy()) {
                tank.setHp(tank.getHp() - LETHALITY);
                if (tank.getHp() <= 0) {
                    tank.setLive(false);
                } else if (tank.isDying()) {
                    TankWar.getInstance().getBlood().reAppearRandomly();
                }
            } else {
                tank.setLive(false);
            }

            this.live = false;
            Explode e = new Explode(x, y);
            TankWar.getInstance().addExplode(e);
            return true;
        }
        return false;
    }

    void hitTanks(List<Tank> tanks) {
        for (Tank tank : tanks) {
            if (hitTank(tank)) {
                return;
            }
        }
    }

    void hitWalls(Wall[] walls) {
        for (Wall wall : walls) {
            boolean hit = this.live && this.getRectangle().intersects(wall.getRectangle());
            if (hit) {
                this.live = false;
                break;
            }
        }
    }
}
