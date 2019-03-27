package ThinkingInJava.uConcurrency.concurrency;

import ThinkingInJava.zUtils.Print;

import java.util.concurrent.TimeUnit;

class ADaemon implements Runnable {
    @Override
    public void run() {
        try {
            Print.print("Starting ADaemon");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Print.print("Exiting via InterruptedException");
        } finally {
            Print.print("This should always run?");
        }
    }
}

public class DaemonsDontRunFinally {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new ADaemon());
        t.setDaemon(true);
        t.start();
        TimeUnit.SECONDS.sleep(1);
    }
}
