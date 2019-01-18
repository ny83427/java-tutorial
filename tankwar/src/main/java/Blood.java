import java.awt.*;

class Blood {
    private int x, y;
    private final int w, h;

    private int step = 0;

    private boolean live = true;

    boolean isLive() {
        return live;
    }

    void reAppearRandomly() {
        this.live = Tools.nextInt(4) == 2;
    }

    void disappear() {
        this.live = false;
    }

    private final int[][] points = {
        {350, 300}, {360, 300}, {375, 275}, {400, 200}, {360, 270}, {365, 290}, {340, 280}
    };

    Blood() {
        this.x = points[0][0];
        this.y = points[0][1];
        w = h = 15;
    }

    void draw(Graphics g) {
        if (!live) return;

        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);

        step++;
        step %= points.length;
        x = points[step][0];
        y = points[step][1];
    }

    Rectangle getRectangle() {
        return new Rectangle(x, y, w, h);
    }
}
