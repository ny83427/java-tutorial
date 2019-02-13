import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Simple demo of RuntimeException concept and handling
 */
public class ExceptionDemo {

    private static void processUserAge(Scanner scanner) {
        while (true) {
            try {
                int age = scanner.nextInt();
                System.out.println("You are " + age + " years old.");
                break;
            } catch (InputMismatchException e) {
                // Attention: without `scanner.next()` it will be a infinite loop!
                System.err.println(scanner.next() + " is not a valid age!" +
                    "Please correct it and repeat");
            }
        }
    }

    private static void processUserAgeViaChecking(Scanner scanner) {
        while (true) {
            String str = scanner.nextLine();
            boolean invalid = false;
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    invalid = true;
                    break;
                }
            }

            if (invalid) {
                System.err.println(str + " is not a valid age!" +
                    "Please correct it and repeat");
            } else {
                int age = Integer.valueOf(str);
                System.out.println("You are " + age + " years old.");
                break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // RuntimeException cases
        processUserAge(scanner);
        processUserAgeViaChecking(scanner);

        scanner.close();
    }

}
