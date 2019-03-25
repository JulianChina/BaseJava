package uConcurrency.concurrency;

import zUtils.Print;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast {
    public enum Status { DRY, BUTTERED, JAMMED }
    private Status status = Status.DRY;
    private final int id;

    public Toast(int idn) {
        id = idn;
    }

    public void butter() {
        status = Status.BUTTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Toast " + id + ": " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {

}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random rand = new Random(47);
    public Toaster(ToastQueue tq) {
        toastQueue = tq;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                //Make Toast
                Toast t = new Toast(count++);
                Print.print(t);
                toastQueue.put(t);
            }
        } catch (InterruptedException e) {
            Print.print("Toaster interrupted");
        }
        Print.print("Toaster off");
    }
}

class Butterer implements Runnable {
    private ToastQueue dryQueue, butteredQueue;
    public Butterer(ToastQueue dry, ToastQueue buttered) {
        dryQueue = dry;
        butteredQueue = buttered;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = dryQueue.take();
                t.butter();
                Print.print(t);
                butteredQueue.put(t);
            }
        } catch (InterruptedException e) {
            Print.print("Butterer interrupted");
        }
        Print.print("Butterer off");
    }
}

class Jammer implements Runnable {
    private ToastQueue butteredQueue, finishedQueue;
    public Jammer(ToastQueue buttered, ToastQueue finished) {
        butteredQueue = buttered;
        finishedQueue = finished;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = butteredQueue.take();
                t.jam();
                Print.print(t);
                finishedQueue.put(t);
            }
        } catch (InterruptedException e) {
            Print.print("Jammer interrupted");
        }
        Print.print("Jammer off");
    }
}

class Eater implements Runnable {
    private ToastQueue finishedQueue;
    private int count = 0;
    public Eater(ToastQueue finished) {
        finishedQueue = finished;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = finishedQueue.take();
                if (t.getId() != count++ || t.getStatus() != Toast.Status.JAMMED) {
                    Print.print(">>> Error: " + t);
                    System.exit(1);
                } else {
                    Print.print("Chomp! " + t);
                }
            }
        } catch (InterruptedException e) {
            Print.print("Eater interrupted");
        }
        Print.print("Eater off");
    }
}

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dryQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Butterer(dryQueue, butteredQueue));
        exec.execute(new Jammer(butteredQueue, finishedQueue));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
