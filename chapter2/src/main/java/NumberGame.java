import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Solution of Chapter 2 Assignments
 *
 * @author Nathanael Yang  2019/01/08 10:09 AM
 */
public class NumberGame {

    /**
     * validate a given integer array to check whether there are at least certain count of elements
     *
     * @param nums  the integer array
     * @param minLength minimum length of the integer array
     * @throws IllegalArgumentException if <code>minLength</code> is non-positive,
     *  or the array is <code>null</code>, or has less than <code>minLength</code> elements
     */
    private static void validateArray(int[] nums, int minLength) {
        if (minLength <= 0) {
            throw new IllegalArgumentException("Minimum array length " + minLength + " is not a positive integer");
        }
        if (nums == null || nums.length < minLength) {
            throw new IllegalArgumentException("Please provide an array with " + minLength + " integers at least");
        }
    }

    /**
     * <pre>
     * scan a given integer array and find the maximum, minimum number
     * the purpose of this exercise if about control flow, statements, logical expressions
     * and how to test your code in normal cases and corner cases
     * </pre>
     *
     * @param numbers the integer array
     * @return an integer array with max first and min second
     * @throws IllegalArgumentException if given integer array is <code>null</code> or empty
     */
    static int[] scanMaxAndMin(int[] numbers) {
        validateArray(numbers, 1);

        int max = numbers[0], min = numbers[0];
        for (int num : numbers) {
            if (num > max) max = num;
            if (num < min) min = num;
        }
        System.out.printf("Max of %s is %d and min is %d%n", Arrays.toString(numbers), max, min);
        // Range<T extends Comparable> range = new Range<>(min, max);
        return new int[]{max, min};
    }

    /**
     * scan a given integer array and find the maximum, second maximum number
     *
     * @param numbers the integer array
     * @return an integer array of max and second max number
     * @throws IllegalArgumentException if given integer array is <code>null</code> or less than 2 elements
     */
    static int[] scanFirstTwoMaxs(int[] numbers) {
        validateArray(numbers, 2);

        int max = Integer.MIN_VALUE, secondMax = Integer.MIN_VALUE;
        for (int num : numbers) {
            if (num > max) {
                secondMax = max;
                max = num;
            } else if (num > secondMax) {
                secondMax = num;
            }
        }
        System.out.printf("Max of %s is %d and second max is %d%n", Arrays.toString(numbers), max, secondMax);
        return new int[]{max, secondMax};
    }

    /**
     * scan a given integer array and find the maximum, second maximum, and third maximum number
     *
     * @param numbers the integer array
     * @return an integer array of max, second max, and third max number
     * @throws IllegalArgumentException if given integer array is <code>null</code> or less than 3 elements
     */
    static int[] scanFirstThreeMaxs(int[] numbers) {
        validateArray(numbers, 3);

        int max = Integer.MIN_VALUE, secondMax = Integer.MIN_VALUE, thirdMax = Integer.MIN_VALUE;
        for (int num : numbers) {
            if (num > max) {
                thirdMax = secondMax;
                secondMax = max;
                max = num;
            } else if (num > secondMax) {
                thirdMax = secondMax;
                secondMax = num;
            } else if (num > thirdMax) {
                thirdMax = num;
            }
        }
        System.out.printf("Max of %s is %d, second max is %d and third max is %d%n",
            Arrays.toString(numbers), max, secondMax, thirdMax);
        return new int[]{max, secondMax, thirdMax};
    }

    /**
     * Brute force strategy to find combinations of three sums sum of them is zero
     * Time complexity O(n^3), which can be improved to O(n^2 * log(n))
     */
    static List<int[]> threeSumsBruteForce(int[] nums) {
        if (nums == null || nums.length < 3) return null;

        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        list.add(new int[]{nums[i], nums[j], nums[k]});
                    }
                }
            }
        }
        return list;
    }

    /**
     * Sort the array first, then using binary search to gain speed improvement<br/>
     * Time complexity O(n^2 * log(n)), certain tips such as early exit will also help
     */
    static List<int[]> threeSums(int[] nums) {
        if (nums == null || nums.length < 3) return null;

        List<int[]> list = new ArrayList<>();
        // Keep track of each indexes, once they are found their status will change
        int[] indicators = new int[nums.length];

        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            // early exit
            if (nums[i] > 0) break;

            for (int j = i + 1; j < nums.length; j++) {
                int k = Arrays.binarySearch(nums, j + 1, nums.length, -nums[i] - nums[j]);
                if (k >= 0 && indicators[i] == 0 && indicators[j] == 0 && indicators[k] == 0) {
                    list.add(new int[]{nums[i], nums[j], nums[k]});
                    indicators[i] = indicators[j] = indicators[k] = 1;
                }
            }
        }
        return list;
    }

    /**
     * <pre>
     * https://leetcode.com/problems/3sum/discuss/7399/Easiest-Java-Solution
     *
     * Attention: this solution only accept unique combinations, which is not exactly same to the context of
     * our number game. For example, [-2, -2, -1, -1, 3, 3, 4] should return 2 as per our requirement, but
     * this approach will return 1 since [-2, -1, 3] occurred twice
     * </pre>
     */
    static List<int[]> uniqueThreeSums(int[] nums) {
        if (nums == null || nums.length < 3) return null;

        List<int[]> list = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i + 2 < nums.length; i++) {
            // since array is sorted, there is no way that the rest integers sum of which can be zero
            if (nums[i] > 0) break;

            // skip same result
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int j = i + 1, k = nums.length - 1;
            int target = -nums[i];
            while (j < k) {
                if (nums[j] + nums[k] == target) {
                    list.add(new int[]{nums[i], nums[j], nums[k]});

                    j++;
                    k--;
                    // skip same result
                    while (j < k && nums[j] == nums[j - 1]) j++;
                    while (j < k && nums[k] == nums[k + 1]) k--;
                } else if (nums[j] + nums[k] > target) {
                    k--;
                } else {
                    j++;
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws NoSuchMethodException,
        InvocationTargetException, IllegalAccessException {
        int[][] numbers = {
            {-1, -2, 3, -4, -5, 9, 10, 11},
            {-1, -1, -2, -2, 3, -4, -5, 9, 8, 4}
        };
        for (int[] nums : numbers) {
            scanMaxAndMin(nums);
            scanFirstTwoMaxs(nums);
            scanFirstThreeMaxs(nums);
        }

        // using reflection to reduce duplicated codes for troubleshooting purpose
        // students can simply ignore implementation details but just get the motivation
        Method[] methods = {
            NumberGame.class.getDeclaredMethod("threeSumsBruteForce", int[].class),
            NumberGame.class.getDeclaredMethod("threeSums", int[].class),
            NumberGame.class.getDeclaredMethod("uniqueThreeSums", int[].class),
        };

        int maxDisplayCount = 100;
        for (int[] nums : numbers) {
            for (Method method : methods) {
                final long start = System.currentTimeMillis();
                @SuppressWarnings("unchecked")
                List<int[]> list = (List<int[]>) method.invoke(null, nums);
                for (int i = 0; i < Math.min(maxDisplayCount, list.size()); i++) {
                    int[] arr = list.get(i);
                    System.out.println(String.format("%d + %d + %d = 0", arr[0], arr[1], arr[2]));
                }
                System.out.println(String.format("%d combinations of 3 nums found by `%s`. Scan %d numbers in %sMS%n",
                    list.size(), method.getName(), nums.length, System.currentTimeMillis() - start));
            }
        }
    }

}
