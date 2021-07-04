package sample;

import sample.network.Network;
import sample.network.Transferable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Transaction implements Transferable {
    String comment,accountNumber ,date;
    String amount,password;
    TransactionType type;

    enum TransactionType{
        DEPOSIT ,WITHDRAWAL ,TRANSFER
    }

    public Transaction(String accountNumber,String password,String amount, String comment,TransactionType type) {
        this.amount = amount;
        this.accountNumber=accountNumber;
        this.comment = comment;
        this.password=password;
        this.type = type;
    }

    public Transaction(TransactionType type ,String comment, String accountNumber, String date, String amount) {
        System.out.println("wrong");
        this.comment = comment;
        this.accountNumber = accountNumber;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public void send() {
        try {
            Network.send("transaction|"+accountNumber+"|"+type+"|"+amount+"|"+password+"|"+comment);
            Alert.message(Network.receive());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Transaction> receive(String accountNumber){
        ArrayList<Transaction> transactions = null;
        try {
            transactions=new ArrayList<>();
            Network.send("get transaction|"+accountNumber);
            int n=Integer.parseInt(Network.receive());
            for(int i=0;i<n;i++){
                String data = Network.receive();
                var splitData = data.split("\\|");
                switch (splitData[2]){
                    case "DEPOSIT"->{
                        transactions.add(new Transaction(TransactionType.DEPOSIT ,splitData[4] ,splitData[1] ,
                                splitData[5] ,splitData[3]));
                    }
                    case  "WITHDRAWAL"->{
                        transactions.add(new Transaction(TransactionType.WITHDRAWAL ,splitData[4] ,splitData[1] ,
                                splitData[5] ,splitData[3]));
                    }
                    case "TRANSFER"->{
                        transactions.add(new Transaction(TransactionType.TRANSFER ,splitData[4] ,splitData[1] ,
                                splitData[5] ,splitData[3]));
                    }
                }
                for(Transaction t:transactions){
                    System.out.println(t.date);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            return transactions;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
