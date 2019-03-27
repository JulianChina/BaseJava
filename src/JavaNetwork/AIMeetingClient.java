package JavaNetwork;

import ThinkingInJava.zUtils.Print;

import java.io.*;
import java.net.Socket;

public class AIMeetingClient {
    String mServerName = "localhost";
    int mPort = 9088;

    public static void main(String[] args) {
        AIMeetingClient client = new AIMeetingClient();
        Print.print("连接到主机：" + client.mServerName + "，端口号：" + client.mPort);
        try {
            Socket socket = new Socket(client.mServerName, client.mPort);
            Print.print("远程主机地址：" + socket.getRemoteSocketAddress());
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            String sendStr = "Hello from " + socket.getLocalSocketAddress();
//            dos.writeUTF();
            dos.write(sendStr.getBytes());

            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            Print.print("服务器响应：" + dis.readUTF());
        } catch (IOException e) {
            Print.print("Julian Socket failed!!! " + e.getMessage());
        }
    }
}
