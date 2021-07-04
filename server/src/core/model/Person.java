package core.model;

import java.util.Arrays;
import java.util.List;

public class Person extends Model<Person>{
    public Person(){
        columns= Arrays.asList("name", "melliCode","email","phone","password");
        requiredColumns=Arrays.asList("name");
        uniqueColumns=Arrays.asList("melliCode");
        validator.put("name","[a-zA-Z]+");

    }
    public List<Account> accounts(){ return new hasMany<Account>(new Account(), "melliCode", "userId").get(); }
    public Account account() {
        return new hasOne<Account>(new Account(), "melliCode", "userId").get();
    }
}
