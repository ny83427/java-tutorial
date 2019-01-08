import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(Lifecycle.PER_CLASS)
class NumberGameTest {
    private int[] thousandInts, twoThousandInts, eightThousandInts, millionInts;

    @BeforeAll
    void init() {
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
    void threeSumsWithDuplicates() {
        int[] nums = {-1, -1, -2, -2, 3, -4, -5, 9, 10, 11};
        assertEquals(2, NumberGame.threeSumsSupportDuplicated(nums));
        assertEquals(2, NumberGame.threeSums(nums));
        assertEquals(2, NumberGame.threeSumsBruteForce(nums));
    }

    @Test
    void threeSums() {
        NumberGame.threeSums(thousandInts);
        NumberGame.threeSumsBruteForce(twoThousandInts);
        NumberGame.threeSumsBruteForce(eightThousandInts);
    }

    @Test
    void performance() {
        NumberGame.threeSums(millionInts);
    }

    @Test
    void threeSumsBruteForce() {
        NumberGame.threeSumsBruteForce(thousandInts);
        NumberGame.threeSumsBruteForce(twoThousandInts);
        NumberGame.threeSumsBruteForce(eightThousandInts);
    }
}