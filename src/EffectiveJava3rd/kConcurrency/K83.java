package EffectiveJava3rd.kConcurrency;

import sun.jvm.hotspot.oops.FieldType;
import sun.jvm.hotspot.oops.Symbol;

//83 明智审慎地使用延迟初始化
public class K83 {
    //延迟初始化是延迟字段的初始化，直到需要它的值。如果不需要该值，则不会初始化字段。这种技术既适用于静态字段，也适用于实例字段。
    //与大多数优化一样，延迟初始化的最佳建议是「除非需要，否则不要这样做」。
    //如果一个字段只在类的一小部分实例上访问，并且初始化该字段的代价很高，那么延迟初始化可能是值得的。唯一确定的方法是以使用和不使用延迟初始化的效果对比来度量类的性能。

    //在大多数情况下，常规初始化优于延迟初始化。
    //如果您使用延迟初始化来取代初始化的循环(circularity)，请使用同步访问器
    //如果需要在静态字段上使用延迟初始化来提高性能，使用lazy initialization holder class模式。这个用法可保证一个类在使用之前不会被初始化。
    //如果需要使用延迟初始化来提高实例字段的性能，请使用双重检查模式。这个模式避免了初始化后访问字段时的锁定成本。
    // Double-check idiom for lazy initialization of instance fields
    private volatile FieldType field;
    private FieldType getField() {
        FieldType result = field;
        if (result == null) { // First check (no locking)
            synchronized(this) {
                if (field == null) // Second check (with locking)
                    field = result = new FieldType(Symbol.create(null));  //computeFieldValue();
            }
        }
        return result;
    }
    //虽然您也可以将双重检查模式应用于静态字段，但是没有理由这样做：the lazy initialization holder class idiom是更好的选择。

    //双重检查模式的两个变体值得注意。有时候，您可能需要延迟初始化一个实例字段，该字段可以容忍重复初始化。如果您发现自己处于这种情况，您可以使用双重检查模式的变体来避免第二个检查。毫无疑问，这就是所谓的「单检查」模式。
    // Single-check idiom - can cause repeated initialization!
    private volatile FieldType field2;
    private FieldType getField2() {
        FieldType result2 = field2;
        if (result2 == null)
            field2 = result2 = new FieldType(Symbol.create(null));  //computeFieldValue();
        return result2;
    }

    //如果您不关心每个线程是否都会重新计算字段的值，并且字段的类型是long或double之外的基本类型，那么您可以选择在单检查模式中从字段声明中删除volatile修饰符。这种变体称为原生单检查模式。

    //您应该正常初始化大多数字段，而不是延迟初始化。
    //如果必须延迟初始化字段以实现性能目标或为了破坏有害的初始化循环，则使用适当的延迟初始化技术。
    // 对于字段，使用双重检查模式；对于静态字段，则应该使用the lazy initialization holder class idiom。例如，可以容忍重复初始化的实例字段，您还可以考虑单检查模式。
}
