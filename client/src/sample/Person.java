package sample;

import sample.network.Network;
import sample.network.Transferable;

import java.awt.image.AreaAveragingScaleFilter;
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
            for (int i=0;i<n;i++){
                String account=Network.receive();
                output.add(account);
            }
            return output;
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }finally {
            return output;
        }
    }

    public void addCommon(String accountNum ,String alias){
        try {
            Network.send("add common account|" + user + "|" + accountNum + "|" + alias);
            Alert.message(Network.receive());
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
    public ArrayList<String> getCommonList(){
        ArrayList<String> list=new ArrayList<>();
        list.add("None");
        try {
            Network.send("get common list|");
            int n=Integer.parseInt(Network.receive());
            System.out.println("n:"+n);
            for(int i=0;i<n;i++){
                list.add(Network.receive());
            }
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }finally {
            return list;
        }
    }
    public String commonNum(String alias){
        String output = "";
        try {
            Network.send("get common accountNumber|"+alias);
            output=Network.receive();
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }finally {
            return output;
        }
    }
}
