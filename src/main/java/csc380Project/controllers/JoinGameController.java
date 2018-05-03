package csc380Project.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import java.io.IOException;

public class JoinGameController {
    @FXML
    JFXTextField port_number_field;

    @FXML
    JFXTextField username_field;

    @FXML
    Label port_error;

    @FXML
    Label username_error;

    private static String portNumber = "";
    private static String username = "";

    @FXML
    public void submitPortNumber(KeyEvent event) {
        portNumber = port_number_field.getText();
    }

    @FXML
    public void submitUsername(KeyEvent event) {
        username = username_field.getText();
    }

    public static String getPortNumber() {
        return portNumber;
    }

    public static String getUsername() {
        return username;
    }

    public void joinGameButtonPress(ActionEvent event) throws IOException {
        if (!portNumber.equals("") && !username.equals("")) {
            if (isInteger(portNumber)) {
                Parent page = FXMLLoader.load(getClass().getClassLoader().getResource("WaitingLobby.fxml"));
                Scene waitingLobbyScene = new Scene(page);
                Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                appStage.setScene(waitingLobbyScene);
                appStage.show();
                appStage.requestFocus();
            }
        } else {
            if (portNumber.equals("") || !isInteger(portNumber)) {
                port_error.setVisible(true);
            } else {
                port_error.setVisible(false);
            }
            if (username.equals("")) {
                username_error.setVisible(true);
                ;
            } else {
                username_error.setVisible(false);
                ;
            }
        }
    }

    public void backButtonPress(ActionEvent event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getClassLoader().getResource("HomeScreen.fxml"));
        Scene homePage = new Scene(homePageParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homePage);
        appStage.show();
        appStage.requestFocus();
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty())
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1)
                    return false;
                else
                    continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0)
                return false;
        }
        return true;
    }

}