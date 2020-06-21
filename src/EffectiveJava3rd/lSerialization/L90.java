package EffectiveJava3rd.lSerialization;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

//90 考虑用序列化代理代替序列化实例
public class L90 {
    //序列化代理模式(serialization proxy pattern)相当简单。
    // 首先，为可序列化的类设计一个私有的静态嵌套类，精确地表示外围类的逻辑状态。
    // 这个嵌套类被称为序列化代理(serialization proxy)，它应该有一个单独的构造器，其参数类型就是那个外围类。
    // 这个构造器只是从它的参数中复制数据：它不需要进行任何一致性检验或者保护性拷贝。
    // 从设计的角度看，序列化代理的默认序列化形式是外围类最好的序列化形式。外围类及其序列代理都必须声明实现Serializable接口。
    // Serialization proxy for Period class
    private static class SerializationProxy implements Serializable {
        private final Date start;
        private final Date end;

        SerializationProxy(Period p) {
            this.start = p.start;
            this.end = p.end;
        }

        private static final long serialVersionUID = 234098243823485285L; // Any number will do (Item 87)
    }

    // Immutable class that uses defensive copying
    public final class Period {
        private final Date start;
        private final Date end;

        /**
         * @param start the beginning of the period
         * @param end   the end of the period; must not precede start
         * @throws IllegalArgumentException if start is after end
         * @throws NullPointerException     if start or end is null
         */
        public Period(Date start, Date end) {
            this.start = new Date(start.getTime());
            this.end = new Date(end.getTime());
            if (this.start.compareTo(this.end) > 0)
                throw new IllegalArgumentException(start + " after " + end);
        }

        public Date start() {
            return new Date(start.getTime());
        }

        public Date end() {
            return new Date(end.getTime());
        }

        public String toString() {
            return start + " - " + end;
        }

        // writeReplace method for the serialization proxy pattern
        private Object writeReplace() {
            return new SerializationProxy(this);
        }

        // readObject method for the serialization proxy pattern
        private void readObject(ObjectInputStream stream)
                throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        // readResolve method for Period.SerializationProxy
        private Object readResolve() {
            return new Period(start, end); // Uses public constructor
        }
    }

    //正如保护性拷贝方法一样，序列化代理方式可以阻止伪字节流的攻击以及内部字段的盗用攻击。
    //序列化代理模式的功能比保护性拷贝的更加强大。序列化代理模式允许反序列化实例有着与原始序列化实例不同的类。
    //序列化代理模式有两个局限性。它不能与可以被客户端拓展的类兼容；它也不能与对象图中包含循环的某些类兼容。

    //当你发现必须在一个不能被客户端拓展的类上面编写readObject或者writeObject方法时，就应该考虑使用序列化代理模式。
    // 想要稳健的将带有重要约束条件的对象序列化时，这种模式是最容易的方法。
}
