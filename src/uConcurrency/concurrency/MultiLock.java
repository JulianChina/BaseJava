package uConcurrency.concurrency;

import zUtils.Print;

public class MultiLock {
    public synchronized void f1(int count) {
        if (count-- > 0) {
            Print.print("f1() calling f2() with count " + count);
            f2(count);
        }
    }
    public synchronized void f2(int count) {
        if (count-- > 0) {
            Print.print("f2() calling f1() with count " + count);
            f1(count);
        }
    }

    public static void main(String[] args) {
        final MultiLock multiLock = new MultiLock();
        new Thread(){
            @Override
            public void run() {
                multiLock.f1(10);
            }
        }.start();
    }
}

