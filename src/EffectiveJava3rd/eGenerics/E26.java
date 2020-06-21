package EffectiveJava3rd.eGenerics;

//26 不要使用原始类型
public class E26 {
    //一个类或接口，它的声明有一个或多个类型参数(type parameters )，被称之为泛型类或泛型接口。
    //泛型类和接口统称为泛型类型(generic types)。
    //每个泛型定义了一个原始类型(raw type)，它是没有任何类型参数的泛型类型的名称。
    //原始类型的行为就像所有的泛型类型信息都从类型声明中被清除一样。它们的存在主要是为了与没有泛型之前的代码相兼容。
    //如果你使用原始类型，则会丧失泛型的所有安全性和表达上的优势。
    //泛型有子类型的规则，List<String>是原始类型List的子类型，但不是参数化类型List<String>的子类型。
    //如果要使用泛型类型，但不知道或关心实际类型参数是什么，则可以使用问号来代替。泛型类型Set<E>的无限制通配符类型是Set<?>(读取"某种类型的集合")。
    //你必须在类字面值(class literals)中使用原始类型。
    //泛型类型信息在运行时被删除，所以在无限制通配符类型以外的参数化类型上使用instanceOf运算符是非法的。使用无限制通配符类型代替原始类型不会以任何方式影响instanceOf运算符的行为。

    //使用原始类型可能导致运行时异常，所以不要使用它们。
    //Set<Object>是一个参数化类型，表示一个可以包含任何类型对象的集合；Set<?>是一个通配符类型，表示一个只能包含某些未知类型对象的集合；Set是一个原始类型，它不在泛型类型系统之列。前两个类型是安全的，最后一个不是。
    //术语                        中文含义          举例
    //Parameterized type         参数化类型         List<String>
    //Actual type parameter      实际类型参数       String
    //Generic type               泛型类型           List<E>
    //Formal type parameter      形式类型参数       E
    //Unbounded wildcard type    无限制通配符类型    List<?>
    //Raw type                   原始类型           List
    //Bounded type parameter     限制类型参数        <E extends Number>
    //Recursive type bound       递归类型限制        <T extends Comparable<T>>
    //Bounded wildcard type      限制通配符类型      List<? extends Number>
    //Generic method             泛型方法           static <E> List<E> asList(E[] a)
    //Type token                 类型令牌           String.class

}
