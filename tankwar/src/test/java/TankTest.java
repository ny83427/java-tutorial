import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TankTest {
    private static class FakeKeyEvent extends KeyEvent {
        private static final Component SOURCE = new JButton();

        private FakeKeyEvent(int keyCode) {
            super(SOURCE, 0, 0, 0, keyCode, (char) keyCode, 0);
        }
    }

    private Tank tank;

    @BeforeEach
    void init() {
        tank = new Tank(40, 40);
    }

    @Test
    void determineDirection() {
        tank.keyPressed(new FakeKeyEvent(KeyEvent.VK_LEFT));
        assertEquals(Direction.Left, tank.direction());
        tank.keyReleased(new FakeKeyEvent(KeyEvent.VK_LEFT));
        assertNull(tank.direction());

        tank.keyPressed(new FakeKeyEvent(KeyEvent.VK_LEFT));
        tank.keyPressed(new FakeKeyEvent(KeyEvent.VK_UP));
        assertEquals(Direction.LeftUp, tank.direction());

        tank.keyReleased(new FakeKeyEvent(KeyEvent.VK_LEFT));
        assertEquals(Direction.Up, tank.direction());
        tank.keyReleased(new FakeKeyEvent(KeyEvent.VK_UP));
        assertNull(tank.direction());

        tank.keyPressed(new FakeKeyEvent(KeyEvent.VK_LEFT));
        tank.keyPressed(new FakeKeyEvent(KeyEvent.VK_UP));
        tank.keyPressed(new FakeKeyEvent(KeyEvent.VK_DOWN));
        assertNull(tank.direction());

        tank.keyReleased(new FakeKeyEvent(KeyEvent.VK_DOWN));
        assertEquals(Direction.LeftUp, tank.direction());
    }
}
