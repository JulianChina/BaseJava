package tAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static zUtils.Print.*;

public class TAnnotation {
    //注解（元数据）为我们在代码中添加信息提供了一种形式化的方法，使我们可以在稍后某个时刻非常方便地使用这些数据；
    //注解可以提供用来完整地描述程序所需的信息，而这些信息是无法用Java来表达的；
    //注解是在实际的源代码级别保存所有的信息，而不是某种注释性的文字；
    //20.1 基本语法
        public static class Testable {
            public void execute() {
                print("Executing...");
            }
            @Test void testExecute() { execute(); }
        }
        //注解的使用方式几乎与修饰符的使用一模一样；
        //定义注解
            //与其他任何Java接口一样，注解也将会编译成class文件；
            @Target(ElementType.METHOD)  //元注解
            @Retention(RetentionPolicy.RUNTIME)  //元注解
            public @interface Test { }
            //注解的元素看起来就像接口的方法，唯一的区别是你可以为其指定默认值；
            //没有元素的注解称为标记注解；
            //UseCase.java; PasswordUtil.java;
        //元注解
            //Java目前只内置了3种标准注解(@Override, @Deprecated, @SuppressWarnings)，以及4种元注解(@Target, @Retention, @Documented, @Inherited)；
            //见P622表；
            //大多数时候，程序员主要是定义自己的注解，并编写自己的处理器来处理它们；
    //20.2 编写注解处理器
        //创建与使用注解处理器：利用反射机制构建工具，外部工具apt；
        //UseCaseTracker.java;
        //注解元素
            //注解元素可用的类型：所有基本类型(int, float, boolean等)、String、Class、enum、Annotation、前面类型的数组；
            //注解元素不可以使用任何包装类型；注解可以嵌套；
        //默认值限制
            //元素不能有不确定的值；
            //对于非基本类型的元素，不能以null作为其值；如果要表示某个元素存在，需要自己定义一些特殊的值；
            @Target(ElementType.METHOD)
            @Retention(RetentionPolicy.RUNTIME)
            public @interface SimulatingNull {
                public int id() default -1;
                public String description() default "";
            }
        //生成外部文件
            //DBTable.java; Constraints.java; SQLString.java; SQLInteger.java; Uniqueness.java;
            //Member.java;
            //名为value()的元素可以采用快捷方式（不用提供名字）赋值；
            //编译器允许程序员对同一个目标同时使用多个注解；使用多个注解的时候，同一个注解不能重复使用；
        //注解不支持继承
            //不能使用关键字extends来继承某个@interface；
        //实现处理器
            //TableCreator.java;
            //由于注解没有继承机制，所以要获得近似多态的行为，使用getDeclaredAnnotations()是唯一的办法；
    //20.3 使用apt处理注释
        //程序员自定义的每一个注解都需要自己的处理器，而apt工具能够很容易地将多个注解处理器组合在一起；
        //ExtractInterface.java; Multiplier.java; InterfaceExtractorProcessor.java; InterfaceExtractorProcessorFactory.java;
    //20.4 将观察者模式用于apt
        //【访问者】设计模式：一个访问者会遍历谋某个数据结构或一个对象的集合，对其中的每一个对象执行一个操作；该数据结构无需有序，而你对每个对象执行的操作，都是特定于此对象的类型，这就将操作与对象解耦；
        //TableCreationProcessorFactory.java;
    //20.5 基于注解的单元测试
        //单元测试是对类中的每个方法提供一个或多个测试的一种实践，其目的是为了有规律地测试一个类的各个部分是否具备正确的行为；
        //在Java中，最著名的单元测试工具就是JUnit；
        //AtUnitExample1.java;
        //将@Unit用于泛型

        //不需要任何"套件"

        //实现@Unit

        //移除测试代码

    //20.6 总结
}
