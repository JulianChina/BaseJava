package ThinkingInJava.rJavaIOSystem;

import net.mindview.util.Directory;
import net.mindview.util.OSExecute;
import net.mindview.util.PPrint;
import net.mindview.util.ProcessFiles;
import nu.xom.*;
import ThinkingInJava.zUtils.Print;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.zip.*;

public class JavaIOSystem {
    //不仅存在各种I/O源端和想要与之通信的接收端（文件、控制台、网络链接等），而且还需要以多种不同的方式与它们进行通信（顺序、随机存取、缓冲、二进制、按字符、按行、按字等）。
    //18.1 File类
        //File（文件）类，它既能代表一个特定文件的名称，又能代表一个目录下的一组文件的名称；
        //目录列表器
            public static class DirList {
                public static void main(String[] args) {
                    File path = new File(".");
                    String[] list;
                    if (args.length == 0) {
                        list = path.list();
                    } else {
                        list = path.list(new DirFilter(args[0]));  //回调，【策略模式】
                    }
                    Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
                    for (String dirItem : list) {
                        Print.print(dirItem);
                    }
                }
            }
            private static class DirFilter implements FilenameFilter {
                private Pattern pattern;

                public DirFilter(String regex) {
                    pattern = Pattern.compile(regex);
                }

                @Override
                public boolean accept(File dir, String name) {
                    return pattern.matcher(name).matches();
                }
            }

            public static class DirList2 {
                public static FilenameFilter filter(final String regex) {
                    return new FilenameFilter() {
                        private Pattern pattern = Pattern.compile(regex);
                        @Override
                        public boolean accept(File dir, String name) {
                            return pattern.matcher(name).matches();
                        }
                    };
                }
                public static void main(String[] args) {
                    File path = new File(".");
                    String[] list;
                    if (args.length == 0) {
                        list = path.list();
                    } else {
                        list = path.list(filter(args[0]));  //回调，【策略模式】
                    }
                    Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
                    for (String dirItem : list) {
                        Print.print(dirItem);
                    }
                }
            }

