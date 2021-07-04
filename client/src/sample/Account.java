package sample;


import javafx.stage.Stage;
import sample.network.Network;
import sample.network.Transferable;

import java.io.*;
import java.net.Socket;

public class Account implements Transferable {
    String accountNumber;
    AccType type;
    String password ,alias=null,amount;
    long balance;

    public Account(String pass , AccType accType , String alias, String amount){
        password=pass;
        type=accType;
        accountNumber=numGenerator();
        this.alias=alias;
        this.amount=amount;
        send();
    }


    public String numGenerator(){

        String num="";
        for(int i=0;i<16;i++){
            num+=""+(int) (Math.random()*10);
        }
        return num;
    }

    @Override
    public void send() {
        try {
            Network.send("createAccount|"+accountNumber+"|"+type+"|"+alias+"|"+password+"|"+amount);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
enum AccType{
    SAVING ,CURRENT;
}