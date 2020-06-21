package EffectiveJava3rd.fEnumsAndAnnotations;

//35 使用实例属性替代序数
public class F35 {
    //永远不要从枚举的序号中得出与它相关的值；请将其保存在实例属性中。
    public enum Ensemble {
        SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5), SEXTET(6), SEPTET(7), OCTET(8), DOUBLE_QUARTET(8), NONET(9), DECTET(10), TRIPLE_QUARTET(12);
        private final int numberOfMusicians;
        Ensemble(int size) { this.numberOfMusicians = size; }
        public int numberOfMusicians() { return numberOfMusicians; }
    }

    //枚举规范对此ordinal方法说道："大多数程序员对这种方法没有用处。它被设计用于基于枚举的通用数据结构，如EnumSet和EnumMap。"
    //除非你在编写这样数据结构的代码，否则最好避免使用ordinal方法。
}
