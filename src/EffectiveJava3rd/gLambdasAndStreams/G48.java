package EffectiveJava3rd.gLambdasAndStreams;

import java.math.BigInteger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;

//48 谨慎使用流并行
public class G48 {
    //在主流语言中，Java一直处于提供简化并发编程任务的工具的最前沿。
    //当Java于1996年发布时，它内置了对线程的支持，包括同步和wait/notify机制。
    //Java5引入了java.util.concurrent类库，带有并发集合和执行器框架。
    //Java7引入了fork-join包，这是一个用于并行分解的高性能框架。
    //Java8引入了流，可以通过对parallel方法的单个调用来并行化。
    //安全和活跃度违规(liveness violation)是并发编程中的事实，并行流管道也不例外。

    // Stream-based program to generate the first 20 Mersenne primes
    public static void main(String[] args) {
        primes().map(p -> BigInteger.valueOf(2).pow(p.intValueExact()).subtract(ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }

    static Stream<BigInteger> primes() {
        return Stream.iterate(BigInteger.valueOf(2), BigInteger::nextProbablePrime);
    }

    //即使在最好的情况下，如果源来自Stream.iterate方法，或者使用中间操作limit方法，并行化管道也不太可能提高其性能。
    //通常，并行性带来的性能收益在ArrayList、HashMap、HashSet和ConcurrentHashMap实例、数组、int类型范围和long类型的范围的流上最好。

    //如果编写自己的Stream，Iterable或Collection实现，并且希望获得良好的并行性能，则必须重写spliterator方法并广泛测试生成的流的并行性能。
    //并行化一个流不仅会导致糟糕的性能，包括活性失败(liveness failures)；它会导致不正确的结果和不可预知的行为(安全故障)。
    //在适当的情况下，只需向流管道添加一个parallel方法调用，就可以实现处理器内核数量的近似线性加速。
    // Prime-counting stream pipeline - benefits from parallelization
    static long pi(long n) {
        return LongStream.rangeClosed(2, n)
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
    // Prime-counting stream pipeline - parallel version
    static long pi2(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }

    //如果要并行化随机数流，请从SplittableRandom实例开始，而不是ThreadLocalRandom(或基本上过时的Random)。
    //SplittableRandom专为此用途而设计，具有线性加速的潜力。
    //ThreadLocalRandom设计用于单个线程，并将自身适应作为并行流源，但不会像SplittableRandom一样快。
    //Random实例在每个操作上进行同步，因此会导致过度的并行杀死争用(parallelism-killing contention)。

    //甚至不要尝试并行化流管道，除非你有充分的理由相信它将保持计算的正确性并提高其速度。
}
