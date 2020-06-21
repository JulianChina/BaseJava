package EffectiveJava3rd.cMethodsCommonToAllObjects;

//11 重写equals方法时同时也要重写hashCode方法
public class C11 {
    //在每个类中，在重写equals方法的时候，一定要重写hashCode方法。
    //1.当在一个应用程序执行过程中，如果在equals方法比较中没有修改任何信息，在一个对象上重复调用hashCode方法时，它必须始终返回相同的值。
        //从一个应用程序到另一个应用程序的每一次执行返回的值可以是不一致的。
    //2.如果两个对象根据equals(Object)方法比较是相等的，那么在两个对象上调用hashCode就必须产生的结果是相同的整数。
    //3.如果两个对象根据equals(Object)方法比较并不相等，则不要求在每个对象上调用hashCode都必须产生不同的结果。但是，为不相等的对象生成不同的结果可能会提高散列表的性能。
    //当无法重写hashCode时，所违反第二个关键条款是：相等的对象必须具有相等的哈希码。
    //不要试图从哈希码计算中排除重要的属性来提高性能。
    //不要为hashCode返回的值提供详细的规范，因此客户端不能合理地依赖它；你可以改变它的灵活性。

    //每次重写equals方法时都必须重写hashCode方法，否则程序将无法正茬运行。你的hashCode方法必须遵从Object类指定的常规约定，并且必须执行合理的工作，将不相等的哈希码分配给不相等的实例。

}
