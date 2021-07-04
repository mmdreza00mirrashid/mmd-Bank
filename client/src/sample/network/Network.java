package sample.network;

import java.io.*;
import java.net.Socket;

public class Network implements Runnable{
    private static Socket server;

    @Override
    public void run() {
        try {
            if(server==null)
                server = new Socket("localhost",8585);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(  String data) throws IOException, InterruptedException {
        if(server==null) {
            Thread t = new Thread(new Network());
            t.start();
            t.join();
        }
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        out.writeUTF(data);
        out.flush();
    }

    public static String receive() throws IOException, InterruptedException {
        if(server==null) {
            Thread t = new Thread(new Network());
            t.start();
            t.join();
        }
        DataInputStream in = new DataInputStream(server.getInputStream());
        return in.readUTF();
    }

}

