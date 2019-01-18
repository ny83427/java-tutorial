import java.awt.*;

class Wall {

    private final int x, y, w, h;

    Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, w, h);
        g.setColor(c);
    }

    Rectangle getRectangle() {
        return new Rectangle(x, y, w, h);
    }
}
