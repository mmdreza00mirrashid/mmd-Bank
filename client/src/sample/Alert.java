package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import java.util.concurrent.atomic.AtomicBoolean;

public class Alert {
    private static boolean result;

    public static void invalidInfo(){
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("خطا");
        stage.setMinWidth(300);
        Label error=new Label("اطلاعات خود را بررسی و دوباره اقدام نمایید!");
        Button close=new Button("تایید");
        close.setOnAction(e -> stage.close());

        VBox layout=new VBox(15);
        layout.getChildren().addAll(error ,close);
        layout.setAlignment(Pos.CENTER);
        Scene scene=new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();

    }
    public static boolean confirmation(String message){

        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(300);
        Label txt=new Label(message);
        Button yes=new Button("بله");
        yes.setOnAction(e->{
            result=true;
            stage.close();
        });
        Button no=new Button("خیر");
        no.setOnAction(e ->{
            result=false;
            stage.close();
        });
        GridPane layout=new GridPane();
        layout.add(txt ,1 ,0);
        layout.add(yes ,0 ,1);
        layout.add(no ,2, 1);
        layout.setAlignment(Pos.CENTER);
        Scene scene=new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
        return result;
    }
}
