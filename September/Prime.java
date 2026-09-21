import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

public class Prime implements Callable<Integer> {
    private int n;

    Prime(int n) {
        this.n = n;
    }

    public boolean is_prime(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int i = 3; i*i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public Integer call() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (is_prime(i)) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.printf("Insert the number of threads to be created: ");
        int n_thread = input.nextInt();
        ArrayList<Future<Integer>> thread_list = new ArrayList<>();
        ExecutorService exe = Executors.newFixedThreadPool(n_thread);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n_thread; i++) {
            Prime task = new Prime(10000000);
            Future<Integer> future1 = exe.submit(task);
            thread_list.add(future1);
        }
        for (int i = 0; i < n_thread; i++) {
            try {
                thread_list.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        exe.shutdown();
        input.close();
        long end = System.currentTimeMillis();
        System.out.printf("Elapsed time: %d\n", end - start);
    }
}
