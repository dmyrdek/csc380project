package csc380Project;

import com.jfoenix.controls.JFXComboBox;
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



public class createLobbyController{
    @FXML
    JFXComboBox maxNumberOfPlayers;

    @FXML
    JFXComboBox numberOfRounds;

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
    }

    public void createGameLaunchServer(ActionEvent event) {
        try {

            GameServer.createServer();


            Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("WaitingLobby.fxml"));
            Scene homePage = new Scene(homePageParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(homePage);
            appStage.show();





        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        maxNumberOfPlayers.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        numberOfRounds.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
    }

}