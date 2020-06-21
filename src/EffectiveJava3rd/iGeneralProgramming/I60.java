package EffectiveJava3rd.iGeneralProgramming;

//60 若需要精确答案就应避免使用float和double类型
public class I60 {
    //float和double类型特别不适合进行货币计算。
    //使用BigDecimal、int或long进行货币计算。
    //除了使用BigDecimal，另一种方法是使用int或long，这取决于涉及的数值大小，还要自己处理十进制小数点。

    //对于任何需要精确答案的计算，不要使用float或double类型。如果希望系统来处理十进制小数点，并且不介意不使用基本类型带来的不便和成本，请使用BigDecimal。
    //如果性能是最重要的，那么你不介意自己处理十进制小数点，而且数值不是太大，可以使用int或long。如果数值不超过9位小数，可以使用int；如果不超过18位，可以使用long。如果数量可能超过18位，则使用BigDecimal。
}
