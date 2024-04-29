package com.example.bookshop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static com.example.bookshop.Database.connectDb;

public class DashboardController extends AnchorPane implements Initializable{

    @FXML
    private Button chat_btn;

    @FXML
    private AnchorPane chat_form;

    @FXML
    private Button availableBook_createBackUp;

    @FXML
    private Label dashboard_username;

    @FXML
    private Button availableBook_addBtn;

    @FXML
    private TextField availableBook_author;

    @FXML
    private TextField availableBook_bookID;

    @FXML
    private TextField availableBook_bookTitle;

    @FXML
    private Button availableBook_clearBtn;

    @FXML
    private TableColumn<BookData, String> availableBook_col_author;

    @FXML
    private TableColumn<BookData, String> availableBook_col_bookID;

    @FXML
    private TableColumn<BookData, String> availableBook_col_bookTitle;

    @FXML
    private TableColumn<BookData, String> availableBook_col_date;

    @FXML
    private TableColumn<BookData, String> availableBook_col_genre;

    @FXML
    private TableColumn<BookData, String> availableBook_col_price;

    @FXML
    private TableColumn<Sale, Integer> dashboard_sale_id;

    @FXML
    private TableColumn<Sale, Double> dashboard_amount_sale;

    @FXML
    private Button availableBook_deleteBtn;

    @FXML
    private AnchorPane availableBook_form;

    @FXML
    private TextField availableBook_genre;

    @FXML
    private TextField availableBook_price;

    @FXML
    private TextField availableBook_search;

    @FXML
    private TableView<BookData> availableBook_tableView;

    @FXML
    private TableView<Sale> dashoard_table_view;

    @FXML
    private Button availableBook_updateBtn;

    @FXML
    private Button availableBooks_btn;

    @FXML
    private Button close;

    @FXML
    private Label dashboard_SB;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TC;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Pane dashboard_form;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button purchase_addBtn;

    @FXML
    private ComboBox<Integer> purchase_bookQuantity;

    @FXML
    private ComboBox<String> purchase_bookTitle;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableColumn<Order, String> purchase_col_author;

    @FXML
    private TableColumn<Order, Integer> purchase_col_bookID;

    @FXML
    private TableColumn<Order, String> purchase_col_bookTitle;

    @FXML
    private TableColumn<Order, String> purchase_col_genre;

    @FXML
    private TableColumn<Order, Double> purchase_col_price;

    @FXML
    private TableColumn<Order, Integer> purchase_col_quantity;

    @FXML
    private TableColumn<Order, Double> purchase_col_total_price;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private Label purchase_info_Book_ID;

    @FXML
    private Label purchase_info_Book_Title;

    @FXML
    private Label purchase_info_author;

    @FXML
    private Label purchase_info_price;

    @FXML
    private Label purchase_info_genre;

    @FXML
    private Button purchase_payBtn;

    @FXML
    private TableView<Order> purchase_tableView;

    @FXML
    private Label purchase_tottal;
    private Image image;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public DashboardController() throws SQLException {
    }

    public void usernameSet(){
        String str = GetData.username;

        char firstLetter = Character.toUpperCase(str.charAt(0));

        String usernameUpperCase = firstLetter + str.substring(1);

        dashboard_username.setText(usernameUpperCase);
    }

