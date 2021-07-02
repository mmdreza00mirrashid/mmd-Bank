package sample;

import java.io.*;
import java.util.ArrayList;

public class Account implements Serializable{
    String name ,user ,pass ,phone ,mail;
    long savingBalance=0,primaryBalance=0;
    ArrayList<Transaction> history=new ArrayList<Transaction>();
    ArrayList<Deposited> depositedAcc=new ArrayList<Deposited>();


    public Account(String name, String user, String pass, String phone, String mail) {
        this.name = name;
        this.user = user;
        this.pass = pass;
        this.phone = phone;
        this.mail = mail;
    }



}
