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

import com.jfoenix.controls.JFXTextField;

public class JoinGameController{
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

    public void joinGameButtonPress(ActionEvent event) throws IOException{
        Parent page = FXMLLoader.load(getClass().getClassLoader().getResource("WaitingLobby.fxml"));
        Scene waitingLobbyScene = new Scene(page);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(waitingLobbyScene);
        appStage.show();
        appStage.requestFocus();
    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("HomeScreen.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }
}