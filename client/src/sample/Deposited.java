package sample;


import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Deposited {
    String accountNumber;
    AccType type;
    String password;
    private long balance;

    public Deposited(String pass ,AccType accType){
        password=pass;
        type=accType;
        accountNumber=numGenerator();
        balance=0;
    }

    public void transfer(){
        Socket mSocket;
        int port=0505;
        String serverAddress = "185.51.200.2";//for example
        OutputStream toServerStream;
        PrintWriter writer;
        try {
            mSocket=new Socket("localhost", port);
            toServerStream = mSocket.getOutputStream();
            writer = new PrintWriter(toServerStream, true);
            writer.println("Add Deposited Account");
            writer.println(accountNumber);
            writer.println(password);
            writer.println(type.name());
            writer.println(""+balance);

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