package sample;

import java.util.Date;

public class Transaction {
    String comment;
    long amount;
    Date date;

    public Transaction(long amount, String comment) {
        this.amount = amount;
        this.comment = comment;
        this.date = new Date();
    }
}
