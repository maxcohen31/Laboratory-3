import java.util.concurrent.*;


class LanciaFreccette implements Callable<Integer> {
    private final int lanci;

    public LanciaFreccette(int lanci) {
        this.lanci = lanci;
    }
    @Override
    public Integer call() {
        int dentro = 0;
        for (int i = 0; i < lanci; i++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if (x * x + y * y <= 1) {
                dentro++;
            }
        }
        System.out.println(Thread.currentThread().getName() + ": " + dentro + " punti nel quarto di cerchio");
        return dentro;
    }
}
