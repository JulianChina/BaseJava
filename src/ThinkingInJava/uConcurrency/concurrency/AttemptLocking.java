package ThinkingInJava.uConcurrency.concurrency;

import ThinkingInJava.zUtils.Print;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();
    public void untimed() {
        boolean caputured = lock.tryLock();
        try {
            Print.print("tryLock(): " + caputured);
        } finally {
            if (caputured) {
                lock.unlock();
            }
        }
    }
    public void timed() {
        boolean caputured = false;
        try {
            caputured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Print.print("tryLock(2, TimeUnit.SECONDS): " + caputured);
        } finally {
            if (caputured) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final AttemptLocking al = new AttemptLocking();
        al.untimed();
        al.timed();
        new Thread() {
            {setDaemon(true);}

            @Override
            public void run() {
                al.lock.lock();
                Print.print("acquired");
            }
        }.start();
        Thread.yield();
        al.untimed();
        al.timed();
    }
}
