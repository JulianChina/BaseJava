package uConcurrency.concurrency;

import zUtils.Print;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BlockedMutex {
    private Lock lock = new ReentrantLock();

    public BlockedMutex() {
        lock.lock();
    }

    public void f() {
        try {
            lock.lockInterruptibly();
            Print.print("lock acquired in f()");
        } catch (InterruptedException e) {
            Print.print("Interrupted from lock acquisition in f()");
        }
    }
}

class Block2 implements Runnable {
    BlockedMutex blocked = new BlockedMutex();

    @Override
    public void run() {
        Print.print("Waiting for f() in BlockedMutex");
        blocked.f();
        Print.print("Broken out of blocked call");
    }
}

public class Interrupting2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Block2());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        Print.print("Issuing t.interrupt()");
        t.interrupt();
    }
}
