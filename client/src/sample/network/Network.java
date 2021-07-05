package sample.network;

import sample.Alert;

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
        }
    }

    public static void send(  String data) throws IOException, InterruptedException {
        if(server==null) {
            Thread t = new Thread(new Network());
            t.start();
            t.join();
        }
        try {
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF(data);
            out.flush();
        }catch (Exception e) {
            Alert.message("در ارتباط با سرور مشکلی پیش امده است");
        }

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

