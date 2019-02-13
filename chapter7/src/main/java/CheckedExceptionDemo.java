import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A simple demo on handling or throwing CheckedException
 * As it's a maven module, make sure set the working directory
 * correctly to module dir, or the behavior will be different
 * from expected.
 */
public class CheckedExceptionDemo {

    public static void main(String[] args) {
        File source = new File("src/main/java/ExceptionDemo1.java");
        // even with argument validity check, still need handle exception
        if (source.exists() && source.isFile()) {
            try {
                Scanner scanner = new Scanner(source);
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                // FileNotFoundException is a checked exception
                e.printStackTrace();
            }
        } else {
            System.err.println(source.getAbsolutePath() + " doesn't exist!");
        }

        try {
            readFileWithoutHandling(new File("src/main/java/ExceptionDemo.java"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scan a file and print all its lines
     *
     * @param file  source file to scan and print line by line
     * @throws FileNotFoundException    if the file doesn't exist
     */
    private static void readFileWithoutHandling(File file) throws FileNotFoundException {
        // We don't check whether file exists or is file here
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }

}
