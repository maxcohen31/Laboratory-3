import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;

public class Negozio {
    public CopyOnWriteArrayList<String> catalogo;
    public ConcurrentHashMap<String, Integer> prodotti;

    Negozio(CopyOnWriteArrayList<String> catalogo, ConcurrentHashMap<String, Integer> prodotti) {
        this.catalogo = catalogo;
        this.prodotti = prodotti;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CopyOnWriteArrayList<String> catalogoIniziale = new CopyOnWriteArrayList<>();
        catalogoIniziale.add("Tastiere");
        catalogoIniziale.add("Cuffie");
        catalogoIniziale.add("Telecamere");
        ConcurrentHashMap<String, Integer> visualizzazioni = new ConcurrentHashMap<>();
        for (String n: catalogoIniziale) {
            visualizzazioni.put(n, 0);
        }
        Negozio store = new Negozio(catalogoIniziale, visualizzazioni);

        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<?>> fut = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fut.add(pool.submit(new Cliente(i, catalogoIniziale, visualizzazioni))); 
        }
        // Attendo i primi 10 thread
        for (Future<?> f : fut) {
            f.get();
        }

        // Aggiungo il mouse al catalogo
        GestoreCatalogo g = new GestoreCatalogo(catalogoIniziale); 
        g.start();
        g.join();
        fut.clear();

        for (int i = 10; i < 20; i++) {
            fut.add(pool.submit(new Cliente(i, catalogoIniziale, visualizzazioni)));
        }
        
        for (Future<?> f : fut) {
            f.get();
        }

        pool.shutdown();

        for (Map.Entry<String, Integer> entry: visualizzazioni.entrySet()) {
            String k = entry.getKey();
            Integer v = entry.getValue();
            System.out.printf("%s: %d", k, v);
            System.out.println("\n");
        }
    }
}
