import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
* Versione che prevede - la chiusura degli sportelli in caso di inattivita',
* - attesa attiva degli utenti che devono entrare nella seconda stanza
* @author Matteo Loporchio
*/

public class UPSol {
    // Numero degli sportelli dell'ufficio
    public static final int numSportelli = 4;
    // Dimensione della coda davanti agli sportelli.
    public static final int dimCoda = 10;
    // Numero di clienti da fare entrare nell'ufficio.
    public static final int numClienti = 500;
    // Tempo di attesa per ritentare di entrare nella seconda stanza se la coda sportelli e' piena.
    public static final long queueDelay = 500;
    // Tempo di attesa per la terminazione del pool.
    public static final long terminationDelay = 5000;
    // Tempo di inattivita' prima della chiusura di uno sportello.
    public static final long closingDelay = 60000;

    public static void main(String[] args) {
        // contatore delle perosne servite
        int count = 0;
        System.out.println("Ufficio aperto\n");
        // Crea la coda (senza limiti di dimensione) per la prima sala
        BlockingQueue<Runnable> coda = new LinkedBlockingQueue<Runnable>();
        // creo il pool di thread personalizzato usando AbortPolicy come politica di
        // rifiuto (ovvero viene sollevata una RejectedExecutionException quando la coda del pool
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                numSportelli,
                numSportelli,
                closingDelay,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(dimCoda),
                new ThreadPoolExecutor.AbortPolicy()
                );
        // Imposto la possibilità di chiusura degli sportelli
        pool.allowCoreThreadTimeOut(true);
        for (int i = 0; i < numClienti; i++) coda.add(new Persona(i)); // prima sala
        while (!coda.isEmpty()) {
            Persona p = (Persona) coda.peek();
            try {
                pool.execute(p);
                coda.poll();
                count++;
            } catch (RejectedExecutionException e) {
                // Se sono qui, significa che la coda davanti gli sportelli è piena (quella del pool)
                // Aspetto un altro po' e ritento
                System.out.printf("Coda piena. " + "Il cliente %d resta in attesa\n", p.id);
                try {
                    Thread.sleep(queueDelay);
                } catch (InterruptedException x) {
                    System.err.println("Interruzione durante sleep");
                }
            }
        }
        // A questo punto chiudo l'ufficio - 1) Attendo un certo tempo affinché i thread possano terminare. 2) PAssato l'intervallo il pool viene chiuso
        pool.shutdown();
        try {
            if (!pool.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS)) pool.shutdown();
        } catch (InterruptedException e) {
            pool.shutdown();
        }
    }        
} 
