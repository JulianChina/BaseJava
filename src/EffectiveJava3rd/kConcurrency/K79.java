package EffectiveJava3rd.kConcurrency;

import EffectiveJava3rd.dClassesAndInterfaces.D18;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//79 避免过度同步
public class K79 {
    //依据情况的不同，过度同步则可能导致性能降低、死锁，甚至不确定的行为。
    //为了避免活性失败和安全性失败，在一个被同步的方法或者代码块中，永远不要放弃对客户端的控制。
    // 换句话说，在一个被同步的区域内部，不要调用设计成要被覆盖的方法，或者是由客户端以函数对象的形式提供的方法。
    // 从包含该同步区域的类的角度来看，这样的方法是外来的(alien)。这个类不知道该方法会做什么事情，也无法控制它。根据外来方法的作用，从同步区域中调用它会导致异常、死锁或者数据损坏。

    // Broken - invokes alien method from synchronized block!
    public static class ObservableSet<E> extends D18.ForwardingSet<E> {
        public ObservableSet(Set<E> set) {
            super(set);
        }

        private final List<SetObserver<E>> observers = new ArrayList<>();

        public void addObserver(SetObserver<E> observer) {
            synchronized (observers) {
                observers.add(observer);
            }
        }

        public Boolean removeObserver(SetObserver<E> observer) {
            synchronized (observers) {
                return observers.remove(observer);
            }
        }

        private void notifyElementAdded(E element) {
            synchronized (observers) {
                for (SetObserver<E> observer : observers)
                    observer.added(this, element);
            }
        }

        @Override
        public boolean add(E element) {
            boolean added = super.add(element);
            if (added)
//                notifyElementAdded(element);
                notifyElementAddedBetter(element);
            return added;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            boolean result = false;
            for (E element : c)
                result |= add(element);
            // Calls notifyElementAdded
            return result;
        }

        // Alien method moved outside of synchronized block - open calls
        private void notifyElementAddedBetter(E element) {
            List<SetObserver<E>> snapshot = null;
            synchronized (observers) {
                snapshot = new ArrayList<>(observers);
            }
            for (SetObserver<E> observer : snapshot)
                observer.added(this, element);
        }
    }

    @FunctionalInterface
    public interface SetObserver<E> {
        // Invoked when an element is added to the observable set
        void added(ObservableSet<E> set, E element);
    }

    public static void main(String[] args) {
        //如果只是粗略地检验一下，ObservableSet会显得很正常。
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
        set.addObserver((s, e) -> System.out.println(e));
        for (int i = 0; i < 100; i++)
            set.add(i);

        //现在我们来尝试一些更复杂点的例子。
        set.addObserver(new SetObserver<Integer>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23)
                    s.removeObserver(this);
            }
        });

        // Observer that uses a background thread needlessly
        set.addObserver(new SetObserver<Integer>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> s.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new AssertionError(ex);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });
    }

    //事实上，要将外来方法的调用移出同步的代码块，还有一种更好的方法。Java类库提供了一个并发集合(concurrent collection)，称作CopyOnWriteArrayList，这是专门为此定制的。
    //CopyOnWriteArrayList是ArrayList的一种变体，它通过重新拷贝整个底层数组，在这里实现所有的写操作。由于内部数组永远不改动，因此迭代不需要锁定，速度也非常快。
    // Thread-safe observable set with CopyOnWriteArrayList
    public static class SafeObservableSet<E> extends D18.ForwardingSet<E> {
        public SafeObservableSet(Set<E> set) {
            super(set);
        }

        private final List<SafeSetObserver<E>> observers = new CopyOnWriteArrayList<>();

        public void addObserver(SafeSetObserver<E> observer) {
            observers.add(observer);
        }

        public Boolean removeObserver(SafeSetObserver<E> observer) {
            return observers.remove(observer);
        }

        private void notifyElementAdded(E element) {
            for (SafeSetObserver<E> observer : observers)
                observer.added(this, element);
        }

        @Override
        public boolean add(E element) {
            boolean added = super.add(element);
            if (added)
                notifyElementAdded(element);
            return added;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            boolean result = false;
            for (E element : c)
                result |= add(element);
            // Calls notifyElementAdded
            return result;
        }
    }
    @FunctionalInterface
    public interface SafeSetObserver<E> {
        // Invoked when an element is added to the observable set
        void added(SafeObservableSet<E> set, E element);
    }

    //在同步区域之外被调用的外来方法被称作"开放调用"(open call)。除了可以避免失败之外，开放调用还可以极大地增加并发性。
    //通常来说，应该在同步区域内做尽可能少的工作。
    //如果正在编写一个可变的类，有两种选择：省略所有的同步，如果想要并发使用，就允许客户端在必要的时候从外部同步；通过内部同步，使这个类变成是线程安全的，你还可以因此获得明显比从外部锁定整个对象更高的并发性。
    //java.util中的集合(除了已经废弃的Vector和Hashtable之外)采用了前一种方法，而java.util.concurrent中的集合则采用了后一种方法。

    //如果方法修改了静态字段，并且该方法很可能要被多个线程调用，那么也必须在内部同步对这个字段的访问。
    //字段本质上就是一个全局变量，即使是私有的也一样，因为它可以被不相关的客户端读取和修改。

    //为了避免死锁和数据破坏，千万不要从同步区字段内部调用外来方法。更通俗地讲，要尽量将同步区字段内部的工作量限制到最少。
    //只有当你有足够的理由一定要在内部同步类的时候，才应该这么做，同时还应该将这个决定清楚地写到文档中。
}
