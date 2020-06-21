package EffectiveJava3rd.fEnumsAndAnnotations;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//37 使用EnumMap替代序数索引
public class F37 {
    //有一个非常快速的Map实现，设计用于枚举键，称为java.util.EnumMap 。
    //EnumMap构造方法接受键类Class型的Class对象:这是一个有限定的类型令牌(bounded type token)，它提供运行时的泛型类型信息(条目33)。
    //通过使用stream(条目45)来管理Map ，可以进一步缩短以前的程序。
    // Using a nested EnumMap to associate data with enum pairs
    public enum Phase {
        SOLID, LIQUID, GAS;
        public enum Transition {
            MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
            BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
            SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);
            private final Phase from;
            private final Phase to; Transition(Phase from, Phase to) {
                this.from = from;
                this.to = to;
            }
            // Initialize the phase transition map
            private static final Map<Phase, Map<Phase, Transition>>
                    m = Stream.of(values()).collect(Collectors.groupingBy(t -> t.from,
                    () -> new EnumMap<>(Phase.class),
                    Collectors.toMap(t -> t.to, t -> t,
                            (x, y) -> y, () -> new EnumMap<>(Phase.class))));
            public static Transition from(Phase from, Phase to) {
                return m.get(from).get(to);
            }
        }
    }

    //使用序数来索引数组很不合适：改用EnumMap。如果你所代表的关系是多维的，请使用<..., EnumMap<... >>。
}
