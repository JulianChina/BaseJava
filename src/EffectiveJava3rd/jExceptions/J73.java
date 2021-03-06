package EffectiveJava3rd.jExceptions;

//73 抛出与抽象对应的异常
public class J73 {
    //更高层的实现应该捕获低层的异常，同时抛出可以按照高层抽象进行解释的异常。这种做法称为异常转译(exception translation)。
    //一种特殊的异常转译形式称为异常链(exception chaining)，如果低层的异常对于调试导致高层异常的问题非常有帮助，使用异常链就很合适。低层的异常(原因)被传到高层的异常，高层的异常提供访问方法(Throwable的getCause方法)来获得低层的异常。
    //高层异常的构造器将原因传到支持链(chaining-aware)的超级构造器，因此它最终将被传给Throwable的其中一个运行异常链的构造器。

    //尽管异常转译与不加选择地从低层传递异常的做法相比有所改进，但是也不能滥用它。

    //如果不能阻止或者处理来自更低层的异常，一般的做法是使用异常转译，只有在低层方法的规范碰巧可以保证"它所抛出的所有异常对于更高层也是合适的"情况下，才可以将异常从低层传播到高层。
    //异常链对高层和低层异常都提供了最佳的功能：它允许抛出适当的高层异常，同时又能捕获低层的原因进行失败分析。
}
