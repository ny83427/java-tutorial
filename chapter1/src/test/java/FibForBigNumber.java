/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import java.math.BigInteger;
import java.util.Arrays;

/**
 * <pre>
 * This is the solution submitted by Jia Zhao, which I made some slight modifications on code
 * style plus some comments for other students to understand.
 *
 * It uses an integer array of 100 elements to store segments of a big number in little endian
 * style, each segment would store a number from 0 to 10^{@value #DIGIT_LENGTH} - 1.
 * To add two 'numbers', we will calculate each segment and process carry correspondingly.
 * After that, we locate the last non-zero segment and output in reversed order and add necessary
 * leading zeros.
 *
 * Its upper limit would be 10^({@value #DIGIT_LENGTH}*{@value #MAX_NUMBER_COUNTS}) - 1.
 *
 * It's a good example to understand the mechanism of BigInteger, though its implementation
 * is much more complex and efficient
 *
 * Original source: https://github.com/AriseshineSky/java-version/blob/master/Fibonacci.java
 * </pre>
 */
class FibForBigNumber {
    private static final int MAX_NUMBER_COUNTS = 100;

    /**
     * 2147483647(2.15 * 10^9) is max value of int, thus we can set threshold to 10^{@value}
     */
    private static final int DIGIT_LENGTH = 8;

    /**
     * Threshold for processing carry, for example, if we store 6 digits, then for
     * 12999999 + 33000002 = 46000001, we will process carry of 1 to value of 12 + 33 to 46 finally
     */
    private static final int VALUE_FOR_CARRY = (int) Math.pow(10, DIGIT_LENGTH + 1);

    private static int[] add(int[] f1, int[] f2) {
        int[] f3 = new int[MAX_NUMBER_COUNTS];
        for (int i = 0; i < MAX_NUMBER_COUNTS; i++) {
            f3[i] = f1[i] + f2[i];
        }

        processCarry(f3);
        return f3;
    }

    private static void processCarry(int[] nums) {
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            if (nums[i] >= VALUE_FOR_CARRY) {
                nums[i + 1] += nums[i] / VALUE_FOR_CARRY;
                nums[i] = nums[i] % VALUE_FOR_CARRY;
            }
        }
    }

    private static void calc(int n) {
        int[][] fb = new int[n + 1][MAX_NUMBER_COUNTS];
        fb[0][0] = 0;
        fb[1][0] = 1;

        for (int i = 2; i <= n; i++) {
            fb[i] = add(fb[i - 1], fb[i - 2]);
        }

        if (DEBUG) Arrays.stream(fb).forEach(arr -> System.out.println(Arrays.toString(arr)));

        int index = MAX_NUMBER_COUNTS - 1;
        // little endian style approach, locate the last non-zero as highest
        while (fb[n][index] == 0) index--;
        System.out.print(fb[n][index]);
        for (int i = index - 1; i >= 0; i--) {
            // add leading zeros before output
            int digits = String.valueOf(fb[n][i]).length();
            for (int j = 0; j < DIGIT_LENGTH - digits; j++) {
                System.out.print('0');
            }
            System.out.print(fb[n][i]);
        }
        System.out.println();
    }

    private static boolean DEBUG;

    public static void main(String[] args) {
        DEBUG = args.length > 0 && args[0].toUpperCase().startsWith("D");
        calc(2048);
        calc(10);

        String fib2048 = "4541530443743789425045571446290689202700908261" +
            "293644428951182390278971452509283435684349718034771730433207742075010299663962" +
            "500640783801879736380774181591579496806948995766259226048959686056348436218766" +
            "394283482473000979306575217575924408151880646518264800221975575899556551648206" +
            "461735151382670421151734360292599059971022927693971037208141410991471449358204" +
            "4185153918055170241694035610145547104337536614028338983073680262684101";
        BigInteger integer = new BigInteger(fib2048);
        System.out.println(integer.toString());
        System.out.println(integer.add(BigInteger.TEN).toString());
        System.out.println(integer.multiply(BigInteger.TEN).toString());
    }

}
