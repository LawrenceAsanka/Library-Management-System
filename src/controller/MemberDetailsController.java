package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.MemberDetailsTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MemberDetailsController {
    public TextField txtSearch;
    public TableView<MemberDetailsTM> tblMemberDetails;
    public JFXTextField txtName;
    public JFXTextField txtNIC;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    ArrayList<MemberDetailsTM> memberDetailsTMArrayList=new ArrayList<>();

    public void initialize(){

        txtNIC.setDisable(true);
        txtName.setDisable(true);
        txtAddress.setDisable(true);
        txtContact.setDisable(true);

        tblMemberDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblMemberDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblMemberDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblMemberDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblMemberDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("contact"));

        loadAllMembers();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblMemberDetails.getItems().clear();
                for (MemberDetailsTM memberDetails : memberDetailsTMArrayList) {
                    if(memberDetails.getId().contains(newValue) ||
                            memberDetails.getName().contains(newValue) ||
                            memberDetails.getNic().contains(newValue) ||
                            memberDetails.getAddress().contains(newValue) ||
                            memberDetails.getContact().contains(newValue)){

                        tblMemberDetails.getItems().add(memberDetails);
                    }
                }
            }
        });

        tblMemberDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MemberDetailsTM>() {
            @Override
            public void changed(ObservableValue<? extends MemberDetailsTM> observable, MemberDetailsTM oldValue, MemberDetailsTM newValue) {
                if(newValue==null){
                    txtSearch.requestFocus();
                    txtNIC.setDisable(true);
                    txtName.setDisable(true);
                    txtAddress.setDisable(true);
                    txtContact.setDisable(true);

                    txtNIC.clear();
                    txtName.clear();
                    txtAddress.clear();
                    txtContact.clear();

                    return;
                }else{
                    txtName.setText(newValue.getName());
                    txtNIC.setText(newValue.getNic());
                    txtAddress.setText(newValue.getAddress());
                    txtContact.setText(newValue.getContact());

                    txtNIC.setDisable(false);
                    txtName.setDisable(false);
                    txtAddress.setDisable(false);
                    txtContact.setDisable(false);
                }

            }
        });
    }


    public void loadAllMembers(){
        ObservableList<MemberDetailsTM> members = tblMemberDetails.getItems();
        ObservableList<MemberDetailsTM> memberDetails = FXCollections.observableList(memberDetailsTMArrayList);


        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Member");
            members.clear();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String nic = resultSet.getString(2);
                String name = resultSet.getString(3);
                String address = resultSet.getString(4);
                String contact = resultSet.getString(5);

                MemberDetailsTM memberDetailsTM = new MemberDetailsTM(id, name, nic, address, contact);
                members.add(memberDetailsTM);
                memberDetails.add(memberDetailsTM);



            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdate_OnAction(ActionEvent actionEvent) {
        MemberDetailsTM selectedItem = tblMemberDetails.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            new Alert(Alert.AlertType.ERROR,"Please select a member to update", ButtonType.OK).showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to update the selected member", ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult()==ButtonType.YES){

            String sql="UPDATE Member SET nic=?,name=?,address=?,contact=? WHERE member_id=?";
            try {
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                preparedStatement.setObject(1,txtNIC.getText());
                preparedStatement.setObject(2,txtName.getText());
                preparedStatement.setObject(3,txtAddress.getText());
                preparedStatement.setObject(4,txtContact.getText());
                preparedStatement.setObject(5,selectedItem.getId());

                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0){
                    new Alert(Alert.AlertType.INFORMATION,"Update Successfully", ButtonType.OK).showAndWait();
                    tblMemberDetails.getSelectionModel().clearSelection();
                    tblMemberDetails.getItems().clear();
                    txtSearch.clear();
                    loadAllMembers();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            tblMemberDetails.getSelectionModel().clearSelection();
            txtSearch.clear();

        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        MemberDetailsTM selectedItem = tblMemberDetails.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            new Alert(Alert.AlertType.ERROR,"Please select a member to delete", ButtonType.OK).showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected member", ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult()==ButtonType.YES){

            try {
                PreparedStatement preparedStatement  = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM Member WHERE member_id=?");
                preparedStatement.setObject(1,selectedItem.getId());

                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0){
                    new Alert(Alert.AlertType.INFORMATION,"Delete Successfully", ButtonType.OK).showAndWait();
                    tblMemberDetails.getSelectionModel().clearSelection();
                    txtSearch.clear();
                    loadAllMembers();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }else{
            tblMemberDetails.getSelectionModel().clearSelection();
            txtSearch.clear();
        }


    }
}
