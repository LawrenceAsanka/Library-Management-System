package controller;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import dbConnection.DBConnection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import util.IssueDetailsTM;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class BookIssueFormController {
    public JFXButton btnAddMember;
    public JFXButton btnAddBook;
    public JFXButton btnBookDetails;
    public JFXButton btnMemberDetail;
    public AnchorPane root;
    public JFXTextField txtBookID;
    public Label lblBookName;
    public Label lblISBN;
    public Label lblAuthor;
    public Label lblAvailable;
    public JFXTextField txtMemberID;
    public Label lblMemberName;
    public JFXDatePicker txtDate;
    public Label lblNIC;
    public Label lblContact;
    public JFXButton btnIssue;
    public Label lblIssueID;
    public HBox book_info;
    public HBox member_info;
    public TableView<IssueDetailsTM> tblIssueDetails;
    public JFXTextField txtMember_id;
    public TextField txtReturnDate;
    public TextField txtReturnBookID;
    public TextField txtReturnBookName;
    public TextField txtReturnAvailable;
    public TextField txtFine_Fee;
    public JFXButton btnReturn;
    public JFXHamburger hamburger;
    public JFXDrawer drawer;
    private final ArrayList<IssueDetailsTM> issueDetailsTMArrayList = new ArrayList<>();

    public void initialize() {

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        //set toolbar
        initDraw();

        txtReturnBookID.setEditable(false);
        txtReturnBookName.setEditable(false);
        txtReturnDate.setEditable(false);
        txtReturnAvailable.setEditable(false);
        txtFine_Fee.setEditable(false);
        Platform.runLater(() -> {
            txtMember_id.requestFocus();
            txtBookID.requestFocus();
        });

        txtMemberID.setDisable(true);
        txtBookID.requestFocus();

        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);

        //date disable
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today) != 0);
                    }

                };
            }

        };
        txtDate.setDayCellFactory(callB);

        tblIssueDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("issue_id"));
        tblIssueDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        tblIssueDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("member_id"));
        tblIssueDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("book_id"));
        tblIssueDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("book_name"));
        tblIssueDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("kept_dates"));

        loadIssueDetails();

        txtMember_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tblIssueDetails.getItems().clear();
                for (IssueDetailsTM issueDetails : issueDetailsTMArrayList) {
                    if (issueDetails.getMember_id().contains(newValue) || issueDetails.getBook_id().contains(newValue)) {
                        tblIssueDetails.getItems().add(issueDetails);
                    }
                    tblIssueDetails.getSelectionModel().clearSelection();

                }
                btnReturn.setDisable(true);
            }
        });

        tblIssueDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IssueDetailsTM>() {
            @Override
            public void changed(ObservableValue<? extends IssueDetailsTM> observable, IssueDetailsTM oldValue, IssueDetailsTM newValue) {


                if (newValue == null) {
                    txtReturnDate.clear();
                    txtReturnBookID.clear();
                    txtReturnBookName.clear();
                    txtReturnAvailable.clear();
                    txtFine_Fee.clear();
                    return;
                } else {
                    LocalDate returnDate = LocalDate.now();
                    txtReturnDate.setText(returnDate.toString());


                    txtReturnBookID.setText(newValue.getBook_id());
                    txtReturnBookName.setText(newValue.getBook_name());
                    txtReturnAvailable.setText("YES");
                    if (newValue.getKept_dates() > 7) {
                        txtFine_Fee.setText("30.00");
                    } else {
                        txtFine_Fee.setText("000.00");
                    }

                    btnReturn.setDisable(false);
                    txtReturnBookID.setEditable(false);
                    txtReturnBookName.setEditable(false);
                    txtReturnDate.setEditable(false);
                    txtReturnAvailable.setEditable(true);
                    txtFine_Fee.setEditable(true);
                }
            }
        });

    }

    private void initDraw() {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/view/toolBar.fxml"));
            drawer.setSidePane(box);

        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventFilter(MouseEvent.MOUSE_CLICKED, (EventHandler<Event>) event -> {
            task.setRate(task.getRate() * -1);
            task.play();
            if (!drawer.isClosed()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }

    /*================================================================================================================*/
    //Starting point of book issue
    public void btnIssue_OnAction(ActionEvent actionEvent) {

        if (txtBookID.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Book ID cannot be null", ButtonType.OK).showAndWait();
            txtBookID.requestFocus();
            return;
        }

        if (txtMemberID.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Member ID cannot be null", ButtonType.OK).showAndWait();
            txtMemberID.requestFocus();
            return;
        }

        if (txtDate.getEditor().getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Date field cannot be null", ButtonType.OK).showAndWait();
            txtDate.getEditor().requestFocus();
            return;
        }

        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO Issue (member_id,book_id,date)values (?,?,?)");
            preparedStatement.setObject(1, txtMemberID.getText());
            preparedStatement.setObject(2, txtBookID.getText());
            preparedStatement.setObject(3, txtDate.getValue());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {

                new Alert(Alert.AlertType.INFORMATION, "Book issued successfully!", ButtonType.OK).showAndWait();
                bookAvailability();
                loadIssueDetails();

                txtBookID.clear();
                txtMemberID.clear();
                txtDate.getEditor().clear();
                clearBook();
                clearMember();
                txtBookID.requestFocus();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    private void bookAvailability() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Book SET availability=? WHERE book_id=?");
            preparedStatement.setObject(1, "NO");
            preparedStatement.setObject(2, txtBookID.getText());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                //TODO
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBooks(ActionEvent actionEvent) {
        clearBook();
        String book_id = txtBookID.getText();
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM  Book WHERE book_id=?");
            preparedStatement.setObject(1, book_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean flag = false;

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String isbn = resultSet.getString(2);
                String title = resultSet.getString(3);
                String author = resultSet.getString(4);
                double price = resultSet.getDouble(5);
                String availability = resultSet.getString(6);

                if (availability.equals("YES")) {
                    lblBookName.setText("Book Name: " + title);
                    lblISBN.setText("ISBN: " + isbn);
                    lblAuthor.setText("Author: " + author);
                    if (availability.equals("YES")) {
                        lblAvailable.setText("Book is Available");
                    } else {
                        lblAvailable.setText("Book is Not Available");
                    }
                    txtMemberID.setDisable(false);
                    txtMemberID.requestFocus();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Book is not available ate this moment,Try another book", ButtonType.OK).showAndWait();
                    txtBookID.selectAll();
                    txtBookID.requestFocus();
                }
                flag = true;

            }

            if (!flag) {
                new Alert(Alert.AlertType.ERROR, "No Book Found,Try again", ButtonType.OK).showAndWait();
                txtBookID.selectAll();
                txtBookID.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    void clearBook() {
        lblBookName.setText("BOOK NAME");
        lblISBN.setText("ISBN");
        lblAuthor.setText("AUTHOR");
        lblAvailable.setText("AVAILABILITY");

    }

    public void loadMember_OnAction(ActionEvent actionEvent) {
        clearMember();
        String member_id = txtMemberID.getText();
        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Member WHERE member_id='" + member_id + "'");
            boolean flag = false;

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String nic = resultSet.getString(2);
                String name = resultSet.getString(3);
                String address = resultSet.getString(4);
                String contact = resultSet.getString(5);

                lblMemberName.setText("Member Name: " + name);
                lblNIC.setText("Member NIC: " + nic);
                lblContact.setText("Phone Number: " + contact);
                txtDate.requestFocus();

                flag = true;
            }

            if (!flag) {
                new Alert(Alert.AlertType.ERROR, "No Member Found,try again", ButtonType.OK).showAndWait();
                txtMemberID.selectAll();
                txtMemberID.requestFocus();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        btnIssue.setDisable(false);
    }

    void clearMember() {
        lblMemberName.setText("NAME");
        lblNIC.setText("NIC");
        lblContact.setText("CONTACT");

    }
    //Ending of the book issue

/*====================================================================================================================*/
    //Starting point of book return
    private void loadIssueDetails() {
        ObservableList<IssueDetailsTM> issueDetails = tblIssueDetails.getItems();
        ObservableList<IssueDetailsTM> issueDetailsArrayObservableList = FXCollections.observableList(issueDetailsTMArrayList);

        String sql = "SELECT Issue.issue_id,Issue.date,Issue.member_id,Issue.book_id,Book.title  FROM Issue  INNER JOIN Book on Issue.book_id = Book.book_id WHERE detail=\"ISSUED\";";
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            tblIssueDetails.getItems().clear();
            while (resultSet.next()) {
                String issue_id = resultSet.getString(1);
                Date issue_date = resultSet.getDate(2);
                String member_id = resultSet.getString(3);
                String book_id = resultSet.getString(4);
                String book_name = resultSet.getString(5);

                LocalDate today = LocalDate.now();

                //Difference between Days
                long diffInDays = ChronoUnit.DAYS.between(issue_date.toLocalDate(), today);

                IssueDetailsTM issue_Details = new IssueDetailsTM(issue_id, issue_date, member_id, book_id, book_name, diffInDays);
                issueDetails.add(issue_Details);
                issueDetailsArrayObservableList.add(issue_Details);
                tblIssueDetails.refresh();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void btnReturn_OnAction(ActionEvent actionEvent) {
        IssueDetailsTM selectedItem = tblIssueDetails.getSelectionModel().getSelectedItem();
        String issue_id = selectedItem.getIssue_id();
        String fine_fee = txtFine_Fee.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to return the book", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Return` VALUES(?,?,?)");
                preparedStatement.setObject(1, txtReturnDate.getText());
                preparedStatement.setObject(2, Integer.parseInt(issue_id));
                preparedStatement.setObject(3, Double.parseDouble(fine_fee));

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Returned Successfully", ButtonType.OK).show();
                    updateBookAvail_afterReturn();
                    updateIssueDetail();
                    tblIssueDetails.getItems().remove(selectedItem);
                    tblIssueDetails.getSelectionModel().clearSelection();
                    tblIssueDetails.refresh();
                    txtMember_id.requestFocus();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateBookAvail_afterReturn() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Book SET availability=? WHERE book_id=?");
            preparedStatement.setObject(1, txtReturnAvailable.getText());
            preparedStatement.setObject(2, txtReturnBookID.getText());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                //TODO
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateIssueDetail() {
        IssueDetailsTM selectedItem = tblIssueDetails.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Issue SET detail=? WHERE issue_id=?");
            preparedStatement.setObject(1, "RETURNED");
            preparedStatement.setObject(2, selectedItem.getIssue_id());

            int affectedRows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
