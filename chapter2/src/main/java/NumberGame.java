public class NumberGame {

    private static void scanMaxAndMin(int[] numbers) {
        int max = 0, min = 0;
        // Your stuff here
        System.out.printf("Max is %d and min is %d", max, min);
    }

    private static void scanFirstTwoMaxs(int[] numbers) {
        int max = 0, secondMax = 0;
        // Your stuff here
        System.out.printf("Max is %d and second max is %d", max, secondMax);
    }

    private static void scanFirstThreeMaxs(int[] numbers) {
        int max = 0, secondMax = 0, thirdMax = 0;
        // Your stuff here
        System.out.printf("Max is %d, second max is %d and third max is %d", max, secondMax, thirdMax);
    }

    private static void threeSums(int[] numbers) {
        int count = 0;
        // Your stuff here
        System.out.printf("There are %d combinations of 3 numbers that sum of them is 0", count);
    }

    public static void main(String[] args) {
        int[] numbers = {-1, -2, 3, -4, -5, 9, 10, 11};
        scanMaxAndMin(numbers);
        scanFirstTwoMaxs(numbers);
        scanFirstThreeMaxs(numbers);
        threeSums(numbers);
    }

}
