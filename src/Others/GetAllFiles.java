package Others;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GetAllFiles {
    public static String mFilePathList = "/Users/julian/yc/workPlainText/file_path_list.txt";
    public static String mProjectPath = "/Users/julian/yc/FSpecificYC/zayhu-android-simple-again";
    private static RandomAccessFile raf;
    public static void main(String[] args) throws IOException {
        raf = new RandomAccessFile(mFilePathList, "rw");
        String path = mProjectPath;
        getFiles(path);
    }

    public static void getFiles(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File file1 : files) {
                if (file1.isDirectory()) {
//                    System.out.println("目录：" + file1.getPath());
                    if (file1.getName().startsWith(".")) {
                        continue;
                    }
                    getFiles(file1.getPath());
                } else {
//                    System.out.println("文件：" + file1.getPath());
                    if (!file1.getName().endsWith(".java")) {
                        continue;
                    }
                    raf.writeBytes(file1.getPath().trim() + "\r\n");
                }

            }
        } else {
//            System.out.println("文件：" + file.getPath());
            if (file.getName().endsWith(".java")) {
                raf.writeBytes(file.getPath().trim() + "\r\n");
            }
        }
    }
}