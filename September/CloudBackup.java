import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Instant;


class CloudBackup implements Runnable {
    private int backup_number = 1;

    public void run() {
        int backup_time = ThreadLocalRandom.current().nextInt(2, 7); // durata casuale tra 2 e 6 secondi
        System.out.println("Uploading backup # " + backup_number + "(" + backup_time + " seconds)\n");

        try {
            Thread.sleep(backup_time * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Backup # " + backup_number + "completed." + Instant.now() + "\n");
        backup_number++;
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); 
        scheduler.scheduleWithFixedDelay(new CloudBackup(), 0, 3, TimeUnit.SECONDS);

        System.out.println("Main is sleeping...\n");
        Thread.sleep(20000);
        scheduler.shutdownNow();
        System.out.println("Scheduler shut down\n");
    }
}
