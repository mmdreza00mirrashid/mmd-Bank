package sample;

import java.io.*;

public class Deposited implements Serializable{
    String accountNumber;
    AccType type;
    String password ,alias=null;
    long balance;
    boolean commonlyUsed=false;

    public Deposited(String pass ,AccType accType ,String alias){
        password=pass;
        type=accType;
        accountNumber=numGenerator();
        balance=0;
        this.alias=alias;
    }

    public Deposited(String accountNumber, String alias) {
        this.accountNumber = accountNumber;
        this.alias = alias;
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