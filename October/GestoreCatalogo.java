import java.util.concurrent.CopyOnWriteArrayList;


public class GestoreCatalogo extends Thread {
    public CopyOnWriteArrayList<String> catalogo;

    GestoreCatalogo(CopyOnWriteArrayList<String> catalogo) {
        this.catalogo = catalogo;
    }

    @Override
    public void run() {
        catalogo.add("Mouse");
    } 
}
