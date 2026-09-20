import java.util.concurrent.ThreadLocalRandom;

public class Auto implements Runnable {
    private String name;
    private static boolean first = false;

    public Auto(String name) {
        this.name = name;
    }

    public void run() {
        int i = 0;
        for (i = 0; i < 10; i++) {
            System.out.printf(this.name + ": lap " + i + "/" + 10 + "\n");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 401));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // Avoiding race condition on first variable
        synchronized (Auto.class) {
            if (!first) {
                System.out.printf(this.name + " "+ "wins!\n");
                first = true;
            }
        }
        System.out.printf(this.name + " has completed the race\n");
    }

    public static void main(String[] args) {
        Auto c1 = new Auto("Ferrari");        
        Auto c2 = new Auto("Mercedes");        
        Auto c3 = new Auto("Pagani");
        Thread th1 = new Thread(c1);
        Thread th2 = new Thread(c2);
        Thread th3 = new Thread(c3);
        th1.start();
        th2.start();
        th3.start();
    }
}
