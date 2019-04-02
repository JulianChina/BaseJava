package JavaConcurrency.bPrinciple;

//6 Java内存模型基础知识
public class fMemoryModel {
    //6.1 并发编程模型的两个关键问题
        //https://redspider.gitbook.io/concurrent/di-er-pian-yuan-li-pian/6#61-bing-fa-bian-cheng-mo-xing-de-liang-ge-guan-jian-wen-ti
        //线程间如何通信？
        //线程间如何同步？
        //消息传递并发模型
        //共享内存并发模型(Java)
    //6.2 Java内存模型的抽象结构
        //运行时内存的划分
            //堆方(共享)、虚本计(私有)
            //内存可见性是针对的共享变量(堆中的变量)。
        //既然堆是共享的，为什么在堆中会有内存不可见问题
            //主内存<--JMM控制-->本地内存-线程
            //线程对共享变量的所有操作都必须在自己的本地内存中进行，不能直接从主内存中读取。
            //JMM通过控制主内存与每个线程的本地内存之间的交互，来提供内存可见性保证。
            //Java中的volatile关键字可以保证多线程操作共享变量的可见性以及禁止指令重排序；
            //synchronized关键字不仅保证可见性，同时也保证了原子性（互斥性）；
            //在更底层，JMM通过内存屏障来实现内存的可见性以及禁止重排序。
        //JMM与Java内存区域划分的区别于联系
}
