import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class Cliente implements Runnable {
    public int id = 0;
    // Struttura per rappresentare il catalogo dei prodotti
    public final CopyOnWriteArrayList<String> catalogo;
    // Struttura pet tenere conto delle visualizzazioni di un dato prodotto
    public final ConcurrentHashMap<String, Integer> visualizzazioni;

    Cliente(int id, CopyOnWriteArrayList<String> catalogo, ConcurrentHashMap<String, Integer> visualizzazioni ) {
        this.id = id;
        this.catalogo = catalogo;
        this.visualizzazioni = visualizzazioni;
    }

    @Override
    public void run() {
        for (String p : catalogo) {
            try {
                // tempo per consultare il catalogo
                Thread.sleep(500); 
                // aggiorna la concurrent hashmap delle visualizzazioni
                visualizzazioni.merge(p, 1, Integer::sum);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                return;
            }
        }
    }
}
