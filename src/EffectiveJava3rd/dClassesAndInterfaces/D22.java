package EffectiveJava3rd.dClassesAndInterfaces;

//22 接口仅用来定义类型
public class D22 {
    //常量接口模式是对接口的糟糕使用。

    //通常，实用工具类要求客户端使用类名来限定常量名。
    //如果大量使用实用工具类导出的常量，则通过使用静态导入来限定具有类名的常量。

    //接口只能用于定义类型。它们不应该仅用于导出常量。
}
