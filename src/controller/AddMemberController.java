package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddMemberController {
    public JFXButton btnAdd;
    public Label lblMemberID;
    public JFXTextField txtMemberName;
    public JFXTextField txtNIC;
    public JFXTextField txtContact;
    public JFXButton btnSave;
    public JFXTextArea txtAddress;

    public void initialize(){
        txtMemberName.setEditable(false);
        txtNIC.setEditable(false);
        txtAddress.setEditable(false);
        txtContact.setEditable(false);
        lblMemberID.setText("M_ID");
        btnSave.setDisable(true);
        btnAdd.requestFocus();
    }


    public void btnAdd_OnAction(ActionEvent actionEvent) {
        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT member_id FROM Member ORDER BY member_id DESC LIMIT 1;");

            if (!resultSet.next()) {
                lblMemberID.setText("M001");
            } else{

                String lastBookID = resultSet.getString(1);
                String substring = lastBookID.substring(1, 4);
                int id = Integer.parseInt(substring) + 1;
                if (id < 10) {
                    lblMemberID.setText("M00" + id);
                } else if (id < 100) {
                    lblMemberID.setText("M0" + id);
                } else {
                    lblMemberID.setText("M" + id);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtMemberName.setEditable(true);
        txtNIC.setEditable(true);
        txtAddress.setEditable(true);
        txtContact.setEditable(true);
        btnSave.setDisable(false);
        txtMemberName.requestFocus();


    }

    public void btnSave_OnAction(ActionEvent actionEvent) {

        if(txtMemberName.getText().trim().isEmpty() || !txtMemberName.getText().matches("^[A-Za-z. ]+$")){
            txtMemberName.getStyleClass().add("wrong-credential");
            txtMemberName.selectAll();
            txtMemberName.requestFocus();
            return;
        }
        if(txtNIC.getText().trim().isEmpty() || !txtNIC.getText().matches("^[0-9]{9}[V|v]+$|^[0-9]{12}$")){
            txtNIC.getStyleClass().add("wrong-credential");
            txtNIC.selectAll();
            txtNIC.requestFocus();
            return;
        }

        if(txtAddress.getText().trim().isEmpty() || !txtAddress.getText().matches("^[A-Za-z0-9/,-. ]+$")){
            txtAddress.getStyleClass().add("wrong-credential");
            txtAddress.selectAll();
            txtAddress.requestFocus();
            return;
        }

     if(txtContact.getText().trim().isEmpty() || !txtContact.getText().matches("^[0-9]{3}[-]+[0-9]{7}$")){
         txtContact.getStyleClass().add("wrong-credential");
         txtContact.selectAll();
         txtContact.requestFocus();
         return;
     }
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO Member VALUES (?,?,?,?,?)");
            pstm.setObject(1, lblMemberID.getText());
            pstm.setObject(2, txtNIC.getText());
            pstm.setObject(3, txtMemberName.getText());
            pstm.setObject(4, txtAddress.getText());
            pstm.setObject(5, txtContact.getText());

            int affectedRows = pstm.executeUpdate();
            if(affectedRows>0){
                new Alert(Alert.AlertType.INFORMATION,"Member added successfully", ButtonType.OK).showAndWait();

                txtMemberName.clear();
                txtNIC.clear();
                txtAddress.clear();
                txtContact.clear();
                txtMemberName.setEditable(false);
                txtNIC.setEditable(false);
                txtAddress.setEditable(false);
                txtContact.setEditable(false);
                lblMemberID.setText("M_ID");
                btnSave.setDisable(true);
                RemoveCredential();
                btnAdd.requestFocus();

            }else{
                new Alert(Alert.AlertType.ERROR,"Please try again!", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void RemoveCredential(){
        txtMemberName.getStyleClass().remove("wrong-credential");
        txtNIC.getStyleClass().remove("wrong-credential");
        txtAddress.getStyleClass().remove("wrong-credential");
        txtContact.getStyleClass().remove("wrong-credential");
    }
    }

