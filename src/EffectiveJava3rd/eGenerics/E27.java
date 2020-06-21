package EffectiveJava3rd.eGenerics;

//27 消除非检查警告
public class E27 {
    //尽可能地消除每一个未经检查的警告。
    //如果你不能消除警告，但你可以证明引发警告的代码是类型安全的，那么(并且只能这样)用@SuppressWarnings(“unchecked”)注解来抑制警告。
    //SuppressWarnings注解可用于任何声明，从单个局部变量声明到整个类。始终在尽可能最小的范围内使用SuppressWarnings注解。
    //如果你发现自己在长度超过一行的方法或构造方法上使用SuppressWarnings注解，则可以将其移到局部变量声明上。你可能需要声明一个新的局部变量，但这是值得的。
    // Adding local variable to reduce scope of @SuppressWarnings
    /*
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unckecked") T[] result = Arrays.copyOf(elements, size, a.getClass());
            return result;
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
    */
    //每当使用@SuppressWarnings("unchecked")注解时，请添加注释，说明为什么是安全的。

    //每个未经检查的警告代表在运行时出现ClassCastException异常的可能性。
    //如果无法消除未经检查的警告，并且可以证明引发该警告的代码是安全类型的，则可以在尽可能小的范围内使用@SuppressWarnings("unchecked")注解来禁止警告。
}
