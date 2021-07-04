package sample;

import sample.network.Network;
import sample.network.Transferable;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Person implements Transferable {
    String name ,user ,pass ,phone ,mail;
    @Override
    public void send() {
        try {
            Network.send("register|" + name + "|" + mail + "|" + phone + "|" + user + "|" + pass + "|");
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
    public ArrayList<String> getAccounts(){
        ArrayList<String> output = new ArrayList<>();
        try {
            Network.send("get Accounts|"+user);
            int n=Integer.parseInt(Network.receive());
            System.out.println(n);
            if(n==0)
                output.add("حسابی برای این اکانت یافت نشد");
            for (int i=0;i<n;i++){
                System.out.println("1");
                String account=Network.receive();
                System.out.println("2");
                System.out.println(account);
                output.add(account);
                System.out.println(output.get(0));
            }
            System.out.println(output.size());
            for (String str:output)
                System.out.println(str);
            return output;
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }finally {
            return output;
        }
    }
}
