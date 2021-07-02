package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ServerTest {
    ServerSocket mServer;
    int serverPort = 0505;
    long num=1;
    public ServerTest() {
        try{
            mServer = new ServerSocket(serverPort);

            while (true){
                Socket client = mServer.accept();
                System.out.println("new client");
                Thread thread=new Thread(new ClientManagement(this ,client));
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main (String[] args){
        new ServerTest();
    }

}
class ClientManagement implements Runnable{
    Socket client;
    ServerTest server;
    InputStream fromClientStream;
    OutputStream toClientStream;
    DataInputStream reader;
    PrintWriter writer;
    ObjectInputStream in;
    ObjectOutputStream out;
    public ClientManagement(ServerTest server, Socket client){
        this.server=server;
        this.client=client;}
    @Override
    public void run() {
        try {
            toClientStream = client.getOutputStream();
            fromClientStream = client.getInputStream();
            writer = new PrintWriter(toClientStream, true);
            reader = new DataInputStream(fromClientStream);
            in = new ObjectInputStream(fromClientStream);
            out= new ObjectOutputStream(toClientStream);
            String inputTest="Sign In";
            while (true){
                if(reader.readLine().equals(inputTest)){
                    Account input=(Account) in.readObject();
                    System.out.println(input.name);
                    System.out.println(input.phone);


                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
