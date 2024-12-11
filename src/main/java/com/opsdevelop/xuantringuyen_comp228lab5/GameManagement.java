package com.opsdevelop.xuantringuyen_comp228lab5;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.text.TableView;

import static javafx.application.Application.launch;


public class GameManagement extends Application {

    private Connection connection;
    private String player_Id;
    private String gameId;

    //Connection method to connect to JDBC
    public Connection connectToDB() {
        try {
            System.out.println("> Start Program ...");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("> Driver Loaded successfully.");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@199.212.26.208:1521:SQLD", "COMP228_F24_soh_51", "Tri09102005");
            System.out.println("Database connected.");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return connection;
    }

    @Override
    public void start(Stage stage) throws IOException {
        connection = connectToDB();

        stage.setTitle("Student Information");
        Image icon = new Image("file:D:\\Centennial\\Semester 3\\COMP-228_Java Programming\\Assignment\\Lab 5\\XuanTriNguyen_COMP228Lab5\\src\\main\\assets\\logo.png");
        stage.getIcons().add(icon);

        FXMLLoader fxmlLoader = new FXMLLoader(GameManagement.class.getResource("gamePlayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        TextField firstname = new TextField();
        TextField lastname = new TextField();
        TextField address = new TextField();
        TextField province = new TextField();
        TextField postalCode = new TextField();
        TextField phoneNumber = new TextField();
        TextField idField = new TextField();
        TextField gameTitle = new TextField("");
        TextField gameScore = new TextField("");
        TextField datePlayed = new TextField("");

        //For Labels
        Label playerInfo = new Label("Player Information:");

        Label firstnameLabel = new Label("First Name:");
        Label lastnameLabel = new Label("Last Name:");
        Label addressLabel = new Label("Address:");
        Label provinceLabel = new Label("Province:");
        Label postalCodeLabel = new Label("Postal Code:");
        Label phoneNumberLabel = new Label("Phone Number:");
        Label idFieldLabel = new Label("Update Player by ID:");

        Label gameInfo = new Label("Game Information:");

        Label gameTitleLabel = new Label("Game Title:");
        Label gameScoreLabel = new Label("Game Score:");
        Label datePlayedLabel = new Label("Date Played:");

        // Providing Padding
        Insets label = new Insets(0, 10, 0, 0);

        firstnameLabel.setPadding(label);
        lastnameLabel.setPadding(label);
        addressLabel.setPadding(label);
        provinceLabel.setPadding(label);
        postalCodeLabel.setPadding(label);
        phoneNumberLabel.setPadding(label);
        idFieldLabel.setPadding(label);
        gameTitleLabel.setPadding(label);
        gameScoreLabel.setPadding(label);
        datePlayedLabel.setPadding(label);

        // Display Buttons
        Button button1 = new Button("Create Player");
        button1.setPrefWidth(150);
        Button button2 = new Button("Display All Players");
        button2.setPrefWidth(150);

        Button updatebtn = new Button("Update");
        updatebtn.setPrefWidth(150);

        // Search button
        Button button3 = new Button("Search by ID");
        button3.setPrefWidth(150);

        // Clear button
        Button clearButton = new Button("Clear");
        clearButton.setPrefWidth(150);

        // Display all
        GridPane displayInfo = new GridPane();
        displayInfo.setPadding(new Insets(20));
        // Set the horizontal gap between columns
        displayInfo.setHgap(10);
        // Set the vertical gap between rows
        displayInfo.setVgap(5);

        // Display labels
        displayInfo.add(playerInfo, 0, 0);
        displayInfo.add(firstnameLabel, 0, 1);
        displayInfo.add(lastnameLabel, 0, 2);
        displayInfo.add(addressLabel, 0, 3);
        displayInfo.add(provinceLabel, 0, 4);
        displayInfo.add(postalCodeLabel, 0, 5);
        displayInfo.add(phoneNumberLabel, 0, 6);
        displayInfo.add(idFieldLabel, 3, 0);

        // Display textfields
        displayInfo.add(firstname, 1, 1);
        displayInfo.add(lastname, 1, 2);
        displayInfo.add(address, 1, 3);
        displayInfo.add(province, 1, 4);
        displayInfo.add(postalCode, 1, 5);
        displayInfo.add(phoneNumber, 1, 6);
        displayInfo.add(idField, 4, 0);

        // Display game information
        displayInfo.add(gameInfo, 3, 3);
        displayInfo.add(gameTitleLabel, 3, 4);
        displayInfo.add(gameTitle, 4, 4);
        displayInfo.add(gameScoreLabel, 3, 5);
        displayInfo.add(gameScore, 4, 5);
        displayInfo.add(datePlayedLabel, 3, 6);
        displayInfo.add(datePlayed, 4, 6);

        //display buttons
        displayInfo.add(updatebtn, 5, 0);
        displayInfo.add(button1, 4, 7);
        displayInfo.add(button2, 5, 7);
        displayInfo.add(button3, 5, 1);
        displayInfo.add(clearButton, 5, 2);

        //EVENT HANDLERS
        //Event handler for Create Player Button
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    createPlayer();  //Call the createPlayer() method when the button is clicked
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            //Method to create a new player
            private void createPlayer() throws SQLException {
                Connection connection = connectToDB();

                //Initialize variables to hold playerID and gameID
                int id_player = 0;
                int id_game = 0;

                //Validate PhoneNumber
                String PhoneNumber = phoneNumber.getText();
                try {
                    Long.parseLong(PhoneNumber);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Phone number should be a number", "Error", JOptionPane.ERROR_MESSAGE); //display error message when phone number is invalid
                    return; // Exit if the phone number is not a number
                }

                //Validate date format
                String datePlayedText = datePlayed.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //Use correct date format
                sdf.setLenient(false); //strictly parse dates according to format
                Date utilDate;
                try {
                    utilDate = sdf.parse(datePlayedText);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please use \"yyyy-MM-dd\"", "Error", JOptionPane.ERROR_MESSAGE); //display error message when date is invalid
                    return; //Exit if the date is not valid
                }
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); //converts java.util.Date object into a java.sql.Date object, to be used in the SQL query.

                //Validate game score
                String gameScoreText = gameScore.getText();
                try {
                    int score = Integer.parseInt(gameScoreText);
                    if (score < 0) {
                        JOptionPane.showMessageDialog(null, "Game score cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Exit if the game score is negative
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Game score should be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit if the game score is not a number
                }

                //Prepare a statement to insert a new player into the PLAYER table
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO PLAYER (FIRST_NAME,LAST_NAME,ADDRESS,PROVINCE, POSTAL_CODE,PHONE_NUMBER) VALUES (?,?,?,?,?,?)", new String[]{"PLAYER_ID"});
                //Retrieve player information from the textfields
                String FirstName = firstname.getText();
                String LastName = lastname.getText();
                String Address = address.getText();
                String Province = province.getText();
                String PostalCode = postalCode.getText();

                //Set parameters for the prepared statement
                stmt.setString(1, FirstName);
                stmt.setString(2, LastName);
                stmt.setString(3, Address);
                stmt.setString(4, Province);
                stmt.setString(5, PostalCode);
                stmt.setString(6, PhoneNumber);

                //Execute the insert query and get the number of affected rows
                int i = stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys(); //Get the autogenerated key (player_ID)

                //If there are generated keys, retrieve the player_ID
                if (resultSet.next()) {
                    id_player = Integer.parseInt(resultSet.getString(1));
                }

                //Display a message dialog to show the number of player records inserted and its name
                JOptionPane.showMessageDialog(null, i + " player record inserted:\n" + "Player" + id_player + ": " + FirstName + " " + LastName);

                //Prepare a statement to insert a new game into the GAME table
                PreparedStatement stmt1 = connection.prepareStatement("INSERT INTO GAME (GAME_TITLE) VALUES (?)", new String[]{"GAME_ID"});

                //Retrieve game information from the textfields
                String GameTitle = gameTitle.getText();
                stmt1.setString(1, gameTitle.getText());

                int j = stmt1.executeUpdate();   //Execute the insert query and get the number of affected rows
                ResultSet resultSet1 = stmt1.getGeneratedKeys();  //Retrieve the autogenerated keys (game_ID)
                //If there are generated keys, retrieve the game_ID
                if (resultSet1.next()) {
                    id_game = Integer.parseInt(resultSet1.getString(1));
                }

                //Display a message dialog to show the number of game records inserted and its title
                JOptionPane.showMessageDialog(null, j + " game record inserted:\n" + "Game" + id_game + ": " + GameTitle);

                //Prepare a statement to insert a new record into the PLAYERANDGAME table
                PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO PLAYERANDGAME (PLAYER_ID, GAME_ID,PLAYING_DATE,SCORE) VALUES (?, ?,?,?)");
                //Set parameters for the prepared statement
                stmt2.setInt(1, id_player);
                stmt2.setInt(2, id_game);
                stmt2.setDate(3, sqlDate);
                stmt2.setInt(4, Integer.parseInt(gameScore.getText()));
                stmt2.executeUpdate();   //Execute the insert query

                //Close result sets, statements, and connection
                resultSet.close();
                resultSet1.close();
                stmt.close();
                stmt1.close();
                stmt2.close();
                connection.close();
                displayTable();
            }
        });

        //event handler for Display Table button
        button2.setOnAction(event -> displayTable());

        //event handler for Update button
        updatebtn.setOnAction(event -> {
            try {
                //Validation of player_ID
                if (player_Id.equals("")) {
                    JOptionPane.showMessageDialog(null, "Player ID is invalid"); //display message when player_id has no value
                    return;
                }
                Connection connection = connectToDB();  //Establish DB connection

                player_Id = idField.getText(); //Updating player_Id with the text from the idField textfield

                //Validate PhoneNumber
                String PhoneNumber = phoneNumber.getText();
                try {
                    Long.parseLong(PhoneNumber);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Phone number should be a number", "Error", JOptionPane.ERROR_MESSAGE); //display error message when phone number is invalid
                    return; // Exit if the phone number is not a number
                }

                // Validate date format
                String datePlayedText = datePlayed.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //Use correct date format
                sdf.setLenient(false); //strictly parse dates according to format
                Date utilDate;
                try {
                    utilDate = sdf.parse(datePlayedText);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please use \"yyyy-MM-dd\"", "Error", JOptionPane.ERROR_MESSAGE); //display error message when date is invalid
                    return; //Exit if the date is not valid
                }
                //java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); //converts java.util.Date object into a java.sql.Date object, to be used in the SQL query.

                // Validate game score
                String gameScoreText = gameScore.getText();
                try {
                    int score = Integer.parseInt(gameScoreText);
                    if (score < 0) {
                        JOptionPane.showMessageDialog(null, "Game score cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Exit if the game score is negative
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Game score should be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit if the game score is not a number
                }

                //Update Player Info
                try (PreparedStatement updateprepStmt = connection.prepareStatement("UPDATE PLAYER SET FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, PROVINCE = ?, POSTAL_CODE = ?, PHONE_NUMBER = ? WHERE PLAYER_ID = ?")) {
                    updateprepStmt.setString(1, firstname.getText());
                    updateprepStmt.setString(2, lastname.getText());
                    updateprepStmt.setString(3, address.getText());
                    updateprepStmt.setString(4, province.getText());
                    updateprepStmt.setString(5, postalCode.getText());
                    updateprepStmt.setString(6, phoneNumber.getText());
                    updateprepStmt.setString(7, player_Id);
                    updateprepStmt.executeUpdate();
                }

                //Update Game Info
                try (PreparedStatement updateprepStmt2 = connection.prepareStatement("UPDATE GAME SET GAME_TITLE = ? WHERE GAME_ID = ?")) {
                    updateprepStmt2.setString(1, gameTitle.getText());
                    updateprepStmt2.setString(2, gameId);
                    updateprepStmt2.executeUpdate();
                }

                //Update PlayerAndGame Info
                try (PreparedStatement updateprepStmt3 = connection.prepareStatement("UPDATE PLAYERANDGAME SET SCORE = ?, PLAYING_DATE = ? WHERE PLAYER_ID = ? AND GAME_ID = ?")) {
                    updateprepStmt3.setString(1, gameScore.getText());
                    updateprepStmt3.setString(2, datePlayed.getText());
                    updateprepStmt3.setString(3, player_Id);
                    updateprepStmt3.setString(4, gameId);
                    updateprepStmt3.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Data updated successfully"); //Display success message

                connection.close();
                displayTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        //event handler for Search player by ID button
        button3.setOnAction(event -> {
            try {
                Connection connection = connectToDB(); //connect to DB
                player_Id = idField.getText();
                Statement searchstmt = connection.createStatement(); //Create a statement object to execute SQL query

                if (player_Id.equals("")) { //checking if player_id is empty
                    JOptionPane.showMessageDialog(null, "Enter valid Player ID");  //display message to enter a valid player ID
                    return;
                }
                //SQL query to retrieve player and game information based on the provided Player_ID
                String query = "SELECT B.FIRST_NAME, B.LAST_NAME, B.ADDRESS, B.PROVINCE, B.POSTAL_CODE, B.PHONE_NUMBER, C.GAME_TITLE, A.SCORE, TO_CHAR(A.PLAYING_DATE, 'YYYY-MM-DD') AS PLAYING_DATE, A.GAME_ID "
                        + "FROM PLAYERANDGAME A "
                        + "INNER JOIN PLAYER B "
                        + "ON A.PLAYER_ID = B.PLAYER_ID "
                        + "INNER JOIN GAME C "
                        + "ON A.GAME_ID = C.GAME_ID "
                        + "WHERE B.PLAYER_ID = " + player_Id;

                ResultSet rs = searchstmt.executeQuery(query); //Execute the SQL query and get the result set

                //validation of existing player
                //Check if the result set is empty or if the Player ID is empty
                if (!rs.next() || player_Id.equals("")) {
                    JOptionPane.showMessageDialog(null, "There is no existing data for this Player ID");
                } else {
                    // Populate the text fields with the retrieved player and game info
                    firstname.setText(rs.getString(1));
                    firstname.setText(rs.getString(1));
                    lastname.setText(rs.getString(2));
                    address.setText(rs.getString(3));
                    province.setText(rs.getString(4));
                    postalCode.setText(rs.getString(5));
                    phoneNumber.setText(rs.getString(6));
                    gameTitle.setText(rs.getString(7));
                    gameScore.setText(rs.getString(8));
                    datePlayed.setText(rs.getString(9));
                    gameId = rs.getString(10);
                }

                //Close the result set, statement, and database connection
                rs.close();
                searchstmt.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        //event handler for Clear button
        clearButton.setOnAction(event -> {
            // Clear all text fields
            firstname.clear();
            lastname.clear();
            address.clear();
            province.clear();
            postalCode.clear();
            phoneNumber.clear();
            idField.clear();
            gameTitle.clear();
            gameScore.clear();
            datePlayed.clear();
        });

        stage.setScene(scene);
        stage.show();
    }


    private void displayTable() {
        DisplayTable.tableView = new TableView();  //Initialize a TableView instance
        DisplayTable displayPlayer = new DisplayTable();
        displayPlayer.showData();   //retrieve data from DB
        Scene scene = new Scene(DisplayTable.tableView);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(900);
        stage.show();
    }
}


public static void main(String[] args) {
    launch(args);
}


