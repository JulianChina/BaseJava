package EffectiveJava3rd.fEnumsAndAnnotations;

import java.util.Set;

//36 使用EnumSet替代位属性
public class F36 {
    //如果枚举类型的元素主要用于集合中，一般来说使用int枚举模式(条目34)。
    // Bit field enumeration constants - OBSOLETE!
    public class Text {
        public static final int STYLE_BOLD = 1 << 0;  //1
        public static final int STYLE_ITALIC = 1 << 1;  //2
        public static final int STYLE_UNDERLINE = 1 << 2;  //4
        public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8
        // Parameter is bitwise OR of zero or more STYLE_ constants
        public void applyStyles(int styles) { }
    }

    //java.util包提供了EnumSet类来有效地表示从单个枚举类型中提取的值集合。
    // EnumSet - a modern replacement for bit fields
    public static class Text2 {
        public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }
        // Any Set could be passed in, but EnumSet is clearly best
        public void applyStyles(Set<Style> styles) {  }
    }
    //text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));

    //仅仅因为枚举类型将被用于集合中，所以没有理由用位属性来表示它。
    //EnumSet类将位属性的简洁性和性能与条目34中所述的枚举类型的所有优点相结合。
}
