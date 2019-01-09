import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
class NumberGameTest {
    private int[] thousandInts, twoThousandInts, eightThousandInts, millionInts;

    @BeforeAll
    void init() {
        // These test files belong to Robert Sedgewick and Kevin Wayne of Princeton University
        // https://algs4.cs.princeton.edu/14analysis/ThreeSum.java.html
        thousandInts = buildArray("1Kints.txt");
        twoThousandInts = buildArray("2Kints.txt");
        eightThousandInts = buildArray("8Kints.txt");
        millionInts = buildArray("1Mints.txt");
    }

    private int[] buildArray(String fileName) {
        try {
            Stream<String> stream = Files.lines(Paths.get(this.getClass().getResource("/" + fileName).toURI()));
            return stream.mapToInt(e -> Integer.valueOf(e.trim())).toArray();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void scanMaxAndMin() {
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanMaxAndMin(null));
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanMaxAndMin(new int[]{}));

        assertArrayEquals(new int[]{6, 1}, NumberGame.scanMaxAndMin(new int[]{1, 2, 3, 4, 5, 6}));
        assertArrayEquals(new int[]{6, 1}, NumberGame.scanMaxAndMin(new int[]{6, 5, 4, 3, 2, 1}));
        assertArrayEquals(new int[]{1, 1}, NumberGame.scanMaxAndMin(new int[]{1}));
        assertArrayEquals(new int[]{1, 1}, NumberGame.scanMaxAndMin(new int[]{1, 1, 1, 1}));
    }

    @Test
    void scanFirstTwoMaxs() {
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstTwoMaxs(null));
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstTwoMaxs(new int[]{}));
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstTwoMaxs(new int[]{1}));

        assertArrayEquals(new int[]{3, 2}, NumberGame.scanFirstTwoMaxs(new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{3, 2}, NumberGame.scanFirstTwoMaxs(new int[]{3, 2, 1}));
        assertArrayEquals(new int[]{1, 1}, NumberGame.scanFirstTwoMaxs(new int[]{1, 1, 1, 1}));
        assertArrayEquals(new int[]{7, 7}, NumberGame.scanFirstTwoMaxs(new int[]{1, 1, 7, 7}));
    }

    @Test
    void scanFirstThreeMaxs() {
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstThreeMaxs(null));
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstThreeMaxs(new int[]{}));
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstThreeMaxs(new int[]{1}));
        assertThrows(IllegalArgumentException.class, () -> NumberGame.scanFirstThreeMaxs(new int[]{1, 2}));

        assertArrayEquals(new int[]{3, 2, 1}, NumberGame.scanFirstThreeMaxs(new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{6, 5, 4}, NumberGame.scanFirstThreeMaxs(new int[]{6, 5, 4, 3, 2, 1}));
        assertArrayEquals(new int[]{1, 1, 1}, NumberGame.scanFirstThreeMaxs(new int[]{1, 1, 1, 1}));
        assertArrayEquals(new int[]{7, 7, 7}, NumberGame.scanFirstThreeMaxs(new int[]{1, 1, 1, 7, 7, 7}));
        assertArrayEquals(new int[]{1, -1, -2}, NumberGame.scanFirstThreeMaxs(new int[]{-1, -2, -3, 1}));
    }

    @Test
    void uniqueThreeSums() {
        int[] nums = {-1, -1, -2, -2, 3, -4, -5, 9, 10, 11};
        assertEquals(2, NumberGame.uniqueThreeSums(nums).size());

        nums = new int[]{-1, -1, -2, -2, 3, 3, -4, -5, 9, 10, 11};
        assertEquals(2, NumberGame.uniqueThreeSums(nums).size());
    }

    @Test
    void threeSumsBruteForceWithoutDuplicates() {
        assertEquals(70, NumberGame.threeSumsBruteForce(thousandInts).size());
        assertEquals(528, NumberGame.threeSumsBruteForce(twoThousandInts).size());
    }

    @Test
    void uniqueThreeSumsWithoutDuplicates() {
        assertEquals(70, NumberGame.uniqueThreeSums(thousandInts).size());
        assertEquals(528, NumberGame.uniqueThreeSums(twoThousandInts).size());
    }

    @Disabled("It's not that good to be a millionaire\uD83D\uDE05")
    void performance() {
        NumberGame.threeSums(millionInts);
        NumberGame.threeSums(eightThousandInts);
    }

    @Test
    void threeSumWithDuplicates() {
        assertEquals(3, NumberGame.threeSums(new int[]{-1, -1, -2, -2, 3, 3, -4, -5, 9, 10, 11}).size());
        assertEquals(2, NumberGame.threeSums(new int[]{-1, -1, -2, -2, 3, 4, -4, -5, 9, 10, 11}).size());

        assertNotEquals(3, NumberGame.threeSumsBruteForce(new int[]{-1, -1, -2, -2, 3, 3, -4, -5, 9, 10, 11}).size());
        assertNotEquals(2, NumberGame.threeSumsBruteForce(new int[]{-1, -1, -2, -2, 3, 4, -4, -5, 9, 10, 11}).size());

        assertNotEquals(3, NumberGame.uniqueThreeSums(new int[]{-1, -1, -2, -2, 3, 3, -4, -5, 9, 10, 11}).size());
    }

}