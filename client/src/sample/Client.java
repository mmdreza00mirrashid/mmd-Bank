package sample;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    Account userAccount;
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
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
}
