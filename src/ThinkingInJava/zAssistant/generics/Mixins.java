package ThinkingInJava.zAssistant.generics;

import ThinkingInJava.zUtils.Print;

import java.util.Date;

public class Mixins {
    public interface TimeStamped {
        long getStamp();
    }

    public static class TimeStampedImp implements TimeStamped {
        private final long timeStamp;
        public TimeStampedImp() {
            timeStamp = new Date().getTime();
        }
        @Override
        public long getStamp() {
            return timeStamp;
        }
    }

    public interface SerialNumbered {
        long getSerialNumber();
    }

    public static class SerialNumberedImp implements SerialNumbered {
        private static long counter = 1;
        private final long serialNumber = counter++;
        @Override
        public long getSerialNumber() {
            return serialNumber;
        }
    }

    public interface Basic {
        public void set(String val);
        public String get();
    }

    public static class BasicImp implements Basic {
        private String value;

        @Override
        public void set(String val) { value = val; }

        @Override
        public String get() { return value; }
    }

    static class Mixin extends BasicImp implements TimeStamped, SerialNumbered {  //【代理】模式
        private TimeStamped timeStamp = new TimeStampedImp();
        private SerialNumbered serialNumber = new SerialNumberedImp();
        public long getStamp() { return timeStamp.getStamp(); }
        public long getSerialNumber() { return serialNumber.getSerialNumber(); }
    }

    public static void main(String[] args) {
        Mixin mixin1 = new Mixin(), mixin2 = new Mixin();
        mixin1.set("test string 1");
        mixin2.set("test string 2");
        Print.print(mixin1.get() + " " + mixin1.getStamp() + " " + mixin1.getSerialNumber());
        Print.print(mixin2.get() + " " + mixin2.getStamp() + " " + mixin2.getSerialNumber());
    }
}
