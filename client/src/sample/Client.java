package sample;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    Account userAccount;
    ArrayList<Deposited> commonlyUsed=new ArrayList<Deposited>();
    Socket mSocket;
    int port=0505;
    String serverAddress = "localhost";//for example
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;
    ObjectInputStream in;
    ObjectOutputStream out;
    public Client(){
        try {
            mSocket=new Socket(serverAddress, port);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            out= new ObjectOutputStream(toServerStream);
            in = new ObjectInputStream(fromServerStream);

        } catch (UnknownHostException unknownHostException) {
            unknownHostException.printStackTrace();
            Alert.message("در ارتباط با سرور مشکلی پیش امده است");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Alert.message("در ارتباط با سرور مشکلی پیش امده است");
        }
    }
    public void signIn(Account account){
        try{
            writer.println("Sign In");
            out.writeObject(account);
            out.flush();
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
    public boolean logIn(String user ,String pass){
        try{
            writer.println("Log In");
            writer.println(user);
            writer.println(pass);
            if(reader.readBoolean()){
                this.userAccount=(Account) in.readObject();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return false;
        }
    }
    public void addDeposited(Deposited deposited){

        try {
            writer.println("Add Deposited");
            out.writeObject(deposited);
            out.flush();
            userAccount= (Account) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void deposit(String pass ,String sum){
        try {
            writer.println("Deposit");
            writer.println(pass);
            writer.println(sum);
            userAccount= (Account) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean withdraw(String pass ,String sum){
        try {
            writer.println("Withdraw");
            writer.println(pass);
            writer.println(sum);
            if(reader.readBoolean()) {
                userAccount = (Account) in.readObject();
                return true;
            }
            else
                return false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            return false;
        }
    }
    public void addCommonAccount(Deposited deposited){
        try {
            writer.println("Add CommonlyUsed Account");
            out.writeObject(deposited);
            out.flush();
            userAccount= (Account) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
