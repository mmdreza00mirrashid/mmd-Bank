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

            String inputTest="Add Deposited Account";
            if(reader.readLine().equals(inputTest)){
                for(int i=0;i<10;i++)
                    System.out.println(reader.readLine());

            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
