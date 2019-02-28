import java.util.ArrayList;
import java.util.List;

/**
 * A simple demo on thread safe issue and the necessity to make test
 * after you made any change - even those you believe will never make
 * mistake - too many mistakes have been made actually
 */
class ThreadSafeDemo {

    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();
        list.add("Tom");
        list.add("Nate");
        list.add("Sarah");

        // You can comment 3 synchronized blocks in the below to test what will happen
        Thread[] threads = {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " started at " + System.currentTimeMillis());
                synchronized (list) {
                    for (String s : list) {
                        System.out.println(Thread.currentThread().getName() + "\t" + s);
                    }
                }
                System.out.println(Thread.currentThread().getName() + " ended at " + System.currentTimeMillis());
            }),
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " started at " + System.currentTimeMillis());
                synchronized (list) {
                    for (int i = 0; i < list.size(); i++) {
                        System.out.printf("%s\t%s(Index: %d / List Size: %d)%n",
                            Thread.currentThread().getName(), list.get(i), i, list.size());
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println(Thread.currentThread().getName() + " ended at " + System.currentTimeMillis());
            }),
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " started at " + System.currentTimeMillis());
                int index = 1;
                System.out.printf("%s \tElement at index %d will be removed.%n",
                    Thread.currentThread().getName(), index);
                synchronized (list) {
                    list.remove(index);
                }
                System.out.printf("%s \tElement at index %d was removed.%n",
                    Thread.currentThread().getName(), index);
                System.out.println(Thread.currentThread().getName() + " ended at " + System.currentTimeMillis());
            })
        };

        for (Thread thread : threads) {
            thread.start();
        }
    }


}
