/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DirectionTest {
    private final String[] objectTypes = {Tank.OBJECT_TYPE, Missile.OBJECT_TYPE};

    @Test
    void get() {
        for (Direction d : Direction.values()) {
            for (String objType : objectTypes) {
                assertNotNull(d.get(objType));
            }
        }
    }
}