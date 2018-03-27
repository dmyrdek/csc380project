package csc380Project.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;

public class Controller {

    @FXML
    TextField portNumber;

    public void createGameButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("CreateLobby.fxml"));
        Scene createLobbyScene = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(createLobbyScene);
        appStage.show();
        appStage.requestFocus();

    }

    public void joinGameButtonPress(ActionEvent event) throws IOException{
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("JoinGame.fxml"));
        Scene createLobbyScene = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(createLobbyScene);
        appStage.show();
        appStage.requestFocus();
    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }




    public void createGameLaunchServer(ActionEvent event) {
        try {

            //GameServer.createServer();
            //GameServer.main();

            Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("WaitingLobby.fxml"));
            Scene homePage = new Scene(homePageParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(homePage);
            appStage.show();
            appStage.requestFocus();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}