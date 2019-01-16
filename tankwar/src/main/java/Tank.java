import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

class Tank implements KeyListener {
    static final int SPEED = 5;

    static final int WIDTH = 30, HEIGHT = 30;

    private static final int BLOOD_BAR_HEIGHT = 10;

    private static final int MAX_HP = 100;

    private static final String OBJECT_TYPE = "tank";

    private int x, y;

    private int hp = MAX_HP;

    private boolean live = true;

    private Direction direction;

    Direction pointDirection = Direction.Down;

    private boolean enemy;

    Tank(int x, int y) {
        this(x, y, false);
    }

    Tank(int x, int y, boolean enemy) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
    }

    int getHp() {
        return this.hp;
    }

    void setHp(int hp) {
        this.hp = hp;
    }

    boolean isLive() {
        return this.live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    boolean isEnemy() {
        return this.enemy;
    }

    void draw(Graphics g) {
        if (!live) {
            if (enemy) TankWar.getInstance().enemyTanks.remove(this);
            return;
        }

        Color color = g.getColor();
        // display blood bar for the tank player can control
        if (!enemy) {
            g.setColor(Color.RED);
            g.drawRect(x, y - BLOOD_BAR_HEIGHT, WIDTH, BLOOD_BAR_HEIGHT);
            int availableHPWidth = WIDTH * hp / MAX_HP;
            g.drawRect(x, y - BLOOD_BAR_HEIGHT, availableHPWidth, BLOOD_BAR_HEIGHT);
            g.setColor(color);
        }

        g.drawImage(this.pointDirection.get(OBJECT_TYPE), x, y, null);

        if (this.direction == null) return;

        x = x + this.direction.xFactor * SPEED;
        y = y + this.direction.yFactor * SPEED;
        this.pointDirection = this.direction;
        // Check bound
        if (x < 0) x = 0;
        if (y < HEIGHT) y = HEIGHT;
        if (x > TankWar.WIDTH - WIDTH) x = TankWar.WIDTH - WIDTH;
        if (y > TankWar.HEIGHT - HEIGHT) y = TankWar.HEIGHT - HEIGHT;

        if (enemy) {
            Direction[] dirs = Direction.values();
            if(step == 0) {
                step = RANDOM.nextInt(12) + 3;
                int rn = RANDOM.nextInt(dirs.length);
                direction = dirs[rn];
            }
            step --;
        }
    }

    private static Random RANDOM = new Random();
    private int step = RANDOM.nextInt(12) + 3;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private boolean bL, bR, bU, bD;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_F2) {
            if (!this.live) {
                this.live = true;
                this.hp = MAX_HP;
            }
        } else if (key == KeyEvent.VK_LEFT) {
            bL = true;
        } else if (key == KeyEvent.VK_UP) {
            bU = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            bR = true;
        } else if (key == KeyEvent.VK_DOWN) {
            bD = true;
        }
        this.determineDirection();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_CONTROL) {
            fire();
        } else if (key == KeyEvent.VK_A) {
            superFire();
        } if (key == KeyEvent.VK_LEFT) {
            bL = false;
        } else if (key == KeyEvent.VK_UP) {
            bU = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            bR = false;
        } else if (key == KeyEvent.VK_DOWN) {
            bD = false;
        }
        this.determineDirection();
    }

    Missile fire() {
        if (!live) return null;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, enemy, pointDirection);
        TankWar.getInstance().missiles.add(m);
        return m;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        for (Direction dir : dirs) {
            fire(dir);
        }
    }

    private Missile fire(Direction dir) {
        if (!live) return null;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, enemy, dir);
        TankWar.getInstance().missiles.add(m);
        return m;
    }

    private void determineDirection() {
        if (bL && !bU && !bR && !bD) direction = Direction.Left;
        else if (bL && bU && !bR && !bD) direction = Direction.LeftUp;
        else if (!bL && bU && !bR && !bD) direction = Direction.Up;
        else if (!bL && bU && bR && !bD) direction = Direction.RightUp;
        else if (!bL && !bU && bR && !bD) direction = Direction.Right;
        else if (!bL && !bU && bR) direction = Direction.RightDown;
        else if (!bL && !bU && bD) direction = Direction.Down;
        else if (bL && !bU && !bR) direction = Direction.LeftDown;
        else if (!bL && !bU) direction = null;
    }

    Rectangle getRectangle() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    void eat(Blood blood) {
        if (this.live && blood.isLive() && this.getRectangle().intersects(blood.getRectangle())) {
            this.hp = MAX_HP;
            blood.setLive(false);
        }
    }

    boolean collidesWithWall(Wall w) {
        if (this.live && this.getRectangle().intersects(w.getRectangle())) {
            this.stay();
            return true;
        }
        return false;
    }

    boolean collidesWithTanks(java.util.List<Tank> tanks) {
        for (Tank t : tanks) {
            if (this != t) {
                if (this.live && t.isLive() && this.getRectangle().intersects(t.getRectangle())) {
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    private void stay() {
        this.direction = null;
    }
}
