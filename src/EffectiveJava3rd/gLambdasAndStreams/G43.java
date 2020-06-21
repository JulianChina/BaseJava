package EffectiveJava3rd.gLambdasAndStreams;

//43 方法引用优于lambda表达式
public class G43 {
    //Java提供了一种生成函数对象的方法，比lambda还要简洁，那就是：方法引用(method references)。
    //方法引用类型            //举例                      //等同的 Lambda
    //Static               //Integer::parseInt         //str -> Integer.parseInt(str)
    //Bound                //Instant.now()::isAftee    //Instant then = Instant.now(); t -> then.isAfter(t)
    //Unbound              //String::toLowerCase       //str -> str.toLowerCase()
    //Class Constructor    //TreeMap<K,V>::new         //() -> new TreeMap<K,V>
    //Array Constructor    //int[]::new                //len -> new int[len]

    //如果方法引用看起来更简短更清晰，请使用它们；否则，还是坚持lambda。
}
