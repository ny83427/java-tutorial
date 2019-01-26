/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <pre>
 * Implementation notes:
 * 1. Check argument: is there any restriction and proper handling?
 * 2. Corner case: will the result become incorrect in certain circumstances?
 * 3. Performance tuning: where is the hotpoint and how to fix it?
 * 4. Code style: is your code formatted clearly and organized purposely? are the log messages helpful?
 * 5. More considerations: thread safe? message internationalization?
 * </pre>
 *
 * @author Nathanael Yang
 */
public class Fibonacci {
    private int lastCacheIndex = -1;

    private BigInteger[] cache = new BigInteger[153];

    private void validateArgument(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Only positive number is meaningful to find " +
                "corresponding Fibonacci number. However, your input is " + n);
        }
    }

    BigInteger calc(int n) {
        this.validateArgument(n);

        // initialize the first and second number in cache
        if (lastCacheIndex == -1) {
            cache[0] = cache[1] = BigInteger.ONE;
            lastCacheIndex = 1;
        }

        // check capacity and resize array if necessary
        if (n > cache.length) {
            BigInteger[] newCache = new BigInteger[Math.max(cache.length * 2, n)];
            System.arraycopy(cache, 0, newCache, 0, lastCacheIndex + 1);
            cache = newCache;
        }

        // return cached result if hit
        if (cache[n - 1] != null) {
            return cache[n - 1];
        }

        // built cache from last cached index on to avoid duplicated calculation
        for (int i = lastCacheIndex + 1; i < n; i++) {
            cache[i] = cache[i - 1].add(cache[i - 2]);
        }
        lastCacheIndex = n - 1;

        return cache[n - 1];
    }

    private BigInteger calcNaively(int n) {
        this.validateArgument(n);
        return n <= 2 ? BigInteger.ONE : (calcNaively(n - 1).add(calcNaively(n - 2)));
    }

    private static final long[] TIME_RANGES = {1000 * 60 * 60, 1000 * 60, 1000};
    private static final String[] TIME_LABELS = {"hours", "minutes", "seconds"};

    private static String formatElapsedTime(long elapsed) {
        for (int i = 0; i < TIME_RANGES.length; i++) {
            if (elapsed >= TIME_RANGES[i]) {
                return String.format("%.2f %s", elapsed * 1.0f / TIME_RANGES[i], TIME_LABELS[i]);
            }
        }

        return elapsed + " ms";
    }

    public static void main(String[] args) {
        Fibonacci fib = new Fibonacci();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            try {
                int n = scanner.nextInt();
                boolean useOptimized = scanner.next().toUpperCase().startsWith("O");
                String method = useOptimized ? "Optimized" : "Naive";
                final long start = System.currentTimeMillis();

                BigInteger result = useOptimized ? fib.calc(n) : fib.calcNaively(n);
                System.out.printf("Fibonacci number of %d is %s. %s calculation spent %s.%n",
                    n, result, method, formatElapsedTime(System.currentTimeMillis() - start));
            } catch (InputMismatchException e) {
                System.err.println(scanner.next() + " is not a valid integer, please correct your input!");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
