package ThinkingInJava.uConcurrency.concurrency;

import net.mindview.util.Print;

public class BasicThreads {
    public static void main(String[] args) {
        Thread t = new Thread(new LiftOff());
        t.start();
        Print.print("Waiting for LiftOff!");
    }
}
