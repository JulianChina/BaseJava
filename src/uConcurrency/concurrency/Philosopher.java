package uConcurrency.concurrency;

import zUtils.Print;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);

    private void pause() throws InterruptedException {
        if (ponderFactor == 0) {
            return;
        }
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
    }
    public Philosopher(Chopstick left, Chopstick right, int ident, int ponder) {
        this.left = left;
        this.right = right;
        this.id = ident;
        this.ponderFactor = ponder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Print.print(this + " thinking");
                pause();
                Print.print(this + " grabbing right");
                right.take();
                Print.print(this + " grabbing left");
                left.take();
                Print.print(this + " eating");
                pause();
                right.drop();
                left.drop();
            }
        } catch (InterruptedException e) {
            Print.print(this + " exiting via interrupt");
        }
    }

    public String toString() {
        return "Philosopher " + id;
    }
}
