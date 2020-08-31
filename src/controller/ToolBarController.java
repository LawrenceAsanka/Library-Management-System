package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolBarController {
    public VBox root;


    public void btnAddMember_OnAction(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/addMember.fxml"));
            Scene mainScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnAddBook_OnAction(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/addBook2.fxml"));
            Scene mainScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnBookDetails_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/bookDetails.fxml"));
            Scene mainScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnMemberDetail_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/memberDetails.fxml"));
            Scene mainScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLogout_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/login2.fxml"));
            Scene mainScene = new Scene(root);
            Stage stage = (Stage)(this.root.getScene().getWindow());
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
