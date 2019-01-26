/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import java.awt.*;

/**
 * An abstract game object that is drawable, can be located and get a rectangle for collision detection
 */
abstract class GameObject {
    int x, y;

    private boolean live = true;

    boolean isLive() {
        return live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    /**
     * <pre>
     * render current object to screen, we don't care about whether it's live or not here
     * it only focuses on draw the object ignoring its status, status management will be
     * centralized in game client
     * </pre>
     *
     * @param g current graphics
     */
    abstract void draw(Graphics g);

    abstract Rectangle getRectangle();

}
