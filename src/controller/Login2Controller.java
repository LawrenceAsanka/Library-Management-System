package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import dbConnection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Login2Controller {
    public JFXTextField txtLogin;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;
    public AnchorPane root;
    public JFXButton btnCancle;

    public void btnLogin_OnAction(ActionEvent actionEvent) {

        String userName = txtLogin.getText();
        String password = txtPassword.getText();

        if (userName.trim().isEmpty()) {
//            new Alert(Alert.AlertType.ERROR, "Please enter the username!", ButtonType.OK).showAndWait();
            txtLogin.getStyleClass().add("wrong-credential");
            txtLogin.selectAll();
            txtLogin.requestFocus();
            return;
        }
        if (password.trim().isEmpty()) {
//            new Alert(Alert.AlertType.ERROR, "Please enter the password!", ButtonType.OK).showAndWait();
            txtPassword.getStyleClass().add("wrong-credential");
            txtPassword.selectAll();
            txtPassword.requestFocus();
            return;
        }

        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * from User where name=? AND password=? ");
            preparedStatement.setObject(1, userName);
            preparedStatement.setObject(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
//                new Alert(Alert.AlertType.ERROR, "Wrong Username/Password!", ButtonType.OK).showAndWait();
                txtLogin.getStyleClass().add("wrong-credential");
                txtPassword.getStyleClass().add("wrong-credential");
                txtPassword.selectAll();
                txtPassword.requestFocus();
            } else {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    try {
                        Parent root = FXMLLoader.load(this.getClass().getResource("/view/bookIssueForm.fxml"));
                        Scene mainScene = new Scene(root);
                        Stage stage = (Stage) (this.root.getScene().getWindow());
                        stage.setScene(mainScene);
                        stage.centerOnScreen();
                        stage.sizeToScene();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void txtPassword_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/bookIssueForm.fxml"));
            Scene mainScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnCancle_OnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
