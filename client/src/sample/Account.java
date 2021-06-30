package sample;

import java.io.*;
import java.net.Socket;

public class Account {
    String name ,user ,pass ,phone ,mail;
    public void transfer(final String serverAddress ,int port){
        OutputStream toServerStream;
        PrintWriter writer;

        try {
            Socket mSocket = new Socket("localhost", port);//will use server address later
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println(name);
            writer.println(user);
            writer.println(pass);
            writer.println(phone);
            writer.println(mail);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
