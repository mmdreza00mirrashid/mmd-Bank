package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class AppMenu {
    Socket mSocket;
    int port=0505;
    String serverAddress = "185.51.200.2";//for example
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;

    public AppMenu(Stage stage) {

        BorderPane layout=new BorderPane();
        Menu accMenu=new Menu("حساب");
        MenuItem addAccount=new MenuItem("افزودن حساب");
        MenuItem manageAccount=new MenuItem("مدیریت حساب ها");
        MenuItem logOut=new MenuItem("حروج از حساب");

        accMenu.getItems().addAll(addAccount ,manageAccount ,logOut);
        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().add(accMenu);
        layout.setTop(menuBar);
        Scene scene=new Scene(layout ,600 ,600);
        stage.setScene(scene);
        stage.show();

    }
    public void AddAccount(PasswordField passwordField , ){
        try {
            mSocket=new Socket("localhost", port);
            fromServerStream = mSocket.getInputStream();
            toServerStream = mSocket.getOutputStream();
            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);
            writer.println("Add Account");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