            public static class DirList3 {
                public static void main(String[] args) {
                    File path = new File(".");
                    String[] list;
                    if (args.length == 0) {
                        list = path.list();
                    } else {
                        list = path.list(new FilenameFilter() {
                            private Pattern pattern = Pattern.compile(args[0]);
                            @Override
                            public boolean accept(File dir, String name) {
                                return pattern.matcher(name).matches();
                            }
                        });  //回调，【策略模式】
                    }
                    Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
                    for (String dirItem : list) {
                        Print.print(dirItem);
                    }
                }
            }
        //目录实用工具
            public static class DirectoryDemo {
                public static void main(String[] args) throws IOException {
                    PPrint.pprint(Directory.walk(".").dirs);
                    Print.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    for (File file : Directory.local(".", "\\.i.*")) {
                        Print.print(file);
                    }
                    Print.print("------------------------------");
                    for (File file : Directory.walk(".", "T.*\\.java")) {
                        Print.print(file.getCanonicalPath());
                    }
                    Print.print("==============================");
                    for (File file : Directory.walk(".", ".*[Zz].*\\.class")) {
                        Print.print(file);
                    }

                    Print.print("++++++++++++++++++++++++++++++");
                    File dot = new File(".");
                    Print.print(dot.getAbsolutePath());
                    Print.print(dot.getAbsoluteFile());
                    Print.print(dot.getCanonicalPath());
                    Print.print(dot.getCanonicalFile());
                    Print.print(dot.getName());
                    Print.print(dot.getParent());
                    Print.print(dot.getParentFile());
                    Print.print(dot.getPath());
                    Print.print("++++++++++++++++++++++++++++++");
                    new ProcessFiles(Print::print, "java").start(args);  //【策略模式】
                }
            }
        //目录的检查及创建
            //File类不仅仅只代表存在的文件或目录。也可以用File对象来创建新的目录或尚不存在的整个目录路径。
            public static class MakeDirectories {
                private static void usage() {
                    System.err.println("Usage:MakeDirectories path1 ...\n" +
                            "Creates each path\n" +
                            "Usage:MakeDirectories -d path1 ...\n" +
                            "Deletes each path\n" +
                            "Usage:MakeDirectories -r path1 path2\n" +
                            "Renames from path1 to path2");
                    System.exit(1);
                }
                private static void fileData(File f) {
                    System.out.println("Absolute path: " + f.getAbsolutePath() +
                            "\n Can read: " + f.canRead() +
                            "\n Can write: " + f.canWrite() +
                            "\n getName: " + f.getName() +
                            "\n getParent: " + f.getParent() +
                            "\n getPath: " + f.getPath() +
                            "\n length: " + f.length() +
                            "\n lastModified: " + f.lastModified());
                    if (f.isFile()) {
                        Print.print("It's a file");
                    } else {
                        Print.print("It's a directory");
                    }
                }
                public static void main(String[] args) {
                    if (args.length < 1) usage();
                    if (args[0].equals("-r")) {
                        if (args.length != 3) usage();
                        File old = new File(args[1]), rname = new File(args[2]);
                        old.renameTo(rname);
                        fileData(old);
                        fileData(rname);
                        return;
                    }
                    int count = 0;
                    boolean del = false;
                    if (args[0].equals("-d")) {
                        count++;
                        del = true;
                    }
                    count--;
                    while (++count < args.length) {
                        File f = new File(args[count]);
                        if (f.exists()) {
                            Print.print(f + " exists");
                            if (del) {
                                Print.print("deleting... " + f);
                                f.delete();
                            }
                        } else {
                            if (!del) {
                                f.mkdirs();
                                Print.print("created " + f);
                            }
                        }
                        fileData(f);
                    }
                }
            }
    //18.2 输入和输出
        //"流"代表任何有能力产出数据的数据源对象或者是有能力接收数据的接收端对象；
        //我们很少使用单一的类来创建流对象，而是通过叠加多个对象来提供所期望的功能（【装饰器设计模式】）；
        //InputStream类型
            //InputStream的作用是用来表示那些从不同数据源产生输入的类；这些源包括：
                //字节数组；String对象；文件；"管道"；一个由其他种类的流组成的序列，以便我们可以将它们收集合并到一个流内；其他数据源，如Internet连接等；
            //每一种数据源都有相应的InputStream子类；FilterInputStream为"装饰器"类提供基类，"装饰器"类可以把属性或有用的接口与输入流连接在一起；
            //见P534表；
        //OutputStream类型
            //该类别的类决定了输出所要去往的目标：
                //字节数组；文件；管道；
            //FilterOutputStream为"装饰器"类提供基类，"装饰器"类可以把属性或有用的接口与输出流连接在一起；
    //18.3 添加属性和有用的接口
        //FilterInputStream和FilterOutputStream这两个类是装饰器的必要条件（以便能为所有正在被修饰的对象提供通用接口）；
        //通过FilterInputStream从InputStream读取数据
            //DataInputStream允许我们读取不同的基本类型数据以及String对象；其他FilterInputStream类则在内部修改InputStream的行为方式；
            //见P536表；FilterInputStream类型：DataInputStream、BufferedInputStream、LineNumberInputStream、PushbackInputStream；
        //通过FilterOutputStream向OutputStream写入
            //DataOutputStream可以将各种基本数据类型以及String对象格式化输出到"流"中，这样，任何机器上的任何DataInputStream都能够读取它们；
            //PrintStream是为了以可视化格式打印所有的基本数据类型以及String对象；PrintStream未完全国际化；
            //BufferedOutputStream对数据流使用缓冲技术，当每次向流写入时，不必每次都进行实际的物理写动作；
            //见P537表；FilterOutputStream类型：DataOutputStream、PrintStream、BufferedOutputStream；
    //18.4 Reader和Writer
        //InputStream和OutputStream在以面向字节形式的I/O中仍可以提供极有价值的功能；Reader和Writer则提供兼容Unicode与面向字符的I/O功能；
        //InputStreamReader可以把InputStream转换为Reader，而OutputStreamWriter可以把OutputStream转换为Writer；【适配器】
        //数据的来源和去处
            //见P538表；
        //更改流的行为
            //见P538表；
            //无论我们何时使用readLine()，都不应该使用DataInputStream，而应该使用BufferedReader；除了这一点，DataInputStream仍是I/O类库的首选成员；
            //PrintWriter提供了一个既能接受Writer对象，又能接受任何OutputStream对象的构造器；
        //未发生变化的类
            //DataOutputStream、File、RandomAccessFile、SequenceInputStream；
    //18.5 自我独立的类：RandomAccessFile
        //RandomAccessFile适用于由大小已知的记录组成的文件；
        //只有RandomAccessFile支持搜寻方法，并且只适用于文件；
    //18.6 I/O流的典型使用方式
        static final String fileName = "/Users/julian/cyl/BaseJavaKotlin/BaseJava/src/ThinkingInJava.rJavaIOSystem/JavaIOSystem.java";
        //缓冲输入文件
            public static class BufferedInputFile {
                static String read(String filename) throws IOException {
                    BufferedReader in = new BufferedReader(new FileReader(filename));
                    String s;
                    StringBuilder sb = new StringBuilder();
                    while ((s = in.readLine()) != null) {  //readLine()会删除换行符；
                        sb.append(s).append("\n");
                    }
                    in.close();
                    return sb.toString();
                }
                public static void main(String[] args) throws IOException {
                    Print.print(read(fileName));
                }
            }
        //从内存输入
            public static class MemoryInput {
                public static void main(String[] args) throws IOException {
                    StringReader in = new StringReader(BufferedInputFile.read(fileName));
                    int c;
                    while ((c = in.read()) != -1) {
                        Print.printnb((char) c);  //必须转型成char才能正确打印；
                    }
                    in.close();
                }
            }
        //格式化的内存输入
            public static class FormattedMemoryInput {
                public static void main(String[] args) {
                    try {
                        DataInputStream in = new DataInputStream(new ByteArrayInputStream(BufferedInputFile.read(fileName).getBytes()));
                        while (true) {
                            Print.printnb((char) in.readByte());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public static class TestEOF {
                public static void main(String[] args) throws IOException {
                    DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
                    while (in.available() != 0) {
                        Print.printnb((char) in.readByte());
                    }
                }
            }
        //基本的文件输出
            public static class BasicFileOutput {
                static String file = "BasicFileOutput.out";
                public static void main(String[] args) throws IOException {
                    BufferedReader in = new BufferedReader(new StringReader(BufferedInputFile.read(fileName)));
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    int lineCount = 1;
                    String s;
                    while ((s = in.readLine()) != null) {
                        out.println(lineCount++ + ": " + s);
                    }
                    out.close();
                    Print.print(BufferedInputFile.read(file));
                }
            }

            public static class FileOutputShortcut {
                static String file = "FileOutputShortcut.out";
                public static void main(String[] args) throws IOException {
                    BufferedReader in = new BufferedReader(new StringReader(BufferedInputFile.read(fileName)));
                    PrintWriter out = new PrintWriter(file);  //其他常见的写入任务都没有快捷方式；
                    int lineCount = 1;
                    String s;
                    while ((s = in.readLine()) != null) {
                        out.println(lineCount++ + ": " + s);
                    }
                    out.close();
                    Print.print(BufferedInputFile.read(file));
                }
            }
        //存储和恢复数据
            public static class StoringAndRecoveringData {
                public static void main(String[] args) throws IOException {
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Data.txt")));
                    out.writeDouble(3.14159);
                    out.writeUTF("That was pi");
                    out.writeDouble(1.41413);
                    out.writeUTF("Square root of 2");
                    out.close();
                    DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("Data.txt")));
                    Print.print(in.readDouble());
                    Print.print(in.readUTF());
                    Print.print(in.readDouble());
                    Print.print(in.readUTF());
                }
            }
            //如果我们使用DataOutputStream写入数据，Java保证我们可以使用DataInputStream准确地读取数据————无论读和写数据的平台多么不同；
            //我们必须：要么为文件的中数据采用固定的格式，要么将额外的信息保存到文件中，以便能够对其进行解析以确定数据的存放位置；
            //对象序列化和XML可能是更容易的存储和读取复杂数据结构的方式；
        //读写随机访问文件
            //RandomAccessFile拥有读取基本类型和UTF-8字符串的各种具体方法；
            public static class UsingRandomAccessFile {
                static String file = "rtest.dat";
                static void display() throws IOException {
                    RandomAccessFile rf = new RandomAccessFile(file, "r");
                    for (int i = 0; i < 7; i++) {
                        Print.print("Value " + i + ": " + rf.readDouble());
                    }
                    Print.print(rf.readUTF());
                    rf.close();
                }
                public static void main(String[] args) throws IOException {
                    RandomAccessFile rf = new RandomAccessFile(file, "rw");
                    for (int i = 0; i < 7; i++) {
                        rf.writeDouble(i * 1.414);
                    }
                    rf.writeUTF("The end of the file");
                    rf.close();
                    display();
                    rf = new RandomAccessFile(file, "rw");
                    rf.seek(5*8);
                    rf.writeDouble(47.001);
                    rf.close();
                    display();
                }
            }
            //RandomAccessFile除了实现DataInput和DataOutput接口之外，有效地与I/O继承层次结构的其他部分实现了分离；
        //管道流
            //PipedInputStream、PipedOutputStream、PipedReader、PipedWriter的价值在理解多线程之后才会显现，管道流用于任务之间的通信；
    //18.7 文件读写的实用工具
        //一个很常见的程序化任务就是读取文件到内存，修改，然后再写出；
        //见TextFile.class;
        //另一种解决读取文件问题的方法是使用JavaSE5中引入的java.util.Scanner类；但是，这只能用于读取文件，而不能用于写入文件，并且这个工具主要是设计用来创建编程语言的扫描器或"小语言"的；
        //读取二进制文件
            //见BinaryFile.class;
    //18.8 标准I/O
        //标准I/O的意义在于：可以很容易地把程序串联起来，一个程序的标准输出可以成为另一个程序的标准输入；
        //从标准输入中读取
            public static class Echo {
                public static void main(String[] args) throws IOException {
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    String s;
                    while (((s = stdin.readLine()) != null) && (s.length() != 0)) {
                        Print.print(s);
                    }
                }
            }
            //System.in和大多数流一样，通常应该对它进行缓冲；
        //将System.out转换成PrintWriter
            public static class ChangeSystemOut {
                public static void main(String[] args) {
                    PrintWriter out = new PrintWriter(System.out, true);  //第二个参数为true，开启自动清空功能，否则，可能看不到输出；
                    out.println("Hello, world");
                }
            }
        //标准I/O重定向
            public static class Redirecting {
                public static void main(String[] args) throws IOException {
                    PrintStream console = System.out;
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
                    PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("test.out")));
                    System.setIn(in);
                    System.setOut(out);
                    System.setErr(out);
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String s;
                    while ((s = br.readLine()) != null) {
                        Print.print(s);
                    }
                    out.close();
                    System.setOut(console);
                }
            }
            //I/O重定向操纵的是字节流，而不是字符流，因此使用的是InputStream和OutputStream，而不是Reader和Writer；
    //18.9 进程控制
        //经常会需要在Java内部执行其他操作系统的程序，并且要控制这些程序的输入和输出；
        //一项常见的任务是运行程序，并将产生的输出发送到控制台；
        public static class OSExecuteDemo {
            public static void main(String[] args) {
                OSExecute.command("javap out/production/BaseJava/ThinkingInJava.rJavaIOSystem/JavaIOSystem$OSExecuteDemo");
            }
        }
        //ProcessBuilder：用于创建操作系统进程，提供一种启动和管理进程（应用程序）的方法；每个ProcessBuilder实例管理一个进程属性集；
    //18.10 新I/O
        //nio速度的提高来自于所使用的结构更接近于操作系统执行I/O的方式：通道和缓冲器；
        //通道要么从缓冲器获得数据，要么向缓冲器发送数据；（可以形象地把通道比作煤矿，把缓冲器比作卡车）
        //唯一直接与通道交互的缓冲器是ByteBuffer————可以存储未加工字节的缓冲器；
        //旧I/O类库中的字节操作流FileInputStream、FileOutputStream、RandomAccessFile被修改了，用以产生FileChannel；
        //java.nio.channels.Channels类提供了实用方法，用以在通道中产生Reader和Writer；
        public static class GetChannel {
            private static final int BSIZE = 1024;
            public static void main(String[] args) throws IOException {
                //Write a file
                FileChannel fc = new FileOutputStream("data.txt").getChannel();
                fc.write(ByteBuffer.wrap("Some text ".getBytes()));
                fc.close();
                fc = new RandomAccessFile("data.txt", "rw").getChannel();
                fc.position(fc.size());
                fc.write(ByteBuffer.wrap("Some more".getBytes()));
                fc.close();
                //Read the file
                fc = new FileInputStream("data.txt").getChannel();
                ByteBuffer buff = ByteBuffer.allocate(BSIZE);
                fc.read(buff);
                buff.flip();
                while (buff.hasRemaining()) {
                    Print.printnb((char) buff.get());
                }
            }
        }
        //通道是一种相当基础的东西：可以向它传送用于读写的ByteBuffer，并且可以锁定文件的某些区域用于独占式访问；
        //nio的目标就是快速移动大量数据；
        public static class ChannelCopy {
            private static final int BSIZE = 1024;
            public static void main(String[] args) throws IOException {
                if (args.length != 2) {
                    Print.print("arguments: sourcefile destfile");
                    System.exit(1);
                }
                FileChannel in = new FileInputStream(args[0]).getChannel(), out = new FileOutputStream(args[1]).getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
                while (in.read(buffer) != -1) {
                    buffer.flip();  //Prepare for writing
                    out.write(buffer);
                    buffer.clear();  //Prepare for reading
                }
            }
        }
        //特殊方法transferTo()和transferFrom()允许我们将一个通道和另一个通道直接相连；
        public static class TransferTo {
            public static void main(String[] args) throws IOException {
                if (args.length != 2) {
                    Print.print("arguments: sourcefile destfile");
                    System.exit(1);
                }
                FileChannel in = new FileInputStream(args[0]).getChannel(), out = new FileOutputStream(args[1]).getChannel();
                in.transferTo(0, in.size(), out);
//                out.transferFrom(in, 0, in.size());
            }
        }
        //转换数据
            public static class BufferToText {
                private static final int BSIZE = 1024;
                public static void main(String[] args) throws IOException {
                    FileChannel fc = new FileOutputStream("data2.txt").getChannel();
                    fc.write(ByteBuffer.wrap("Some text".getBytes()));
                    fc.close();
                    fc = new FileInputStream("data2.txt").getChannel();
                    ByteBuffer buff = ByteBuffer.allocate(BSIZE);
                    fc.read(buff);
                    buff.flip();
                    //Doesn't work
                    Print.print(buff.asCharBuffer());

                    //Decode using the system default Charset
                    buff.rewind();
                    String encoding = System.getProperty("file.encoding");  //发现默认字符集；
                    Print.print("Decoded using " + encoding + ": " + Charset.forName(encoding).decode(buff));  //对字符串进行解码；

                    //we could encode with something that will print
                    fc = new FileOutputStream("data2.txt").getChannel();
                    fc.write(ByteBuffer.wrap("Some text".getBytes("UTF-16BE")));
                    fc.close();
                    fc = new FileInputStream("data2.txt").getChannel();
                    buff.clear();
                    fc.read(buff);
                    buff.flip();
                    Print.print(buff.asCharBuffer());

                    //use a CharBuffer to write through
                    fc = new FileOutputStream("data2.txt").getChannel();
                    buff = ByteBuffer.allocate(24);
                    buff.asCharBuffer().put("Some text");
                    fc.write(buff);
                    fc.close();
                    fc = new FileInputStream("data2.txt").getChannel();
                    buff.clear();
                    fc.read(buff);
                    buff.flip();
                    Print.print(buff.asCharBuffer());
                }
            }
            //缓冲器容纳的是普通的字节，为了把它们转成字符，我们要么在输入它们的时候对其进行编码，要么在将其从缓冲器输出时对它们进行解码；
            public static class AvailableCharsets {
                public static void main(String[] args) {
                    SortedMap<String, Charset> charsets = Charset.availableCharsets();
                    Iterator<String> it = charsets.keySet().iterator();
                    while (it.hasNext()) {
                        String csName = it.next();
                        Print.printnb(csName);
                        Iterator aliases = charsets.get(csName).aliases().iterator();
                        if (aliases.hasNext()) {
                            Print.printnb(": ");
                        }
                        while (aliases.hasNext()) {
                            Print.printnb(aliases.next());
                            if (aliases.hasNext()) {
                                Print.printnb(", ");
                            }
                        }
                        Print.print();
                    }
                }
            }
        //获取基本类型
            //尽管ByteBuffer只能保存字节类型的数据，但是它具有可以从其所容纳的字节中产生出各种不同基本类型值的方法；
            public static class GetData {
                private static final int BSIZE = 1024;
                public static void main(String[] args) {
                    ByteBuffer bb = ByteBuffer.allocate(BSIZE);
                    int i = 0;
                    while (i++ < bb.limit()) {
                        if (bb.get() != 0) {
                            Print.print("nonzero");
                        }
                    }
                    Print.print("i = " + i);

                    bb.rewind();
                    bb.asCharBuffer().put("Howdy!");
                    char c;
                    while ((c = bb.getChar()) != 0) {
                        Print.printnb(c + " ");
                    }
                    Print.print();

                    bb.rewind();
                    bb.asShortBuffer().put((short) 471142);
                    Print.print(bb.getShort());

                    bb.rewind();
                    bb.asIntBuffer().put(99471142);
                    Print.print(bb.getInt());

                    bb.rewind();
                    bb.asLongBuffer().put(99471142);
                    Print.print(bb.getLong());

                    bb.rewind();
                    bb.asFloatBuffer().put(99471142);
                    Print.print(bb.getFloat());

                    bb.rewind();
                    bb.asDoubleBuffer().put(99471142);
                    Print.print(bb.getDouble());
                }
            }
        //视图缓冲器
            //视图缓冲器可以让我们通过某个特定的基本数据类型的视窗查看其底层的ByteBuffer；
            public static class ViewBuffers {
                public static void main(String[] args) {
                    ByteBuffer bb = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, 0, 'a'});
                    bb.rewind();
                    Print.printnb("Byte Buffer ");
                    while (bb.hasRemaining()) {
                        Print.printnb(bb.position() + "->" + bb.get() + ", ");
                    }
                    Print.print();

