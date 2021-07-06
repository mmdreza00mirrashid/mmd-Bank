package core.network;

import core.model.Account;
import core.model.Model;

import java.util.Arrays;

public class CommonlyUsedAccount extends Model<CommonlyUsedAccount> {
    public CommonlyUsedAccount(){
        columns= Arrays.asList("userId","accountNumber","alias");

    }
}
