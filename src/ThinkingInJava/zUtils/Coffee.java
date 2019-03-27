package ThinkingInJava.zUtils;

public class Coffee {
    private static long counter = 0;
    private final long id = counter++;
    public String toString() {
        return getClass().getSimpleName() + " " + id;
    }

    public static class Latte extends Coffee { }
    public static class Mocha extends Coffee { }
    public static class Cappuccino extends Coffee { }
    public static class Americano extends Coffee { }
    public static class Breve extends Coffee { }
}




