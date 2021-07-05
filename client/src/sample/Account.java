package sample;


import javafx.stage.Stage;
import sample.network.Network;
import sample.network.Transferable;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Account implements Transferable {
    String accountNumber;
    AccType type;
    String password ,alias=null,amount;

    public Account(String pass , AccType accType , String alias, String amount){
        password=pass;
        type=accType;
        accountNumber=numGenerator();
        this.alias=alias;
        this.amount=amount;
        send();
    }

    public Account(String accountNumber , String alias, String amount ,AccType accType){
        type=accType;
        this.accountNumber=accountNumber;
        this.alias=alias;
        this.amount=amount;
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
    public static void remove(String accountNumber ,String password ,String dest){
        try {
            Network.send("removeAccount|"+accountNumber+"|"+password);
            String amount=Network.receive();
            TransferMoney t=new TransferMoney("حذف حساب"+accountNumber.substring(12 ,16) ,accountNumber ,dest ,amount ,password);
            t.send();
            Alert.message(Network.receive());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Account> getList(){
        ArrayList<Account> accounts=new ArrayList<>();
        try {
            Network.send("get account list");
            int n=Integer.parseInt(Network.receive());
            for(int i=0;i<n;i++){
                String data=Network.receive();
                var splitData = data.split("\\|");
                accounts.add(new Account(splitData[0] ,splitData[3] ,splitData[1] ,AccType.valueOf(splitData[2])));
            }
            System.out.println(accounts.size());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            return accounts;
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccType getType() {
        return type;
    }

    public void setType(AccType type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
enum AccType{
    SAVING ,CURRENT;
}