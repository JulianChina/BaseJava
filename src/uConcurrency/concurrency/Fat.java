package uConcurrency.concurrency;

import zUtils.Print;

public class Fat {
    private volatile double d;
    private static int counter = 0;
    private final int id = counter++;

    public Fat() {
        for (int i = 0; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double) i;
        }
    }

    public void operation() {
        Print.print(this);
    }

    public String toString() {
        return "Fat id: " + id;
    }
}
