package sample;

public class Deposit extends Transaction{

    public Deposit(String accountNumber,String password, String amount, String comment) {
        super(accountNumber, password,amount, comment,TransactionType.DEPOSIT);
    }

}
