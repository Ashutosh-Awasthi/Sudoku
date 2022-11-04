package com.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Init extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Init.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.setResizable(false);
//        stage.getIcons().add(new Image("C:\\Users\\Ashutosh Awasthi\\OneDrive\\Desktop\\SOFTABLITZ\\Sudoku\\src\\main\\java\\com\\example\\sudoku\\mainico.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}