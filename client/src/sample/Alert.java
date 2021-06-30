package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.Button;

public class Alert {
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
}
