package core.model;

import java.util.Arrays;

public class Loan extends Model<Loan>{
    public Loan(){
        columns= Arrays.asList("accountNumber","monthlyAmount","installment","status");
        uniqueColumns=Arrays.asList("accountNumber");

    }
}
