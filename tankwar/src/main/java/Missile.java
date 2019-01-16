import java.awt.*;

class Missile {
    static final int SPEED = 10;

    static final int WIDTH = 10, HEIGHT = 10;

    private static final String OBJECT_TYPE = "missile";

    Point point;
    Direction dir;

    private boolean enemy;
    private boolean live = true;

    Missile(int x, int y, Direction dir) {
        this.point = new Point(x, y);
        this.dir = dir;
    }

    Missile(int x, int y, boolean enemy, Direction dir) {
        this(x, y, dir);
        this.enemy = enemy;
    }

    void draw(Graphics g) {
        if (!live) {
            TankWar.getInstance().missiles.remove(this);
        } else {
            g.drawImage(dir.get(OBJECT_TYPE), point.x, point.y, null);
            point.translate(dir.xFactor * SPEED, dir.yFactor * SPEED);
            if (point.x < 0 || point.x > TankWar.WIDTH || point.y < 0 || point.y > TankWar.HEIGHT) {
                live = false;
            }
        }
    }

    boolean isLive() {
        return live;
    }

    Rectangle getRectangle() {
        return new Rectangle(point.x, point.y, WIDTH, HEIGHT);
    }

    boolean hitTank(Tank t) {
        if (this.live && this.getRectangle().intersects(t.getRectangle()) && t.isLive() && this.enemy != t.isEnemy()) {
            if (!t.isEnemy()) {
                t.setHp(t.getHp() - 20);
                if (t.getHp() <= 0) t.setLive(false);
            } else {
                t.setLive(false);
            }

            this.live = false;
            Explode e = new Explode(point.x, point.y);
            TankWar.getInstance().explodes.add(e);
            return true;
        }
        return false;
    }

    boolean hitTanks(java.util.List<Tank> tanks) {
        for (Tank tank : tanks) {
            if (hitTank(tank)) {
                return true;
            }
        }
        return false;
    }

    boolean hitWall(Wall wall) {
        boolean hit = this.live && this.getRectangle().intersects(wall.getRectangle());
        if (hit) this.live = false;
        return hit;
    }
}
