package Others;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ClearComment {

    public static String rootDir = "";

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
//        deepDir(rootDir);
        deepDir();
    }

    public static void deepDir() throws FileNotFoundException, UnsupportedEncodingException {
        String line = null;
        InputStream is = new FileInputStream(GetAllFiles.mFilePathList);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            line = line.trim();
            System.out.println(line);
            if (!line.endsWith("StateMachine.java")) {  //因为注释文档太长，估计超过可处理的长度，手动单独搞
                clearComment(line);
            }
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deepDir(String rootDir) throws FileNotFoundException, UnsupportedEncodingException {
        File folder = new File(rootDir);
        if (folder.isDirectory()) {
            String[] files = folder.list();
            for (int i = 0; i < files.length; i++) {
                File file = new File(folder, files[i]);
                if (file.isDirectory() && file.isHidden() == false) {
//                    System.out.println(file.getPath());
                    deepDir(file.getPath());
                } else if (file.isFile() && file.getName().endsWith(".java")) {
                    clearComment(file.getPath());
                }
            }
        } else if (folder.isFile() && folder.getName().endsWith(".java")) {
            clearComment(folder.getPath());
        }
    }

    public static void clearComment(String filePathAndName) throws FileNotFoundException, UnsupportedEncodingException {
        StringBuffer buffer = new StringBuffer();
        String line = null;
        InputStream is = new FileInputStream(filePathAndName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            buffer.append(line);
            buffer.append("\r\n");
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileContent = buffer.toString();

        Map<String, String> patterns = new HashMap<String, String>();
        patterns.put("([^:])\\/\\/.*", "$1");
        patterns.put("\\s+\\/\\/.*", "");
        patterns.put("^\\/\\/.*", "");
        patterns.put("^\\/\\*\\*.*\\*\\/$", "");
        patterns.put("\\/\\*.*\\*\\/", "");
        patterns.put("/\\*(\\s*\\*\\s*.*\\s*?)*\\*\\/", "");

        Iterator<String> keys = patterns.keySet().iterator();
        String key = null, value = "";
        while (keys.hasNext()) {

            key = keys.next();
            value = patterns.get(key);
            fileContent = replaceAll(fileContent, key, value);
        }
//        System.out.println(fileContent);

        try {
            File f = new File(filePathAndName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            FileOutputStream out = new FileOutputStream(filePathAndName);
            byte[] bytes = fileContent.getBytes("UTF-8");
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String replaceAll(String fileContent, String patternString, String replace) {
        String str = "";
        Matcher m = null;
        Pattern p = null;
        try {
            p = Pattern.compile(patternString);
            m = p.matcher(fileContent);
            str = m.replaceAll(replace);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            m = null;
            p = null;
        }
        return str;
    }

}