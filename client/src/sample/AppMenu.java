package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class AppMenu {
    Socket mSocket;
    Account account=new Account();
    int port=0505;
    String serverAddress = "185.51.200.2";//for example
    Stage window;
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;
    public  AppMenu(Stage primaryStage){
        window=primaryStage;
        login();
    }
    public void show() {

        BorderPane layout=new BorderPane();
        Menu accMenu=new Menu("حساب");
        MenuItem addAccount=new MenuItem("افزودن حساب");
        addAccount.setOnAction(e ->AddAccount());
        MenuItem manageAccount=new MenuItem("مدیریت حساب ها");
        MenuItem logOut=new MenuItem("حروج از حساب");
        logOut.setOnAction(e-> {
            if(Alert.confirmation("از خروج از حساب خود اطمینان دارید؟"))
                login();
        });

        accMenu.getItems().addAll(addAccount ,manageAccount , new SeparatorMenuItem() ,logOut);
        String color="#37FF33";
        accMenu.setStyle("-fx-background-color: " + color + ";");
        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().add(accMenu);
        layout.setTop(menuBar);
        VBox vBox=new VBox();
        Label lblBalance=new Label();
        lblBalance.setText("موجودی:"+account.primaryBalance+"");
        lblBalance.setFont(Font.font("Tahoma", FontWeight.LIGHT ,50));
        vBox.getChildren().add(lblBalance);
        vBox.setAlignment(Pos.TOP_CENTER);
        layout.setCenter(vBox);
        Scene scene=new Scene(layout ,600 ,600);
        window.setScene(scene);
        window.show();

    }
    public void AddAccount(){
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Text message= new Text("افتتاح حساب جدید");
        grid.add(message ,1 ,1);
        Label lblType=new Label("نوع حساب");
        grid.add(lblType ,2,2);
        CheckBox saving=new CheckBox("حساب پس انداز");
        grid.add(saving ,1,2);
        CheckBox primary=new CheckBox("حساب جاری");
        grid.add(primary,0,2);
        saving.setOnAction(e ->{
            primary.setSelected(false);

        });
        primary.setOnAction(e -> saving.setSelected(false));
        Label lblPass=new Label("رمز عبور:");
        grid.add(lblPass ,2,3);
        PasswordField passwordField=new PasswordField();
        grid.add(passwordField ,1 ,3);
        Label lblNum=new Label("شماره حساب: ");
        lblNum.setFont(Font.font("Tahoma", FontWeight.LIGHT ,20));
        lblNum.setVisible(false);
        grid.add(lblNum ,1,4);
        Button done=new Button("اتمام");
        grid.add(done ,3,5);
        Button back=new Button("بازگشت");
        grid.add(back ,0 ,5);
        back.setOnAction(e ->show());

        done.setOnAction(e -> {
            if(saving.isSelected()||primary.isSelected()){
                if(!passwordField.getText().isEmpty()){
                    Deposited acc;
                    if(saving.isSelected())
                        acc=new Deposited(passwordField.getText() ,AccType.SAVING);
                    else
                        acc=new Deposited(passwordField.getText() ,AccType.CURRENT);
                    acc.transfer();
                    lblNum.setText("شماره حساب: "+acc.accountNumber);
                    lblNum.setVisible(true);
                    passwordField.setVisible(false);
                    done.setVisible(false);
                }
            }
        });
        window.setScene(new Scene(grid ,600 ,300));

    }
    public void login(){
        String User="Admin";
        String Pass="Admin";
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
                show();

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
        Button back=new Button("بازگشت");
        back.setOnAction(e ->login());
        grid.add(back ,0 ,6);
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
    public boolean NumericCheck(String strNum){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(strNum).matches();
    }
    public boolean emailCheck(String mail){
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        return pattern.matcher(mail).matches();
    }
}
