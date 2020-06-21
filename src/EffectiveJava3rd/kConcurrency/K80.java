package EffectiveJava3rd.kConcurrency;

//80 executor、task和stream优先于线程
public class K80 {
    //java.util.concurrent.Executors类包含了静态工厂，能为你提供所需的大多数executor。
    // 然而，如果你想来点特别的，可以直接使用ThreadPoolExecutor类。这个类允许你控制线程池操作的几乎每个方面。
    //不仅应该尽量不要编写自己的工作队列，而且还应该尽量不直接使用线程。
    //从本质上讲，Executor Framework所做的工作是执行，Collections Framework所做的工作是聚合(aggregation)。

    //fork-join任务用ForkJoinTask实例表示，可以被分成更小的子任务，包含ForkJoinPool的线程不仅要处理这些任务，还要从另一个线程中"偷"任务，以确保所有的线程保持忙碌，从而提高CPU使用率、提高吞吐量，并降低延迟。
    //并发的stream是在fork-join池上编写的，我们不费什么力气就能享受到它们的性能优势，前提是假设它们正好适用于我们手边的任务。
}
