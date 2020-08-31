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
import util.BookDetailsTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class BookDetailsController {
    public TableView<BookDetailsTM> tblBookDetails;
    public TextField txtSearch;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXTextField txtISBN;
    public JFXTextField txtName;
    public JFXTextField txtAuthor;
    public JFXTextField txtPrice;
    public JFXTextField txtAvailability;
    private ArrayList<BookDetailsTM> bookDetailsTMArrayList = new ArrayList<>();


    public void initialize() {

        txtISBN.setDisable(true);
        txtName.setDisable(true);
        txtAuthor.setDisable(true);
        txtPrice.setDisable(true);
        txtAvailability.setDisable(true);

        tblBookDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblBookDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblBookDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblBookDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblBookDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblBookDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("availability"));

        loadAllBooks();



            txtSearch.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    ObservableList bookDetails1 = tblBookDetails.getItems();
                    bookDetails1.clear();
                    for (BookDetailsTM bookDetails : bookDetailsTMArrayList) {
                        if(bookDetails.getId().contains(newValue) ||
                        bookDetails.getName().contains(newValue) ||
                        bookDetails.getIsbn().contains(newValue) ||
                        bookDetails.getAuthor().contains(newValue) ||
                                (bookDetails.getPrice()+"").contains(newValue) ||
                        bookDetails.getAvailability().contains(newValue)){

                            bookDetails1.add(bookDetails);
                        }
                    }
                }
            });

            tblBookDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookDetailsTM>() {
                @Override
                public void changed(ObservableValue<? extends BookDetailsTM> observable, BookDetailsTM oldValue, BookDetailsTM newValue) {
                    if(newValue==null){

                        txtISBN.clear();
                        txtName.clear();
                        txtAuthor.clear();
                        txtPrice.clear();
                        txtAvailability.clear();
                        return;

                    }else {
                        txtISBN.setDisable(false);
                        txtName.setDisable(false);
                        txtAuthor.setDisable(false);
                        txtPrice.setDisable(false);
                        txtAvailability.setDisable(false);

                        txtISBN.setText(newValue.getIsbn());
                        txtName.setText(newValue.getName());
                        txtAuthor.setText(newValue.getAuthor());
                        txtPrice.setText(newValue.getPrice().toString());
                        txtAvailability.setText(newValue.getAvailability());
                    }
                }
            });
    }


    private void loadAllBooks() {
        ObservableList bookDetails1 = tblBookDetails.getItems();

        ObservableList<BookDetailsTM> bookDetailsArrayObservableList = FXCollections.observableList(bookDetailsTMArrayList);

        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Book");
            bookDetails1.clear();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String isbn = resultSet.getString(2);
                String title = resultSet.getString(3);
                String author = resultSet.getString(4);
                double price = resultSet.getDouble(5);
                String availability = resultSet.getString(6);

                BookDetailsTM bookDetailsTM = new BookDetailsTM(id, isbn, title, author, price, availability);
                bookDetails1.add(bookDetailsTM);
                bookDetailsArrayObservableList.add(bookDetailsTM);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void btnUpdate_OnAction(ActionEvent actionEvent) {
        BookDetailsTM selectedItem = tblBookDetails.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            new Alert(Alert.AlertType.ERROR,"Please select a book to update", ButtonType.OK).showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to update the book?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult()==ButtonType.YES){

            try {
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Book SET isbn=?,title=?,author=?,price=?,availability=? WHERE book_id=?");
                preparedStatement.setObject(1,txtISBN.getText());
                preparedStatement.setObject(2,txtName.getText());
                preparedStatement.setObject(3,txtAuthor.getText());
                preparedStatement.setObject(4,Double.parseDouble(txtPrice.getText()));
                preparedStatement.setObject(5,txtAvailability.getText());
                preparedStatement.setObject(6,selectedItem.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if(affectedRows>0){

                    new Alert(Alert.AlertType.INFORMATION,"Update Successfully",ButtonType.OK).showAndWait();
                    tblBookDetails.getSelectionModel().clearSelection();
                    txtISBN.setDisable(true);
                    txtName.setDisable(true);
                    txtAuthor.setDisable(true);
                    txtPrice.setDisable(true);
                    txtAvailability.setDisable(true);
                    tblBookDetails.getItems().clear();
                    loadAllBooks();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            tblBookDetails.getSelectionModel().clearSelection();
            txtISBN.setDisable(true);
            txtName.setDisable(true);
            txtAuthor.setDisable(true);
            txtPrice.setDisable(true);
            txtAvailability.setDisable(true);
        }


    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        BookDetailsTM selectedItem = tblBookDetails.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            new Alert(Alert.AlertType.ERROR,"Please select a book to delete", ButtonType.OK).showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the book?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult()==ButtonType.YES){
            try {
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM Book WHERE book_id=?");
                preparedStatement.setObject(1,selectedItem.getId());
                int affectedRows = preparedStatement.executeUpdate();

                if(affectedRows>0){
                    new Alert(Alert.AlertType.INFORMATION,"Delete Successfully",ButtonType.OK).showAndWait();
                    tblBookDetails.getSelectionModel().clearSelection();
                    tblBookDetails.getItems().clear();
                    loadAllBooks();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            tblBookDetails.getSelectionModel().clearSelection();
        }
    }
}
