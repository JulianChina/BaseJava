package zAssistant.annotations;

import net.mindview.atunit.*;
import net.mindview.util.OSExecute;

import static zUtils.Print.*;
public class AtUnitExample1 {
    public String methodOne() {
        return "This is methodOne";
    }
    public int methodTwo() {
        print("This is methodTwo");
        return 2;
    }
    @Test
    boolean methodOneTest() {
        return methodOne().equals("This is methodOne");
    }
    @Test boolean m2() { return methodTwo() == 2; }
    @Test private boolean m3() { return true; }
    @Test boolean failureTest() { return false; }
    @Test boolean anotherDisppointment() { return false; }

    public static void main(String[] args) {
        OSExecute.command("java net.mindview.atunit.AtUnit zAssistant.annotations.AtUnitExample1");
    }
}
