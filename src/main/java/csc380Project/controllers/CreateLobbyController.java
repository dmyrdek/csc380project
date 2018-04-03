package csc380Project.controllers;

import csc380Project.server.*;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

public class CreateLobbyController {
    @FXML
    JFXComboBox maxNumberOfPlayers;

    @FXML
    JFXComboBox numberOfRounds;

    @FXML
    JFXTextField port_number_field;

    private static String portNumber = "";

    @FXML
    public void submitPortNumber(KeyEvent event){
        portNumber = port_number_field.getText();
    }

    public static String getPortNumber(){
        return portNumber;
    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("HomeScreen.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }

    public void createGameLaunchServer(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("WaitingLobby.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);

        //GameServer.createServer();
        //GameServer.main(null);
        //String[] arguments = new String[] {};
        //new SyncClients().main(arguments);

        new StartGameSever().start();

        Platform.runLater(
            () -> {
            appStage.show();
            appStage.requestFocus();
           }
        );
    }

    @FXML
    public void initialize() {
        maxNumberOfPlayers.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        numberOfRounds.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15");
    }

    private class StartGameSever extends Thread{
        public void run(){
            String[] arguments = new String[] {};
            new SyncClients().main(arguments);
        }
    }
    
}