                    CharBuffer cb = ((ByteBuffer) bb.rewind()).asCharBuffer();
                    Print.printnb("Char Buffer ");
                    while (cb.hasRemaining()) {
                        Print.printnb(cb.position() + "->" + cb.get() + ", ");
                    }
                    Print.print();

                    ShortBuffer sb = ((ByteBuffer) bb.rewind()).asShortBuffer();
                    Print.printnb("Short Buffer ");
                    while (sb.hasRemaining()) {
                        Print.printnb(sb.position() + "->" + sb.get() + ", ");
                    }
                    Print.print();

                    IntBuffer ib = ((ByteBuffer) bb.rewind()).asIntBuffer();
                    Print.printnb("Int Buffer ");
                    while (ib.hasRemaining()) {
                        Print.printnb(ib.position() + "->" + ib.get() + ", ");
                    }
                    Print.print();

                    LongBuffer lb = ((ByteBuffer) bb.rewind()).asLongBuffer();
                    Print.printnb("Long Buffer ");
                    while (lb.hasRemaining()) {
                        Print.printnb(lb.position() + "->" + lb.get() + ", ");
                    }
                    Print.print();

                    FloatBuffer fb = ((ByteBuffer) bb.rewind()).asFloatBuffer();
                    Print.printnb("Float Buffer ");
                    while (fb.hasRemaining()) {
                        Print.printnb(fb.position() + "->" + fb.get() + ", ");
                    }
                    Print.print();

