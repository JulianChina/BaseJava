package ThinkingInJava.lErrorHandlingThroughException;

import ThinkingInJava.zUtils.Print;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class ErrorHandlingThroughException {
    //12.1 概念
        //异常处理程序；
    //12.2 基本异常
        //异常情形；
    //12.3 捕获异常
        //try块
        //异常处理程序
            //终止与恢复；
    //12.4 创建自定义异常
        //如果调用默认版本"e.printStackTrace();"，则信息将被输出到标准错误流。
        //异常与记录日志
        static class LoggingException extends Exception {
            private static Logger logger = Logger.getLogger(LoggingException.class.getSimpleName());
            public LoggingException() {
                StringWriter trace = new StringWriter();
                printStackTrace(new PrintWriter(trace));
                logger.severe(trace.toString());

            }
        }
        public static class LoggingExceptions {
            public static void main(String[] args) {
                try {
                    throw new LoggingException();
                } catch(LoggingException e) {
                    System.err.println("Caught " + e);
                }
                try {
                    throw new LoggingException();
                } catch(LoggingException e) {
                    System.err.println("Caught " + e);
                }
            }
        }

        static class LoggingException2 extends Exception {
            private static Logger logger = Logger.getLogger(LoggingException2.class.getSimpleName());
            static void logException(Exception e) {
                StringWriter trace = new StringWriter();
                e.printStackTrace(new PrintWriter(trace));
                logger.severe(trace.toString());
            }
            public static void main(String[] args) {
                try {
                    throw new NullPointerException();
                } catch(NullPointerException e) {
                    logException(e);
                }
            }
        }
    //12.5 异常说明
        //在编译时被强制检查的异常称为被检查的异常。
    //12.6 捕获所有异常
        //printStackTrace(); printStackTrace(PrintStream); printStackTrace(java.io.PrintWriter); fillInStackTrace();
        public static class ExceptionMethods {
            public static void main(String[] args) {
                try {
                    throw new Exception("My Exception");
                } catch (Exception e) {
                    Print.print("Caught Exception");
                    Print.print("getMessage():" + e.getMessage());
                    Print.print("getLocalizedMessage():" + e.getLocalizedMessage());
                    Print.print("toString():" + e);
                    Print.print("printStackTrace():");
                    e.printStackTrace(System.out);
                }
            }
        }
        //栈轨迹
            //getStackTrace(); StackTraceElement;
        //重新抛出异常
            //调用fillInStackTrace()的那一行就成了异常的新发生地。
        //异常链
            //常常需要在捕获一个异常后再抛出另一个异常，并且希望把原始异常的信息保存下来，这被称为异常连。
            //在Throwable的子类中，只有三种基本的异常类提供了带cause参数的构造器，分别是Error、Exception、RuntimeException。
            //如果要把其他类型的异常链接起来，应该使用initCause()方法而不是构造器。
    //12.7 Java标准异常
        //特例：RuntimeException
            //不受检查异常，对于发现编译器无法检测到的编程错误十分重要。
    //12.8 使用finally进行清理
        //finally用来做什么
            //当要把除内存之外的资源恢复到他们的初始状态时，就要用到finally子句，需要清理的资源包括：已经打开的文件或网络连接，在屏幕上画的图形，甚至可以是外部世界的某个开关。
            //当涉及break和continue语句的时候，finally子句也会得到执行。
        //在return中使用finally
            //finally子句总是会执行。
        //缺憾：异常丢失
    //12.9 异常的限制
        //当覆盖方法的时候，只能抛出在基类方法的异常说明里列出的那些异常。
        //异常限制对构造器不起作用。
        //派生类构造器不能捕获基类构造器抛出的异常。
        //在继承和覆盖过程中，某个特定方法的"异常说明的接口"不是变大了而是变小了————这恰好和类接口在继承时的情形相反。
    //12.10 构造器
        //在创建需要清理的对象之后，立即进入一个try-finally语句块。
    //12.11 异常匹配
    //12.12 其他可选方式
        //历史
        //观点
        //把异常传递给控制台
        //把"被检查的异常"转换为"不检查的异常"
            //直接把"被检查的异常"包装进RuntimeException里面。
    //12.13 异常使用指南
        //1.在恰当的级别处理问题；
        //2.解决问题并且重新调用产生异常的方法；
        //3.进行少许修补，然后绕过异常发生的地方继续执行；
        //4.用别的数据进行计算，以代替方法预计会返回的值；
        //5.把当前运行环境所能做的事尽量做完，然后把相同的异常重抛到更高层；
        //6.把当前运行环境所能做的事尽量做完，然后把不同的异常抛到更高层；
        //7.终止程序；
        //8.进行简化；
        //9.让类库和程序更安全。
    //12.14 总结
}
