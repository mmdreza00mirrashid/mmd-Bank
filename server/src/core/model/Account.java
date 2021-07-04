package core.model;

import java.util.Arrays;

public class Account extends Model<Account> {
    public Account(){
        columns= Arrays.asList("userId", "accountNumber","type","alias","password","amount");
        uniqueColumns=Arrays.asList("accountNumber");

    }
}
