package EffectiveJava3rd.hMethods;

//49 检查参数有效性
public class H49 {
    //验证参数失败可能导致违反故障原子性(failure atomicity)(条目76)。
    //对于公共方法和受保护方法，请使用Java文档@throws注解来记在在违反参数值限制时将引发的异常(条目74)。
    //通常，生成的异常是IllegalArgumentException，IndexOutOfBoundsException或NullPointerException(条目72)。
    //在Java7中添加的Objects.requireNonNull方法灵活方便，因此没有理由再手动执行空值检查。
    //非公共方法可以使用断言检查其参数。
    //检查方法中未使用但存储以供以后使用的参数的有效性尤为重要。
    //检查构造方法参数的有效性对于防止构造对象违反类不变性(class invariants)非常重要。

    //每次编写方法或构造方法时，都应该考虑对其参数存在哪些限制。应该记在这些限制，并在方法体的开头使用显式检查来强制执行这些限制。
}
