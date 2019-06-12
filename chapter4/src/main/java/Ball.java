import javax.swing.*;
import java.awt.*;

class Ball implements Drawable {
    private static final int IMAGE_SIZE = 32;

    private final int size;

    private final Color color;

    private Location location;

    void setLocation(Location location) {
        this.location = location;
    }

    Location getLocation() {
        return this.location;
    }

    Ball(int size, Color color, Location location) {
        this.size = size;
        this.color = color;
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("A Size %d ball with %s, located at %s", size, color.toString(), location);
    }

    @Override
    public void draw(Graphics g) {
        // Or draw an arc from 0 to 360 with giving color
        g.drawImage(new ImageIcon(this.getClass().getResource("/ball.png")).getImage(),
            this.location.x - IMAGE_SIZE / 2, this.location.y - IMAGE_SIZE / 2, null);
    }

    /**
     * Determine whether soccer ball is in net, it can be any side
     */
    boolean checkScore(Score score) {
        boolean scored = (this.location.x <= 50 || this.location.x >= Constants.FIELD_WIDTH - 50) &&
            (this.location.y >= Constants.FIELD_HEIGHT / 2 - 90 && this.location.y <= Constants.FIELD_HEIGHT / 2 + 90);

        if (scored) {
            if (this.location.x <= 50) {
                score.updateVisitingTeam();
            } else {
                score.updateHomeTeam();
            }
        }
        return scored;
    }

    boolean isOutOfField() {
        return this.location.x < 0 || this.location.x > Constants.FIELD_WIDTH ||
            this.location.y < 0 || this.location.y > Constants.FIELD_HEIGHT;
    }
}
