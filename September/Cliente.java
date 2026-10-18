import java.util.concurrent.*

public class Persona implements Runnable {
    // identificativo del cliente
    public final int id;
    // Minimo intervallo di tempo per le operazioni del cliente
    public final long minDelay = 0;
    // Massimo intervallo di tempo per le operazioni del cliente
    public final long maxDelay = 1000;

    /** 
     * Costruttore della classe Persona
     * @param id l'id del cliente
     */
    Persona(int id) {
        this.id = id;
    }
    
    /**
     * Metodo contenente la logica del cliente.
     * Ogni cliente dell'ufficio genera un intervallo di tempo
     * casuale e attende per tale numero di millisecondi prima
     * di terminare.
    */
    @Override
    public void run() {
        System.out.printf("Cliente %d arrivato allo sportello.\n", id);
        long delay = ThreadLocalRandom.current().nextLong(minDelay, maxDelay);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            System.err.println("Interruzione su sleep");
            return;
        }
        System.out.printf("Cliente %d ha abbandonato l'ufficio\n", id);
    }
    
}
