package EffectiveJava3rd.bCreatingAndDestroyingObjects;

import java.io.*;

//09 使用try-with-resources语句替代try-finally语句
public class B09 {
    // try-finally - No longer the best way to close resources!
    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }

    // try-finally is ugly when used with more than one resource!
    private static final int BUFFER_SIZE = 1024;
    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    // try-with-resources - the the best way to close resources!
    static String rightFirstLineOfFile(String path) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }

    // try-with-resources on multiple resources - short and sweet
    static void rightCopy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        }
    }

    // try-with-resources with a catch clause
    static String bestFirstLineOfFile(String path, String defaultVal) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }

    //在处理必须关闭的资源时，使用try-with-resources语句替代try-finally语句。生成的代码更简洁，更清晰，并且生成的异常更有用。

}
