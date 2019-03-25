package uConcurrency.concurrency;

import zUtils.Print;

public class MoreBasicThreads {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new LiftOff()).start();
        }
        Print.print("Waiting for LiftOff!");
    }
}
