package EffectiveJava3rd.bCreatingAndDestroyingObjects;

//01 考虑使用静态工厂方法替代构造方法
public class B01 {
    //一个类可以提供一个公共静态工厂方法，它只是一个返回类实例的静态方法。
    public static Boolean valueOf(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }
    //静态工厂方法的第1个优点，不像构造方法，它们是有名字的；
    //静态工厂方法的第2个优点，与构造方法不同，它们不需要每次调用时都创建一个对象；
    //静态工厂方法的第3个优点，与构造方法不同，它们可以返回其返回类型的任何子类型的对象；
        //Java8 要求所有接口的静态成员都是公共的。Java9 允许私有静态方法，但静态字段和静态成员类仍然需要公开。
    //静态工厂方法的第4个优点，返回对象的类可以根据输入参数的不同而不同；
    //静态工厂方法的第5个优点，在编写包含该方法的类时，返回的对象的类不需要存在；
        //服务提供者框架中有三个基本组:服务接口，它表示实现;提供者注册 API，提供者用来注册实现;以及服务访 问 API，客户端使用该 API 获取服务的实例。
        //服务提供者框架的一个可选的第四个组件是一个服务提供者接口，它描述了一个生成服务接口实例的工厂对象。 在没有服务提供者接口的情况下，必须对实现进行反射实例化。
    //只提供静态工厂方法的主要限制是，没有公共或受保护构造方法的类不能被子类化。
    //静态工厂方法的第2个缺点是，程序员很难找到它们。
    //常见的静态工厂方法：
        //from——类型转换方法，它接受单个参数并返回此类型的相应实例，例如: Date d = Date.from(instant);
        //of——一个聚合方法，接受多个参数并返回该类型的实例，并把他们合并在一起，例如: Set faceCards = EnumSet.of(JACK, QUEEN, KING);
        //valueOf——from和to更为详细的替代方式，例如: BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
        //instance或getInstance——返回一个由其参数(如果有的话)描述的实例，但不能说它具有相同的值，例如: StackWalker luke = StackWalker.getInstance(options);
        //create或newInstance——与instance或getInstance类似，除了该方法保证每个调用返回一个新的实例，例如: Object newArray = Array.newInstance(classObject, arrayLen);
        //getType——与getInstance类似，但是如果在工厂方法中不同的类中使用。Type是工厂方法返回的对象类型，例如: FileStore fs = Files.getFileStore(path);
        //newType——与newInstance类似，但是如果在工厂方法中不同的类中使用。Type是工厂方法返回的对象类型，例如: BufferedReader br = Files.newBufferedReader(path);
        //type——getType和newType简洁的替代方式，例如: List litany = Collections.list(legacyLitany);
}
