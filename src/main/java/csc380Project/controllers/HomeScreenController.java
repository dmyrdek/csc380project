package csc380Project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.application.HostServices;


public class HomeScreenController{

    @FXML
    Hyperlink github_link;

    @FXML
    Hyperlink ngrok_link;

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

    public void howToPlayButtonPress(ActionEvent event) throws IOException{
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("HowToPlay.fxml"));
        Scene createLobbyScene = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(createLobbyScene);
        appStage.show();
        appStage.requestFocus();
    }

    public void quitGameButtonPress(ActionEvent event){
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.close();
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

    @FXML
    public void initialize() {
        github_link.setOnAction(t ->{
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/dmyrdek/csc380project"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        );

        ngrok_link.setOnAction(t ->{
            try {
                Desktop.getDesktop().browse(new URI("https://ngrok.com/"));
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (URISyntaxException e2) {
                e2.printStackTrace();
            }
        }
    );
    }

}