    public ObservableList<BookData> availableBooksListData(){
        ObservableList<BookData> listData = FXCollections.observableArrayList();
        String sql = "SELECT book_id, title, author, genre, price FROM book";
        try{
            connect = connectDb();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while(result.next()){
                BookData bookD = new BookData(result.getInt("book_id"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("genre"),
                        result.getDouble("price"));
                listData.add(bookD);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<BookData> availableBooksList;

    public void availableBooksShowListData(){
        availableBooksList = availableBooksListData();

        availableBook_col_bookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        availableBook_col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        availableBook_col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        availableBook_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        availableBook_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        // засунуть все это в интерфейс
        availableBook_tableView.setItems(availableBooksList);
    }

    public void availableBooksAdd() {
        // SQL запрос для добавления книги
        String sql = "INSERT INTO book (book_id, title, author, genre, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            if (availableBook_bookID.getText().isEmpty() ||
                    availableBook_bookTitle.getText().isEmpty() ||
                    availableBook_author.getText().isEmpty() ||
                    availableBook_genre.getText().isEmpty() ||
                    availableBook_price.getText().isEmpty()) {
                showError("Error", "Please enter all the fields correctly.");
                return;
            }

            String checkDataSql = "SELECT book_id FROM book WHERE book_id = ?";
            try (PreparedStatement checkStatement = connect.prepareStatement(checkDataSql)) {
                int bookID;
                try {
                    bookID = Integer.parseInt(availableBook_bookID.getText());
                } catch (NumberFormatException e) {
                    showError("Error", "Please enter a valid integer for book ID.");
                    return;
                }

                checkStatement.setInt(1, bookID);
                ResultSet result = checkStatement.executeQuery();

                if (result.next()) {
                    showError("Error Message", "Book ID: " + bookID + " already exists!");
                    return;
                }
            }

            prepare.setInt(1, Integer.parseInt(availableBook_bookID.getText()));
            prepare.setString(2, availableBook_bookTitle.getText());
            prepare.setString(3, availableBook_author.getText());
            prepare.setString(4, availableBook_genre.getText());

            double price;
            try {
                price = Double.parseDouble(availableBook_price.getText());
                prepare.setDouble(5, price);
            } catch (NumberFormatException e) {
                showError("Error", "Please enter a valid price.");
                return;
            }

            prepare.executeUpdate();

            showInfo("Information Message", "Successfully Added!");

            availableBooksShowListData();
            availableBooksClear();

        } catch (SQLException e) {
            showError("Database Error", "An error occurred while adding the book.");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void availableBooksDelete(){
        String sql = "DELETE FROM book WHERE book_id = '" + availableBook_bookID.getText() + "'";
        connect = connectDb();
        try{
            Alert alert;
            if (availableBook_bookID.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter all the fields correctly.");
                alert.showAndWait();
                return;
            }
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Book ID: " + availableBook_bookID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            if(option.isPresent() && option.get().equals(ButtonType.OK)){
                statement = connect.createStatement();
                statement.executeUpdate(sql);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted!");
                alert.showAndWait();

                availableBooksShowListData();
                availableBooksClear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void availableBooksUpdate(){
        String sql = "UPDATE book SET title = '"
                + availableBook_bookTitle.getText() + "', author = '"
                + availableBook_author.getText() + "', genre = '"
                + availableBook_genre.getText() + "', price = '"
                + availableBook_price.getText() + "' WHERE book_id = '"
                + availableBook_bookID.getText() + "'";

        connect = connectDb();
        try{
            Alert alert;

            if (availableBook_bookID.getText().isEmpty()
                    || availableBook_bookTitle.getText().isEmpty()
                    || availableBook_author.getText().isEmpty()
                    || availableBook_genre.getText().isEmpty()
                    || availableBook_price.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter all the fields correctly.");
                alert.showAndWait();
                return;
            }

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Book ID: " + availableBook_bookID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                availableBooksShowListData();
                availableBooksClear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void availableBooksClear(){
        availableBook_bookID.setText("");
        availableBook_bookTitle.setText("");
        availableBook_author.setText("");
        availableBook_genre.setText("");
        availableBook_price.setText("");
    }

    public void availableBooksSelect(){
        BookData bookD = availableBook_tableView.getSelectionModel().getSelectedItem();
        int num = availableBook_tableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        availableBook_bookID.setText(String.valueOf(bookD.getBookID()));
        availableBook_bookTitle.setText(bookD.getTitle());
        availableBook_author.setText(bookD.getAuthor());
        availableBook_genre.setText(bookD.getGenre());
        availableBook_price.setText(String.valueOf(bookD.getPrice()));
    }

    public void availableBooksSearch(){

        FilteredList<BookData> filter = new FilteredList<>(availableBooksList, e -> true);

        availableBook_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateBookData -> {

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateBookData.getBookID().toString().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getTitle().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getAuthor().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getGenre().toLowerCase().contains(searchKey)){
                    return true;
                }else return false;
            });
        });

        SortedList<BookData> sortList = new SortedList(filter);
        sortList.comparatorProperty().bind(availableBook_tableView.comparatorProperty());
        availableBook_tableView.setItems(sortList);
    }

    public void setBookInfoPurchase() {
        purchase_bookTitle.setOnAction(event -> {
            String selectedTitle = purchase_bookTitle.getValue();
            BookData selectedBook = getBookDataByTitle(selectedTitle);

            if (selectedBook != null) {
                purchase_info_Book_ID.setText(String.valueOf(selectedBook.getBookID()));
                purchase_info_Book_Title.setText(selectedBook.getTitle());
                purchase_info_author.setText(selectedBook.getAuthor());
                purchase_info_genre.setText(selectedBook.getGenre());
                purchase_info_price.setText(String.valueOf(selectedBook.getPrice()));
            } else {
                purchase_info_Book_ID.setText("N/A");
                purchase_info_Book_Title.setText("N/A");
                purchase_info_author.setText("N/A");
                purchase_info_genre.setText("N/A");
                purchase_info_price.setText("N/A");
                System.out.println("Книга с названием " + selectedTitle + " не найдена.");
            }
        });
    }

    public static ObservableList<String> getBookTitles() {
        ObservableList<String> bookTitles = FXCollections.observableArrayList();
        String query = "SELECT title FROM book";

        try (Connection connection = connectDb();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                bookTitles.add(title);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookTitles;
    }

    public static BookData getBookDataByTitle(String title) {
        String query = "SELECT * FROM book WHERE title = ?";
        BookData bookData = null;

        try (Connection connection = connectDb();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int bookID = resultSet.getInt("book_id");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                double price = resultSet.getDouble("price");

                bookData = new BookData(bookID, title, author, genre, price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookData;
    }

    private int getLatestSaleId() throws SQLException {
        String query = "SELECT sale_id FROM \"sales\" ORDER BY sale_id DESC LIMIT 1";
        try (Connection connect = connectDb();
             PreparedStatement prepare = connect.prepareStatement(query);
             ResultSet resultSet = prepare.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("sale_id") + 1;
            } else {
                return 1;
            }
        }
    }

    public void initializeNewSale() {
        int currentSaleId = sale.getSale_id();
        boolean saleExists = false;

        String checkSaleExistsSQL = "SELECT COUNT(*) FROM sales WHERE sale_id = ?";
        try (Connection connect = connectDb();
             PreparedStatement ps = connect.prepareStatement(checkSaleExistsSQL)) {
            ps.setInt(1, currentSaleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    saleExists = (count > 0);
                }
            }
        } catch (SQLException e) {
            showError("Database Error", "An error occurred while checking sale existence.");
            System.err.println(e.getMessage());
            return;
        }

        if (!saleExists) {
            String insertNewSaleSQL = "INSERT INTO sales (\"totalAmount\") VALUES (?)";
            try (Connection connect = connectDb();
                 PreparedStatement ps = connect.prepareStatement(insertNewSaleSQL)) {
                ps.setNull(1, Types.DOUBLE);
                ps.executeUpdate();
            } catch (SQLException e) {
                showError("Database Error", "An error occurred while inserting a new sale.");
                System.err.println(e.getMessage());
            }
        }
    }

    int latestSaleId = getLatestSaleId();
    Sale sale = new Sale(latestSaleId);
    public void addBookToOrder(){
        initializeNewSale();
        String selectedTitle = purchase_bookTitle.getValue();
        BookData selectedBook = getBookDataByTitle(selectedTitle);
        sale.addBook(selectedBook);
        int quantity = purchase_bookQuantity.getValue();
        double total = selectedBook.getPrice() * quantity;
        String sql = "INSERT INTO \"orderItems\" (sale_id, book_id, quantity, total) VALUES (?, ?, ?, ?)";

        try (Connection connect = connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, sale.getSale_id());
            prepare.setInt(2, selectedBook.getBookID());
            prepare.setInt(3, quantity);
            prepare.setDouble(4, total);
            double sale_total = sale.getSale_total();
            sale_total += selectedBook.getPrice() * quantity;
            sale.setSale_total(sale_total);
            purchase_tottal.setText("₸" + String.valueOf(sale.getSale_total()));

            int rowsAffected = prepare.executeUpdate();

            if (rowsAffected > 0) {
                showInfo("Information Message", "Successfully Added!");
                orderListDataInTable();
                clearFields();
            } else {
                showError("Database Error", "An error occurred while adding the book.");
            }

        } catch (SQLException e) {
            showError("Database Error", "An error occurred while adding the book.");
            e.printStackTrace();
        }
    }

    public ObservableList<Order> purchaseOrderDataList(){
        ObservableList<Order> listData = FXCollections.observableArrayList();
        String sql = "SELECT b.book_id, b.title, b.author, b.genre, b.price, oi.quantity, b.price * oi.quantity AS total " +
                "FROM book b " +
                "JOIN \"orderItems\" oi ON b.book_id = oi.book_id " +
                "JOIN sales s ON oi.sale_id = s.sale_id " +
                "WHERE s.sale_id = ?";
        try{
            connect = connectDb();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, sale.getSale_id());
            result = prepare.executeQuery();
            while(result.next()){
                Order orderData = new Order(result.getInt("book_id"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("genre"),
                        result.getDouble("price"),
                        result.getInt("quantity"),
                        result.getDouble("total"));
                listData.add(orderData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<Order> orderBooksList;
    public void orderListDataInTable(){
        orderBooksList = purchaseOrderDataList();
        purchase_col_bookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        purchase_col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        purchase_col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        purchase_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_col_total_price.setCellValueFactory(new PropertyValueFactory<>("total"));
        purchase_tableView.setItems(orderBooksList);
        purchase_tottal.setText("₸" + String.valueOf(sale.getSale_total()));
    }

    public void finalizeOrder() {
        double totalAmount = 0.0;

        String getOrderTotalSQL = "SELECT SUM(total) FROM \"orderItems\" WHERE sale_id = ?";
        try (Connection connect = connectDb();
             PreparedStatement ps = connect.prepareStatement(getOrderTotalSQL)) {
            ps.setInt(1, sale.getSale_id());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalAmount = rs.getDouble(1);
                }
            }

            String updateSalesSQL = "UPDATE sales SET \"totalAmount\" = ? WHERE sale_id = ?";
            try (PreparedStatement ps2 = connect.prepareStatement(updateSalesSQL)) {
                ps2.setDouble(1, totalAmount);
                ps2.setInt(2, sale.getSale_id());

                int rowsAffected = ps2.executeUpdate();
                if (rowsAffected > 0) {
                    showInfo("Order Updated", "Order has been added successfully.");

                    sale.sale_total_reset();
                    sale.orderedBooks_reset();
                    purchase_tottal.setText("₸0.0");

                    ObservableList<?> tableData = purchase_tableView.getItems();
                    tableData.clear();
                    purchase_tableView.setItems(FXCollections.observableArrayList());
                } else {
                    showError("Database Error", "Failed to update the existing sale.");
                }
            }
        } catch (SQLException e) {
            showError("Database Error", "An error occurred while finalizing the order.");
            System.err.println(e.getMessage());
        }
    }

    public void clearFields(){
        purchase_info_Book_ID.setText(" ");
        purchase_info_Book_Title.setText(" ");
        purchase_info_author.setText(" ");
        purchase_info_genre.setText(" ");
        purchase_info_price.setText(" ");
        purchase_bookTitle.setValue(null);
        purchase_bookQuantity.setValue(null);
    }

    public void updateTotalSales() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.connectDb();

            String query = "SELECT COUNT(*) AS \"totalSales\" FROM sales";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int totalSales = resultSet.getInt("totalSales");
                dashboard_TC.setText(String.valueOf(totalSales));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса:");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов:");
                e.printStackTrace();
            }
        }
    }

    public void createBackup() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String filename = "backup_" + now.format(formatter) + ".ser";

        List<BookData> bookDataList = new ArrayList<>();

        Connection connection = connectDb();
        if (connection == null) {
            System.err.println("Failed to connect to the database.");
            return;
        }

        String query = "SELECT book_id, title, author, genre, price FROM book";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer bookID = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                double price = resultSet.getDouble("price");

                BookData bookData = new BookData(bookID, title, author, genre, price);
                bookDataList.add(bookData);
            }

        } catch (SQLException e) {
            System.err.println("Failed to execute query:");
            e.printStackTrace();
            return;
        }
        Alert alert;

        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(bookDataList);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Backup successfully created: " + filename);
            alert.showAndWait();

        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Can not create backup.");
            alert.showAndWait();
            System.err.println("Failed to serialize data:");
            e.printStackTrace();
        }
    }

    public void updateTotalSoldBooks() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.connectDb();
            String query = "SELECT SUM(quantity) AS totalSoldBooks FROM \"orderItems\"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int totalSoldBooks = resultSet.getInt("totalSoldBooks");
                dashboard_SB.setText(String.valueOf(totalSoldBooks));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса:");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов:");
                e.printStackTrace();
            }
        }
    }

    public void updateTotalSum() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.connectDb();
            String query = "SELECT SUM(\"totalAmount\") AS totalSum FROM sales";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double totalSumma = resultSet.getInt("totalSum");
                dashboard_TI.setText("₸" + String.valueOf(totalSumma));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса:");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов:");
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Sale> salesDataList(){
        ObservableList<Sale> listData = FXCollections.observableArrayList();
        String sql = "SELECT sale_id, \"totalAmount\" FROM sales";
        try{
            connect = connectDb();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while(result.next()){
                Sale saleData = new Sale(result.getInt("sale_id"), result.getDouble("totalAmount"));
                listData.add(saleData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<Sale> salesBooksList;
    public void salesListDataInTable(){
        salesBooksList = salesDataList();
        dashboard_sale_id.setCellValueFactory(new PropertyValueFactory<>("sale_id"));
        dashboard_amount_sale.setCellValueFactory(new PropertyValueFactory<>("sale_total"));
        dashoard_table_view.setItems(salesBooksList);
    }

    public void switchFrom(ActionEvent event){
        if (event.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            availableBook_form.setVisible(false);
            purchase_form.setVisible(false);
            chat_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: linear-gradient(to top right, #769BBF, #8AB7E2)");
            availableBooks_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");
            chat_btn.setStyle("-fx-background-color: transparent");

            usernameSet();
            updateTotalSales();
            updateTotalSoldBooks();
            updateTotalSum();
            salesListDataInTable();
        }else if (event.getSource() == availableBooks_btn){
            dashboard_form.setVisible(false);
            availableBook_form.setVisible(true);
            purchase_form.setVisible(false);
            chat_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            availableBooks_btn.setStyle("-fx-background-color: linear-gradient(to top right, #769BBF, #8AB7E2)");
            purchase_btn.setStyle("-fx-background-color: transparent");
            chat_btn.setStyle("-fx-background-color: transparent");
            availableBooksShowListData();
        }else if (event.getSource() == purchase_btn){
            dashboard_form.setVisible(false);
            availableBook_form.setVisible(false);
            purchase_form.setVisible(true);
            chat_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            availableBooks_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: linear-gradient(to top right, #769BBF, #8AB7E2)");
            chat_btn.setStyle("-fx-background-color: transparent");
        } else if(event.getSource() == chat_btn){
            dashboard_form.setVisible(false);
            availableBook_form.setVisible(false);
            purchase_form.setVisible(false);
            chat_form.setVisible(true);

            dashboard_btn.setStyle("-fx-background-color: transparent");
            availableBooks_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");
            chat_btn.setStyle("-fx-background-color: linear-gradient(to top right, #769BBF, #8AB7E2)");
        }
    }
    private double x = 0;
    private double y = 0;
    public void logout(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            if(option.get().equals(ButtonType.OK)){
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("login-menu.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) ->{
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) ->{
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void close(){
        System.exit(0);
    }
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void initialize(URL location, ResourceBundle resources) {
        dashboard_form.setVisible(true);
        availableBook_form.setVisible(false);
        purchase_form.setVisible(false);
        dashboard_btn.setStyle("-fx-background-color: linear-gradient(to top right, #769BBF, #8AB7E2)");
        availableBooks_btn.setStyle("-fx-background-color: transparent");
        purchase_btn.setStyle("-fx-background-color: transparent");
        usernameSet();

        purchase_bookTitle.setItems(getBookTitles());
        purchase_bookQuantity.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        setBookInfoPurchase();

        orderListDataInTable();
        updateTotalSales();
        updateTotalSoldBooks();
        updateTotalSum();
        salesListDataInTable();

        availableBook_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            availableBooksSelect();
        });

    }
}
