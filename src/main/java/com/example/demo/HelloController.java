package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class HelloController {

    @FXML
    private PasswordField password;

    @FXML
    private Button signinBtn;

    @FXML
    private AnchorPane signinForm;

    @FXML
    private TextField username;


    @FXML
    private PasswordField re_password;

    @FXML
    private PasswordField re_replypassword;

    @FXML
    private TextField re_username;

    @FXML
    private Button signupBtn;

    @FXML
    private AnchorPane signupForm;

    @FXML
    private AnchorPane welcomeForm;

    @FXML
    private Label welcomeuser;
    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    public void reBtn() throws Exception{

        if (re_username.getText().isEmpty() || re_password.getText().isEmpty() || re_replypassword.getText().isEmpty() ) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields!");
            alert.showAndWait();

        }
        else {
            String reData = "INSERT INTO danhsachtaikhoan(username, password)"
                    + "VALUES(?,?)";
            connect = JDBCUtil.getConnection();


            String checkUsername = "SELECT * FROM danhsachtaikhoan WHERE username = '" + re_username.getText() + "'";

            prepare = connect.prepareStatement(checkUsername);
            result = prepare.executeQuery();



            if (result.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText(re_username.getText()+ " is already taken");
                alert.showAndWait();
            }


            else {
                if(checkPassword(re_replypassword))
                {
                    prepare = connect.prepareStatement(reData);


                    prepare.setString(1, re_username.getText());


                    prepare.setString(2, encodePassword(re_password.getText()));


                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered Account!");
                    alert.showAndWait();


                    switchForm(signupForm, getScene("signin.fxml" ),signupBtn);
                }

                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("The password is not SAME!");
                    alert.showAndWait();
                }

            }

        }
    }

    public Scene getScene(String fileFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        return scene;
    }
    public void switchForm(AnchorPane signForm, Scene scene, Button btn) throws IOException {

// Lấy Stage hiện tại
        Stage currentStage = (Stage) btn.getScene().getWindow();
        currentStage.setScene(scene);
        currentStage.show();
    }

    public boolean checkPassword(PasswordField re_replypassword){
        return re_password.getText().equals(re_replypassword.getText());
    }

    public void loginBtn() throws Exception{



        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields!");
            alert.showAndWait();
        }
        else {
            String selectData = "SELECT username, password FROM danhsachtaikhoan WHERE username = ? and password = ?";

            connect = JDBCUtil.getConnection();


            prepare = connect.prepareStatement(selectData);
            prepare.setString(1, username.getText());
            prepare.setString(2, encodePassword(password.getText()));

            result = prepare.executeQuery();

            // Nếu đăng nhập thành công sẽ chuyển sang giao diện chính của chương trình
            if (result.next()) {

                switchForm(signinForm, getScene("welcome.fxml"), signinBtn);


            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password!");
                alert.showAndWait();
            }
        }
    }

    public static String encodePassword(String password)
    {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodeHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for(byte b : encodeHash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();

        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}