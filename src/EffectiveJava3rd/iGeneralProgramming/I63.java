package EffectiveJava3rd.iGeneralProgramming;

//63 当心字符串连接引起的性能问题
public class I63 {
    //字符串串联运算符重复串联n个字符串需要n的平方级时间。
    //要获得能接受的性能，请使用StringBuilder代替String来存储正在构建的语句。

    //不要使用字符串连接操作符合并多个字符串，除非性能无关紧要。否则使用StringBuilder的append方法。
    //或者，使用字符数组，再或者一次只处理一个字符串，而不是组合它们。
}
