package EffectiveJava3rd.iGeneralProgramming;

//58 for-each循环优于传统for循环
public class I58 {
    //for-each循环通过隐藏迭代器或索引变量来消除混乱和出错的机会。
    //有三种常见的情况是你不能分别使用for-each循环的：
    //有损过滤(Destructive filtering)————如果需要遍历集合，并删除指定选元素，则需要使用显式迭代器，以便可以调用其remove方法。通常可以使用在Java8中添加的Collection类中的removeIf方法，来避免显式遍历。
    //转换————如果需要遍历一个列表或数组并替换其元素的部分或全部值，那么需要列表迭代器或数组索引来替换元素的值。
    //并行迭代————如果需要并行地遍历多个集合，那么需要显式地控制迭代器或索引变量，以便所有迭代器或索引变量都可以同步进行。

    //for-each循环不仅允许遍历集合和数组，还允许遍历实现Iterable接口的任何对象，该接口由单个方法组成。

    //for-each循环在清晰度，灵活性和错误预防方面提供了超越传统for循环的令人注目的优势，而且没有性能损失。尽可能使用for-each循环优先于for循环。
}
