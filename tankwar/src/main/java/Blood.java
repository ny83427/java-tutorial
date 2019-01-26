/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import javax.swing.*;
import java.awt.*;

class Blood extends GameObject {
    private int step;

    /**
     * When player tank's hp is lower than threshold, re-appear with 66.7% possibility
     */
    void reAppearRandomly() {
        this.setLive(Tools.nextInt(4) < 3);
    }

    private final int[][] points = {
        {350, 300}, {360, 300}, {375, 275}, {400, 200}, {360, 270}, {365, 290}, {340, 280}
    };

    private final Image image;

    Blood() {
        this.x = points[0][0];
        this.y = points[0][1];
        this.image = new ImageIcon(this.getClass().getResource("images/blood.png")).getImage();
    }

    @Override
    void draw(Graphics g) {
        g.drawImage(image, x, y, null);
        step++;
        step %= points.length;
        x = points[step][0];
        y = points[step][1];
    }

    @Override
    Rectangle getRectangle() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}
