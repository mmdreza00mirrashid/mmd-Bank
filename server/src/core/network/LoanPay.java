package core.network;

import core.model.Account;
import core.model.Loan;
import core.model.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoanPay implements Runnable{
    @Override
    public void run() {
        var loans=new Loan().where("status" ,"true");
        for (Loan l:loans){
            l.update("installment",String.valueOf(Integer.parseInt(l.get("installment"))-1));
            var accounts=new Account().where("accountNumber",l.get("accountNumber"));
            Account account=accounts.get(0);
            account.update("amount",String.valueOf
                    (Integer.parseInt(account.get("amount"))-Integer.parseInt(l.get("monthlyAmount"))));
            Transaction t=new Transaction();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.now();
            t.addData("accountNumber",l.get("accountNumber"));
            t.addData("type","WITHDRAWAL");
            t.addData("amount",l.get("monthlyAmount"));
            t.addData("comment","پرداخت قسط");
            t.addData("date" ,formatter.format(localDateTime));
            t.save();
        }
        var finished=new Loan().where("installment","0");
        for(Loan l:finished)
            l.update("status","false");
    }
}
