package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Account {
    String name ,user ,pass ,phone ,mail;
    long savingBalance=0,primaryBalance=0;
    ArrayList<Transaction> history=new ArrayList<Transaction>();
    //server info
    Socket mSocket;
    int port=0505;
    String serverAddress = "185.51.200.2";//for example
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;
    public void transfer(final String serverAddress ,int port){
        OutputStream toServerStream;
        PrintWriter writer;

        try {
            Socket mSocket = new Socket("localhost", port);//will use server address later
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println("Sign In");
            writer.println(name);
            writer.println(user);
            writer.println(pass);
            writer.println(phone);
            writer.println(mail);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean login(String user , String pass){
        try {
            mSocket=new Socket("localhost", port);
            toServerStream = mSocket.getOutputStream();
            fromServerStream = mSocket.getInputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            writer.println("Log In");
            writer.println(user);
            writer.println(pass);
            if(reader.readBoolean()){
                primaryBalance=Integer.parseInt(reader.readLine());
                savingBalance=Integer.parseInt(reader.readLine());
                return true;
            }
            else
                return false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void addTransaction(String comment ,long amount){
        try {
            Transaction transaction=new Transaction(amount ,comment);
            mSocket=new Socket("localhost", port);
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println("Add Transaction");
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(toServerStream));
            out.writeObject(transaction);
            out.flush();
            history.add(transaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
