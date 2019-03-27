package ThinkingInJava.uConcurrency.concurrency;

public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;

    public static int nexeSerialNumber() {
        return serialNumber++;  //Not thread-safe
    }
}
