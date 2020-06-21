package EffectiveJava3rd.dClassesAndInterfaces;

//21 为后代设计接口
public class D21 {
    //许多新的默认方法被添加到Java8的核心集合接口中，主要是为了方便使用lambda表达式.
    //编写一个默认方法并不总是可能的，它保留了每个可能的实现的所有不变量。

    //在默认方法的情况下，接口的现有实现类可以在没有错误或警告的情况下编译，但在运行时会失败。
    //应该避免使用默认方法向现有的接口添加新的方法，除非这个需要是关键的。
}
