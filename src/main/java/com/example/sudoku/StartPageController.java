package com.example.sudoku;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartPageController implements Initializable {
    public Button loadGameButton;
    protected Stage stage;
    @FXML
    protected TextField nameField;
    @FXML
    protected ChoiceBox<String> difficultyField;
    @FXML
    protected ChoiceBox<String> sizeField;
    @FXML
    protected Button startButton;
    @FXML
    protected TextField sizeTextField;


    // creates a new scene for Sudoku Grid
    public void onStartButtonClick(ActionEvent event) throws IOException {
        String name = nameField.getText();
        int difficulty = parseDifficulty();
        int size = parseSize();

        FXMLLoader fxmlLoader = new FXMLLoader(StartPageController.class.getResource("SudokuView.fxml"));
        BorderPane root = (BorderPane) fxmlLoader.load();
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        root.setCenter(gp);


        // Creating TextFields and styling them
        TextField[][] tfs = new TextField[size][size];
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++) {
                tfs[i][j] = new TextField();
                tfs[i][j].setPrefWidth(50);
                tfs[i][j].setPrefHeight(25);
                tfs[i][j].setFont(new Font(25));
                tfs[i][j].setAlignment(Pos.CENTER);
                gp.add(tfs[i][j], j, i);

                // alternate coloring
                int SQN = (int)Math.sqrt(size);
                if( (i/SQN + j/SQN)%2==0 )
                    tfs[i][j].setStyle("-fx-background-color: #FCF6F5FF; -fx-border-color: #000;");
                else
                    tfs[i][j].setStyle("-fx-background-color: #F0E1B9FF; -fx-border-color: #000;");
            }



        // create necessary changes in the scene
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        // populating necessary fields in SudokuController Object
        SudokuController sc = fxmlLoader.getController();
        sc.populate(new Game(name, difficulty, size), stage, root, tfs);


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] diffs = {"Easy", "Medium", "Hard"};
        String[] sizes = {"4x4", "9x9", "16x16", "Custom"};
        difficultyField.getItems().addAll(diffs);
        difficultyField.setValue("Difficulty");
        sizeField.getItems().addAll(sizes);
        sizeField.setValue("Board Size");

        difficultyField.setOnAction(this::onSetDifficulty);
        sizeField.setOnAction(this::onSetSize);
    }

    private void onSetDifficulty(ActionEvent event) {

    }

    private void onSetSize(ActionEvent event)   {
        if(sizeField.getValue().equals("Custom"))
        {
            sizeTextField.setEditable(true);
        }else{
            sizeTextField.setText("");
            sizeTextField.setEditable(false);
        }
    }

    private int parseSize()    {
        String value = sizeField.getValue();
        if(value.equals("9x9"))
            return 9;
        else if(value.equals("4x4"))
            return 4;
        else if(value.equals("16x16"))
            return 16;
        else
            return 0;
    }

    private int parseDifficulty() {
        String value = difficultyField.getValue();

        if(value.equals("Easy"))
            return 1;
        else if(value.equals("Medium"))
            return  55;
        else if (value.equals("Hard"))
            return 58;
        else return 0;
    }

    public void onLoadButtonClick(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Open");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("save file", "*.txt"));


        try{
            File file = fc.showOpenDialog(stage);
            System.out.println(file.getAbsoluteFile());
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsoluteFile()));
            Game game = (Game) ois.readObject();
            ois.close();

            int size = game.getSize();

            FXMLLoader fxmlLoader = new FXMLLoader(StartPageController.class.getResource("SudokuView.fxml"));
            BorderPane root = (BorderPane) fxmlLoader.load();
            GridPane gp = new GridPane();
            root.setCenter(gp);


            // Creating TextFields
            TextField[][] tfs = new TextField[size][size];
            for(int i=0; i<size; i++)
                for(int j=0; j<size; j++) {
                    tfs[i][j] = new TextField();
                    tfs[i][j].setPrefWidth(50);
                    tfs[i][j].setPrefHeight(25);
                    tfs[i][j].setFont(new Font(25));
                    tfs[i][j].setAlignment(Pos.CENTER);
                    gp.add(tfs[i][j], j, i);
                }



            // create necessary changes in the scene
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // populating necessary fields in SudokuController Object
            SudokuController sc = fxmlLoader.getController();
            sc.populate(game, stage, root, tfs);


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(true);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onLeaderboardButtonClick(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPageController.class.getResource("Leaderboard.fxml"));
        BorderPane root = (BorderPane) fxmlLoader.load();
        ScrollPane sp = new ScrollPane();

        // create necessary changes in the scene
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        // populating necessary fields in SudokuController Object
        LeaderboardController sc = fxmlLoader.getController();
        sc.populate(stage);


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSigninButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPageController.class.getResource("LoginPage.fxml"));
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Sudoku");
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }
}
