package EffectiveJava3rd.lSerialization;

import java.util.Date;

//88 保护性地编写readObject方法
public class L88 {
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
    }

    //readObject方法实际上相当于另外一个公有的构造器，它要求同其他构造器一样警惕所有的注意事项。
    // 构造器必须检查其参数的有效性，并且在必要的时候对参数进行保护性拷贝，同样的，readObject方法也需要这样做。
    //不严格的说，readObject方法是一个"用字节流作为唯一参数"的构造器。
    //当一个对象被反序列化的时候，对于客户端不应该拥有的对象引用，如果那个字段包含了这样的对象引用，就必须做保护性拷贝，这是非常重要的。

    //与构造器一样，readObject方法不可以调用可被覆盖的方法，无论是直接调用还是间接调用都不可以。

    //在编写readObject方法的时候，都要这样想：你正在编写一个公有的构造器，无论给它传递什么样的字节流，它都必须产生一个有效的实例。不要假设这个字节流一定代表着一个真正被序列化的实例。
    // 类中的对象引用字段必须保持为私有属性，要保护性的拷贝这些字段中的每个对象。不可变类中的可变组件就属于这一类别。
    // 对于任何约束条件，如果检查失败就抛出一个InvalidObjectException异常。这些检查动作应该跟在所有的保护性拷贝之后。
    // 如果整个对象图在被反序列化之后必须进行验证，就应该使用ObjectInputValidation接口。
    // 无论是直接方法还是间接方法，都不要调用类中任何可被覆盖的方法。
}
