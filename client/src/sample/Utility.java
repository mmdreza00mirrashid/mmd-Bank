package sample;

import sample.network.Network;

import java.io.IOException;

public class Utility {
    public static String getBalance(){
        try {
            Network.send("balance");
            return Network.receive();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