                    DoubleBuffer db = ((ByteBuffer) bb.rewind()).asDoubleBuffer();
                    Print.printnb("Double Buffer ");
                    while (db.hasRemaining()) {
                        Print.printnb(db.position() + "->" + db.get() + ", ");
                    }
                    Print.print();
                }
            }
            //不同的机器会使用不同的字节排序方法来存储数据；如，0x12345678的存储————大端（高位优先）0x12 34 56 78；小端（地位优先）0x78 56 34 12；
            public static class Endians {
                public static void main(String[] args) {
                    ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
                    bb.asCharBuffer().put("abcdef");
                    Print.print(Arrays.toString(bb.array()));

                    bb.rewind();
                    bb.order(ByteOrder.BIG_ENDIAN);
                    bb.asCharBuffer().put("abcdef");
                    Print.print(Arrays.toString(bb.array()));

                    bb.rewind();
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    bb.asCharBuffer().put("abcdef");
                    Print.print(Arrays.toString(bb.array()));
                }
            }
        //用缓冲器操纵数据
            //ByteBuffer是将数据移进移出通道的唯一方式，并且我们只能创建一个独立的基本类型缓冲器，或者使用"as"方法从ByteBuffer中获得；我们不能把基本类型缓冲器转成ByteBuffer；
        //缓冲器的细节
            //Buffer由数据及mark(标记)、position(位置)、limit(界限)、capacity(容量)四个索引组成；
            //见P560表、P561图；
                //clear(): position->0, limit->capacity;
                //flip(): limit->position, position->0;
                //mark(): mark->position;
                //reset(): position->mark;
            //我们总是以操纵ByteBuffer为目标，因为它可以和通道进行交互；
            //见P562-563解析流程图；
            //如果要打印缓冲器，只能打印出position和limit之间的字符；
        //内存映射文件
            //内存映射文件允许我们创建和修改那些因为太大而不能放入内存的文件；
            //有了内存映射文件，我们就可以假定整个文件都放在内存中，而且可以完全把它们当做非常大的数组来访问；
            public static class LargeMappedFiles {
                static int length = 0x8FFFFFF;
                public static void main(String[] args) throws IOException {
                    MappedByteBuffer out = new RandomAccessFile("test.dat", "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
                    for (int i = 0; i < length; i++) {
                        out.put((byte) 'x');
                    }
                    Print.print("Finished writing");
                    for (int i = length / 2; i < length / 2 + 6; i++) {
                        Print.printnb((char) out.get(i));
                    }
                }
            }
            //底层操作系统的文件映射工具是用来最大化地提高性能；
            //尽管"旧"的I/O流在用nio实现之后性能有所提高，但是"映射文件访问"往往可以更加显著地加快速度；
        //文件加锁
            //文件锁对其他的操作系统进程是可见的，因为Java的文件加锁直接映射到了本地操作系统的加锁工具；
            public static class FileLocking {
                public static void main(String[] args) throws Exception {
                    FileOutputStream fos = new FileOutputStream("file.txt");
                    FileLock fl = fos.getChannel().tryLock();
                    if (fl != null) {
                        Print.print("Locked File");
                        TimeUnit.MILLISECONDS.sleep(100);
                        fl.release();
                        Print.print("Released Lock");
                    }
                    fos.close();
                }
            }
            //SocketChannel、DatagramChannel和ServerSocketChannel不需要加锁，因为它们是从单进程实体继承而来；我们通常不在两个进城之间共享网络Socket；
            //tryLock()是非阻塞式的，lock()是阻塞式的；
            //无参数的加锁方法将根据文件尺寸的变化而变化，但是具有固定尺寸的锁不随文件尺寸的变化而变化；
            //对独占锁或者共享锁的支持必须由底层的操作系统提供；锁的类型（共享或独占）可以通过FileLock.isShared()进行查询；
            //对映射文件的部分加锁
            public static class LockingMappedFiles {
                static final int LENGTH = 0x8FFFFFF;
                static FileChannel fc;
                public static void main(String[] args) throws IOException {
                    fc = new RandomAccessFile("test.dat", "rw").getChannel();
                    MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, LENGTH);
                    for (int i = 0; i < LENGTH; i++) {
                        out.put((byte) 'x');
                    }
                    new LockAndModify(out, 0, LENGTH / 3);
                    new LockAndModify(out, LENGTH / 2, LENGTH / 2 + LENGTH / 4);
                }
                private static class LockAndModify extends Thread {
                    private ByteBuffer buff;
                    private int start, end;
                    LockAndModify(ByteBuffer mbb, int start, int end) {
                        this.start = start;
                        this.end = end;
                        mbb.limit(end);
                        mbb.position(start);
                        buff = mbb.slice();
                        start();
                    }

                    @Override
                    public void run() {
                        try {
                            FileLock fl = fc.lock(start, end, false);
                            Print.print(Thread.currentThread().getName() + " Locked: " + start + " to " + end);
                            Print.print(Thread.currentThread().getName() + " position: " + buff.position() + ", limit " + buff.limit());
                            while (buff.position() < buff.limit() - 1) {
                                buff.put((byte) (buff.get() + 1));
                            }
                            fl.release();
                            Print.print(Thread.currentThread().getName() + " Released: " + start + " to " + end);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
    //18.11 压缩
        //Java I/O类库中的类支持读写压缩格式的数据流；压缩类库是按字节方式而不是字符方式处理的；
        //见P568表；
        //用GZIP进行简单压缩
            //对单个数据流进行压缩，可能是比较适合的选择；
            public static class GZIPCompress {
                public static void main(String[] args) throws IOException {
                    BufferedReader in = new BufferedReader(new FileReader(args[0]));
                    BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream("test.gz")));
                    Print.print("Writing file");
                    int c;
                    while ((c = in.read()) != -1) {
                        out.write(c);
                    }
                    in.close();
                    out.close();
                    Print.print("Reading file");
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream("test.gz"))));
                    String s;
                    while ((s = in2.readLine()) != null) {
                        Print.print(s);
                    }
                }
            }
        //用Zip进行多文件保存
            public static class ZipCompress {
                public static void main(String[] args) throws IOException {
                    FileOutputStream f = new FileOutputStream("test.zip");
                    CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());  //说明见P570；
                    ZipOutputStream zos = new ZipOutputStream(csum);
                    BufferedOutputStream out = new BufferedOutputStream(zos);
                    zos.setComment("A test of Java Zipping");
                    for (String arg : args) {
                        Print.print("Writing file " + arg);
                        BufferedReader in = new BufferedReader(new FileReader(arg));
                        zos.putNextEntry(new ZipEntry(arg));  //说明见P570；
                        int c;
                        while ((c = in.read()) != -1) {
                            out.write(c);
                        }
                        in.close();
                        out.flush();
                    }
                    out.close();
                    Print.print("CheckSum: " + csum.getChecksum().getValue());
                    Print.print("Reading file");
                    FileInputStream fi = new FileInputStream("test.zip");
                    CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32());
                    ZipInputStream in2 = new ZipInputStream(csumi);
                    BufferedInputStream bis = new BufferedInputStream(in2);
                    ZipEntry ze;
                    while ((ze = in2.getNextEntry()) != null) {  //说明见P570；
                        Print.print("Reading file " + ze);
                        int x;
                        while ((x = bis.read()) != -1) {
                            System.out.write(x);
                        }
                    }
                    if (args.length == 1) {
                        Print.print("CheckSum: " + csumi.getChecksum().getValue());
                    }
                    bis.close();
                    ZipFile zf = new ZipFile("test.zip");  //说明见P570；
                    Enumeration e = zf.entries();
                    while (e.hasMoreElements()) {
                        ZipEntry ze2 = (ZipEntry) e.nextElement();
                        Print.print("File: " + ze2);
                    }
                }
            }
            //GZIP或Zip库的使用并不仅仅局限于文件————它可以压缩任何东西，包括需要通过网络发送的数据；
        //Java档案文件
            //Zip格式也被应用于JAR(Java ARchive, Java档案文件)文件格式中；
            //JAR文件也是跨平台的；JAR文件中的每个条目都可以加上数字化签名；
            //一个JAR文件由一组压缩文件构成，同时还有一张描述了所有这些文件的"文件清单"（可以自行创建文件清单，也可以由jar程序自动生成）；
            //jar命令使用说明见P571；
    //18.12 对象序列化
        //Java的对象序列化将那些实现了Serializable接口的对象转换成一个字节序列，并能够在以后将这个字节序列完全恢复为原来的对象；
        //对象必须在程序中显示地序列化（serialize）和反序列化还原（deserialize）；
        //对象序列化加入为了支持两种特性：Java的远程方法调用（Remote Method Invocation, RMI）；Java Beans；
        //对象序列化是基于字节的，通过包装ObjectOutputStream和ObjectInputStream来进行；对象序列化建立的是能追踪对象内所包含的所有引用的"对象网"；
        //Worm.java;
        //可利用序列化将对象读写到任何DataInputStream或者DataOutputStream，甚至包括网络；
        //在对一个Serializable对象进行还原的过程中，没有调用任何构造器，包括默认的构造器；整个对象都是通过从InputStream中取得数据恢复而来的；
        //寻找类
            //Alien.java; FreezeAlien.java; ThawAlien.java;
            //必须保证Java虚拟机能找到相关的.class文件；
        //序列化的控制
            //可通过实现Externalizable接口————代替实现Serializable接口————来对序列化过程进行控制，writeExternal()和readExternal()会在序列化和反序列化还原的过程中被自动调用；
            //对于Serializable对象，对象完全以它存储的二进制位为基础来构造，而不调用构造器；
            //对于Externalizable对象，所有普通的默认构造器都会被调用（包括在字段定义时的初始化），然后调用readExternal()；必须注意————所有默认的构造器都会被调用，才能使Externalizable对象产生正确的行为；
            public static class Blip3 implements Externalizable {
                private int i;
                private String s;
                public Blip3() {  //必须为public
                    Print.print("Blip3 Constructor");
                }
                public Blip3(String x, int a) {
                    Print.print("Blip3(String x, int a) Constructor");
                    s = x;
                    i = a;
                }
                public String toString() { return s + i; }
                @Override
                public void writeExternal(ObjectOutput out) throws IOException {
                    Print.print("Blip3.writeExternal");
                    out.writeObject(s);
                    out.writeInt(i);
                }
                @Override
                public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
                    Print.print("Blip3.readExternal");
                    s = (String) in.readObject();
                    i = in.readInt();
                }
                public static void main(String[] args) throws IOException, ClassNotFoundException {
                    Print.print("Constructing objects: ");
                    Blip3 b3 = new Blip3("A string ", 47);
                    Print.print(b3);
                    ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("Blip3.out"));
                    Print.print("Saving objects: ");
                    o.writeObject(b3);
                    o.close();
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("Blip3.out"));
                    Print.print("Recovering objects: ");
                    b3 = (Blip3) in.readObject();
                    Print.print(b3);
                }
            }
            //我们不仅需要在writeExternal()方法中将来自对象的重要信息写入，还必须在readExternal()中恢复数据；Externalizable没有任何默认行为来写入任何成员对象；
            //transient(瞬时)关键字
                //在Externalizable中，我们可以只对所需要的部分进行显示序列化，这样控制敏感信息被序列化；
                //在Serializable中，所有序列化操作都会自动进行，所以我们需要使用transient关键字逐个字段地关闭序列化；transient的意思是"不麻烦你保存或恢复数据————我自己会处理的"；
                //由于Externalizable对象在默认情况下不保存它们的任何字段，所以transient关键字只能和Serializable对象一起使用；
            //Externalizable的替代方法
                //实现Serializable接口，并"添加添加添加"如下签名的两个方法，它们分别会被ObjectOutputStream和ObjectInputStream中的相应同名方法所调用：
                    //private void writeObject(ObjectOutputStream stream) throws IOException;
                    //private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException;
                //在上述添加的writeObject()内部，可以调用defaultWriteObject()来选择执行默认的writeObject()；
                //在上述添加的readObject()内部，可以调用defaultReadObject()来选择执行默认的readObject()；
                public static class SerialCtl implements Serializable {
                    private String a;
                    private transient String b;
                    public SerialCtl(String aa, String bb) {
                        a = "Not Transient: " + aa;
                        b = "Transient: " + bb;
                    }
                    public String toString() { return a + "\n" + b; }
                    private void writeObject(ObjectOutputStream stream) throws IOException {
                        stream.defaultWriteObject();
                        stream.writeObject(b);
                    }
                    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
                        stream.defaultReadObject();
                        b = (String) stream.readObject();
                    }
                    public static void main(String[] args) throws IOException, ClassNotFoundException {
                        SerialCtl sc = new SerialCtl("Test1", "Test2");
                        Print.print("Before:\n" + sc);
                        ByteArrayOutputStream buff = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(buff);
                        out.writeObject(sc);
                        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buff.toByteArray()));
                        SerialCtl sc2 = (SerialCtl) in.readObject();
                        Print.print("After:\n" + sc2);
                    }
                }
        //使用"持久性"
            //MyWorld.java;
            //我们可以通过一个字节数组来使用对象序列化，从而实现对任何可Serializable对象的"深度复制"————深度复制意味着我们复制的是整个对象网，而不仅仅是基本对象及其引用；
            //只要将任何对象序列化到单一流中，就可以恢复出与我们写入时一样的对象网，并且没有任何意外重复复制出的对象；
            //假如想序列化static值，必须自己动手去实现；
            //必须维护写入序列化文件和从该文件中读回的顺序；
    //18.13 XML
        //一种更具互操作性的对象序列化解决方案是将数据转换为XML格式；
        public static class Person {
            private String first, last;
            public Person(String first, String last) {
                this.first = first;
                this.last = last;
            }
            public nu.xom.Element getXML() {
                nu.xom.Element person = new nu.xom.Element("person");
                nu.xom.Element firstName = new nu.xom.Element("firstName");
                firstName.appendChild(first);
                nu.xom.Element lastName = new nu.xom.Element("lastName");
                lastName.appendChild(last);
                person.appendChild(firstName);
                person.appendChild(lastName);
                return person;
            }

            public Person(nu.xom.Element person) {
                first = person.getFirstChildElement("firstName").getValue();
                last = person.getFirstChildElement("lastName").getValue();
            }
            public String toString() { return first + " " + last; }
            public static void format(OutputStream os, Document doc) throws IOException {
                Serializer serializer = new Serializer(os, "ISO-8859-1");
                serializer.setIndent(4);
                serializer.setMaxLength(60);
                serializer.write(doc);
                serializer.flush();
            }

            public static void main(String[] args) throws IOException {
                List<Person> people = Arrays.asList(new Person("YL", "Chen"), new Person("Jack", "Cheng"), new Person("Mark", "Li"));
                Print.print(people);
                nu.xom.Element root = new nu.xom.Element("people");
                for (Person p : people) {
                    root.appendChild(p.getXML());
                }
                Document doc = new Document(root);
                format(System.out, doc);
                format(new BufferedOutputStream(new FileOutputStream("People.xml")), doc);
            }
        }

        public static class People extends ArrayList<Person> {
            public People(String fileName) throws IOException, ParsingException {
                Document doc = new Builder().build(fileName);
                Elements elements = doc.getRootElement().getChildElements();
                for (int i = 0; i < elements.size(); i++) {
                    add(new Person(elements.get(i)));
                }
            }
            public static void main(String[] args) throws IOException, ParsingException {
                People p = new People("People.xml");
                Print.print(p);
            }
        }
    //18.14 Preferences
        //Preferences API用于存储和读取用户的偏好以及程序配置项的设置，只能存储基本类型和字符串，并且每个字符串的存储长度不能超过8K；
        public static class PreferencesDemo {
            public static void main(String[] args) throws Exception {
                Preferences prefs = Preferences.userNodeForPackage(PreferencesDemo.class);  //用于个别用户的偏好
//                prefs = Preferences.systemNodeForPackage(PreferencesDemo.class);  //用于通用的安装配置
                prefs.put("Location", "Oz");
                prefs.put("Footwear", "Ruby Slippers");
                prefs.putInt("Companions", 4);
                prefs.putBoolean("Are there witches ?", true);
                int usageCount = prefs.getInt("UsageCount", 0);
                usageCount++;
                prefs.putInt("UsageCount", usageCount);
                for (String key : prefs.keys()) {
                    Print.print(key + ": " + prefs.get(key, null));
                }
                Print.print("How many companions does Dorothy have ? " + prefs.getInt("Companions", 0));
            }
        }
        //Preferences API利用合适的系统资源完成了数据存储的任务；
    //18.15 总结
}
