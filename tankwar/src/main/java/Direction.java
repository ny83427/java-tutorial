/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import javax.swing.*;
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

    private final String abbrev;

    /**
     * factor to multiply with horizontal moving speed
     */
    final int xFactor;

    /**
     * factor to multiply with vertical moving speed
     */
    final int yFactor;

    Direction(String abbrev, int xFactor, int yFactor) {
        this.abbrev = abbrev;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
    }

    private static final Map<String, Image> CACHE = new HashMap<>();

    /**
     * <pre>
     * get image of current object in giving direction, based on convention over configuration
     * image name should follow pattern of "${objectType}${direction.abbrev}.gif"
     * this would simplify the original approach greatly and reduce a lot of code
     * </pre>
     * @param objectType    object type
     */
    Image get(String objectType) {
        return CACHE.computeIfAbsent(objectType + this.abbrev,
            key -> new ImageIcon(this.getClass().getResource("images/" + key + ".gif")).getImage());
    }
}
