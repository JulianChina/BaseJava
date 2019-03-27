package ThinkingInJava.dControlExecutionFlow;

import java.util.Random;

import static java.lang.System.out;

public class ControlExecutionFlow {
    //4.1 true和false
    //4.2 if-else
    //4.3 迭代：do-while\for\逗号操作符
    //4.4 Foreach语法
    public static class ForEachFloat {
        public static void main(String[] args) {
            Random random = new Random(47);
            float[] f = new float[10];
            for (int i = 0; i < 10; i++) {
                f[i] = random.nextFloat();
            }
            for (float x : f){
                out.println(x);
            }
        }
    }
    public static class ForEachString {
        public static void main(String[] args) {
            for (char c : "A boy bad".toCharArray()){
                out.print(c + " ");
            }
        }
    }
    //4.5 return
    //4.6 break和continue
    //4.7 臭名昭著的goto
    //4.8 switch
    //4.9 总结

    public static void main(String[] args) {
        ForEachFloat.main(args);
        ForEachString.main(args);
    }
}
