package sample;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }



    @Override
    public void start(Stage PrimaryStage) throws Exception {
        PrimaryStage.setTitle("MMD Bank");
        AppMenu appMenu=new AppMenu(PrimaryStage);

    }

}