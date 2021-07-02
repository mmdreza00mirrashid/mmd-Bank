package sample;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    String comment;
    long amount;
    Date date;

    public Transaction(long amount, String comment) {
        this.amount = amount;
        this.comment = comment;
        this.date = new Date();
    }
}
