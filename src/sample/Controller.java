package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    public void createGameButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getResource("CreateLobby.fxml"));
        Scene createLobbyScene = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(createLobbyScene);
        appStage.show();

    }

    public void joinGameButtonPress(ActionEvent event) throws IOException{
        Parent homePageParent = FXMLLoader.load(getClass().getResource("JoinGame.fxml"));
        Scene createLobbyScene = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(createLobbyScene);
        appStage.show();
    }



}
