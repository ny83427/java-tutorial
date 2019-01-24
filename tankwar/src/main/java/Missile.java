import java.awt.*;
import java.util.List;

class Missile extends GameObject {
    private static final int SPEED = 10;

    static final int WIDTH = 10, HEIGHT = 10;

    static final String OBJECT_TYPE = Missile.class.getName().toLowerCase();

    private final Direction dir;

    private boolean enemy;

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
        g.drawImage(dir.get(OBJECT_TYPE), x, y, null);
        x += dir.xFactor * SPEED;
        y += dir.yFactor * SPEED;
        if (x < 0 || x > TankWar.WIDTH || y < 0 || y > TankWar.HEIGHT) {
            this.setLive(false);
        }
    }

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    private static final int LETHALITY = 20;

    boolean hitTank(Tank tank) {
        if (this.isLive() && this.getRectangle().intersects(tank.getRectangle()) &&
            tank.isLive() && this.enemy != tank.isEnemy()) {
            if (!tank.isEnemy()) {
                tank.setHp(tank.getHp() - LETHALITY);
                if (tank.getHp() <= 0) {
                    tank.setLive(false);
                }
            } else {
                tank.setLive(false);
            }

            this.setLive(false);
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

    void hitWalls(List<Wall> walls) {
        for (Wall wall : walls) {
            boolean hit = this.isLive() && this.getRectangle().intersects(wall.getRectangle());
            if (hit) {
                this.setLive(false);
                break;
            }
        }
    }
}
