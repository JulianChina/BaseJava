package EffectiveJava3rd.bCreatingAndDestroyingObjects;

//04 使用私有构造器执行非实例化
public class B04 {
    //试图通过创建抽象类来强制执行非实例化是行不通的；
    //可以通过包含一个私有构造方法来实现类的非实例化；
    // Noninstantiable utility class
    public class UtilityClass {
        private UtilityClass() {
            throw new AssertionError();
        }
    }
}
