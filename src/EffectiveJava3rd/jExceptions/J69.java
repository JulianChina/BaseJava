package EffectiveJava3rd.jExceptions;

//69 只针对异常的情况下才使用异常
public class J69 {
    //异常应该只用于异常的情况下；他们永远不应该用于正常的程序控制流程。
    //设计良好的API不应该强迫它的客户端为了正常的控制流程而使用异常。

    //异常是为了在异常情况下被设计和使用的。不要将它们用于普通的控制流程，也不要编写迫使它们这么做的API。
}