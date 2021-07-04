package core.network;

import core.model.Account;
import core.model.Model;
import core.model.Person;
import core.model.Transaction;

import java.net.Socket;

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
                            t.addData("accountNumber",splitData[1]);
                            t.addData("type",splitData[2]);
                            t.addData("amount",splitData[3]);
                            t.addData("comment",splitData[5]);
                            Account a=accounts.get(0);
                            if(a.get("password").equals(splitData[4])) {
                                t.save();
                                a.update("amount", Integer.parseInt(splitData[3]) + Integer.parseInt(a.get("amount")));
                                send("true", client);
                            }
                            else{
                                send("wrongPassword", client);
                            }
                        }
                        else {
                            send("accountNotFound",client);
                        }
                    }
                    case "balance"->{
                        long sum=0;
                        for(var a:p.accounts()){
                            sum+=Integer.parseInt(a.get("amount"));
                        }
                        send(String.valueOf(sum),client);
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
