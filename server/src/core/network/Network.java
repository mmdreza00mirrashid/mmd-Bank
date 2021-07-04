package core.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Network implements Runnable{

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8585);
            while (true){
                Thread t =new Thread(new Response(ss.accept()));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void send(  String data,Socket client) throws IOException {
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeUTF(data);
        out.flush();
    }

    public static String receive(Socket client) throws IOException {
        DataInputStream in = new DataInputStream(client.getInputStream());
        return in.readUTF();
    }
}
