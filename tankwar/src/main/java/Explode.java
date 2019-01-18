import javax.swing.*;
import java.awt.*;

class Explode {

    private int x, y;
    private int step = 0;

    private static final Image[] IMAGES = new Image[11];
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
        // display 10 images continuously to simulate animation effect, thus will end after last frame
        boolean live = step < IMAGES.length;
        if (!live) {
            TankWar.getInstance().removeExplode(this);
            step = 0;
            return;
        }

        g.drawImage(IMAGES[step], x, y, null);
        if (step == 0) {
            Tools.playAudio("explode.wav");
        }
        step++;
    }

}
