package EffectiveJava3rd.bCreatingAndDestroyingObjects;

//03 使用私有构造方法或枚类实现Singleton属性
public class B03 {
    // Singleton with public final field
    public static class Elvis {
        public static final Elvis INSTANCE = new Elvis();

        private Elvis() { }

        public void leaveTheBuilding() {

        }
    }
    //特权客户端可以使用AccessibleObject.setAccessible方法，以反射方式调用私有构造方法。如果需要防御此攻击，请修改构造函数，使其在请求创建第二个实例时抛出异常。

    // Singleton with static factory
    public static class Elvis2 {
        private static final Elvis2 INSTANCE = new Elvis2();

        private Elvis2() { }

        public static Elvis2 getInstance() {
            return INSTANCE;
        }

        public void leaveTheBuilding() {

        }
    }

    // Enum singleton - the preferred approach
    public enum Elvis3 {
        INSTANCE;

        public void leaveTheBuilding() {

        }
    }
}
