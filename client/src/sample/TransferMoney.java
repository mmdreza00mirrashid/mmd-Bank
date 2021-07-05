package sample;

import sample.network.Network;
import sample.network.Transferable;

import java.io.IOException;

public class TransferMoney implements Transferable {
    String comment,accountNumber ,destAccount;
    String amount,password ,date;
    Transaction.TransactionType type;
    @Override
    public void send() {
        try {
            Network.send("transfer transaction|"+accountNumber+"|"+destAccount+"|"+amount+"|"+password+"|"+comment);
            Alert.message(Network.receive());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public TransferMoney(String comment, String accountNumber, String destAccount, String amount, String password) {
        this.comment = comment;
        this.accountNumber = accountNumber;
        this.destAccount = destAccount;
        this.amount = amount;
        this.password = password;
        this.send();
    }
}
