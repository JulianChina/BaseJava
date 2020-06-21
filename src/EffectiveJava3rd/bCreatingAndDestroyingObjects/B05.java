package EffectiveJava3rd.bCreatingAndDestroyingObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//05 依赖注入优于硬连接资源(hardwiring resources)
public class B05 {
    //在创建新实例时将资源传递到构造方法中，这是依赖项注入的一种形式；
    public class SpellChecker {
        private final Lexicon dictionary;

        public SpellChecker(Lexicon dictionary) {
            this.dictionary = Objects.requireNonNull(dictionary);
        }

        public boolean isValid(String word) {
            return true;
        }

        public List<String> suggestions(String type) {
            return new ArrayList<>();
        }
    }

    public class Lexicon {
    }

    //Java8中引入的Supplier<T>接口非常适合代表工厂。在输入上采用Supplier<T>的方法通常应该使用有界的通配符类型(bounded wildcard type)约束工厂的类型参数，以允许客户端传入工厂，创建指定类型的任何子类型。
    //依赖注入框架: Dagger、Guice、Spring
    //不要使用单例或静态的实用类来实现一个类，该类依赖于一个或多个底层资源，这些资源的行为会影响类的行为，并且不让类直接创建这些资源。
    //将资源或工厂传递给构造方法(或静态工厂或builder模式)。
}
