package EffectiveJava3rd.iGeneralProgramming;

//57 最小化局部变量的作用域
public class I57 {
    //用于最小化局部变量作用域的最强大的技术是再首次使用的地方声明它。
    //几乎每个局部变量声明都应该包含一个初始化器。
    //优先选择for循环而不是while循环。
    //最小化局部变量作用域的最终技术是保持方法小而集中。
    //如果在同一方法中组合两个行为(activities)，则与一个行为相关的局部变量可能会位于执行另一个行为的代码范围内。为了防止这种情况发生，只需将方法分为两个：每个行为对应一个方法。
}