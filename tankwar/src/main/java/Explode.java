import javax.swing.*;
import java.awt.*;

class Explode {

    int x, y;
    private boolean live = true;

    int step = 0;

    private static boolean init = false;

    private static Image[] IMAGES = new Image[11];

    static {
        for (int i = 0; i < IMAGES.length; i++) {
            IMAGES[i] = new ImageIcon(Explode.class.getResource("images/" + i + ".gif")).getImage();
        }
    }

    Explode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void draw(Graphics g) {
        if (!init) {
            for (int i = 0; i < IMAGES.length; i++) {
                g.drawImage(IMAGES[i], -100, -100, null);
            }
            init = true;
        }

        if (!live) {
            TankWar.getInstance().explodes.remove(this);
            return;
        }

        if (step == IMAGES.length) {
            live = false;
            step = 0;
            return;
        }

        g.drawImage(IMAGES[step], x, y, null);

        step++;
    }

}
