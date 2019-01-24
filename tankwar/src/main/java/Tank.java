import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

class Tank extends GameObject implements KeyListener {
    /**
     * width of tank in current direction, using {@link Direction#Down} as initial value
     */
    private int width = 35;
    /**
     * height of tank in current direction, using {@link Direction#Down} as initial value
     */
    private int height = 34;

    private static final int SPEED = 5;

    private static final int BLOOD_BAR_HEIGHT = 10;

    private static final int MAX_HP = 100;

    private static final int LOW_HP_THRESHOLD = 50;

    static final String OBJECT_TYPE = Tank.class.getName().toLowerCase();

    private int hp = MAX_HP;

    private Direction direction;

    private Direction pointDirection = Direction.Down;

    private final boolean enemy;

    Tank(int x, int y) {
        this(x, y, false);
    }

    Tank(int x, int y, boolean enemy) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
    }

    void initDirection(Direction direction) {
        this.direction = direction;
        this.pointDirection = direction;

        Image img = this.pointDirection.get(OBJECT_TYPE);
        width = img.getWidth(null);
        height = img.getHeight(null);
    }

    int getHp() {
        return this.hp;
    }

    void setHp(int hp) {
        this.hp = hp;
    }

    void setLive(boolean live) {
        super.setLive(live);
        if (!this.isLive() && !this.enemy) {
            Tools.playAudio("death.mp3");
        }
    }

    boolean isDying() {
        return this.hp <= LOW_HP_THRESHOLD;
    }

    boolean isEnemy() {
        return this.enemy;
    }

    @Override
    void draw(Graphics g) {
        Image img = this.pointDirection.get(OBJECT_TYPE);
        width = img.getWidth(null);
        height = img.getHeight(null);
        // display blood bar for the tank player can control
        if (!enemy) {
            g.setColor(Color.RED);
            g.drawRect(x, y - BLOOD_BAR_HEIGHT, width, BLOOD_BAR_HEIGHT);
            int availableHPWidth = width * hp / MAX_HP;
            g.fillRect(x, y - BLOOD_BAR_HEIGHT, availableHPWidth, BLOOD_BAR_HEIGHT);

            Image pet = new ImageIcon(this.getClass().getResource("images/pet-camel.gif")).getImage();
            g.drawImage(pet, x - pet.getWidth(null) - 5, y, null);
        }

        g.drawImage(img, x, y, null);
        if (this.direction != null) {
            x = x + this.direction.xFactor * SPEED;
            y = y + this.direction.yFactor * SPEED;
            this.pointDirection = this.direction;
            this.checkBound();
        }
    }

    private static final int BORDER_DELTA_X = 15, BORDER_DELTA_Y = 25;

    private void checkBound() {
        if (x < 0) x = 0;
        if (x > TankWar.WIDTH - width - BORDER_DELTA_X) {
            x = TankWar.WIDTH - width - BORDER_DELTA_X;
        }

        int minY = enemy ? 0 : BLOOD_BAR_HEIGHT;
        if (y < minY) y = minY;
        if (y > TankWar.HEIGHT - height - minY - BORDER_DELTA_Y) {
            y = TankWar.HEIGHT - height - minY - BORDER_DELTA_Y;
        }
    }

    void actRandomly() {
        Direction[] dirs = Direction.values();
        if (step == 0) {
            step = Tools.nextInt(12) + 3;
            int rn = Tools.nextInt(dirs.length);
            pointDirection = direction = dirs[rn];
            if (Tools.nextBoolean())
                this.fire();
        }
        step--;
    }

    private int step = Tools.nextInt(12) + 3;

    @Override
    public void keyTyped(KeyEvent e) {
        // -> Ignore
    }

    private boolean bL, bR, bU, bD;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            TankWar.getInstance().startGame();
        } else if (key == KeyEvent.VK_F2) {
            if (!this.isLive()) {
                this.setLive(true);
                this.hp = MAX_HP;
                TankWar.getInstance().restart();
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
        } else if (key == KeyEvent.VK_LEFT) {
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

    private void fire() {
        Tools.playAudio("shoot.wav");
        this.fire(pointDirection);
    }

    private void superFire() {
        Tools.playAudio(Tools.nextBoolean() ? "supershoot.wav" : "supershoot.aiff");

        Direction[] dirs = Direction.values();
        for (Direction dir : dirs) {
            this.fire(dir);
        }
    }

    private void fire(Direction dir) {
        if (!this.isLive()) return;
        int x = this.x + width / 2 - Missile.WIDTH / 2;
        int y = this.y + height / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, enemy, dir);
        TankWar.getInstance().addMissile(m);
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

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }

    void collidesWithBlood(Blood blood) {
        if (!this.enemy && this.isLive() && blood.isLive() && this.getRectangle().intersects(blood.getRectangle())) {
            this.hp = MAX_HP;
            Tools.playAudio("revive.wav");
            blood.setLive(false);
        }
    }

    void collidesWithWalls(List<Wall> walls) {
        for (Wall wall : walls) {
            if (this.isLive() && this.getRectangle().intersects(wall.getRectangle())) {
                this.turnAround();
                break;
            }
        }
    }

    void collidesWithTanks(List<Tank> tanks) {
        for (Tank t : tanks) {
            if (this == t) continue;

            if (this.isLive() && t.isLive() && this.getRectangle().intersects(t.getRectangle())) {
                if (this.enemy) {
                    // enemy tank will turn back while player tank will wait for control
                    this.turnAround();
                }
                t.turnAround();
                return;
            }
        }
    }

    private void turnAround() {
        if (this.direction == null) return;

        if (direction == Direction.Up || direction == Direction.LeftUp || direction == Direction.RightUp) {
            direction = Direction.Down;
        } else if (direction == Direction.Down || direction == Direction.LeftDown || direction == Direction.RightDown) {
            direction = Direction.Up;
        } else if (direction == Direction.Left) {
            direction = Direction.Right;
        } else {
            direction = Direction.Left;
        }

        x += Tools.nextInt(11);
        y += Tools.nextInt(11);
        if (x >= TankWar.WIDTH * 3 / 4) x = Tools.nextInt(TankWar.WIDTH / 2);
        if (y >= TankWar.HEIGHT * 3 / 4) y = Tools.nextInt(TankWar.HEIGHT - height * 2);
        this.checkBound();
    }
}
