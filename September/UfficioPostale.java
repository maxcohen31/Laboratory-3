/** 
 *  Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. Nell'ufficio
    esiste:

    ● un'ampia sala d'attesa in cui ogni persona può entrare liberamente. Quando
    entra, ogni persona prende il numero dalla numeratrice e aspetta il proprio
    turno in questa sala.

    ● una seconda sala, meno ampia, posta davanti agli sportelli, in cui si può
    entrare solo a gruppi di k persone

    • una persona si mette quindi prima in coda nella prima sala, poi passa nella
    seconda sala.
    
    • ogni persona impiega un tempo differente per la propria operazione allo
    sportello. Una volta terminata l'operazione, la persona esce dall'ufficio

    Scrivere un programma in cui:

    ● l'ufficio viene modellato come una classe JAVA, in cui viene attivato un
    ThreadPool di dimensione uguale al numero degli sportelli
    
    ●la coda delle persone presenti nella sala d'attesa è gestita esplicitamente
    dal programma

    ● la seconda coda (davanti agli sportelli) è quella gestita implicitamente dal
    ThreadPool

    ● ogni persona viene modellata come un task, un task che deve essere
    assegnato ad uno dei thread associati agli sportelli
    
    ● si preveda di far entrare tutte le persone nell'ufficio postale, all'inizio del
    programma
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.BlockingQueue;

public class UfficioPostale { 
    
    // Fist and second hall
    public BlockingQueue<Cliente> first_hall = new LinkedBlockingQueue<>();
    private int first_hall_capacity;
    private int k; // Clients allowed in the socond hall
    private int desk = 4;
    
    UfficioPostale(int first_hall_capacity, int k) {
        this.first_hall_capacity = first_hall_capacity;
        this.k = k;
        this.desk = 4;
    }

    // Each customer enters the first waiting hall
    public void open_first_hall() {
        for (int i = 0; i < first_hall_capacity; i++) {
            first_hall.add(new Cliente(i));
        }
    }

    public void serve_client() {
        ExecutorService worker = Executors.newFixedThreadPool(desk);
        while (!first_hall.isEmpty()) {
            int n = Math.min(k, first_hall.size());
            for (int i = 0; i < n; i++) {
                try {
                    worker.submit(first_hall.take());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } 
            } 
        }
        worker.shutdown();
    } 

    // Simulates a person at the postal office
    static class Cliente implements Runnable {

        private int client_number = 0;

        Cliente(int ticket) {
            this.client_number = ticket;
        }    

        public void run() {
            System.out.println("Serving the client #" + client_number + "\n");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000)); // Simulates the serving time 
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Client #" + client_number + " served\n");
        }

    }
    
    public static void main(String[] args) {
        UfficioPostale up = new UfficioPostale(100, 10);
        up.open_first_hall();
        up.serve_client();
    }
}


