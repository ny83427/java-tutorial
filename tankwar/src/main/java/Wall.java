import java.awt.*;

class Wall extends GameObject {

    private final int w, h;

    Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    void setLive(boolean live) {
        throw new UnsupportedOperationException("Hey dude, how dare you set a wall to dead?");
    }

    @Override
    void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, w, h);
        g.setColor(c);
    }

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, w, h);
    }
}
