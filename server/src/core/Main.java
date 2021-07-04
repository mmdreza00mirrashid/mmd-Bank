package core;
import core.model.Account;
import core.model.Person;
import core.network.Network;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class Main {
    private static Socket client;
    public static void main(String[] args) throws IOException {
        Thread t= new Thread(new Network());
        t.start();

//        Person persons = new Person();
//        for(Person p: persons.where("phone",2345234)){
//           p.update("email","testsdfsd");
//        }
//        System.out.println(new Account().where("accountNumber","6457523637188052"));

    }

}
