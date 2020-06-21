package EffectiveJava3rd.lSerialization;

//86 非常谨慎地实现Serializable
public class L86 {
    //实现Serializable接口的一个主要代价是，一旦类的实现被发布，它就会降低更改该类实现的灵活性。
    //每个可序列化的类都有一个与之关联的唯一标识符。
    //实现Serializable接口的第二个代价是，增加了出现bug和安全漏洞的可能性。
    //实现Serializable接口的第三个代价是，它增加了与发布类的新版本相关的测试负担。
    //实现Serializable接口并不是一个轻松的决定。
    //为继承而设计的类很少情况适合实现Serializable接口，接口也很少情况适合扩展它。
    //内部类不应该实现Serializable。
    //内部类的默认序列化形式是不确定的。但是，静态成员类可以实现Serializable接口。
}
