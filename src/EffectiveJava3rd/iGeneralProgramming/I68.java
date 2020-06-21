package EffectiveJava3rd.iGeneralProgramming;

//68 遵守被广泛认可的命名约定
public class I68 {
    //Java平台有一组完善的命名约定，其中许多约定包含在《The Java Language Specification》。不严格地讲，命名约定分为两类：排版和语法。
    //有少量的与排版有关的命名约定，包括包、类、接口、方法、字段和类型变量。
    //包名和模块名应该是分层的，组件之间用句点分隔。组件应该由小写字母组成，很少使用数字。任何在你的组织外部使用的包，名称都应该以你的组织的Internet域名开头，并将组件颠倒过来，例如，edu.cmu、com.google、org.eff。
    //类和接口名称，包括枚举和注释类型名称，应该由一个或多个单词组成，每个单词的首字母大写，例如List或FutureTask。
    //方法和字段名遵循与类和接口名相同的排版约定，除了方法或字段名的第一个字母应该是小写，例如remove或ensureCapacity。
    //前面规则的唯一例外是"常量字段"，它的名称应该由一个或多个大写单词组成，由下划线分隔，例如VALUES或NEGATIVE_INFINITY。
    //局部变量名与成员名具有类似的排版命名约定，但允许使用缩写，也允许使用单个字符和短字符序列，它们的含义取决于它们出现的上下文。
    //类型参数名通常由单个字母组成。最常见的是以下五种类型之一：T表示任意类型，E表示集合的元素类型，K和V表示Map的键和值类型，X表示异常。函数的返回类型通常为R。任意类型的序列可以是T、U、V或T1、T2、T3。
    //Identifier Type       //Example
    //Package or module     //org.junit.jupiter.api , com.google.common.collect
    //Class or Interface    //Stream, FutureTask, LinkedHashMap,HttpClient
    //Method or Field       //remove, groupingBy, getCrc
    //Constant Field        //MIN_VALUE, NEGATIVE_INFINITY
    //Local Variable        //i, denom, houseNum
    //Type Parameter        //T, E, K, V, X, R, U, V, T1, T2

    //可实例化的类，包括枚举类型，通常使用一个或多个名词短语来命名，例如Thread、PriorityQueue或ChessPiece。
    //不可实例化的实用程序类通常使用复数名词来命名，例如collector或Collections。
    //接口的名称类似于类，例如集合或比较器，或者以able或ible结尾的形容词，例如Runnable、Iterable或Accessible。
    //注解类型有很多的用途，所以没有哪部分占主导地位。名词、动词、介词和形容词都很常见，例如，BindingAnnotation、Inject、ImplementedBy或Singleton。

    //执行某些操作的方法通常用动词或动词短语(包括对象)命名，例如，append或drawImage。
    //返回布尔值的方法的名称通常以单词is或has(通常很少用)开头，后面跟一个名词、一个名词短语，或者任何用作形容词的单词或短语，例如isDigit、isProbablePrime、isEmpty、isEnabled或hasSiblings。
    //返回被调用对象的非布尔函数或属性的方法通常使用以get开头的名词、名词短语或动词短语来命名，例如size、hashCode或getTime。

    //一些方法名称值得特别注意。
    // 转换对象类型(返回不同类型的独立对象)的实例方法通常称为toType，例如toString或toArray。
    // 返回与接收对象类型不同的视图的方法通常称为asType，例如asList。
    // 返回与调用它们的对象具有相同值的基本类型的方法通常称为类型值，例如intValue。
    // 静态工厂的常见名称包括from、of、valueOf、instance、getInstance、newInstance、getType和newType。
}
