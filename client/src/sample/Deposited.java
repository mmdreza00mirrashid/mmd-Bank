package sample;


import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Deposited implements Serializable{
    String accountNumber;
    AccType type;
    String password ,alias=null;
    long balance;
    boolean commonlyUsed;

    public Deposited(String pass ,AccType accType ,String alias){
        password=pass;
        type=accType;
        accountNumber=numGenerator();
        balance=0;
        this.alias=alias;
    }

    public void transferData(){
        Socket mSocket;
        int port=0505;
        String serverAddress = "localhost";//for example
        OutputStream toServerStream;
        PrintWriter writer;
        try {
            mSocket=new Socket(serverAddress, port);
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println("Add Deposited Account");
            ObjectOutputStream out= new ObjectOutputStream(new BufferedOutputStream(mSocket.getOutputStream()));
            out.writeObject(this);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String numGenerator(){
        String num="";
        for(int i=0;i<16;i++){
            num+=""+(int) (Math.random()*10);
        }
        return num;
    }
}
enum AccType{
    SAVING ,CURRENT;
}