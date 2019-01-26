/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import java.awt.*;

class Field implements Drawable {
    /**
     * Name of the soccer field/stadium, for example:
     * San Siro, Nou Camp, Bernabeu, Old Trafford
     */
    private final String name;

    /**
     * length(width in Swing) of the soccer field in meters
     */
    private final int width;

    /**
     * width(height in Swing) of the soccer field in meters
     */
    private final int height;

    Field(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    /**
     * middle point, where the game begins or refresh after any goal
     */
    Location middle() {
        // What will it happen if we simply return a field called 'middle' here?
        return new Location(this.width / 2, this.height / 2);
    }

    @Override
    public void draw(Graphics g2d) {
        // g2d.drawImage(new ImageIcon(this.getClass().getResource("/field.jpg")).getImage(), 0, 0, null);
        g2d.setColor(new Color(11, 102, 35));
        g2d.fill3DRect(0, 0, width, height, true);

        g2d.setColor(Color.WHITE);
        final int bigForbiddenAreaWidth = 165, bigForbiddenAreaHeight = 360;
        g2d.drawRect(0, (height - bigForbiddenAreaHeight) / 2,
            bigForbiddenAreaWidth, bigForbiddenAreaHeight);
        g2d.drawRect(width - bigForbiddenAreaWidth, (height - bigForbiddenAreaHeight) / 2,
            bigForbiddenAreaWidth, bigForbiddenAreaHeight);

        final int smallForbiddenAreaWidth = 55, smallForbiddenAreaHeight = 180;
        g2d.drawRect(0, (height - smallForbiddenAreaHeight) / 2,
            smallForbiddenAreaWidth, smallForbiddenAreaHeight);
        g2d.drawRect(width - smallForbiddenAreaWidth, (height - smallForbiddenAreaHeight) / 2,
            smallForbiddenAreaWidth, smallForbiddenAreaHeight);

        g2d.drawLine(width / 2, 0, width / 2, height);
        int radius = 50;
        g2d.drawArc(width / 2 - radius, height / 2 - radius, radius * 2, radius * 2, 0, 360);

        g2d.setColor(Color.GRAY);
        g2d.fill3DRect(0, height, width, 40, true);
    }
}
