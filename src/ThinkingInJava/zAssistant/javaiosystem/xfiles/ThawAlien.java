package ThinkingInJava.zAssistant.javaiosystem.xfiles;

import ThinkingInJava.zUtils.Print;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ThawAlien {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("X.file"));
        Object mystery = in.readObject();
        Print.print(mystery.getClass());
    }
}
