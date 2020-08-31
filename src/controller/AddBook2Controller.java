package controller;

import com.jfoenix.controls.JFXButton;
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

public class AddBook2Controller {
    public JFXButton btnAdd;
    public Label lblBookID;
    public JFXTextField txtBookName;
    public JFXTextField textISBN;
    public JFXTextField TextAuthor;
    public JFXTextField TextPrice;
    public JFXTextField TextAvailability;
    public JFXButton btnSave;


    public void initialize() {
        txtBookName.setEditable(false);
        textISBN.setEditable(false);
        TextAuthor.setEditable(false);
        TextPrice.setEditable(false);
//        TextAvailability.setEditable(false);
        lblBookID.setText("B_ID");
        btnSave.setDisable(false);
        btnAdd.requestFocus();
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
//        TextAvailability.setText("YES");

        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT book_id FROM Book ORDER BY book_id DESC LIMIT 1;");

            if (!resultSet.next()) {
                lblBookID.setText("B001");
            } else {

                String lastBookID = resultSet.getString(1);
                String substring = lastBookID.substring(1, 4);
                int id = Integer.parseInt(substring) + 1;
                if (id < 10) {
                    lblBookID.setText("B00" + id);
                } else if (id < 100) {
                    lblBookID.setText("B0" + id);
                } else {
                    lblBookID.setText("B" + id);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtBookName.setEditable(true);
        textISBN.setEditable(true);
        TextAuthor.setEditable(true);
        TextPrice.setEditable(true);
//        TextAvailability.setEditable(true);
        txtBookName.requestFocus();
        btnSave.setDisable(false);

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {

        if (txtBookName.getText().trim().isEmpty() || !txtBookName.getText().matches("^[A-Za-z. ]+$")) {
            txtBookName.getStyleClass().add("wrong-credential");
        txtBookName.selectAll();
        txtBookName.requestFocus();
            return;
        }

        if(textISBN.getText().trim().isEmpty() || !textISBN.getText().matches("^[0-9]+$")){
            textISBN.getStyleClass().add("wrong-credential");
            textISBN.selectAll();
            textISBN.requestFocus();
            return;
        }

      if(TextAuthor.getText().trim().isEmpty() || !TextAuthor.getText().matches("^[A-Za-z. ]+$")){
          TextAuthor.getStyleClass().add("wrong-credential");
          TextAuthor.selectAll();
          TextAuthor.requestFocus();
          return;
      }

     if(TextPrice.getText().trim().isEmpty() || !TextPrice.getText().matches("^[0-9]{2,4}\\.[0-9]{2}")){
         TextPrice.getStyleClass().add("wrong-credential");
         TextPrice.selectAll();
         TextPrice.requestFocus();
         return;
     }

    /*if(TextAvailability.getText().trim().isEmpty() || !TextAvailability.getText().equals("YES")){
        TextAvailability.getStyleClass().add("wrong-credential");
        new Alert(Alert.AlertType.ERROR, "Enter YES(Use Capital letters)", ButtonType.OK).showAndWait();
        TextAvailability.selectAll();
        TextAvailability.requestFocus();
        return;
    }*/

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO Book (book_id,isbn,title,author,price)VALUES (?,?,?,?,?)");
            pstm.setObject(1, lblBookID.getText());
            pstm.setObject(2, textISBN.getText());
            pstm.setObject(3, txtBookName.getText());
            pstm.setObject(4, TextAuthor.getText());
            pstm.setObject(5, TextPrice.getText());

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Book added successfully", ButtonType.OK).showAndWait();

                textISBN.clear();
                txtBookName.clear();
                TextAuthor.clear();
                TextPrice.clear();
//                TextAvailability.clear();
                txtBookName.setEditable(false);
                textISBN.setEditable(false);
                TextAuthor.setEditable(false);
                TextPrice.setEditable(false);
//                TextAvailability.setEditable(false);
                lblBookID.setText("B_ID");
                Remove();
                btnSave.setDisable(true);

                btnAdd.requestFocus();

            } else {
                new Alert(Alert.AlertType.ERROR, "Please try again!", ButtonType.OK).showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void Remove(){
        txtBookName.getStyleClass().remove("wrong-credential");
        textISBN.getStyleClass().remove("wrong-credential");
        TextAuthor.getStyleClass().remove("wrong-credential");
        TextPrice.getStyleClass().remove("wrong-credential");
    }
}
