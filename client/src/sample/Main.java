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
    Stage window;
    Account account;
    public static void main(String[] args){
        launch(args);
    }

    public void SignIn(boolean error){
        //Account acc=new Account();
        boolean data;
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblName=new Label("نام");
        grid.add(lblName,0 ,1);
        TextField txtName=new TextField();
        txtName.setPromptText("name");
        grid.add(txtName, 1, 1);
        Label lblUser=new Label("کد ملی");
        grid.add(lblUser,0 ,2);
        TextField txtUser=new TextField();
        txtUser.setPromptText("ID");
        grid.add(txtUser, 1, 2);
        Label lblPass=new Label("رمز عبور");
        grid.add(lblPass,0 ,3);
        PasswordField txtPass=new PasswordField();
        txtPass.setPromptText("password");
        grid.add(txtPass, 1, 3);
        Label lblPhone=new Label("تلفن");
        grid.add(lblPhone,0 ,4);
        TextField txtPhone=new TextField();
        txtPhone.setPromptText("Phone");
        grid.add(txtPhone, 1, 4);
        Label lblMail=new Label("پست الکترونیکی");
        grid.add(lblMail,0 ,5);
        TextField txtMail=new TextField();
        txtMail.setPromptText("Mail");
        grid.add(txtMail, 1, 5);
        Button Sign=new Button("ثبت نام");
        grid.add(Sign ,2 ,6);
        if(error){
            Alert.invalidInfo();
            System.out.println(account.name);
            txtMail.setText(account.mail);
            txtName.setText(account.name);
            txtPhone.setText(account.phone);
            txtUser.setText(account.user);

        }
        Sign.setOnAction(e->{
            account=new Account();
            account.name=txtName.getText();
            account.user=txtUser.getText();
            account.pass=txtPass.getText();
            account.phone=txtPhone.getText();
            account.mail=txtMail.getText();
            if(!(txtName.getText().isEmpty() || txtUser.getText().isEmpty() || txtPass.getText().isEmpty()
                    || txtPhone.getText().isEmpty() || txtMail.getText().isEmpty())){

                if(NumericCheck(txtUser.getText()) && emailCheck(txtMail.getText()) && NumericCheck(txtPhone.getText())
                        &&txtUser.getText().length()==10){ //because id has 10 numbers
                    //account.transfer
                    login();
                }
                else
                    SignIn(true);

            }
            else {
                SignIn(true);
                }
        });
        Scene scene=new Scene(grid ,600 ,600);
        window.setScene(scene);
        window.show();

    }
    public void login(){
        String User="Admin";
        String Pass="Admin";
        window.setTitle("log in");
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Text welcome= new Text("برای ادامه وارد حساب کاربری خود شوید");
        welcome.setFont(Font.font("Tahoma", FontWeight.LIGHT ,20));
        grid.add(welcome,1 ,0);

        Label lblUser=new Label("نام کاربری");
        grid.add(lblUser,0 ,2);
        TextField txtUser=new TextField();
        txtUser.setPromptText("username");
        grid.add(txtUser, 1, 2);

        Label lblPass=new Label("رمز عبور");
        grid.add(lblPass,0 ,3);
        PasswordField txtPass=new PasswordField();
        txtPass.setPromptText("password");
        grid.add(txtPass, 1, 3);

        Button loginButton=new Button("ورود");
        grid.add(loginButton ,1 ,4);
        loginButton.setOnAction(e->{
            if(User.equals(txtPass.getText()) && Pass.equals(txtPass.getText())) {
                System.out.println("welcome");
                AppMenu menu=new AppMenu(window);
            }

        });
        Button Sign=new Button("ثبت نام");
        grid.add(Sign ,0 ,4);
        Sign.setOnAction(e->{
            SignIn(false);
        });

        Scene scene=new Scene(grid ,600 ,600);
        window.setScene(scene);
        window.show();
    }

    @Override
    public void start(Stage PrimaryStage) throws Exception {
        window=PrimaryStage;
        login();

    }
    public boolean NumericCheck(String strNum){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(strNum).matches();
    }
    public boolean emailCheck(String mail){
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        return pattern.matcher(mail).matches();
    }
}