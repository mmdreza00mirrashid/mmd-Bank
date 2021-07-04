package sample;

public class Withdrawal extends Transaction{
    public Withdrawal(String accountNumber,String password, String amount, String comment) {
        super(accountNumber, password,amount, comment,TransactionType.WITHDRAWAL);
    }

}
