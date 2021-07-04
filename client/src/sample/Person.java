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
}
