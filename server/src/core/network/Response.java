package core.network;

import core.model.*;

import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static core.network.Network.*;

public class Response implements Runnable {
    private Socket client;
    private Person p;
    public Response(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String data;
        try {
            while (true) {
                data = receive(client);
                var splitData = data.split("\\|");
                switch (splitData[0]) {
                    case "register" -> {
                        Person p = new Person();
                        p.addData("name", splitData[1]);
                        p.addData("email", splitData[2]);
                        p.addData("phone", splitData[3]);
                        p.addData("melliCode", splitData[4]);
                        p.addData("password", splitData[5]);
                        try {
                            p.save();
                        } catch (Model.UniqueDataException e) {
                            System.out.println(1);
                        }
                    }
                    case "login"->{

                        var p=new Person().where("melliCode",splitData[1]);
                        if(p.size() >0){
                            if(p.get(0).get("password").equals(splitData[2])) {
                                send("true", client);
                                this.p = p.get(0);
                            }
                            else
                                send("wrongPassword",client);
                        }
                        else{
                            send("userNotFound",client);
                        }
                    }
                    case "createAccount"->{
                        Account account = new Account();
                        account.addData("accountNumber",splitData[1]);
                        account.addData("type",splitData[2]);
                        account.addData("alias",splitData[3]);
                        account.addData("password",splitData[4]);
                        account.addData("amount",splitData[5]);
                        account.addData("userId",p.get("melliCode"));
                        account.save();

                    }
                    case "transaction"->{
                        Transaction t = new Transaction();
                        var accounts = new Account().where("accountNumber",splitData[1]);
                        if(accounts.size()>0){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.now();
                            t.addData("accountNumber",splitData[1]);
                            t.addData("type",splitData[2]);
                            t.addData("amount",splitData[3]);
                            t.addData("comment",splitData[5]);
                            t.addData("date" ,formatter.format(localDateTime));
                            Account a=accounts.get(0);
                            if(a.get("password").equals(splitData[4])) {
                                if ((Integer.parseInt(splitData[3]) + Integer.parseInt(a.get("amount")))>=0){
                                    t.save();
                                    a.update("amount", Integer.parseInt(splitData[3]) + Integer.parseInt(a.get("amount")));
                                    send("عملیات با موفیقت انجام شد", client);
                                }
                                else
                                    send("موجودی شما کافی نمی باشد",client);

                            }
                            else{
                                send("رمز عبور وارد شده صحیح نمیباشد", client);
                            }
                        }
                        else {
                            send("شماره حساب یافت نشد",client);
                        }
                    }
                    case "balance"->{
                        long sum=0;
                        for(var a:p.accounts()){
                            sum+=Integer.parseInt(a.get("amount"));
                        }
                        send(String.valueOf(sum),client);
                    }
                    case "get account list"->{
                        send(String.valueOf(p.accounts().size()),client);
                        for(var a:p.accounts()){
                            send(a.get("accountNumber")+"|"+a.get("amount")+"|"+a.get("type")+"|"+a.get("alias"),client);
                        }
                    }
                    case  "get transaction"->{
                        var transactions=new Transaction().where("accountNumber" ,splitData[1]);
                        send(String.valueOf(transactions.size()) ,client);
                        for(int i=0;i<transactions.size() ;i++){
                            send("transaction|"+transactions.get(i).get("accountNumber")+"|"+transactions.get(i).get("type")+"|"+transactions.get(i).get("amount")+"|"
                                    +transactions.get(i).get("comment")+"|"+transactions.get(i).get("date") ,client);
                        }
                    }
                    case "add common account"->{
                        CommonlyUsedAccount a=new CommonlyUsedAccount();
                        var accounts=new CommonlyUsedAccount().where("userId" ,p.get("melliCode"));
                        boolean exist=false;
                        var account=new Account().where("accountNumber",splitData[2]);
                        if(account.size()>0){
                            for (CommonlyUsedAccount c:accounts){
                                if(splitData[3].equals(c.get("alias")))
                                    exist=true;
                            }
                            if(exist)
                                send("حسابی با این نام مستعار موجود است",client);
                            else {
                                a.addData("accountNumber" ,splitData[2]);
                                a.addData("userId" ,splitData[1]);
                                a.addData("alias" ,splitData[3]);
                                a.save();
                                send("حساب با موفقیت افزوده شد",client);
                            }
                        }
                        else
                            send("شماره حساب وارد شده در سامانه وجود ندارد",client);


                    }
                    case "get common list"->{
                        var accounts=new CommonlyUsedAccount().where("userId" ,p.get("melliCode"));
                        send(String.valueOf(accounts.size()) ,client);
                        for(int i=0;i<accounts.size();i++){
                            send(accounts.get(i).get("alias") ,client);

                        }
                    }
                    case "get common accountNumber"->{
                        var accounts=new CommonlyUsedAccount().where("userId" ,p.get("melliCode"));
                        for (CommonlyUsedAccount c:accounts){
                            if(splitData[1].equals(c.get("alias")))
                                send(c.get("accountNumber"),client);
                        }
                    }
                    case "get Accounts"->{
                        var account=new Account().where("userId" ,splitData[1]);
                        send(String.valueOf(account.size()) ,client);
                        for(int i=0;i<account.size();i++){
                            send(account.get(i).get("accountNumber") ,client);
                        }
                    }
                    case "removeAccount"->{
                        var account=new Account().where("accountNumber" ,splitData[1]);
                        if(account.size()>0){
                            Account a=account.get(0);
                            if(a.get("password").equals(splitData[2])){
                                send(a.get("amount") ,client);
                                a.update("userId" ,"none");
                                send("حذف خساب با موفیقت انجام شد", client);
                            }
                            else
                                send("رمز عبور وارد شده صحیح نمیباشد", client);
                        }
                        else {
                            send("شماره حساب موردنظر یافت نشد",client);
                        }
                    }
                    case "loan"->{
                        Loan l=new Loan();
                        long amount=(1000000*Integer.parseInt(splitData[2])*
                                (100+Integer.parseInt(splitData[4]))/100)/Integer.parseInt(splitData[3]);
                        var loans=new Loan().where("accountNumber",splitData[1]);
                        if(loans.size()==0){
                            l.addData("accountNumber",splitData[1]);
                            l.addData("monthlyAmount" ,String.valueOf(amount));
                            l.addData("installment" ,splitData[3]);
                            l.addData("status" ,true);
                            l.save();
                            var accounts=new Account().where("accountNumber",splitData[1]);
                            Account a=accounts.get(0);
                            a.update("amount",String.valueOf(Integer.parseInt(a.get("amount"))+(1000000*Integer.parseInt(splitData[2]))));
                            Transaction t=new Transaction();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.now();
                            t.addData("accountNumber",splitData[1]);
                            t.addData("type","DEPOSIT");
                            t.addData("amount",String.valueOf(1000000*Integer.parseInt(splitData[2])));
                            t.addData("comment","واریز وام");
                            t.addData("date" ,formatter.format(localDateTime));
                            t.save();
                            send("وام به حساب شما واریز شد",client);
                        }
                        else {
                            Loan loan=loans.get(0);
                            if(loan.get("status").equals("true"))
                                send("اقساط این حساب هنو به پایان نرسیده اند",client);
                            else {
                                loan.update("monthlyAmount" ,String.valueOf(amount));
                                loan.update("installment" ,splitData[3]);
                                loan.update("status" ,true);
                                var accounts=new Account().where("accountNumber",splitData[1]);
                                Account a=accounts.get(0);
                                a.update("amount",String.valueOf(Integer.parseInt(a.get("amount"))+(1000000*Integer.parseInt(splitData[2]))));
                                send("وام جدیدی به حساب شما واریز شد",client);
                            }
                        }

                    }
                    case "transfer transaction"->{
                        Transaction t = new Transaction();
                        Transaction dest=new Transaction();
                        var accounts = new Account().where("accountNumber",splitData[1]);
                        if(accounts.size()>0){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.now();
                            t.addData("accountNumber",splitData[1]);
                            t.addData("type","TRANSFER");
                            t.addData("amount","-"+splitData[3]);
                            t.addData("comment",splitData[5]);
                            t.addData("date" ,formatter.format(localDateTime));
                            Account a=accounts.get(0);
                            if(a.get("password").equals(splitData[4])) {
                                var destination=new Account().where("accountNumber",splitData[2]);
                                if(destination.size()>0){
                                    dest.addData("accountNumber",splitData[2]);
                                    dest.addData("type","TRANSFER");
                                    dest.addData("amount",splitData[3]);
                                    dest.addData("comment",splitData[5]);
                                    dest.addData("date" ,formatter.format(localDateTime));
                                    dest.save();
                                    t.save();
                                    Account d=destination.get(0);
                                    a.update("amount",Integer.parseInt(a.get("amount"))-Integer.parseInt(splitData[3]));
                                    d.update("amount",Integer.parseInt(d.get("amount"))+Integer.parseInt(splitData[3]));
                                    send("انتقال با موفیقت انجام شد", client);
                                }
                                else
                                    send("شماره حساب مقصد صحیح نمی باشد", client);

                            }
                            else{
                                send("رمز عبور وارد شده صحیح نمیباشد", client);
                            }
                        }
                        else {
                            send("شماره حساب یافت نشد",client);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
