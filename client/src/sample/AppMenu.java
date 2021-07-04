package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.network.Network;

import java.awt.datatransfer.Transferable;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class AppMenu {
    Person account = new Person();
    Stage window;

    public AppMenu(Stage primaryStage) {
        window = primaryStage;
        login();
    }

    public MenuBar topMenu() {
        Menu home = new Menu("خانه");
        home.setOnAction(e -> show());
        Menu accMenu = new Menu("حساب");
        MenuItem addAccount = new MenuItem("افزودن حساب");
        addAccount.setOnAction(e -> AddAccount());
        MenuItem manageAccount = new MenuItem("مدیریت حساب ها");
        MenuItem common=new MenuItem("افزودن حساب پرکاربرد");
        MenuItem logOut=new MenuItem("حروج از حساب");
        logOut.setOnAction(e-> {
            if(Alert.confirmation("از خروج از حساب خود اطمینان دارید؟"))
                login();
        });
        accMenu.getItems().addAll(addAccount ,manageAccount , new SeparatorMenuItem() ,common ,new SeparatorMenuItem(),logOut);
        String color = "#37FF33";
        //accMenu.setStyle("-fx-background-color: " + color + ";");

        Menu activities = new Menu("عملیات های بانکی");
        MenuItem deposit = new MenuItem("واریز");
        deposit.setOnAction(e -> deposit());
        MenuItem withdraw = new MenuItem("برداشت");
        withdraw.setOnAction(e->withdraw());
        MenuItem transfer = new MenuItem("کارت به کارت");
        transfer.setOnAction(e->MoneyTransfer());
        MenuItem bill=new MenuItem("پرداخت قبض");
        bill.setOnAction(e ->payBill());
        MenuItem loan=new MenuItem("درخواست وام");
        activities.getItems().addAll(deposit ,withdraw ,transfer ,bill ,new SeparatorMenuItem() ,loan);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(home, accMenu, activities);
        return menuBar;
    }

    public void show() {
        window.setTitle("Home page");
        BorderPane layout = new BorderPane();
        layout.setTop(topMenu());
        VBox vBox = new VBox();
        Label lblBalance = new Label();
        lblBalance.setText("موجودی کل:" + Utility.getBalance() + "");
        lblBalance.setFont(Font.font("Tahoma", FontWeight.LIGHT, 50));
        vBox.getChildren().add(lblBalance);
        vBox.setAlignment(Pos.TOP_CENTER);
        layout.setBottom(vBox);
        layout.setCenter(history());
        Scene scene = new Scene(layout,  650, 600);
        window.setScene(scene);
        window.show();

    }

    public void AddAccount() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        window.setTitle("افتتاح حساب");
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblType = new Label("نوع حساب");
        grid.add(lblType, 2, 2);
        CheckBox saving = new CheckBox("حساب پس انداز");
        grid.add(saving, 1, 2);
        CheckBox primary = new CheckBox("حساب جاری");
        grid.add(primary, 0, 2);
        saving.setOnAction(e -> {
            primary.setSelected(false);

        });
        primary.setOnAction(e -> saving.setSelected(false));
        Label lblPass = new Label("رمز عبور:");
        grid.add(lblPass, 2, 3);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 3);
        TextField txtNum = new TextField();
        txtNum.setEditable(false);
        txtNum.getStyleClass().add("copyable-label");
        txtNum.setMinWidth(200);
        txtNum.setVisible(false);
        grid.add(txtNum, 1, 4);
        Label lblAlias = new Label("نام مستعار: ");
        grid.add(lblAlias, 3, 5);
        TextField alias = new TextField();
        alias.setPromptText("میتوانید خالی بگذارید");
        grid.add(alias, 1, 5);
        Label lblAmount = new Label("موجودی اولیه: ");
        grid.add(lblAmount, 3, 6);
        TextField amount = new TextField();
        grid.add(amount, 1, 6);
        Button done = new Button("اتمام");
        grid.add(done, 3, 7);
        Button back = new Button("بازگشت");
        grid.add(back, 0, 7);
        back.setOnAction(e -> show());

        done.setOnAction(e -> {
            if (saving.isSelected() || primary.isSelected()) {
                if (!passwordField.getText().isEmpty()) {
                    Account acc;
                    String strAllis;
                    if(alias.getText().isEmpty())
                        strAllis="ندارد";
                    else
                        strAllis=alias.getText();
                    if (saving.isSelected())
                        acc = new Account(passwordField.getText(), AccType.SAVING, strAllis,amount.getText());
                    else
                        acc = new Account(passwordField.getText(), AccType.CURRENT, strAllis,amount.getText());
                    txtNum.setText("شماره حساب: " + acc.accountNumber);
                    txtNum.setVisible(true);
                    passwordField.setVisible(false);
                    done.setVisible(false);
                }
            }
        });
        Scene scene=new Scene(grid, 600, 300);
        scene.getStylesheets().add(getClass().getResource("copyable-text.css").toExternalForm());
        window.setScene(scene);

    }

    public void login() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Text welcome = new Text("برای ادامه وارد حساب کاربری خود شوید");
        welcome.setFont(Font.font("Tahoma", FontWeight.LIGHT, 20));
        grid.add(welcome, 1, 0);

        Label lblUser = new Label("نام کاربری");
        grid.add(lblUser, 0, 2);
        TextField txtUser = new TextField();
        txtUser.setPromptText("username");
        grid.add(txtUser, 1, 2);

        Label lblPass = new Label("رمز عبور");
        grid.add(lblPass, 0, 3);
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("password");
        grid.add(txtPass, 1, 3);

        Button loginButton = new Button("ورود");
        grid.add(loginButton, 1, 4);
        loginButton.setOnAction(e -> {
            String result = "";
            try {
                Network.send("login|" + txtUser.getText() + "|" + txtPass.getText());
                result = Network.receive();
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            if (result.equals("true")) {
                account.user=txtUser.getText();
                show();
            }
            else
                Alert.message(result);

        });
        Button Sign = new Button("ثبت نام");
        grid.add(Sign, 0, 4);
        Sign.setOnAction(e -> {
            SignIn(false);
        });

        Scene scene = new Scene(grid, 600, 600);
        window.setScene(scene);
        window.show();
    }

    public void SignIn(boolean error) {
        boolean data;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblName = new Label("نام");
        grid.add(lblName, 0, 1);
        TextField txtName = new TextField();
        txtName.setPromptText("name");
        grid.add(txtName, 1, 1);
        Label lblUser = new Label("کد ملی");
        grid.add(lblUser, 0, 2);
        TextField txtUser = new TextField();
        txtUser.setPromptText("ID");
        grid.add(txtUser, 1, 2);
        Label lblPass = new Label("رمز عبور");
        grid.add(lblPass, 0, 3);
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("password");
        grid.add(txtPass, 1, 3);
        Label lblPhone = new Label("تلفن");
        grid.add(lblPhone, 0, 4);
        TextField txtPhone = new TextField();
        txtPhone.setPromptText("Phone");
        grid.add(txtPhone, 1, 4);
        Label lblMail = new Label("پست الکترونیکی");
        grid.add(lblMail, 0, 5);
        TextField txtMail = new TextField();
        txtMail.setPromptText("Mail");
        grid.add(txtMail, 1, 5);
        Button Sign = new Button("ثبت نام");
        grid.add(Sign, 2, 6);
        Button back = new Button("بازگشت");
        back.setOnAction(e -> login());
        grid.add(back, 0, 6);
        if (error) {
            Alert.invalidInfo();
            System.out.println(account.name);
            txtMail.setText(account.mail);
            txtName.setText(account.name);
            txtPhone.setText(account.phone);
            txtUser.setText(account.user);

        }
        Sign.setOnAction(e -> {
            account = new Person();
            account.name = txtName.getText();
            account.user = txtUser.getText();
            account.pass = txtPass.getText();
            account.phone = txtPhone.getText();
            account.mail = txtMail.getText();
            if (!(txtName.getText().isEmpty() || txtUser.getText().isEmpty() || txtPass.getText().isEmpty()
                    || txtPhone.getText().isEmpty() || txtMail.getText().isEmpty())) {

                if (NumericCheck(txtUser.getText()) && emailCheck(txtMail.getText()) && NumericCheck(txtPhone.getText()) && txtUser.getText().length() == 10) { //because id has 10 numbers
                    account.send();
                    login();
                } else
                    SignIn(true);

            } else {
                SignIn(true);
            }
        });
        Scene scene = new Scene(grid, 600, 600);
        window.setScene(scene);
        window.show();

    }

    public void deposit() {
        BorderPane layout = new BorderPane();
        layout.setTop(topMenu());
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblAccountNum = new Label("شماره حساب خود را وارد کنید");
        grid.add(lblAccountNum, 1, 0);
        TextField accountNum = new TextField();
        grid.add(accountNum, 1, 1);
        Label lblPass = new Label("رمز عبور حساب خود را وارد کنید");
        grid.add(lblPass, 1, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 3);
        Label lblSum = new Label("مبلغ مورد نظر را وارد کنید");
        grid.add(lblSum, 1, 4);
        TextField textField = new TextField();
        grid.add(textField, 1, 5);
        Button done = new Button("واریز");
        grid.add(done, 3, 5);
        done.setOnAction(e -> {
            if (!passwordField.getText().isEmpty() && NumericCheck(textField.getText())) {
                Deposit d= new Deposit(accountNum.getText(),passwordField.getText(),textField.getText(),"test");
                d.send();
                show();
            }
        });
        layout.setCenter(grid);
        window.setScene(new Scene(layout, 600, 600));
        window.show();
    }

    public void payBill(){
        window.setTitle("پرداخت قبض");
        BorderPane layout=new BorderPane();
        layout.setTop(topMenu());
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblID=new Label("شناسه قیض خود را وارد کنید");
        grid.add(lblID ,1 ,0);
        TextField Id=new TextField();
        grid.add(Id ,1,1);
        Label lblIdPay=new Label("شناسه پرداخت را وارد کنید");
        grid.add(lblIdPay ,1,2);
        TextField IdPay=new TextField();
        grid.add(IdPay ,1,3);
        Button check=new Button("بررسی قبض");
        grid.add(check ,4, 4);
        int amount= 1000*((int) (((Math.random()*100)+1)));
        Label sum=new Label("مبلغ قبض: "+amount+" تومان ");
        grid.add(sum ,1 ,5);
        Label lblAccountNum = new Label("شماره حساب خود را وارد کنید");
        grid.add(lblAccountNum, 1, 6);
        TextField accountNum = new TextField();
        grid.add(accountNum, 1, 7);
        Label lblPass=new Label("رمز حساب خود را وارد کنید");
        grid.add(lblPass ,1 ,8);
        lblPass.setVisible(false);
        PasswordField passwordField=new PasswordField();
        grid.add(passwordField ,1 ,9);
        passwordField.setVisible(false);
        Button done =new Button("پرداخت");
        grid.add(done ,4 ,10);
        done.setVisible(false);
        sum.setVisible(false);
        check.setOnAction(e ->{
            check.setVisible(false);
            sum.setVisible(true);
            lblPass.setVisible(true);
            passwordField.setVisible(true);
            done.setVisible(true);
        });
        done.setOnAction(e ->{
            Withdrawal w= new Withdrawal(accountNum.getText(),passwordField.getText(),String.valueOf(-1*amount),"پرداخت قبض");
            w.send();
            show();
        });
        layout.setCenter(grid);
        window.setScene(new Scene(layout ,600 ,600));
        window.show();
    }

    public void withdraw(){
        window.setTitle("برداشت وجه");
        BorderPane layout=new BorderPane();
        layout.setTop(topMenu());
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblAccountNum = new Label("شماره حساب خود را وارد کنید");
        grid.add(lblAccountNum, 1, 0);
        TextField accountNum = new TextField();
        grid.add(accountNum, 1, 1);
        Label lblPass=new Label("رمز عبور حساب خود را وارد کنید");
        grid.add(lblPass ,1 ,2);
        PasswordField passwordField=new PasswordField();
        grid.add(passwordField ,1,3);
        Label lblSum=new Label("مبلغ مورد نظر را وارد کنید");
        grid.add(lblSum ,1,4);
        TextField textField=new TextField();
        grid.add(textField ,1,5);
        Label lblComment=new Label("توضیحات");
        grid.add(lblComment ,1 ,6);
        TextField comment=new TextField();
        grid.add(comment ,1 ,7);
        Button done=new Button("برداشت");
        grid.add(done ,3, 8);
        done.setOnAction(e->{
            if(!passwordField.getText().isEmpty() &&NumericCheck(textField.getText())){
                Withdrawal w;
                if(comment.getText().isEmpty()){
                    w=new Withdrawal(accountNum.getText() ,passwordField.getText() ,"-"+textField.getText() ," ");
                }
                else
                    w=new Withdrawal(accountNum.getText() ,passwordField.getText() ,"-"+textField.getText() ,comment.getText());
                w.send();
                show();
            }
        });
        layout.setCenter(grid);
        window.setScene(new Scene(layout ,600 ,600));
        window.show();
    }

    public void MoneyTransfer(){
        window.setTitle("انتقال وجه");
        BorderPane layout=new BorderPane();
        layout.setTop(topMenu());
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 10, 5, 10));
        Label lblAccountNum = new Label("شماره حساب خود را وارد کنید");
        grid.add(lblAccountNum, 1, 0);
        TextField accountNum = new TextField();
        grid.add(accountNum, 1, 1);
        Label lblPass=new Label("رمز عبور حساب خود را وارد کنید");
        grid.add(lblPass ,1 ,2);
        PasswordField passwordField=new PasswordField();
        grid.add(passwordField ,1,3);
        Label lblDest=new Label("شماره حساب مقصد را وارد کنید");
        grid.add(lblDest,1,4);
        TextField destAccount=new TextField();
        grid.add(destAccount ,1,5);
        Label lblSum=new Label("مبلغ مورد نظر را وارد کنید");
        grid.add(lblSum ,1,6);
        TextField textField=new TextField();
        grid.add(textField ,1,7);
        Button done=new Button("انتقال وجه");
        grid.add(done ,3, 8);
        done.setOnAction(e->{
            if(!passwordField.getText().isEmpty() && !destAccount.getText().isEmpty() &&
                    NumericCheck(textField.getText()) && NumericCheck(destAccount.getText())){
                TransferMoney t=new TransferMoney("test" ,accountNum.getText() ,destAccount.getText() ,
                        textField.getText() ,passwordField.getText());
                t.send();
                show();
            }
        });
        layout.setCenter(grid);
        window.setScene(new Scene(layout ,600 ,600));
        window.show();
    }

    public VBox history(){
        VBox layout=new VBox();
        TableView<Transaction> table=new TableView<>();
        TableColumn<Transaction ,String> numColumn=new TableColumn<>("شماره حساب");
        numColumn.setMinWidth(200);
        numColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        TableColumn<Transaction ,String> commentColumn=new TableColumn<>("توضیحات");
        commentColumn.setMinWidth(100);
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        TableColumn<Transaction ,String> sumColumn=new TableColumn<>("مبلغ");
        sumColumn.setMinWidth(100);
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction , Date> dateColumn=new TableColumn<>("تاریخ");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction ,String> typeColumn=new TableColumn<>("نوع تراکنش");
        typeColumn.setMinWidth(150);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ChoiceBox<String> choiceBox=new ChoiceBox<>();
        ArrayList<String> accounts=account.getAccounts();
        for (String str:accounts){
            choiceBox.getItems().add(str);
        }
        choiceBox.setValue(accounts.get(0));
        choiceBox.setMinWidth(200);
        table.setItems(getTransaction(choiceBox.getValue()));
        table.getColumns().addAll(numColumn ,commentColumn ,sumColumn ,dateColumn ,typeColumn);
        Button update=new Button("بروزرسانی");
        update.setOnAction(e->{
            table.setItems(getTransaction(choiceBox.getValue()));
        });
        layout.getChildren().addAll(choiceBox ,table ,update);
        layout.setAlignment(Pos.TOP_CENTER);
        return layout;
    }

    public ObservableList<Transaction> getTransaction(String accountNumber){
        ObservableList<Transaction> transactions= FXCollections.observableArrayList();
        ArrayList<Transaction> history=Transaction.receive(accountNumber);
        for(Transaction t:history){
            transactions.add(t);
        }
        return transactions;
    }

    public boolean NumericCheck(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(strNum).matches();
    }

    public boolean emailCheck(String mail) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        return pattern.matcher(mail).matches();
    }
}
