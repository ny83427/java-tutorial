import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * A simple demo on {@link ConcurrentModificationException} and how to
 * remove elements from a collection properly
 */
public class RemoveWhileLoopDemo {

    private List<String> buildNames() {
        List<String> names = new ArrayList<>();
        names.add("Peter");
        names.add("Tom");
        names.add("Jerry");
        names.add("Arthur");
        names.add("Joe");
        return names;
    }

    /**
     * The classic loop will avoid CME if these conditions were met:
     * 1. i < list.size() -> you cannot store a local variable of size
     * as it will change in the process of loop
     * 2. using remove(object) or remove(index) will both work, but index
     * is more sufficient. However, pay attention to List<Integer> case
     */
    private void removeWhileLoop1() {
        List<String> names = this.buildNames();
        for (int i = 0; i < names.size(); i++) {
            System.out.print(i + ", ");
            if (names.get(i).equals("Jerry")) {
                names.remove(i);
            }
        }
        System.out.printf("%n1st %d names: %s%n", names.size(), names);
    }

    private void removeWhileLoop2() {
        List<String> names = this.buildNames();
        try {
            int size = names.size();
            for (int i = 0; i < size; i++) {
                System.out.print(i + ", ");
                if (names.get(i).equals("Jerry")) {
                    names.remove(i);
                }
            }
            System.out.printf("%n2nd %d names: %s%n", names.size(), names);
        } catch (IndexOutOfBoundsException e) {
            System.out.printf("%n:( OOPS! 2nd failed: " + e + "%n");
        }
    }

    private void removeWhileLoop3() {
        List<String> names = this.buildNames();
        try {
            for (String name : names) {
                if (name.equals("Joe")) {
                    // Remove will succeed, but iterate will simply FAIL!
                    names.remove(name);
                }
            }
            System.out.printf("3rd %d names: %s%n", names.size(), names);
        } catch (ConcurrentModificationException e) {
            System.out.println(":( OOPS! 3rd failed: " + e);
        }
    }

    private void removeWhileLoop4() {
        final List<String> names = this.buildNames();
        try {
            int index = 0;
            for (String name : names) {
                if (name.equals("Jerry")) {
                    names.remove(index);
                }
                index++;
            }
            System.out.printf("4th %d names: %s%n", names.size(), names);
        } catch (ConcurrentModificationException e) {
            System.out.println(":( OOPS! 4th failed: " + e);
        }
    }

    private void removeWhileLoop5() {
        final List<String> names = this.buildNames();
        for (Iterator<String> iterator = names.iterator(); iterator.hasNext(); ) {
            String name = iterator.next();
            if (name.equals("Arthur")) {
                iterator.remove();
            }
        }
        System.out.printf("5th %d names: %s%n", names.size(), names);
    }

    private void removeWhileLoop6() {
        final List<String> names = this.buildNames();
        try {
            names.forEach(e -> {
                if (e.equals("Joe")) {
                    names.remove(e);
                }
            });
            System.out.printf("6th %d names: %s%n", names.size(), names);
        } catch (ConcurrentModificationException e) {
            System.out.println(":( OOPS! 6th failed: " + e);
        }
    }

    private void removeWhileLoop7() {
        final List<String> names = this.buildNames();
        names.removeIf(e -> e.equals("Jerry"));
        System.out.printf("7th %d names: %s%n", names.size(), names);
    }

    public static void main(String[] args) {
        final RemoveWhileLoopDemo demo = new RemoveWhileLoopDemo();
        demo.removeWhileLoop1();
        demo.removeWhileLoop2();
        demo.removeWhileLoop3();
        demo.removeWhileLoop4();
        demo.removeWhileLoop5();
        demo.removeWhileLoop6();
        demo.removeWhileLoop7();
    }

}
