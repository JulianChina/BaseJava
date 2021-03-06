package EffectiveJava3rd.fEnumsAndAnnotations;

//41 使用标记接口定义类型
public class F41 {
    //标记接口(marker interface)，不包含方法声明，只是指定(或"标记")一个类实现了具有某些属性的接口。
    //考虑Serializable接口(第12章)。通过实现这个接口，一个类表明它的实例可以写入ObjectOutputStream(或"序列化")。

    //标记接口定义了一个由标记类实例实现的类型；标记注解则不会。
    //标记接口对于标记注解的另一个优点是可以更精确地定位目标。

    //标记接口和标记注释都有其用处。如果你想定义一个没有任何关联的新方法的类型，一个标记接口是一种可行的方法。如果要标记除类和接口以外的程序元素，或者将标记符合到已经大量使用注解类型的框架中，那么标记注解是正确的选择。
    //如果发现自己正在编写目标为ElementType.TYPE的标记注解类型，请花点时间确定它是否应该是注释类型，是不是标记接口是否更合适。
}
