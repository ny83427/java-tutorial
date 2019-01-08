import java.util.Arrays;

public class NumberGame {
    private static void validateArray(int[] nums, int minLength) {
        if (nums == null || nums.length < minLength) {
            throw new IllegalArgumentException("Please provide an array with " + minLength + " integers at least");
        }
    }

    private static void scanMaxAndMin(int[] numbers) {
        validateArray(numbers, 1);

        int max = numbers[0], min = numbers[0];
        for (int num : numbers) {
            if (num > max) max = num;
            if (num < min) min = num;
        }
        System.out.printf("Max is %d and min is %d%n", max, min);
    }

    private static void scanFirstTwoMaxs(int[] numbers) {
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
        System.out.printf("Max is %d and second max is %d%n", max, secondMax);
    }

    private static void scanFirstThreeMaxs(int[] numbers) {
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
        System.out.printf("Max is %d, second max is %d and third max is %d%n", max, secondMax, thirdMax);
    }

    static int threeSumsBruteForce(int[] numbers) {
        final long start = System.currentTimeMillis();

        int count = 0;
        boolean printed = false;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                for (int k = j + 1; k < numbers.length; k++) {
                    if (numbers[i] + numbers[j] + numbers[k] == 0) {
                        if (count < 100) {
                            System.out.println(String.format("%d + %d + %s = 0", numbers[i], numbers[j], numbers[k]));
                        } else if (!printed) {
                            System.out.println("......");
                            printed = true;
                        }
                        count++;
                    }
                }
            }
        }
        System.out.println(String.format("%d combinations of 3 nums sum of which is zero. Scan %d numbers in %sMS%n",
            count, numbers.length, System.currentTimeMillis() - start));
        return count;
    }

    static int threeSums(int[] numbers) {
        final long start = System.currentTimeMillis();

        int count = 0;
        boolean printed = false;
        Arrays.sort(numbers);
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                int k = Arrays.binarySearch(numbers, j + 1, numbers.length, -numbers[i] - numbers[j]);
                if (k >= 0) {
                    if (count < 100) {
                        System.out.println(String.format("%d + %d + %s = 0", numbers[i], numbers[j], numbers[k]));
                    } else if (!printed) {
                        System.out.println("......");
                        printed = true;
                    }
                    count++;
                }
            }
        }
        System.out.println(String.format("%d combinations of 3 nums sum of which is zero. Scan %d numbers in %sMS%n",
            count, numbers.length, System.currentTimeMillis() - start));
        return count;
    }

    /**
     * https://leetcode.com/problems/3sum/discuss/7399/Easiest-Java-Solution
     */
    static int threeSumsSupportDuplicated(int[] nums) {
        final long start = System.currentTimeMillis();

        int count = 0;
        Arrays.sort(nums);
        boolean printed = false;
        for (int i = 0; i + 2 < nums.length; i++) {
            // skip same result
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = i + 1, k = nums.length - 1;
            int target = -nums[i];
            while (j < k) {
                if (nums[j] + nums[k] == target) {
                    if (count < 100) {
                        System.out.println(String.format("%d + %d + %s = 0", nums[i], nums[j], nums[k]));
                    } else if (!printed) {
                        System.out.println("......");
                        printed = true;
                    }
                    count++;

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

        System.out.println(String.format("%d combinations of 3 nums sum of which is zero. Scan %d numbers in %sMS%n",
            count, nums.length, System.currentTimeMillis() - start));
        return count;
    }

    public static void main(String[] args) {
        int[] numbers = {-1, -2, 3, -4, -5, 9, 10, 11};
        scanMaxAndMin(numbers);
        scanFirstTwoMaxs(numbers);
        scanFirstThreeMaxs(numbers);

        threeSumsBruteForce(numbers);
        threeSums(numbers);

        threeSumsBruteForce(new int[] {-1, -1, -2, -2, 3, -4, -5, 9, 10, 11});
        threeSums(new int[] {-1, -1, -2, -2, 3, -4, -5, 9, 10, 11});
    }

}
