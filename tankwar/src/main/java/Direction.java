import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Eight directions of game objects, such as {@link Tank}, {@link Missile} and etc.
 * Left, Up, Right, Down and within
 */
enum Direction {
    Left("L", -1, 0),
    LeftUp("LU", -1, -1),
    Up("U", 0, -1),
    RightUp("RU", 1, -1),
    Right("R", 1, 0),
    RightDown("RD", 1, 1),
    Down("D", 0, 1),
    LeftDown("LD", -1, 1);

    final String abbrev;

    final int xFactor;

    final int yFactor;

    Direction(String abbrev, int xFactor, int yFactor) {
        this.abbrev = abbrev;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
    }

    private static Map<String, Image> CACHE = new HashMap<>();

    Image get(String objectType) {
        return CACHE.computeIfAbsent(objectType + this.abbrev,
            key -> Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/" + key + ".gif")));
    }
}
