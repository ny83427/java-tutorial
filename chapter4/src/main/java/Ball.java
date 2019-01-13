import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Ball {

    @Getter
    private final int size;

    @Getter
    private final Color color;

    @Setter
    @Getter
    private Location location;

    public Ball(int size, Color color, Location location) {
        this.size = size;
        this.color = color;
        this.location = location;
    }
}
