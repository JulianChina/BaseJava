package ThinkingInJava.mString;

import net.mindview.util.BinaryFile;
import net.mindview.util.TextFile;
import ThinkingInJava.zUtils.Print;

import java.io.*;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MString {
    //13.1 不可变String
        //String对象是不可变的。
        //String类中每一个看起来会修改String值的方法，实际上都是创建了一个全新的String对象，以包含修改后的字符串内容。而最初的String对象则丝毫未动。
    //13.2 重载"+"与StringBuilder
        //用于String的"+"和"+="是Java中仅有的两个重载过的操作符，而Java并不允许程序员重载任何操作符。
        //StringBuilder是字符串操作的首选；StringBuffer是线程安全的，因此开销会更大一点儿。
    //13.3 无意识的递归
        //如果想要打印出对象的内存地址，应该调用Object.toString()，这才是负责此任务的方法。
    //13.4 String上的操作
        //见P288-289的表格；
        //String类的方法都会返回一个新的String对象。如果内容没有发生改变，String的方法只是返回指向原对象的引用而已。
    //13.5 格式化输出
        //printf()
        //System.out.format()/System.out.printf()
            public static class SimpleFormat {
                public static void main(String[] args) {
                    int x = 5;
                    double y = 5.332542;
                    System.out.println("Row:[" + x + " " + y  + "]");
                    System.out.format("Row:[%d %f]\n", x, y);
                    System.out.printf("Row:[%d %f]\n", x, y);
                }
            }
        //Formatter类
            //在Java中，所有新的格式化功能都由java.util.Formatter类处理。Formatter的构造器经过重载可以接受多种输出目的地，最常用的还是PrintStream、OutputStream、File。
        //格式化说明符
            //更复杂更精细的格式化修饰符：%[argument_index$][flags][width][.precision]conversion
            //"-"用于改变对齐方向；
            //precision，用于String时，表示打印String时输出字符的最大数量；用于浮点数时，表示小数部分要显示出来的位数；不能用于整数，会抛异常；
            public static class Receipt {
                private double total = 0;
                private Formatter f = new Formatter(System.out);
                public void printTitle() {
                    f.format("%-15s %5s %10s\n", "Item", "Qty", "Price");
                    f.format("%-15s %5s %10s\n", "----", "---", "-----");
                }
                public void print(String name, int qty, double price) {
                    f.format("%-15.15s %5d %10.2f\n", name, qty, price);
                    total += price;
                }
                public void printTotal() {
                    f.format("%-15s %5s %10.2f\n", "Tax", "", total*0.06);
                    f.format("%-15s %5s %10s\n", "", "", "-----");
                    f.format("%-15s %5s %10.2f\n", "Total", "", total*1.06);
                }
                public static void main(String[] args) {
                    Receipt receipt = new Receipt();
                    receipt.printTitle();
                    receipt.print("Jack's Magic Beans", 4, 4.25);
                    receipt.print("Princess Peas", 3, 5.1);
                    receipt.print("Three Bears Porridge", 1, 14.25);
                    receipt.printTotal();
                }
            }
        //Formatter转换
            //d/c/b/s/f/e/x/h/%
        //String.format()
            public static class Hex {
                public static String format(byte[] data) {
                    StringBuilder result = new StringBuilder();
                    int n = 0;
                    for (byte b : data) {
                        if (n%16==0) {
                            result.append(String.format("%05X: ", n));
                        }
                        result.append(String.format("%02X ", b));
                        n++;
                        if (n%16==0) {
                            result.append("\n");
                        }
                    }
                    result.append("\n");
                    return result.toString();
                }
                public static void main(String[] args) {
                    try {
                        if (args.length==0) {
//                            System.out.println(format(inputStream2ByteArray("/Users/julian/cyl/BaseJavaKotlin/out/production/BaseJava/ThinkingInJava.mString/MString$Hex.class")));
                            System.out.println(format(BinaryFile.read("/Users/julian/cyl/BaseJavaKotlin/out/production/BaseJava/ThinkingInJava.mString/MString$Hex.class")));
                        } else {
//                            System.out.println(format(inputStream2ByteArray(args[0])));
                            System.out.println(format(BinaryFile.read(new File(args[0]))));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                private static byte[] inputStream2ByteArray(String filePath) throws IOException {
                    InputStream in = new FileInputStream(filePath);
                    byte[] data = toByteArray(in);
                    in.close();
                    return data;
                }

                private static byte[] toByteArray(InputStream in) throws IOException {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int n = 0;
                    while ((n = in.read(buffer)) != -1) {
                        out.write(buffer, 0, n);
                    }
                    return out.toByteArray();
                }
            }
    //13.6 正则表达式
        //正则表达式提供了一种完全通用的方式，能够解决各种字符串处理相关的问题：匹配、选择、编辑以及验证。
        //基础
            //在Java中，"\\"表示我要插入一个正则表达式的反斜线；如果要插入一个普通的反斜线，应该使用"\\\\"。
            //String自带的正则功能：matches()\split()\replaceFirst()\replaceAll()。
            //如果正则表达式不是只使用一次的话，非String对象的正则表达式明显具备更佳的性能。
        //创建正则表达式
            //见P298；java.util.regex.Pattern;
        //量词
            //贪婪型、勉强型、占有型；
            //多数正则表达式操作都接受CharSequence类型的参数；
        //Pattern和Matcher
            //find()
            public static class TestRegularExpression {
                public static void main(String[] args) {
                    if (args.length<2) {
                        Print.print("Input not enough!");
                        System.exit(0);
                    } else {
                        Print.print("Input:\"" + args[0] + "\"");
                        for (String arg : args) {
                            Print.print("Regular expression: \"" + arg + "\"");
                            Pattern p = Pattern.compile(arg);
                            Matcher m = p.matcher(args[0]);
                            while (m.find()) {
                                Print.print("Match \"" + m.group() + "\" at position " + m.start() + "-" + (m.end()-1));
                            }
                        }
                    }
                }
            }
            public static class Finding {
                public static void main(String[] args) {
                    Matcher m = Pattern.compile("\\w+").matcher("Evening is full of the linnet's wings");
                    while (m.find()) {
                        Print.printnb(m.group() + " ");
                    }
                    Print.print();
                    int i = 0;
                    while (m.find(i)) {
                        Print.printnb(m.group() + " ");
                        ++i;
                    }
                }
            }
            //组(Groups)
            public static class Groups {
                public static final String POEM = "Asking the young boy beneath the pine,\n" +
                        "He says, “Master is off gathering herbs,\n" +
                        "Just someplace in these mountains —\n" +
                        "The clouds are deep — I don’t know where.";

                public static void main(String[] args) {
                    Matcher m = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$").matcher(POEM);
                    while (m.find()) {
                        for (int j = 0; j <= m.groupCount(); j++) {
                            Print.printnb("[" + m.group(j) + "]");
                        }
                        Print.print();
                    }
                }
            }
            //start()与end()、lookingAt()与matches()
            public static class StartEnd {
                public static final String input = "As long as there is injustice, whenever a\n" +
                        "Targathian baby cries out, wherever a disress\n" +
                        "signal sounds among the star ... We'll be there.\n" +
                        "This fine ship, and this fine crew ...\n" +
                        "Never give up! Never surrender!";
                private static class Display {
                    private boolean regexPrinted = false;
                    private String regex;
                    Display(String regex) {
                        this.regex = regex;
                    }
                    void display(String message) {
                        if (!regexPrinted) {
                            Print.print(regex);
                            regexPrinted = true;
                        }
                        Print.print(message);
                    }
                }
                static void examine(String s, String regex) {
                    Display d = new Display(regex);
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(s);
                    while (m.find()) {
                        d.display("find() '" + m.group() + "' start = " + m.start() + " end = " + m.end());
                    }
                    if (m.lookingAt()) {
                        d.display("lookingAt() '" + m.group() + "' start = " + m.start() + " end = " + m.end());
                    }
                    if (m.matches()) {
                        d.display("matches() '" + m.group() + "' start = " + m.start() + " end = " + m.end());
                    }
                }

                public static void main(String[] args) {
                    for (String in : input.split("\n")) {
                        Print.print("Input: " + in);
                        for (String regex : new String[]{"\\w*ere\\w*", "\\w*ever", "T\\w+", "Never.*?!"}) {
                            examine(in, regex);
                        }
                    }
                }
            }
            //Pattern标记
                //见P304-305
        //split()
            public static class SplitDemo {
                public static void main(String[] args) {
                    String input = "This!!unusual use !!of exclamaion!!points";
                    Print.print(Arrays.toString(Pattern.compile("!!").split(input)));
                    Print.print(Arrays.toString(Pattern.compile("!!").split(input, 3)));
                }
            }
        //替换操作
            /*!  kk  xx
             Here is A block of text to use as input to
            the regular expression matcher. Note that we'll
            first extract the block of text by looking for
            the special delimiters, then process the
            extracted block. !*/
            public static class TheReplacements {
                public static void main(String[] args) {
                    String s = TextFile.read("/Users/julian/cyl/BaseJavaKotlin/BaseJava/src/ThinkingInJava.mString/MString.java");
                    Matcher mInput = Pattern.compile("/\\*!(.*)!\\*/", Pattern.DOTALL).matcher(s);
                    if (mInput.find()) {
                        s = mInput.group(1);
                    }
                    s = s.replaceAll(" {2,}", " ");
                    s = s.replaceAll("(?m)^ +", "");
                    Print.print(s);
                    s = s.replaceFirst("[aeiou]", "(VOWEL1)");
                    StringBuffer sbuf = new StringBuffer();
                    Pattern p = Pattern.compile("[aeiou]");
                    Matcher m = p.matcher(s);
                    while (m.find()) {
                        m.appendReplacement(sbuf, m.group().toUpperCase());
                    }
                    m.appendTail(sbuf);
                    Print.print(sbuf);
                }
            }
        //reset()
            //通过reset()，可以将现有的Matcher对象应用于一个新的字符序列；
            //使用不带参数的reset()，可以将Matcher对象重新设置到当前字符序列的起始位置。
            public static class Resetting {
                public static void main(String[] args) {
                    Matcher m = Pattern.compile("[frb][aiu][gx]").matcher("fix the rug with bags");
                    while (m.find()) {
                        Print.print(m.group() + " ");
                    }
                    Print.print();
                    m.reset("fix the rig with rags");
                    while (m.find()) {
                        Print.print(m.group() + " ");
                    }
                }
            }
        //正则表达式与Java I/O
            public static class JGrep {
                public static void main(String[] args) {
                    if (args.length<2) {
                        System.exit(0);
                    } else {
//                        String regex = new String(args[1]);
//                        Print.print(regex);
//                        Pattern p = Pattern.compile(regex);
                        Pattern p = Pattern.compile("\\b[Ssct]\\w+");
                        int index = 0;
                        Matcher m = p.matcher("");
                        for (String line : new TextFile(args[0])) {
                            m.reset(line);
                            while (m.find()) {
                                Print.print(index++ + ": " + m.group() + ": " + m.start());
                            }
                        }
                    }
                }
            }
    //13.7 扫描输入
        public static class BetterRead {
            private static String input = "Sir Robin of Camelot\n22 1.61803";
            public static void main(String[] args) {
                Scanner stdin = new Scanner(input);
                Print.print("What is your name?");
                String name = stdin.nextLine();
                Print.print(name);
                Print.print("How old are you? What is your favorite double?");
                Print.print("(input: <age> <double>)");
                int age = stdin.nextInt();
                double favorite = stdin.nextDouble();
                Print.print(age);
                Print.print(favorite);
                System.out.format("Hi %s.\n", name);
                System.out.format("In 5 years you'll be %d.\n", (age+5));
                System.out.format("My favorite double is %f.\n", (favorite/2));
            }
        }
        //Scanner的构造器可以接受任何类型的输入对象，包括FIle对象、InputStream、String或者Readable对象。
        //Scanner定界符
            public static class ScannerDelimiter {
                public static void main(String[] args) {
                    Scanner scanner = new Scanner("12, 42, 78, 99, 42");
                    scanner.useDelimiter("\\s*,\\s*");
                    while (scanner.hasNextInt()) {
                        Print.print(scanner.nextInt());
                    }
                    Pattern p = scanner.delimiter();
                    Print.print(p.toString());
                }
            }
        //用正则表达式扫描
            public static class ThreatAnalyzer {
                static String threatData =
                        "58.27.82.161@01/10/2005\n" +
                        "55.69.124.128@01/11/2005\n" +
                        "204.27.82.186@01/11/2005\n" +
                        "192.27.168.191@01/10/2006\n" +
                        "108.212.82.161@23/12/2006\n" +
                        "[Next log section with different data format]";
                public static void main(String[] args) {
                    Scanner scanner = new Scanner(threatData);
                    String pattern = "(\\d+[.]\\d+[.]\\d+[.]\\d+)@(\\d{2}/\\d{2}/\\d{4})";  //正则表达式中不能含有定界符；
                    while (scanner.hasNext(pattern)) {
                        scanner.next(pattern);
                        MatchResult match = scanner.match();
                        String ip = match.group(1);
                        String date = match.group(2);
                        System.out.format("Threat on %s from %s\n", date, ip);
                    }
                }
            }
    //13.8 StringTokenizer
        //已经废弃了。
    //13.9 总结
}
