package sample;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Account {
    String name ,user ,pass ,phone ,mail;
    long savingBalance=0,primaryBalance=0;
    ArrayList<Transaction> history=new ArrayList<Transaction>();
    ArrayList<Deposited> depositedAcc=new ArrayList<Deposited>();
    //server info
    Socket mSocket;
    int port=0505;
    String serverAddress = "localhost";//for example
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;
    public void transfer(final String serverAddress ,int port){
        OutputStream toServerStream;
        PrintWriter writer;

        try {
            Socket mSocket = new Socket(serverAddress, port);//will use server address later
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
            mSocket=new Socket(serverAddress, port);
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

    public void addDeposited(Deposited account){
        depositedAcc.add(account);
        for(Deposited deposited: depositedAcc){
            System.out.println(deposited.password);
        }

    }

    public void addTransaction(String comment ,long amount){
        try {
            Transaction transaction=new Transaction(amount ,comment);
            mSocket=new Socket(serverAddress, port);
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
    public boolean depositOperation(String password ,String sum){// remember this doesnt change server data center
        try {
            mSocket=new Socket(serverAddress, port);
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println("Deposit Operation");
            writer.println(password);
            writer.println(sum);
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(toServerStream));
            out.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;//fix this later

    }
    public Account update(){
        Account input = null;
        try {
            mSocket=new Socket(serverAddress, port);
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println("Update Info");
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(mSocket.getInputStream()));
            input= (Account) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return input;

    }
}
