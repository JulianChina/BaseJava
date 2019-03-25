package uConcurrency.concurrency;

import net.mindview.util.DaemonThreadFactory;
import net.mindview.util.Print;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory implements Runnable {
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
        ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for (int i = 0; i < 10; i++) {
            exec.execute(new DaemonFromFactory());
        }
        Print.print("All daemons started");
        TimeUnit.MILLISECONDS.sleep(500);
    }
}
