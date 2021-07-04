package sample;

import sample.network.Network;
import sample.network.Transferable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public abstract class Transaction implements Transferable {
    String comment,accountNumber;
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

    @Override
    public void send() {
        try {
            Network.send("transaction|"+accountNumber+"|"+type+"|"+amount+"|"+password+"|"+comment);
            Alert.message(Network.receive());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
