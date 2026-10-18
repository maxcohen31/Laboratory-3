import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CalcolaPi {
    public static void main(String[] args) {
        int task = 4;
        int lanciPerTask = 1_000_000;

        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<Integer>> risultati = new ArrayList<>();

        try {
            // Sottomette tutti i task
            for (int i = 0; i < task; i++) {
                risultati.add(pool.submit(new LanciaFreccette(lanciPerTask)));
            }
            int totDentro = 0;
            for (Future<Integer> ris : risultati) {
                totDentro += ris.get();
            }

            double pi = 4.0 * totDentro / (task * lanciPerTask);
            System.out.println("Stima di pi: " + pi);
            System.out.println("Valore di pi: " + Math.PI);
        } finally {
        pool.shutdown();
        }
    }
}
