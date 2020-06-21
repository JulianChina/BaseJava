package EffectiveJava3rd.eGenerics;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//33 优先考虑类型安全的异构容器
public class E33 {
    // Typesafe heterogeneous container pattern - implementation
    public static class Favorites {
        private Map<Class<?>, Object> favorites = new HashMap<>();

        public <T> void putFavorite(Class<T> type, T instance) {
            favorites.put(Objects.requireNonNull(type), instance);
        }

        public <T> T getFavorite(Class<T> type) {
            return type.cast(favorites.get(type));
        }
    }

    // Typesafe heterogeneous container pattern - client
    public static void main(String[] args) {
        Favorites f = new Favorites();
        f.putFavorite(String.class, "Java");
        f.putFavorite(Integer.class, 0xcafebabe);
        f.putFavorite(Class.class, Favorites.class);
        String favoriteString = f.getFavorite(String.class);
        int favoriteInteger = f.getFavorite(Integer.class);
        Class<?> favoriteClass = f.getFavorite(Class.class);
        System.out.printf("%s %x %s%n", favoriteString, favoriteInteger, favoriteClass.getName());
    }

    //Favorites实例是类型安全的：当你请求一个字符串时它永远不会返回一个整数。它也是异构的：与普通Map不同，所有的键都是不同的类型。因此，我们将Favorites称为类型安全异构容器(typesafe heterogeneous container)。

    //Favorites类有两个限制值得注意。
    //首先，恶意客户可以通过使用原始形式的Class对象，轻松破坏Favorites实例的类型安全。但生成的客户端代码在编译时会生成未经检查的警告。
    /*
    // Achieving runtime type safety with a dynamic cast
    public<T> void putFavorite(Class<T> type, T instance) {
        favorites.put(type, type.cast(instance));
    }
    */
    //Favorites类的第二个限制是它不能用于不可具体化的(non-reifiable)类型(条目28)。

    //Favorites使用的类型令牌(type tokens)是无限制的：getFavorite和putFavorite接受任何Class对象。
    //注解API(条目39)广泛使用限定类型的令牌。

    //假设有一个Class<?>类型的对象，并且想要将它传递给需要限定类型令牌的方法。可以将对象转换为Class<? extends Annotation>。
    //Class类提供了一种安全(动态)执行这种类型转换的实例方法。该方法被称为asSubclass，并且它转换所调用的Class对象来表示由其参数表示的类的子类。如果转换成功，该方法返回它的参数；如果失败，则抛出ClassCastException异常。
    // Use of asSubclass to safely cast to a bounded type token
    static Annotation getAnnotation(AnnotatedElement element,
                                    String annotationTypeName) {
        Class<?> annotationType = null; // Unbounded type token
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }

    //泛型API的通常用法(以集合API为例)限制了每个容器的固定数量的类型参数。你可以通过将类型参数放在键上而不是容器上来解决此限制。
    //可以使用Class对象作为此类型安全异构容器的键。以这种方式使用的Class对象称为类型令牌。也可以使用自定义键类型。
}
