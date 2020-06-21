package EffectiveJava3rd.bCreatingAndDestroyingObjects;

//08 避免使用Finalizer和Cleaner机制
public class B08 {
    //Finalizer和Cleaner机制的一个缺点是不能保证他们能够及时执行。
    //不要相信System.gc和System.runFinalization方法。他们可能会增加Finalizer和Cleaner机制被执行的几率，但不能保证一定会执行。

    //Finalizer机制的另一个问题是在执行Finalizer机制过程中，未捕获的异常会被忽略，并且该对象的Finalizer机制也会终止。

    //使用finalizer和cleaner机制会导致严重的性能损失。
    //finalizer机制有一个严重的安全问题:它们会打开你的类来进行finalizer机制攻击。

    //第二种合理使用Cleaner机制的方法与本地对等类(native peers)有关。本地对等类是一个由普通对象委托的本地(非Java)对象。
/*
    public static class Room implements AutoCloseable {
        private static final Cleaner cleaner = Cleaner.create();  //Java9

        private static class State implements Runnable {
            int numJunkPiles;

            State(int numJunkPiles) {
                this.numJunkPiles = numJunkPiles;
            }

            @Override
            public void run() {
                System.out.println("Cleaning room");
                numJunkPiles = 0;
            }
        }

        private final State state;

        private final Cleaner.Cleanable cleanable;

        public Room(int numJunkPiles) {
            state = new State(numJunkPiles);
            cleanable = cleaner.register(this, state);
        }

        @Override
        public void close() throws Exception {
            cleanable.clean();
        }
    }

    public static class Adult {
        public static void main(String[] args) {
            try (Room myRoom = new Room(7)) {
                System.out.println("Goodbye");
            }
        }
    }

    public static class Teenager {
        public static void main(String[] args) {
            new Room(99);
            System.out.println("Peace out");
        }
    }
*/
    //除了作为一个安全网或者终止非关键的本地资源，不要使用Cleaner机制，或者是在Java9发布之前的finalizers机制。
}
