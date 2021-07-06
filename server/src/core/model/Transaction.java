package core.model;

import java.util.Arrays;

public class Transaction extends Model<Transaction> {
    public Transaction(){
        columns= Arrays.asList("accountNumber","comment","amount","type","date");

    }
}
