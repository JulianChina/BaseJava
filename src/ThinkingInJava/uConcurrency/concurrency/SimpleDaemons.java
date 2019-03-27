package ThinkingInJava.uConcurrency.concurrency;

import net.mindview.util.Print;

import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                Print.print(Thread.currentThread() + " " + this);
            }
        } catch (InterruptedException e) {
            Print.print("sleep() interrupted");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread daemon = new Thread(new SimpleDaemons());
            daemon.setDaemon(true);
            daemon.start();
        }
        Print.print(Thread.currentThread() + " All daemons started");
        TimeUnit.MILLISECONDS.sleep(175);
    }
}
