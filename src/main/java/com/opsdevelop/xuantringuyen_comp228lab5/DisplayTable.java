package com.opsdevelop.xuantringuyen_comp228lab5;
import com.opsdevelop.xuantringuyen_comp228lab5.GameManagement;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayTable extends Application {
    private ObservableList<ObservableList<String>> data; //declare ObservableList to hold table data
    static TableView<ObservableList<String>> tableView; //declare TableView to display the data

    //Method to retrieve and display data in TableView
    public void showData() {
        data = FXCollections.observableArrayList(); //Initialize ObservableList
        try {
            GameManagement conTable = new GameManagement(); //Create GameManagement object
            Connection connection = conTable.connectToDB(); //connection to DB
            Statement stmt = connection.createStatement(); //Create statement for SQL queries

            //SQL query to retrieve player and game information from multiple tables
            String query = "SELECT B.PLAYER_ID, B.FIRST_NAME || ' ' || B.LAST_NAME AS PLAYER_NAME, B.ADDRESS, B.POSTAL_CODE, B.PROVINCE, B.PHONE_NUMBER, C.GAME_TITLE, A.SCORE, A.PLAYING_DATE \r\n" +
                    "FROM PLAYERANDGAME A \r\n" +
                    "INNER JOIN PLAYER B \r\n" +
                    "ON A.PLAYER_ID = B.PLAYER_ID \r\n" +
                    "INNER JOIN GAME C \r\n" +
                    "ON A.GAME_ID = C.GAME_ID \r\n" +
                    "ORDER BY B.PLAYER_ID";

            ResultSet rs = stmt.executeQuery(query); //Execute the query and get the result set

            //Create table columns based on result set
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                int j = i;

                //Create TableColumn with column name
                TableColumn<ObservableList<String>, String> tbl1 = new TableColumn<>(rs.getMetaData().getColumnName(i + 1));

                //Set cell value factory to populate cells with data from the ObservableList
                tbl1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                        String value = param.getValue().get(j);
                        // Handle null values
                        if (value == null) {
                            return new SimpleStringProperty("");
                        } else {
                            return new SimpleStringProperty(value);
                        }
                    }
                });
                tableView.getColumns().add(tbl1); //Add TableColumn to TableView
            }

            //Populate data into the ObservableList
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    String value = rs.getString(i);
                    // Handle date formatting for the 'PLAYING_DATE' column. Only show the date and not the time
                    if (columnName.equals("PLAYING_DATE")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(value);
                        value = sdf.format(date);
                    }
                    // Handle null values
                    if (value == null) {
                        row.add("");
                    } else {
                        row.add(value);
                    }
                }
                data.add(row); //Add row to the ObservableList
            }

            tableView.setItems(data); //Set data into the TableView

            //close result set, statement and connection
            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Building Database"); //Display error message
        }
    }

    @Override
    public void start(Stage arg0) throws Exception {

    }
}

